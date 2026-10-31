package hu.kistarcsa.halloweennavigator;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {
    private static final int REQ_LOCATION = 11;
    private static final int REQ_FILE = 12;
    private static final String ORIGIN = "https://appassets.androidplatform.net";
    private WebView web;
    private ValueCallback<Uri[]> fileCallback;
    private GeolocationPermissions.Callback geoCallback;
    private String geoOrigin;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        web = new WebView(this);
        setContentView(web);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setGeolocationEnabled(true);
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(true); // Android file picker returns content:// URIs
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        WebView.setWebContentsDebuggingEnabled(false);
        WebViewAssetLoader loader = new WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this)).build();
        web.setWebViewClient(new WebViewClientCompat() {
            @Override public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return loader.shouldInterceptRequest(request.getUrl());
            }
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (ORIGIN.equals(uri.getScheme()+"://"+uri.getAuthority())) return false;
                if ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    return true;
                }
                return true;
            }
        });
        web.setWebChromeClient(new WebChromeClient() {
            @Override public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                if (!ORIGIN.equals(origin)) { callback.invoke(origin,false,false); return; }
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    callback.invoke(origin,true,false);
                } else {
                    geoCallback=callback; geoOrigin=origin;
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_LOCATION);
                }
            }
            @Override public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback, FileChooserParams params) {
                if (fileCallback!=null) fileCallback.onReceiveValue(null);
                fileCallback=callback;
                try {
                    Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    i.setType("application/json");
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(i,REQ_FILE);
                    return true;
                } catch(Exception ex) { fileCallback=null; return false; }
            }
        });
        web.addJavascriptInterface(new JsonExporter(), "AndroidExport");
        web.loadUrl(ORIGIN+"/assets/index.html");
    }
    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grants) {
        super.onRequestPermissionsResult(requestCode,permissions,grants);
        if (requestCode==REQ_LOCATION && geoCallback!=null) {
            boolean allowed=false;
            for(int g:grants) if(g==PackageManager.PERMISSION_GRANTED) allowed=true;
            geoCallback.invoke(geoOrigin,allowed,false);
            geoCallback=null;
        }
    }
    @Override protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==REQ_FILE && fileCallback!=null) {
            Uri uri=(resultCode==RESULT_OK && data!=null)?data.getData():null;
            fileCallback.onReceiveValue(uri==null?null:new Uri[]{uri});
            fileCallback=null;
        }
    }
    public class JsonExporter {
        @JavascriptInterface public void saveJson(String filename, String json) {
            if (!filename.matches("[a-zA-Z0-9_\\-]{1,100}\\.json") || json.length()>2500000) return;
            new Thread(()->{
                Uri uri=null;
                try {
                    ContentValues values=new ContentValues();
                    values.put(MediaStore.Downloads.DISPLAY_NAME,filename);
                    values.put(MediaStore.Downloads.MIME_TYPE,"application/json");
                    values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS+"/HalloweenNavigator");
                    uri=getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI,values);
                    if (uri==null) throw new IllegalStateException("Unable to create download");
                    try(OutputStream out=getContentResolver().openOutputStream(uri)) {
                        if(out==null)throw new IllegalStateException("Unable to open download");
                        out.write(json.getBytes(StandardCharsets.UTF_8));
                    }
                    runOnUiThread(()->Toast.makeText(MainActivity.this,"JSON mentve: Letöltések/HalloweenNavigator",Toast.LENGTH_LONG).show());
                } catch(Exception e) {
                    if(uri!=null)getContentResolver().delete(uri,null,null);
                    runOnUiThread(()->Toast.makeText(MainActivity.this,"Exportálási hiba: "+e.getMessage(),Toast.LENGTH_LONG).show());
                }
            }).start();
        }
    }
    @Override public void onBackPressed() {
        if(web.canGoBack()) web.goBack(); else super.onBackPressed();
    }
    @Override protected void onDestroy() { if(web!=null)web.destroy(); super.onDestroy(); }
}

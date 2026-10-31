package hu.kistarcsa.halloweennavigator;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

public class WalkLocationService extends Service implements LocationListener {
    public static final String ACTION_START="hu.kistarcsa.halloweennavigator.START_WALK_GPS";
    public static final String ACTION_STOP="hu.kistarcsa.halloweennavigator.STOP_WALK_GPS";
    private final IBinder binder=new LocalBinder();
    private LocationManager manager;
    private Location previous;
    private long previousElapsed;
    private double meters;
    public class LocalBinder extends Binder { WalkLocationService service(){return WalkLocationService.this;} }
    @Override public IBinder onBind(Intent intent){return binder;}
    @Override public void onCreate(){super.onCreate();manager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        SharedPreferences p=getSharedPreferences("walk_gps",MODE_PRIVATE);
        meters=Double.longBitsToDouble(p.getLong("meters",Double.doubleToLongBits(0d)));
        previousElapsed=0;
    }
    @Override public int onStartCommand(Intent i,int flags,int startId){
        if(i!=null&&ACTION_STOP.equals(i.getAction())){
            stopUpdates();stopForeground(STOP_FOREGROUND_REMOVE);stopSelf();return START_NOT_STICKY;
        }
        NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.createNotificationChannel(new NotificationChannel("walk_tracking","Halloween séta GPS",NotificationManager.IMPORTANCE_LOW));
        Notification n=new Notification.Builder(this,"walk_tracking")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentTitle("Halloween Navigator – séta folyamatban")
            .setContentText("A GPS-követés lezárt képernyőn is aktív")
            .setOngoing(true).build();
        if(Build.VERSION.SDK_INT>=29)startForeground(301,n,ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION);
        else startForeground(301,n);
        startUpdates();
        return START_STICKY;
    }
    private void startUpdates(){
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
          &&checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){stopSelf();return;}
        try{
            // Moderate sampling reduces battery drain; screen-off reception does not depend on WebView.
            if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5f,this);
            if(manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,12000,15f,this);
        }catch(SecurityException e){stopSelf();}
    }
    @Override public void onLocationChanged(Location l){
        if(l.getAccuracy()>40)return;
        long now=l.getElapsedRealtimeNanos();
        if(previous!=null){
            double step=previous.distanceTo(l),dt=(now-previousElapsed)/1e9;
            if(dt>0&&dt<120&&step>4&&step<Math.max(25,dt*2.5)){
                meters+=step;
            }
        }
        previous=new Location(l);previousElapsed=now;
        getSharedPreferences("walk_gps",MODE_PRIVATE).edit()
            .putLong("meters",Double.doubleToLongBits(meters))
            .putLong("locationTime",l.getTime())
            .putLong("latitude",Double.doubleToLongBits(l.getLatitude()))
            .putLong("longitude",Double.doubleToLongBits(l.getLongitude()))
            .putLong("accuracy",Double.doubleToLongBits(l.getAccuracy()))
            .apply();
    }
    public static void reset(Context c){
        c.getSharedPreferences("walk_gps",MODE_PRIVATE).edit().putLong("meters",Double.doubleToLongBits(0d)).apply();
    }
    private void stopUpdates(){if(manager!=null)manager.removeUpdates(this);}
    @Override public void onDestroy(){stopUpdates();super.onDestroy();}
}

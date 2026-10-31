# Halloween Navigátor – Android tesztprojekt

A projekt a 0.6.1-es, 34 ellenőrzött koordinátát tartalmazó webalkalmazás önálló Android WebView-változata. **Az APK nincs előre lefordítva.** A GitHub Actions segítségével fordítható le ingyenesen, nyilvános repositoryn.

## APK fordítása GitHubon

1. Töltsd fel a ZIP tartalmát a `katonagabor75/Halloween` repository **gyökérmappájába** a könyvtárszerkezet megtartásával. A `README.md` és más meglévő fájlok maradhatnak. A ZIP-ben szereplő `app/src/main/assets/index.html` nem helyettesíti a weboldal főkönyvtári `index.html` fájlját.
2. A GitHubon válaszd az **Actions → Android APK build → Run workflow** menüt. A `push` esemény automatikusan is elindíthatja.
3. A sikeres futás oldalán az **Artifacts** részben töltsd le a **HalloweenNavigator-debug-apk** csomagot, és csomagold ki.
4. A `app-debug.apk` fájlt másold át a Galaxy S24-re, majd nyisd meg. Az Android engedélyt kérhet az adott fájlkezelőből történő telepítéshez. Csak a saját lefordított APK-t telepítsd.
5. Az alkalmazás indításakor engedélyezd a helymeghatározást. A már exportált útvonalat az alkalmazás importmezőjével töltsd be.

## Fontos jellemzők és korlátok

- A webes 0.6.1 alkalmazást tartalmazza, a 34 helyszín beépítve.
- Az alkalmazás **nem** a GitHub Pages oldalát tölti be: a HTML az APK-ban van, ezért egy régebbi/újabb GitHub Pages verzió nem módosítja automatikusan.
- A térképcsempék, Leaflet könyvtár és Valhalla gyalogos útvonal-kérések internetkapcsolatot igényelnek; a nyilvános szolgáltatás elérhetősége nem garantált.
- A GPS csak az alkalmazás előtérben tartásakor működik megbízhatóan. **Nincs háttérben futó GPS és kanyarodásonkénti hangos navigáció.**
- A tervező heurisztikus, az idő-/távkorlátokat és az utcai útvonalakat menet közben ellenőrizni kell. Nem biztonságkritikus navigációs rendszer.
- JSON export: `Letöltések/HalloweenNavigator/`; import a fájlválasztóval.
- A webes böngészőben korábban helyileg tárolt adatokat nem emeli át automatikusan az APK-ba; importálj JSON-t.
- Debug APK csak kipróbálásra; végleges terjesztéshez aláírt release build és további teszt szükséges.

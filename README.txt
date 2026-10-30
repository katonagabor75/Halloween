HALLOWEEN NAVIGÁTOR – 0.1 prototípus (2026-10-30)

Ez telepíthető webalkalmazás (PWA), NEM natív Android APK.
Androidos GPS-követéshez HTTPS alatt kell megnyitni.

TELEPÍTÉS TELEFONRA (ajánlott):
1. Nyiss egy új nyilvános GitHub-repositoryt, töltsd fel az index.html,
   manifest.webmanifest, icon.svg és sw.js fájlokat a gyökérkönyvtárba.
2. Settings / Pages / Deploy from a branch / main / root / Save.
3. Nyisd meg a megjelenő https://...github.io/... címet Android Chrome-ban.
4. Chrome menü -> Hozzáadás a kezdőképernyőhöz / Telepítés.
5. Engedélyezd a helyadatokat. Nyomd meg a GPS indítása gombot.

HASZNÁLAT:
- Először a 'Címek beazonosítása' (34 cím internetes lekérése) gomb.
- Ellenőrizd külön a cégeket, Kékházat és az esetleges rossz geokódokat.
- Helyszín javítása: a pont mellett 📍, utána koppintás a térképen.
- Válassz startot: GPS-pozíció, térképre koppintás vagy automatikus javaslat.
- Állítsd be az időt, maximális sétát, útvonaltípust.
- Tervezés. Az újratervezést csak jóváhagyás után fogadja el.

KORLÁTOK:
- A külső címkereső és útvonaltervező ingyenes publikus demo: leállhat/limitálhat.
- A kezdeti útvonalkeresés heurisztikus és becsült távolságú; a választott
  szakaszokat ezután külső gyalogos útvonaltervező ellenőrzi, ha elérhető.
- Nincs garantált globális optimum, nincs natív háttér-GPS, nincs hangnavigáció.
- A valódi séta előtt teszteld a koordinátákat. Valós forgalmi szabályokat tarts be.
- A GPS pozíció a böngészőben feldolgozott adat; a gyalogos útvonal API a
  lekérdezett koordinátákat megkapja. A geokódolás címeket küld a szolgáltatónak.

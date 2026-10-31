Halloween Navigátor 0.4 — 34 előre beépített, a felhasználó által ellenőrzött koordináta

GITHUB FRISSÍTÉS:
A korábbi 0.3 fájlok közül az index.html-t cseréld le. A manifest.webmanifest és icon.svg változatlan, ezek maradhatnak. Az üres sw.js is maradhat.
AJÁNLOTT: töltsd fel a mellékelt halloween_kistarcsa_koordinatak.json adatmentést is a repository gyökerébe.

GitHubon fájlcsere: nyisd meg az index.html fájlt -> ceruza / Edit file -> másold bele az új index.html teljes tartalmát -> Commit changes. Másik lehetőség: töröld a régi index.html-t, és Upload files-szal töltsd fel az újat.
A fő webcím marad: https://katonagabor75.github.io/Halloween/
Frissítés után böngészőben kemény újratöltés vagy telepített PWA bezárása/újranyitása. Böngészőadatokat ne törölj, ha nem exportáltad a helyi módosításaidat.

ÚJ:
* Mind a 34 koordináta a megadott, ellenőrzött listából töltődik be. Automatikus címkeresés nincs.
* A korábbi, ugyanazon böngészőben manuálisan javított koordináta elsőbbséget élvez.
* Az eredeti helyszín JSON biztonsági másolatként a csomag része.

FONTOS KORLÁTOK:
A 0.4 útvonalválasztása továbbra is közelítő (heurisztika), ezért a szigorú távolság- és időkorlát teljesülését nem garantálja. A Valhalla utcahálózati nyomvonal lekérése internetet és működő nyilvános szolgáltatást igényel. A GPS böngészőháttérben leállhat. A parkoló javaslat nem ellenőrzi a parkolhatóságot. A szolgáltatói térkép- és útvonalhibákat ellenőrizd indulás előtt.

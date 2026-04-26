# Command-line pizzeria haldussüsteem

- Marina Gorelikova (github: MarinaGorelikovaUT)
- Jessica Brjuhhov (github: jackdaw5)
- Denis Artemjev (github: DenisArtemjevUT)

## Projekti lühikirjeldus

Meie projekt on käsurealt kasutatav pizzeria haldussüsteem. Programm võimaldab hallata pizzeria laudu, broneeringuid, menüüd ja tellimusi. Ettekandjad saavad luua tellimusi ja siduda need kindla lauaga, kokad näevad valmistamist vajavaid tellimusi ning saavad muuta nende staatust.

## Planeeritav funktsionaalsus

- pizzeria laudade haldamine
- laudade broneerimine kindlaks ajaks
- pizzeria menüü vaatamine
- tellimuse loomine lauale
- tellimusele toodete lisamine
- tellimuse staatuse muutmine
- köögi vaade aktiivsetele tellimustele
- ettekandja vaade valmis tellimustele
- tellimuste ajaloo vaatamine
- kasutaja rollid: manager, waiter, cook

## Etapi 1 planeeritav funktsionaalsus

**1. Laudade nimekirja vaatamine**
Kasutaja saab kuvada kõigi pizzeria laudade nimekirja. Iga laua kohta kuvatakse selle number, mahtuvus ja praegune staatus (vaba / hõivatud / broneeritud).

**2. Broneeringu lisamine**
Kasutaja saab broneerida laua kindlaks kuupäevaks ja kellaajaks, sisestades külalise nime ja inimeste arvu. Süsteem kontrollib, kas laud on valitud ajal vaba, ning salvestab broneeringu.

**3. Menüü vaatamine**
Kasutaja saab vaadata pizzade, lisandite ja jookide nimekirja. Iga toote kohta kuvatakse nimi, kirjeldus ja hind. Menüü on jagatud kategooriatesse (nt pitsad, lisandid, joogid, magustoidud).

**4. Tellimuse loomine konkreetsele lauale**
Ettekandja saab luua uue tellimuse, sidudes selle kindla lauaga. Tellimusele omistatakse unikaalne identifikaator ja algstaatus (NEW).

**5. Tellimuse põhiandmete salvestamine**
Süsteem salvestab tellimuse põhiinfo programmi mällu: laua number, menüüst valitud tooted, loomise aeg ja staatus. Püsisalvestus (failid/andmebaas) — järgmistes etappides.

**6. Käsurea menüü programmi kasutamiseks**
Programmi juhitakse terminalis tekstimenüü kaudu. Kasutaja näeb saadaolevate käskude nimekirja, sisestab numbri ja saab tulemuse. Menüü kohandub vastavalt kasutaja rollile.

## Tööjaotus

**Denis — lauad ja broneeringud**
- Klass Table: number, mahtuvus, staatus
- Klass Reservation: laud, külalise nimi, kuupäev/kellaaeg, külaliste arv
- TableService: laudade haldamise loogika, vabade laudade otsimine
- ReservationService: broneeringu lisamine koos konfliktide kontrollimisega

**Marina — menüü ja tellimused**
- Klass MenuItem: nimi, kirjeldus, hind, kategooria (nt pitsa, lisand, jook)
- Klass Order: identifikaator, laud, staatus, loomise aeg
- Klass OrderItem: menüütoode, kogus
- MenuService: menüü haldamine, vaikimisi andmed (hardcoded)
- OrderService: tellimuse loomine ja muutmine

**Jessica — kasutajad, rollid ja käsurea menüü**
- Klass User: nimi, roll
- Klassid Manager, Waiter, Cook
- CommandLineMenu: valikute kuvamine, sisendi töötlemine, meetodite kutsumine
- Main.java: programmi käivitamine ja üldine juhtloogika
- Lihtne "autoriseerimine" — rolli valimine programmi käivitamisel

---

## Etapi 1 kokkuvõte

Kõik planeeritud funktsionaalsus on realiseeritud ja toimib. Programm käivitub terminalis, võimaldab valida rolli ning kasutada kõiki põhifunktsioone. Allpool kirjeldame, mida igaüks meeskonnast tegi ja kuidas programm toimib.

### Denis — lauad ja broneeringud

**Lauad ja staatused:**
- Realiseeris klassi `Table` väljadega number, mahutavus ja staatus
- Lisas `TableService` klassi mis haldab kõiki restorani laudu ja pakub meetodeid vabade laudade otsimiseks
- Lauad on erineva mahutavusega (2 ja 6 kohta)

**Broneeringud:**
- Realiseeris klassi `Reservation` mis salvestab külalise nime, külaliste arvu, laua ja broneeringu aja
- `ReservationService` kontrollib broneerimise käigus laua saadavust ja mahutavust
- Broneerimisprotsess võimaldab valida konkreetse kuupäeva ja kellaaja
- Lisas valideerimise — ei saa broneerida rohkem külalisi kui laual mahub, ega sisestada valet kuupäeva
- Realiseeris broneeringute salvestamise Java serialiseerimise abil — salvestamine vajab veel parandamist

---

### Marina — menüü ja tellimused

**Menüü:**
- Realiseeris klassi `MenuItem` väljadega nimi, kirjeldus, hind ja kategooria
- Kategooriad on realiseeritud `enum`-ina: `PIZZA`, `SIDE`, `DRINK`, `DESSERT`
- `MenuService` sisaldab kogu pizzeria menüüd ja pakub meetodeid toodete otsimiseks nime, numbri ja kategooria järgi
- Lisas meetodi `printMenuWithCategoryChoice()` mis võimaldab sirvida menüüd kategooriate kaupa navigeerimisega ja tagasiliikumisega

**Tellimused:**
- Realiseeris klassid `Order`, `OrderItem` ja `OrderService`
- `Order` kasutab `enum OrderStatus` staatuste jaoks: `NEW`, `IN_PROGRESS`, `READY`, `DELIVERED`, `PAID`
- Tellimuse loomise ajal salvestatakse `LocalDateTime` tempelmärk ja genereeritakse automaatne unikaalne number
- `OrderItem` sisaldab lippu `isNew` lisatellimuste märgistamiseks
- Ettekandja saab luua tellimuse, lisada tooteid, vastu võtta lisatellimusi ja sulgeda tellimuse pärast makse laekumist
- Lisatellimused märgistatakse kokale `[UUS]` sildiga
- Tellimust ei saa sulgeda enne kui kokk on selle valmis märkinud
- Pärast tasumist eemaldatakse tellimus süsteemist ja laud vabastatakse
- Pärast makse kinnitamist saab ettekandja printida kviitungi koos toodete nimekirja ja kogusummaga
---

### Jessica — kasutajad, rollid ja käsurea menüü

**Kasutajad ja rollid:**
- Realiseeris kasutajate hierarhia: `User`, `Manager`, `Waiter`, `Cook`, `Guest`
- Rollid on realiseeritud `enum`-ina: `MANAGER`, `WAITER`, `COOK`, `GUEST`
- Programmi käivitamisel valib kasutaja oma rolli

**Käsurea menüü:**
- Ehitas CLI arhitektuuri Handler-mustri alusel — iga rollil on oma `MenuHandler` klass
- `CommandLineMenu` haldab programmi voogu ja suunab tegevused õigele handler-ile
- Realiseeris eraldi handler-id: `ManagerMenuHandler`, `WaiterMenuHandler`, `CookMenuHandler`, `GuestMenuHandler`
- Ettekandja liides võimaldab lisada tooteid käsklustega `M` (menüü), `V` (vaata tellimust), `X` (eemalda toode), `0` (lõpeta)
- Iga roll näeb ainult oma rolli jaoks mõeldud menüüvalikuid

## Programmi kirjeldus

Pizzeria haldussüsteem on käsurealt kasutatav Java rakendus, mis simuleerib pizzeria igapäevast tööd. Programm toetab nelja erinevat rolli: manager, ettekandja, kokk ja külaline.

---

### Programmi käivitamine

Programm käivitatakse `Main.java` klassi käivitamisega IntelliJ IDEA-s. Kohe avaneb rolli valiku menüü:
```
---PIZZERIA SÜSTEEM---
Vali roll:
1. Manager
2. Ettekandja
3. Kokk
4. Külaline (broneerimiseks)
9. Välju programmist
```

Vali number ja sisene oma rolli. Iga roll näeb ainult oma tööks vajalikke valikuid. Rolli vahetamiseks logi välja (valik `0`) ja vali uus roll.

---

### Rollid ja nende võimalused

**Manager** haldab laudu ja broneeringuid. Ta näeb kõigi laudade seisu koos staatuste ja broneeringute infoga, saab broneerida lauda valitud ajaks, vaadata ja tühistada broneeringuid. Lisaks saab manager vaadata kõiki aktiivseid tellimusi koos toodete ja kogusummaga ning sirvida pizzeria menüüd kategooriate kaupa.

**Ettekandja** loob tellimusi ja teenindab laudu. Uue tellimuse loomisel valib ettekandja laua ja lisab tooteid — menüü avamiseks vajuta `M`, hetke tellimuse vaatamiseks `V`, toote eemaldamiseks `X` ja lõpetamiseks `0`. Toote lisamiseks kirjuta toote number ja kogus tühikuga (nt `1 2`). Laua teenindamisel saab ettekandja vaadata aktiivset tellimust, võtta vastu lisatellimusi ja esitada arve — aga ainult siis, kui kokk on tellimuse valmis märkinud. Pärast makse kinnitamist vabastatakse laud automaatselt.

**Kokk** näeb kõiki aktiivseid tellimusi koos toodete, koguste ja loomise ajaga. Lisatellimused on märgistatud `[UUS]` sildiga, et oleks kohe näha mis vajab tähelepanu. Kui tooted on valmis, märgib kokk tellimuse valmis.

**Külaline** saab broneerida laua ja sirvida menüüd. Broneerimiseks valib külaline vaba laua, sisestab nime, külaliste arvu ja soovitud aja. Süsteem kontrollib automaatselt mahutavust ja kuupäeva korrektsust.

---

### Tellimuse elutsükkel
```
NEW → IN_PROGRESS → READY → PAID
```

1. Ettekandja loob tellimuse ja kinnitab selle — staatus `IN_PROGRESS`, tellimus läheb kokale
2. Kokk valmistab tooted ja märgib tellimuse valmis — staatus `READY`
3. Ettekandja esitab arve ja klient tasub — staatus `PAID`, laud vabastatakse automaatselt

---

### Broneeringu protsess

1. Kasutaja valib vaba laua — süsteem näitab kõigi laudade seisu
2. Sisestab nime ja külaliste arvu — süsteem kontrollib mahutavust
3. Sisestab broneeringu kuupäeva ja kellaaja — süsteem valideerib andmed
4. Broneering salvestatakse, laua staatus muutub `BRONEERITUD` ja külaline saab kinnituse

## Teadaolevad vead ja planeeritavad täiendused

### Teadaolevad vead
- **Sisendi valideerimine:** kui kasutaja sisestab numbri asemel tähe, läheb programm kokku. Plaanime selle vea järgmises etapis parandada.
- **Broneeringu loogika:** hetkel muutub laud kohe broneerituks ja on teistele kättesaamatu kuni broneeringu tühistamiseni, sõltumata broneeringu kellaajast. Näiteks kui broneering on tehtud homseks kella 18:00-ks, ei saa lauda täna õhtul ega homme hommikul kasutada. Plaanime broneeringu loogika ümber teha nii, et laud blokeeritaks ainult konkreetsel broneeringu ajal.
- **Broneeringute salvestamine:** broneeringud ei säili programmi taaskäivitamisel.

### Planeeritavad täiendused
- **Tellimuste salvestamine:** hetkel ei salvestata tellimusi, andmed kaovad pärast programmi sulgemist. Plaanime lisada tellimuste salvestamise järgmises etapis.
- **Restorani lahtiolekuajad:** hetkel saab lauda broneerida ükskõik milliseks kellaajaks, ka öösel. Plaanime lisada valideerimise restorani lahtiolekuaegade põhjal.
- **Tellimuste ajalugu:** manager näeb hetkel ainult aktiivseid tellimusi, suletud tellimused kaovad süsteemist. Plaanime lisada tellimuste ajaloo vaatamise võimaluse.
- **Broneeringu otsing:** hetkel puudub võimalus otsida broneeringut külalise nime järgi. Plaanime lisada otsingufunktsionaalsuse järgmises etapis.

## Etapi 2 kokkuvõte

### Denis — broneeringud ja tellimuste salvestamine

- Muutis broneerimise loogikat `GuestMenuHandler`-is ja `ManagerMenuHandler`-is — nüüd küsib programm esmalt nime, laua tüüpi, külaliste arvu ja aega, ning alles seejärel näitab vabu laudu just sellel ajal
- Lisas meetodi `getAvailableTablesForTime()` `ReservationService`-i
- Külalise jaoks ei saa enam broneerida lauda väljaspool restorani lahtiolekuaegu (10:00–20:00)
- Lisas tellimuste salvestamise taaskäivituste vahel — `MenuItem`, `OrderItem`, `Order` said `Serializable`-ks, `OrderService`-i lisati meetodid `saveOrders()` ja `loadOrders()`, tellimus salvestatakse iga muudatuse järel faili `orders.ser`

### Marina — menüü, tellimuste ajalugu ja veaparandused

- Parandas menüü tootenumbrite vea — kategooria vaates kuvatakse nüüd globaalsed numbrid, mis vastavad tellimisel sisestatavale numbrile
- Lisas restorani lahtiolekuaegade valideerimise (10:00–20:00) ka manaažeri broneerimisse
- Lisas suletud tellimuste ajaloo — manaažer näeb nüüd kõiki tasutud tellimusi koos toodete ja kogusummaga (punkt 7 menüüs)
- Lisas `closedOrders` nimekirja `OrderService`-i ning uuendas `saveOrders()` ja `loadOrders()` meetodeid, et säilitada ajalugu taaskäivituste vahel
- Parandas laua staatuse taastamise programmi käivitamisel — aktiivsete tellimustega lauad märgitakse automaatselt `HOIVATUD`-ks

## Planeeritavad täiendused järgmistes etappides

- **Graafiline kasutajaliides:** plaanime hakata arendama graafilist kasutajaliidest (GUI), et muuta programm mugavamaks ja visuaalselt atraktiivsemaks
- **Süsteemi tugi ja hooldus:** plaanime parandada programmi stabiilsust, lisada vigade käsitlemist ja tagada süsteemi pikaajaline toimimine

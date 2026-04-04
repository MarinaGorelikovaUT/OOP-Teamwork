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

## Etapi 1 tulemus

Etapi lõpuks peab programm:
- käivituma terminalis
- võimaldama valida kasutaja rolli
- kuvama laudade nimekirja koos staatustega
- vastu võtma broneeringuid ja kontrollima laua saadavust
- kuvama pizzeria menüüd koos hindadega
- looma tellimuse, mis on seotud kindla lauaga
- töötama vigadeta põhiliste kasutusstsenaariumite korral

## Mis esitatakse

| Klassid | Kes |
|---------|-----|
| Table, Reservation, TableService, ReservationService | Denis |
| MenuItem, Order, OrderItem, MenuService, OrderService | Marina |
| User, rollid, CommandLineMenu, Main | Jessica |

## KASUTUS
- clone the represitorium with:
``` git clone https://github.com/MarinaGorelikovaUT/OOP-Teamwork/ ```

- ehita ja käivita projekt. Jälgi instruktsiooni ja kasuta numbrid programmi valimiseks
``` mvn exec:java -Dexec.mainClass="com.pizzeria.Main" ```   
📱 KvizApp — RMA26
Mobilna aplikacija za fakultetske kvizove.
---
🏗️ Arhitektura
Paket	Opis
`data/models`	Modeli podataka (Kviz, Predmet, Grupa, Pitanje, KvizTaken, Odgovor)
`data/repositories`	Pristup backend servisu kroz suspend funkcije
`domain`	Use case klase sa poslovnom logikom (filtriranje kvizova)
`network`	Retrofit konfiguracija i ApiService
`util`	Pomoćne funkcije i konstante (parsiranje datuma, boje, default URL)
`viewmodel`	Stanje ekrana kroz StateFlow (KvizViewModel, AuthViewModel, KvizDetaljiViewModel)
`ui/theme`	Compose theming (boje, tipografija)
`ui/theme/screen`	Ekrani aplikacije (Login, Kvizovi, Filter/Upis, Profil, KvizDetalji)
`ui/theme/components`	Komponente za ponovnu upotrebu (KvizItem, UpisForm, EmptyState, FilterDropdown)
`navigation`	Jetpack Navigation Compose
---
👤 Poboljšanja — korisnička perspektiva
> Analiza je rađena iz ugla korisnika koji prvi put otvara aplikaciju i nema uvid u to kako je iznutra napravljena.
1. Login ekran sa hashom 🔑
Hash je u početnoj verziji bio fiksno definisan u kodu (`"demo"`), zbog čega je aplikacija bila ograničena na korištenje jednog korisničkog naloga. Kako bi se omogućila upotreba aplikacije za različite studente, implementiran je `LoginScreen` ekran na kojem korisnik unosi vlastiti hash. Uneseni hash se zatim pohranjuje putem `AccountRepository` klase i koristi tokom rada aplikacije. Na ovaj način aplikacija više nije vezana za demo nalog, već je prilagođena korištenju od strane bilo kojeg studenta.
2. Loading indikatori i prikaz grešaka ⏳
Dok su podaci pristizali sa servera, ekran je djelovao zamrznuto, bez ikakvog znaka da se nešto dešava. Dodani su indikatori učitavanja i jasne poruke koje razlikuju "nema interneta" od "greška servera", tako da korisnik u svakom trenutku zna šta se dešava umjesto da nagađa je li aplikacija zaglavila.
3. Bodovi i status "urađeno" na kartici kviza 🎯
Pošto backend ne ažurira polje `zavrsen` automatski, status "urađeno" se sada računa na osnovu broja stvarno poslanih odgovora u odnosu na broj pitanja u kvizu. Kartica dodatno prikazuje osvojene bodove, pa korisnik odmah vidi rezultat bez ulaska u kviz. Ukoliko je korisnik završio kviz, ne može ga raditi ponovo, te će mu se klikom na kviz prikazati samo ostvareni rezultat.
4. Provjera upisa prije pristupa kvizu 🚫
Ranije se moglo kliknuti na bilo koji kviz, čak i sa predmeta na koji korisnik nije upisan. Sada se razlikuju dvije situacije — "niste upisani na predmet" i "ovo nije kviz Vaše grupe" (kad je korisnik upisan na predmet, ali u drugoj grupi) — uz dugme koje vodi direktno na upis, čime poruka postaje precizna i korisnik odmah zna šta da uradi.
5. Onemogućeno ponovno odgovaranje 🔒
Korisnik je ranije mogao više puta kliknuti na različite opcije istog pitanja. Sada se odabrani odgovor ističe bojom, ostale opcije se zaključaju, a pitanje dobija oznaku "Odgovoreno ✓", što daje jasan i predvidljiv tok rješavanja kviza bez nepotrebnih zahtjeva ka serveru.
6. Pretraga kvizova 🔍
Dodano je polje za pretragu koje filtrira kvizove dok korisnik kuca, i to po nazivu kviza ili po nazivu predmeta, jer korisnik često zna predmet, a ne tačan naziv kviza — takva pretraga brže pronalazi traženi kviz kod dužih listi.
7. Profil i navigacija 👋
Dodan je dropdown meni sa ikonice profila (prikaz hasha, prijava/odjava) i poseban `ProfilScreen` sa avatarom, statistikom (dostupni / u toku / završeni kvizovi) i dugmetom za odjavu, čime korisnik dobija pregled svog napretka na jednom mjestu, a navigacija djeluje organizovanije.
8. Automatsko osvježavanje liste 🔄
Boja statusa kviza se ranije nije ažurirala automatski nakon završetka kviza. Dodan je `DisposableEffect` koji osluškuje `ON_RESUME` i sam povlači svježe podatke svaki put kad se korisnik vrati na listu, pa su podaci uvijek aktuelni bez ikakve dodatne akcije.
9. Pull-to-refresh ⬇️
Dodana je mogućnost povlačenja liste nadole za ručno osvježavanje, čime korisnik dobija kontrolu da provjeri ima li novosti kad god to poželi.
10. Progress bar i ekran rezultata u kvizu 📊
Tokom rješavanja kviza sada se prikazuje "Pitanje X/Y", a na kraju ekran sa brojem bodova i ukupnim brojem pitanja, pa korisnik prati napredak i dobija jasan pregled rezultata na kraju umjesto da kviz jednostavno nestane.
11. Dugme "Pokušaj ponovo" na grešci 🔁
Ranije je, u slučaju greške, korisnik morao povući listu za refresh da bi pokušao ponovo. Sada, intuitivnije, postoji jasno dugme uz poruku greške kako bi aplikacija ponovo pokušala učitati podatke.
12. Potvrda prije odjave ✅
Dugme "Odjavi se" je ranije odmah odjavljivalo korisnika. Sada se prikazuje `AlertDialog` s pitanjem za potvrdu, i na `ProfilScreen`-u i u dropdown meniju, čime se sprječava slučajan gubitak sesije jednim pogrešnim klikom.
---
🛠️ Poboljšanja — razvojna perspektiva
> Analiza je rađena iz ugla novog developera koji prvi put otvara projekat i treba nastaviti razvoj.
1. Use Case sloj 🧩
`KvizViewModel` je ranije sam radio i parsiranje datuma i filtriranje kvizova unutar jedne funkcije. U skladu s principima razdvajanja odgovornosti (Separation of Concerns), sva logika filtriranja izdvojena je u `FiltrirajKvizoveUseCase` (domain paket), dok ViewModel samo prosljeđuje odabrani filter i preuzima filtrirane rezultate koje prikazuje korisničkom interfejsu. ViewModel je sada kraći i čitljiviji, a logika filtriranja se može mijenjati nezavisno od UI sloja.
2. Repozitoriji ne gutaju greške ⚠️
Svi repozitoriji su ranije imali `catch (e: Exception) { emptyList() }`, što je značilo da je svaka greška, uključujući "nema interneta", pretvarana u praznu listu bez da ViewModel ikad sazna da se nešto loše desilo. Repozitoriji sada propuštaju grešku dalje (osim 404, gdje "ne postoji" ima smisla kao `null`), pa aplikacija stvarno može prikazati korisniku tačan razlog problema umjesto praznog ekrana.
3. Centralizovano parsiranje datuma 📅
Ista logika za pretvaranje datuma sa servera bila je kopirana na tri različita mjesta — u `KvizItem`-u, `KvizViewModel`-u i use case-u. Sada je izdvojena u `util/DateUtil.kt`, čime se eventualna izmjena formata datuma sa backenda radi na jednom mjestu umjesto na više.
4. Konstante umjesto "magičnih" vrijednosti 🔢
Referentno vrijeme, default URL backenda i boje statusa kviza bile su hardkodirane i razbacane u više fajlova. Sve su prebačene u `util/Constants.kt`, čime su važne vrijednosti sada vidljivije i na jednom mjestu.
5. Bolji raspored paketa 📦
Uočeno je da je `ApiConfig` (mrežna konfiguracija) smještena u `data/repositories` umjesto uz `ApiService` u `network` paketu, te da je funkcija za računanje preostalog vremena bila smještena među UI komponente. Svi su premješteni na logičnija mjesta, čime se brže pronalazi šta gdje pripada.
---
🔄 Tok korištenja aplikacije
Korisnik se prijavljuje hashom (demo hash: `demo`)
Na ekranu Upis može birati filter i upisati se na predmete/grupe
Na ekranu Kvizovi vidi listu, pretražuje i pristupa kvizovima
Klikom na kviz otvara se KvizDetalji — tu se pokreće kviz, odgovara na pitanja uz progress bar, i na kraju prikazuje rezultat
Na Profilu se prati statistika i po potrebi vrši odjava

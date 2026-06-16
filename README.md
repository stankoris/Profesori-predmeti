# Sistem za upravljanje profesorima i predmetima
 
Full-Stack web aplikacija razvijena korišćenjem Spring Boot tehnologije.
 
## Korišćene tehnologije
- Backend: JAVA (Spring Boot)
- Baza podataka: MySQL
- Frontend: HTML, CSS, JavaScript
 
## Funkcionalnosti
- Registracija i prijava korisnika
- CRUD operacije za profesore i predmete
- Role-based autorizacija (ADMIN/USER)
- Validacija podataka i exception handling

Projekat sadrži SQL skripte koje omogućavaju jednostavno kreiranje i inicijalizaciju baze podataka.

**profesori_predmeti_database_scheme.sql** – skripta koja sadrži strukturu baze podataka i koristi se za kreiranje svih potrebnih tabela.

**profesori_predmeti_data.sql** – skripta koja sadrži inicijalne test podatke koji se mogu koristiti za demonstraciju funkcionalnosti aplikacije.

U okviru test podataka definisana su dva korisnička naloga:

 ## Admin nalog
Username: admin
Password: admin123

 ## Korisnički nalog
Username: user
Password: user123
## 
Podaci za prijavu su takođe prikazani ispod login forme u aplikaciji radi lakšeg testiranja.
Korisnici se mogu registrovati putem registracione forme, pri čemu je moguće kreirati samo standardni korisnički nalog, dok se administratorski nalog ne može registrovati putem aplikacije.
##

Aplikacija omogućava pregled predmeta i profesora kada smo ulogovani kao User i izmeni, brisanje i dodavanje kada smo ulogovani kao Admin. Kada nismo ulogovani, aplikacija neće prikazivati ni predmete ni profesore.

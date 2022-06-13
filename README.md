# FilmwebScraper
A web application designed to download, store and display data from Filmweb website.
## Setup, build and run
* Clone repository and enter its directory:
```bash
$ git clone https://github.com/JakubRostowski/filmweb-scraper-web.git
$ cd filmweb-scraper-web
```
* Build and run via Docker:
```bash
$ docker compose up -d
```
## Additional info
* At this point, project uses **Admin** and **User** roles, both with their own functionality. Their login credentials are:
```text
Login: admin
Password: 123

Login: user
Password: 123
```
* There is no initial movie data, but you can force program to get it at endpoint:
```text
http://localhost:8080/api/update
```

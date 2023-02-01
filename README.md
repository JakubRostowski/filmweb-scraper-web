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
Note: "-d" parameter is optional - it prevents generating logs.
## Additional info
* At this point, project uses **Admin** and **User** roles, both with their own functionality. Their login credentials are:
```text
Login: admin
Password: 123

Login: user
Password: 123
```
* There is no initial movie data, but program will update it on startup.
You can disable it by setting ```scraper.update-after-startup``` in application.properties to ```false```.
If you want to update movies manually, you can do it at endpoint:
```text
http://localhost:8080/api/update
```
* By default, program updates database every midnight. You can modify it by overwriting ```scraper.cron-update-value``` property.

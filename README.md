# RoomMate

## Start

### ENV Variablen

Für RoomMate:

- `CLIENT_ID` - Client ID für die Authentifizierung
- `CLIENT_SECRET` - Client Secret für die Authentifizierung
- `POSTGRES_PASSWORD` - Passwort für die Datenbank
- `POSTGRES_USER` - Benutzername für die Datenbank
- `ADMINS` - Liste von Admin Usernames, getrennt durch Kommas (z.B. `admin1,admin2`)

Client_ID und Client_Secret bei GitHub hergestellt werden.
Die Datenbank wird automatisch erstellt über docker-compose.yml.

Für KeyMaster:

- `ROOMMATE_ENDPOINT` - Endpunkt für die Kommunikation mit RoomMate (hier: `/api/access`)
- `ROOMMATE_URL` - URL für die Kommunikation mit RoomMate (hier: `http://localhost:8080`)

## Bedienung

### Roommate

Authentifizieren Sie sich zunächst mit GitHub, damit sie RoomMate nutzen können.

Zunächst sollte die Datenbank leer sein.
Ein Admin kann Räume und Sitze Erstellen. Equipments müssen erst auf aus der Admin-Seite Equipments hinzugefügt werden.

Ein Admin sieht immer einen "Go to Admin Page" Button, der ihn zur Admin-Seite bringt und einen "Edit"-Button, der ihn in den Edit-Modus bringt.

Jeder User kann sich einen Sitzplatz reservieren. Die Reservierung kann auch wieder gelöscht werden.
Alle Reservierungen von einem User können auf der Profil-Seite eingesehen werden. Dazu drücken sie auf ihren Username in der Navbar. oder gehen sie auf `/<username>`.

Man kann auch auf die Profile anderer User gehen und sich deren Reservierungen ansehen.

Admins können auch die Reservierungen von anderen Usern löschen und Seats zu einem gewissen Zeitpunkt sperren. Sperrungen löschen überlappende Reservierungen von Usern. Sperrungen von Admins dürfen sich jedoch überschneiden.

### KeyMaster

Wenn Sie eine Reservierung in einem Raum haben, Können sie Ihren Username und den Namen des Raumes im KeyMaster eingetragen. Nach spätestens 2 Minuten synchronisieren sich die Reservierungen mit KeyMaster und sie können sehen, dass ihr zugriff zum Raum im KeaMaster sichtbar ist.
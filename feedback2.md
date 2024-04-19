# Feedback

Das Projekt funktioniert bisher ganz gut. Es gibt aber noch einige Probleme, die du beheben solltest.

## Modellierung der Geschäftslogik

## Aggregate
Ich bin mir nicht sicher wie viele Aggregate du hast. Da keine klaren Grenzen zwischen den Aggregaten definiert sind.
Wenn man sich die Services scheint, es als ob du drei Aggregate hast. Room, Seat und Reservation.
Allerdings, wenn man sich die Objekte selbst anschaut existiert anscheinend nur noch eins, und zwar Room. Und die anderen Objekte sind Teil davon.

Du nutzt eine abgeschlossene Menge an Equipment. Das heißt, falls wir jetzt zum Beispiel auch Kabel abbilden wollen, wie das Programm neu kompilieren müssen.

## Architektur und Struktur
Du hast die Onion Architektur fast richtig umgesetzt. Allerdings liegen die Application Services in der Domain. Das ist nicht schlimm, aber es ist nicht die Onion Architektur.
Die Anbindung mit KeyMaster ist auch noch nicht vollständig.

## Authentifikation und Autorisierung
Du hast die Authentifikation und Autorisierung gut umgesetzt. 
Das einzige was noch fehlt die Admins, außerhalb des Codes zu definieren.
Allerdings hast du das Client_Secret mithilfe von Git geleaked. Das ist nicht gut. Du solltest Secrets niemals in Git speichern. Wenn du Tutoren mitteilen willst, wie sie das Projekt starten können, dann kannst du das zum beispiel in der README.md machen.
Dort beschreibst du wie man sich entsprechende Secrets selbst generieren kann.


## Accessibility 
Die Anwendung ist zwar mit reiner Tastatur bedienbar, allerdings ist die Struktur teilweise nicht so sinnvoll.
Zum Beispiel muss man bei der Suche dreimal die Seite wechseln um zur tatsächlichen Reservierung zu kommen. Wo man, dann auch nochmals die Reservierungszeit angeben muss.
Das ist unintuitiv und sollte, wenn möglich geändert werden. Ist aber nicht so schlimm. 

## Betrieb
Funktioniert alles 

## Code
Du hast noch einige Spotbugs Fehler. Die solltest du beheben.

## Dokumentationen
Fehlt  noch.

## Funktionalität
Der Admin kann nicht beliebiges Equipment hinzufügen. Nach diesem kann auch nicht gesucht werden.
Die Belgeung ist auch noch nicht in Keymaster.
Aktuell lassen sich auch noch Reservierungen erstellen die vor der aktuellen Zeit liegen. Und auch Reservierungen wo die Endzeit vor der Startzeit liegt.


## Testing
Deine Tests hängen aktuell alle davon ab, ob tatsächlich die Datenbank läuft. 
Du solltest in den meisten Fällen Mocks verwenden. So wie ich das sehe hast du keinen einzigen UnitTest, sondern nur Integrationstests.
Im Normalfall sollte die Anzahl der Integrationstests deutlich geringer sein als die Anzahl der UnitTests. 
Auch läuft dein Test zur Onion Architektur nicht. Das ist nicht schlimm, aber du solltest das beheben.
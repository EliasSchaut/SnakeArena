# Programmierwettbewerb SnakeArena

Jeder kennt das Spiel [Snake](https://en.wikipedia.org/wiki/Snake_(video_game_genre)). \
Man stelle sich nun vor, dass Spielbrett sei um einiges großer und es gäbe mehrere Schlangen und Äpfel. Doch die Schlangen werden nicht von Menschen gesteuert, sondern von KIs.

Genau darum geht es in diesem Programmierwettbewerb:\
Genau zwei Erstis bilden ein Team. Ein Team soll die KI einer Schlange programmieren. Diese wird dann mit den Schlangen der anderen Teams zusammen auf ein Spielbrett gepackt und das Spiel beginnt!

## Modalitäten
* Programmiersprache: Java 15 (da essenziell im 1. Semester)
* Teamgröße: genau zwei lila Erstis
* Hilfsmittel: Alles erlaubt (außer schummeln)!
* Code: Der gesamte Sourcecode steht allen Teams in diesem Git-Repo zur Verfügung, sodass alle Teams ihre Schlangen ordentlich testen können. 
* Abgabe: Ausschließlich die Klasse MySnake auf einer Abgabeplattform. Das heißt, dass man den restlichen Code nicht umschreiben sollte, da am Ende wirklich nur MySnake zählt.
* **Deadline**: Donnerstag, 29.10. um 12 Uhr

## Spezifikation der Abgabe
* Die Abgabe *MySnake* muss drei Kriterien erfüllen, um akzeptiert zu werden:
  * Die öffentliche Variable **NAME** muss mit einem beliebigen Teamnamen gesetzt sein
  * Die öffentliche Variable **COLOR** muss mit einer beliebigen Teamfarbe gesetzt sein
  * Die öffentliche Funktion **think()** muss mit Code gefüllt werden.
* Zudem scheiden Schlangen, die versuchen zu schummeln, aus. Schummeln meint hierbei von seiner eigenen Schlange Code von anderen nicht dafür vorgesehenen Klassen zu manipulieren z.B. über shared memory irgendwie auf BoardLogic zugreifen und die kill()-Methode bei jeder anderen außer seiner Schlange auszuführen. Der Code aller Schlangen wird nach der Deadline händisch kontrolliert!
* **Abgabesystem:** https://snake.president-code.golf/

## Regeln
### Allgemeine Spielregeln
* Durch Drücken der Taste P wird das Spiel pausiert bzw. fortgesetzt. Das Spiel startet standardmäßig pausiert.
* Das Spiel ist rundenbasiert
* In jeder Runde wird die think() Methode jeder Schlange aufgerufen und deren Rückgabe für die Schlangenbewegungen verwendet
* Nach jeder Runde wird das Ergebnis aller Schlangenbewegungen in der Anwendung angezeigt
* Ein Spiel endet, wenn nur noch eine Schlange an einem Rundenanfang am Leben ist oder das Spiel sich sichtbar in ein nie endenden Prozess befindet
* Das Spielfeld hat eine Größe von 20 mal 20 Feldern

### Schlangen Start
* Alle Schlangen starten mit der Länge drei
* Alle Schlangen werden zu Beginn eines Spiels zufällig auf freien Feldern des Spielbretts verteilt
* Alle Schlangen starten als horizontale Linie mit dem Kopf auf der ganz rechten Seite dieser Linie

### Schlangen Bewegung
* Alle Schlangen bewegen sich pro Runde nur einen Schritt
* Die Schlange darf sich nur in eine der vier Richtungen hoch, rechts, runter oder links bewegen
* Die Bewegung der Schlange geht immer von ihrem Kopf aus

### Schlangen Besonderheiten
* Ist eine Schlange einen Apfel, wird sie um ein Feld länger. 
* Eine Schlange darf nicht unendlich überlegen. Wenn die Ausführung der think() Methode in der Schlange länger als 10 Millisekunden dauert, wird diese abgebrochen und sie läuft einen Schritt nach rechts

### Schlangen Sterben & Leben
* Eine Schlange **lebt** weiter, wenn sie folgende Felder betreten würde:
  * Ein leeres Feld mit nichts drinnen
  * Ein Feld mit einem Apfel darin
  * Ein Feld in dem der eigene Schlangenschwanz drinnen ist
* Eine Schlange **stirbt**, wenn sie folgende Felder betreten würde:
  * Ein Feld mit einem Schlangenkörper darin (außer der eigene Schlangenschwanz)
  * Ein Feld mit einer Barriere darin
  * Ein Feld, das außerhalb des Spielfeldes ist
* Stirbt eine Schlange, wird sie zu einer grauen Barriere, die ihre aktuelle Position stetig beibehält. Diese ist ab sofort nicht mehr steuerbar.

### Äpfel
* Alle Apfel sind rot
* Es gibt immer maximal zwei Äpfel auf dem Spielfeld
* Alle Äpfel werden immer zufällig auf freien Felder platziert
* Wird ein Apfel gegessen, wir ein neuer platziert

## Punkteverteilung
Die Punkteverteilung pro Spiel richtet sich nach der Länge der Schlangen am Ende eines Spiels. Im Folgenden eine Auflistung der Punkte, die man pro Spiel bekommen kann:
1. Platz und somit längste Schlange - 3 Punkte
2. Platz und somit zweitlängste Schlange - 2 Punkte
3. Platz und somit drittlängste Schlange - 1 Punkt

## Sieg
* Alle Schlangen werden zufällig auf vier möglichst gleich große Gruppen aufgeteilt und spielen dort zwei Spiele. 
* Die Schlange mit der höchsten Punktzahl (bei Gleichstand beide) in einer Gruppe gilt nach den zwei Spielen als Sieger der Gruppe.
* Daraufhin werden alle Sieger aller Gruppen in ein episches Finale geworfen. Dort werden drei Spiele gespielt.

Der 1. Platz aus dem Finale bekommt 2 Säcke Schokolade.\
Der 2. Platz aus dem Finale bekommt 1 Sack Schokolade.

# Programmierwettbewerb SnakeArena

Jeder kennt das Spiel [Snake](https://en.wikipedia.org/wiki/Snake_(video_game_genre)) \
Man stelle sich nun vor, dass Spielbrett sei um einiges großer und es gäbe mehrere Schlangen und Äpfel. Doch die Schlangen werden nicht von Menschen gesteuert, sondern von KIs.

Genau darum geht es in diesem Programmierwettbewerb:\
Genau zwei Erstis bilden ein Team. Ein Team soll die KI einer Schlange programmieren. Diese wird dann mit den Schlangen der anderen Teams zusammen auf ein Spielbrett gepackt und das Spiel beginnt!

## Modalitäten
* Programmiersprache: Java (da essenziell im 1. Semester)
* Der gesamte Sourcecode steht allen Teams in diesem Git-Repo zur Verfügung, sodass alle Teams ihre Schlangen ordentlich testen können. Jedoch darf am Ende nur die Schlange MySnake abgegeben werden. Das heißt, dass man den restlichen Code nicht umschreiben sollte, da am Ende wirklich nur MySnake zählt.
* Allgemein ist der Code recht gut dokumentiert und nicht besonders groß. Es lohnt sich also, sich die Dokumentation durchzulesen, wenn sich Fragen zum Code finden. Am wichtigsten ist dabei vermutlich die Klasse BoardInfo. Durch diese kann die KI der Schlange alle nötigen Informationen über das Spielfeld bekommen.
* Es gibt eine Schlange DebugSnake. Diese ist mit den Pfeiltasten steuerbar. Diese dient ausschließlich zum Testen der KI von MySnake.
* Man kann das Spiel durch drücken auf die Taste P pausieren bzw. fortsetzen. Das Spiel startet standartmäßig pausiert.

## Spezifikation der Aufgabe
Die Schlange, die ein Team programmieren soll, ist die Klasse MySnake, die von der abstrakten Klasse Snake erbt. Sie ist wie folgt aufgebaut:
* MySnake besitzt eine öffentliche Variable NAME. Hier darf sich das Team einen kreativen Schlangennamen überlegen. Dieser wird dann auch neben dem Spielbrett gezeigt 
* MySnake besitzt eine öffentliche Variable COLOR. Hier darf sich das Team eine Farbe überlegen. Diese Farbe hat dann die Schlange und der Schlangenname neben dem Spielbrett
* MySnake besitzt eine öffentliche Funktion think. Hier soll das Team die KI der Schlange implementieren. \
Grob gesagt bekommt die Funktion think als Parameter alle wichtigen Informationen über das Spielbrett. Aus diesen muss die Funktion eine Richtung berechnen und diese zurückgeben. Es gibt die vier Richtungen: hoch, rechts, runter und links. Die Werte der Richtungen sind als öffentliche statische finale globale Variablen aus der Oberklasse Snake ansprechbar. \
Der Parameter, den die think-Methode bekommt, ist eine Instanz der Klasse BoardInfo. Dort sind alle wichtigen Infos über das Board enthalten. Ein Blick in die Klasse lohnt sich. Hier sind die entsprechenden Methoden genau dokumentiert.

## Spezifikation zum Spielfeld
* Das Spielfeld hat eine größe 20 mal 20 Feldern
* Es gibt immer genau 2 Äpfel auf dem Spielfeld

## Regeln
Es gelten folgende Regeln für ein Spiel:
* Ein Spiel läuft so lange, bis nur noch eine Schlange am Leben ist,
* Alle Schlangen starten mit der Länge drei
* Alle Schlangen werden zu Beginn eines Spiels zufällig auf freien Feldern des Spielbretts verteilt
* Alle Schlangen starten als horizontale Linie mit dem Kopf auf der ganz rechten Seite dieser Linie
* Alle Schlangen bewegen sich pro Runde nur einen Schritt
* Die Schlange darf sich nur in eine der vier Richtungen hoch, rechts, runter oder links bewegen
* Die Bewegung der Schlange geht immer von ihrem Kopf aus
* Stirbt eine Schlange, wird sie zu einer grauen Barriere, die ihre aktuelle Position stetig beibehält. Diese ist ab sofort nicht mehr steuerbar. 
* Stößt eine Schlange durch ihrern Schritt gegen ein Hindernis (den Rand des Spielfeldes, eine andere Schlange oder Barrieren), stribt sie
* Eine Schlange darf nicht unendlich überlegen. Wenn die Ausführung der think Methode in der Schlange länger als X Millisekunden dauert, wird diese abgebrochen und sie läuft  einen Schritt nach rechts
* Alle Apfel sind rot
* Alle Äpfel werden immer zufällig auf freien Felder plaziert
* Ist eine Schlange einen Apfel, wird sie um ein Feld länger und es erscheint ein neuer Apfel zufällig auf dem Spielfeld

## Punkteverteilung und Sieg
Die Punkteverteilung pro Spiel richtet sich nach der Länge der Schlangen am Ende eines Spiels. Im Folgenden eine Auflistung der Punkte, die man pro Spiel bekommen kann:
1. Platz und somit längste Schlange - 3 Punkte
2. Platz und somit zweitlängste Schlange - 2 Punkte
3. Platz und somit drittlängste Schlange - 1 Punkt

Gespielt wird 3 Spiele. Danach werden alle Punkte aus allen Runden von allen Teams zusammenaddiert und es gewinnt das Team mit den meisten Punkten.

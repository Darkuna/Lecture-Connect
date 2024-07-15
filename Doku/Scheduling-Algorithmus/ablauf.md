# Ablauf der Zuweisung von CourseSessions zu RoomTables 

1. Vorbedingungen prüfen
   1. Ist die verfügbare Zeit für Kurse ohne Computerzwang >= die benötigte Zeit?
   2. Ist die verfügbare Zeit für Kurse mit Computerzwang >= die benötigte Zeit?
   (*Anmerkung: hier könnte noch eingebaut werden, dass, wenn noch genug Zeit in Räumen
   mit Computern übrig ist und zu wenig in Räumen ohne Computer, sollten auch Kurse ohne
   Computerzwang in Computerräume zugeteilt werden*)

2. Kurse ohne Computerzwang Räumen ohne Computern zuweisen
   1. Liste der Kurse in 3 Listen aufteilen:
      1. CourseSessions von Gruppenkursen
      2. CourseSessions von Kursen mit Split
      3. Einzelne CourseSessions (von Kursen, die nur eine CourseSession haben)
   2. Einzelne CourseSessions zuweisen:
      1. Für jede CourseSession in der Liste:
         1. Eine Queue mit möglichen Kandidaten für eine Zuweisung füllen
            1. Besteht jeweils aus dem zeitlich erstmöglichen und einem zufälligen Kandidaten jedes RoomTables
         2. Einen Kandidaten finden, der folgende Constraints erfüllt:
            1. Die Raumkapazität muss groß genug sein, um die Teilnehmeranzahl des Kurses abzudecken, sollte aber auch nicht mehr als doppelt so groß als die Teilnehmeranzahl sein (verhindert, dass z.B. PS in einem großen Hörsaal stattfinden).
            2. Die Zeitslot, in dem sich der Kandidat befindet, darf sich nicht mit einem oder mehreren TimingConstraints der CourseSession überschneiden.
            3. CourseSessions desselben Semesters dürfen nicht in anderen Räumen zur selben Zeit zugewiesen sein.
         3. CourseSession dem Kandidaten zuweisen und als zugewiesen markieren.
   3. CourseSessions von Gruppenkursen zuweisen:
      1. Für alle CourseSessions desselben Kurses:
         1. Eine Liste aller Kandidaten eines zufälligen Tages erstellen
         2. Alle Kandidaten filtern, die die obigen Constraints nicht erfüllen
         3. Wenn die Anzahl potentieller Kandidaten größer als die Anzahl der CourseSessions `n` ist, werden sie den ersten `n` Kandidaten zugewiesen. (*Anmerkung: möglicherweise werden bessere Ergebnisse erzielt, wenn die Liste vor der Zuweisung nochmal zufällig umverteilt*).
         4. Wenn die Anzahl der Kandidaten nicht ausreichend ist, wird wieder bei 3.1.1 begonnen, aber stattdessen mit dem nächsten Tag. (*Anmerkung: Es könnte besser sein, statt mit dem nächsten Tag zu beginnen, stattdessen mögliche Kandidaten des nächsten Tages - oder dem Tag davor, falls es ein Freitag ist - einfach in die Liste hinzuzufügen, um die Gesamtzahl möglicher Kandidaten besser zu nutzen*).
   4. CourseSessions von Splitkursen zuweisen:
      1. 
3. Kurse mit Computerzwang Räumen mit Computern zuweisen
   1. Selbe Vorgehensweise wie für Kurse ohne Computerzwang
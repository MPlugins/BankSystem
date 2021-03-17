# BankSystem von [Marvin Leiers](https://www.marvinleiers.de)

* Bargeld + Bankkonto
* Startguthaben
* Sicherer Itemvault für jeden Spieler
* Einfach zu bedienen
* Kompatibel mit Datenbank


## Allgemeines

Das BankSystem führt eine digitale Währung (Name änderbar) in das Spiel ein. Deine Spieler können also eine eigene Wirtschaft mit ihren eigenen Regeln erstellen. Das Plugin ist mit **Vault** kompatibel, das bedeutet, dass andere Plugins ohne Umwege auf deine Währung zugreifen können (Shops z.B) Im Falle eines Betrugs, kann das Szenario ganz einfach rekonstruiert werden, denn sämtliche Überweisungen / Transaktionen werden in der Datenbank erfasst und gesichert. 

Neue Spieler erhalten 5000 Bargeldeinheiten der Währung, sobald sie den Server betreten. Mit **/bank** können Spieler ein Bankkonto erstellen, um ihr Geld sicher zu lagern. Denn Spieler die sterben, lassen einen **15% - 35% Anteil** ihres Bargeldes fallen, den andere Spieler aufheben können.

Spieler, die bereits eine Konto haben, können mit **/bank** ihre Finanzen und Kostbarkeiten verwalten. Mit einem Klick auf **Einzahlen** können die Spieler durch Eingabe in den Chat die einzuzahlende Summe festlegen, analog funktioniert die Funktion des **Auszahlen**s. Mit einen Klick auf **Schließach** wird den Spielern eine Möglichkeit geboten, ihre kostbarsten Gegenstände in einem 27-Slot großen, privaten Schließfach zu lagern, auf das kein anderer Zugriff hat.


## Befehle

* **/money**, **/geld** (*permssion: befehl.money*): Zeige den aktuellen Stand deines Bar-/Geldes an.
* **/pay** <Spieler> <Anzahl> (*permission: befehl.money*): Überweise einen Betrag deines Geldes auf das Konto eines anderen Spielers.

# Handbuch zum Nutzung Server-Komponente und Client-Komponente

Inhaltsverzeichnis:

- [Handbuch zum Nutzung der Website "PARLIAMENT SENTIMENT RADAR"](#Handbuch-zum-Nutzung-der-Website "PARLIAMENT SENTIMENT RADAR")
  -[Einleitung](##Einleitung)
    -[Borwort](###Vorwort)
    -[Vorbereitung](###Vorbereitung)
  -[Frontend](##Frontend)
    -[Chart Visualisation](###Chart-Visualisation)
    -[Filter Funktion](###Filter-Funktion)
    -[Volltext Funktion](###Volltext-Funktion)
  -[Backend](##Backend)  

## Einleitung

### Vorwort

Die Anwendung ist browserbasiert. Die Adminstration läuft in folgenden Browsern:

``` markdown
- Internet Explorer
- Mozilla Firefox 
- Safari 
- Chrome 
```

Dieses Handbuch ist zur Erläuterung der Verwendung des gemeinschaftliche Gruppenabschlussprojekt PRG-Praktikum WiSe 2021/2022 "Parliament Sentiment Radar" zuständig.
Die Lösung dieses Gruppenprojekts haben sich die Teilnehmerinnen jeweils in Gruppen zusammengefunden. Hierzu verwenden wir, wie auch schon in den vorherrigen Übungen, GitLab.

Um alle Anwendungsfunktionen nutzen zu können, müssen Sie in Ihren Browsereinstellungen Folgendes zulassen:

```markdown
- Ausführung von Java Script
- Verwendung von Sitzungscookies
- Öffnen von Popup-Fenstern
```

### Vorbereitung

<details open>
<summary>Hide Information</summary>
Unsere Gruppe hat zur Bearbeitung der Abschlussaufgabe ein privates Projekt namens "Gruppe_8_Mittwoch_3_ParliamentSentimentRadar" in Gitlab gestellt und hat unserer Tutorin das Projekt mit der Berechtigung lesend frei gegeben.

Alles JavaScript-Dateien wurden ausführlich dokumentiert.

Das "Parliament Sentiment Radar" besteht aus zwei Komponenten:  einer Server-Komponente, implementiert in Java, sowie einer Client-Komponente, implementiert
in JavaScript unter Verwendung von SB Admin 2 und beinhaltet folgenden Funktionalitäten:

```markdown
  1. Einlesen von Parlamentsdebatten des Deutschen Bundestages im XML-Format.
  2. Analysieren, Aufbereiten und datenbankgestütztes Abspeichern der abgefragten Parlamentsreden.
  3. Verarbeitung der Parlamentsreden mittels angegebener NLP-Verfahren.
  4. Datenbankgestütztes Abfragen und Auswerten der Ergebnisse.
  5. Implementierung eines RESTful Webservice zur Bereitstellung    der Parlamentsreden für die Client-Komponente.
  6. Implementierung einer Visualisierungskomponente unter Verwendung von SB Admin 2 gemäß Aufgabe 3.
```

Unsere Server-Komponente ist unter Verwendung von Java Spark als RESTful Webservice implementiert.
</details>

Zum Testen und Dokumentieren der REST-Abfragen verwenden wir [__swagger.io__](http://38.242.210.53/).

## Frontend

Die Hauptaufgabe von Frontend-Teil dient dazu, die Ergebnisse mittels SB Admin 2 gemäß der definierten Aufgaben zu visualisieren und analysieren. Es gibt zwei Websites in unsere Frontend-Teil.

### Main Website - Dashboard

![image](https://cdn.discordapp.com/attachments/871441710373277746/946134892041879572/a83c0bad8098d7e5a3c24aa1195cc2c.png)

### Chart Visualisation

Das Dashboard __"Übersicht"__ visualisiert die gesamte Parlament Daten aus unsere Datenbank:

```markdown
- Tokenchart
- Poschart
- Sentimentchart
- Named Entities chart
- Rednerchart mit Redner-Bild als Hover-Effekt
```

### Filter Funktion

Man kann im oberen Seitenbereich je nach dem Suchfeld frei entscheiden, ob man durch Dropdown-Menü oder selbst tippen, die entsprechenden Filterergebnisse suchen:

- Speaker-ID (Format: Dropdown-Menü oder 8-stellige ID-Zahlen wie z.B. 11004908)
  ![speaker](https://cdn.discordapp.com/attachments/871441710373277746/946526903014137936/unknown.png)
- Fraktionsnamen,
  hier ist in dem Bild als Beispiel "FDP" geschrieben (Format: Dropdown-Menü oder Fraktionszugehörigkeit)
  ![fraction](https://cdn.discordapp.com/attachments/871441710373277746/946526903278399538/unknown.png)
- minimale Anzahl z.B. für Token, Pos usw.
  hier ist in dem Bild als Beispiel "1000" geschrieben (Format: Dropdown-Menü oder 3 bis 4-stellige Zahlen)
  ![minimum](https://cdn.discordapp.com/attachments/871441710373277746/946526903504883732/unknown.png)
- Beginn- und End-Zeitraum (Format: Dropdown-Menü)
- ![time](https://cdn.discordapp.com/attachments/871441710373277746/946526903769129010/unknown.png)
  
Es wird nach Klicken ![search](https://cdn.discordapp.com/attachments/871441710373277746/946526904029163530/unknown.png)   ein sogenannte __"Sub-Dashboard"__ angezeit (mit Suchanfragetitel), dies enthält die gleichen Visualisierungen und Optionen wie dem "Main" Dashboard, jedoch nur für die Suchanfrage.

In dem Bild sieht man, dass man kein SpeakerID aber Fraktion "FDP" und einen minimale Anzahl "1000" eingegeben hat.
![filterexample](https://cdn.discordapp.com/attachments/871441710373277746/946705721712607252/unknown.png)

Das Sub-Dashboard kann durch Button <font color =red>"Delete sub dashboard"</font> wieder entfernt werden.

### Sub Website - Reden

### Volltext Funktion

Links bei der Sidebar unter "Dashboard" sieht man die Website "reden". Die Volltext-Visualisierung ist für jede einzelne Parlaments-Rede implementiert und beinhaltet folgenden Funktionen:

![fulltext](https://cdn.discordapp.com/attachments/871441710373277746/946523390867566672/rede_beispiel.png)

Alle im Text gefundenen Named Entities werden farblich hinterlegt.

$\textcolor{red}{Person}$
$\textcolor{blue}{Ort}$
$\textcolor{green}{Organisation}$

- Der Sprech*innenname, Informationen zu seiner/ihrer Partei- und Fraktionszugehörigkeit, sowie ein Foto(sofern vorhanden) werden im Kopf der Rede angezeigt. (Bei dem obigen Beispiel ist der Abgeordnete Name: Marc Bernhard, Fraktionszugehörigkeit: AfD)
- Ermöglichung eines Filters über eine Navigation durch die Tagesordnung der Protokolle.
- ![fulltextfilter](https://cdn.discordapp.com/attachments/871441710373277746/946532642097078292/unknown.png)
- Man wählt mit dem DropDown-Menü in der Reihenfolge:
    1. Wahlperiode
    2. Sitzung
    3. Tagesordnungspunkte
    4. RedeID

## Backend

Flussdiagramm für Backend:
![flussdiagramm](asset/flussdiagramm.png)

Zuerst lesen die XML File ein. Dann verwendet Factory, um die Daten in unsere eigene Datenstruktur zu speichern. Dann schreiben die Daten in MongoDB ein. Verwendet wieder die Fabrik, um Daten aus MongoDB zu lesen. Und implementiert Restful -Schnittstelle. Auf diese Weise kann der Frontend über diese Restful-Schnittstelle visualisiert werden.

Funktionsweise von Backend ist nicht relevant für Benutzer. Deshalb werden hier keine Details genannt. Die Details werden während des Vorrechnen erläutert.

### XML File einzulesen

Führt Klasse ProtocolMongoDBWriter aus. Durch Main-Methode erstellt Object handler von Klasse MongoDBConnectionHandler. Dann können wir mit MongoDB verbinden. Über ParliamentFactory lest die XML File ein.

```java
MongoDBConnectionHandler handler = new MongoDBConnectionHandler("config/config.json");
String protocolDirectory = "Daten/20. Wahlperiode";
ParliamentFactory factory = new ParliamentFactory_Impl();
factory.initFromDirectory(protocolDirectory);
writeProtocols(factory.getProtocols(), handler);
writeSpeakers(factory.getParliamentMembers(), handler);
writeSpeechs(factory.getSpeeches(), handler);
writeFractions(factory.getFractions(), handler);
```

### Methode von MongoDB

Über das Objekt handler von MongoDBConnectionHandler kann man folgende Methode verwenden.
Diese Methode werden in Klasse MongoDBConnectionHandler gespeichert.

- Erstellen
- Einschreiben
- Lesen
- Updaten
- Löschen

#### Erstellen

Wenn wir das Project ausführen, wird MongoDBConnectionHandler schon ergestellt.

#### Einschreiben

1.Schreib nur ein Dokument in MongoDB ein:
Mit diesem Befehl kann man einen Dokument in eine spezielle Collection in MongoDB einschreiben.

```java
handler.writeDocument(collectionName, document);
```

2.Schreib mehrerer als eins Dokumente in MongoDB ein:
Mit diesem Befehl kann man mehrere Dokumente in eine spezielle Collection in MongoDB einschreiben.

```java
handler.writeDocuments(collectionName, documents);
```

#### Lesen

Suche nach einem bestimmten Dokument in einer bestimmten Collection in MongoDB.
Kann man durch diesem Befehl erreichen:

```java
handler.getDocument(collectionName, query);
```

#### Updaten

Mit diesem Method kann man die Dokument, der in eine spezielle Collection in MongoDB ist, updaten.
Kann man mit diesem Befehl erreichen:

```java
handler.updataDocument(collectionName, query, newDocument);
```

Wenn die Dokument erfolgreich updatet wird, geben ture aus.

#### Löschen

Mit diesem Method kann man die Dokument, der in eine spezielle Collection in MongoDB ist, löschen.
Kann man mit diesem Befehl erreichen:

```java
handler.deleteDocument(collectionName, query);
```

Wenn die Dokument erfolgreich gelöscht wird, geben ture aus.

### RESTful Schnittstelle

Wenn die XML File eingelesen und NLP analysiert werden, werden durch ParliamentFactory in MongoDB eingeschriben. Aber das Frontend kann jedoch keine Daten direkt von MongoDB für die Visualisierung abrufen. Deshalb verwenden wir hier RESTful Schnittstelle.

Hier ist unsere Swagger.
![swagger](asset/swagger_1.png)
![swagger](asset/swagger_2.png)

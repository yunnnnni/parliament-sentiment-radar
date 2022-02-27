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
    -[XML File einzulesen](###XML-File-einzulesen)
    -[Methode von MongoDB](###Methode-von-MongoDB)
      -[Erstellen](####Erstellen)
      -[Einschreiben](####Einschreiben)
      -[Lesen](####Lesen)
      -[Updaten](####Updaten)
      -[Löschen](####Löschen)
    -[RESTful Schnittstelle](###RESTful-Schnittstelle)
      -[Return protocols](####Return-protocols)
      -[Return speakers](####Return-speakers)
      -[Return speeches](####Return-speeches)
      -[Return fractions](####Return-fractions)
      -[Return annotations](####Return-annotations)

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

Führt die *Main-Methode* der ProtocolMongoDBWriter-Klasse aus. Der Object *handler* für die Klasse MongoDBConnectionHandler wird über die Main-Methode erstellt. Wir können dann eine Verbindung zu MongoDB herstellen. Die Parlamentstexte werden direkt vom Bundestag heruntergeladen. Die XML-Datei wird dann über ParliamentFactory eingelesen.

```java
public static void main(String[] args) {
  MongoDBConnectionHandler handler = new MongoDBConnectionHandler("config/config.json");
  String protocolDirectory = "Daten/20. Wahlperiode";
  ParliamentFactory factory = new ParliamentFactory_Impl();
  factory.initFromDirectory(protocolDirectory);
//    writeProtocols(factory.getProtocols(), handler);
  writeSpeakers(factory.getParliamentMembers(), handler);
//    writeSpeechs(factory.getSpeeches(), handler);
//    writeFractions(factory.getFractions(), handler);
}
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
Verwenden diese Methode *writeDocument* von Klass MongoDBConnectionHandler:

```java
public void writeDocument(String collectionName, Document document){
  try{
      MongoCollection<Document> collection = this.getCollection(collectionName);
      collection.insertOne(document);
    } catch (Exception e){
        e.printStackTrace();
    }
}
```

Mit diesem Befehl kann man einen Dokument in eine spezielle Collection in MongoDB einschreiben.

```java
handler.writeDocument(collectionName, document);
```

2.Schreib mehrerer als eins Dokumente in MongoDB ein:
Verwenden diese Methode *writeDocuments* von Klass MongoDBConnectionHandler:

```java
public void writeDocuments(String collectionName, List<Document> documents){
  try{
      MongoCollection<Document> collection = this.getCollection(collectionName);
      collection.insertMany(documents);
  } catch (Exception e){
      e.printStackTrace();
  }
}
```

Mit diesem Befehl kann man mehrere Dokumente in eine spezielle Collection in MongoDB einschreiben.

```java
handler.writeDocuments(collectionName, documents);
```

#### Lesen

Suche nach einem bestimmten Dokument in einer bestimmten Collection in MongoDB.
Verwenden diese Methode *getDocument* von Klass MongoDBConnectionHandler:

```java
public Document getDocument(String collectionName, Bson query){
  try{
      MongoCollection<Document> collection = this.getCollection(collectionName);
      return collection.find(query).first();
  } catch (NullPointerException e){
      System.err.println("MongoDBConnectionHandler.getDocument: collection " + collectionName + " not found!");
      return null;
  } catch (Exception e) {
      e.printStackTrace();
      return null;
  }
}
```

Kann man durch diesem Befehl erreichen:

```java
handler.getDocument(collectionName, query);
```

#### Updaten

Mit diesem Method kann man die Dokument, der in eine spezielle Collection in MongoDB ist, updaten.
Verwenden diese Methode *updataDocument* von Klass MongoDBConnectionHandler:

```java
public boolean updataDocument(String collectionName, Bson query, Document newDocument){
  MongoCollection<Document> collection = this.getCollection(collectionName);
  try{
      UpdateResult result = collection.replaceOne(query, newDocument);
      if (result != null && result.getModifiedCount() == 0 && result.getMatchedCount() == 0){
          try {
              this.getCollection(collectionName).insertOne(newDocument);
              return true;
          }
          catch (Exception e){
              System.out.println(e.getMessage());
          }
      }
  } catch (Exception e){
      e.printStackTrace();
  }
  return false;
}
```

Kann man mit diesem Befehl erreichen:

```java
handler.updataDocument(collectionName, query, newDocument);
```

Wenn die Dokument erfolgreich updatet wird, geben ture aus.

#### Löschen

Mit diesem Method kann man die Dokument, der in eine spezielle Collection in MongoDB ist, löschen.
Verwenden diese Methode *deleteDocument* von Klass MongoDBConnectionHandler:

```java
public Boolean deleteDocument(String collectionName, Bson query){
  try {
      MongoCollection<Document> collection = this.getCollection(collectionName);
      DeleteResult result = collection.deleteOne(query);
      System.out.println("Deleted document count: " + result.getDeletedCount());
      return true;
  } catch (Exception e) {
//      System.err.println("Unable to delete due to an error: " + e);
      e.printStackTrace();
      return false;
  }
}
```

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

#### Return protocols

*Get protocol:*

Sollt man hier zwei Parameters *session* und *term* eingeben.
Nach Eingabe der Parameter klicken Sie auf die Schaltfläche *Try it out*:
![protocol_1](asset/protocol_1.png)

Struktur der Ergebnisse:
![protocol_2](asset/protocol_2.png)

*Get protocols:*

Hier müssen keine Parameter eingegeben werden.
Klicken Sie auf die Schaltfläche *Try it out*:
![protocols_1](asset/protocols_1.png)

Struktur der Ergebnisse:
![protocols_2](asset/protocols_2.png)

*Get agendaitem:*

Sollt man hier drei Parameters *session*, *term* und *id* eingeben.
Nach Eingabe der Parameter klicken Sie auf die Schaltfläche *Try it out*:
![agendaitem_1](asset/agendaitem_1.png)

Struktur der Ergebnisse:
![agendaitem_2](asset/agendaitem_2.png)

*Get agendaitems:*

Sollt man hier zwei Parameters *session* und *term* eingeben.
Nach Eingabe der Parameter klicken Sie auf die Schaltfläche *Try it out*:
![agendaitems_1](asset/agendaitems_1.png)

Struktur der Ergebnisse:
![agendaitems_2](asset/agendaitems_2.png)

#### Return speakers

*Get speakers:*

Sollt man hier fünf Parameters *user*, *fraction*, *minimum*, *time* and *time* eingeben.
Nach Eingabe der Parameter klicken Sie auf die Schaltfläche *Try it out*:
![speakers_1](asset/speakers_1.png)

Struktur der Ergebnisse:
![speakers_2](asset/speakers_2.png)

#### Return speeches

*Get speeches:*

Sollt man hier fünf Parameters *user*, *fraction*, *minimum*, *time* and *time* eingeben.
Nach Eingabe der Parameter klicken Sie auf die Schaltfläche *Try it out*:
![speeches_1](asset/speeches_1.png)

Struktur der Ergebnisse:
![speeches_2](asset/speeches_2.png)

*Get speech:*

Sollt man hier ein Parameter *id* eingeben.
Nach Eingabe der Parameter klicken Sie auf die Schaltfläche *Try it out*:
![speech_1](asset/speech_1.png)

Struktur der Ergebnisse:
![speech_2](asset/speech_2.png)

#### Return fractions

*Get fractions:*

Hier müssen keine Parameter eingegeben werden.
Klicken Sie auf die Schaltfläche *Try it out*:
![fractions_1](asset/fractions_1.png)

Struktur der Ergebnisse:
![fractions_2](asset/fractions_2.png)

*Get fraction:*

Sollt man hier ein Parameter *name* eingeben.
Nach Eingabe der Parameter klicken Sie auf die Schaltfläche *Try it out*:
![fraction_1](asset/fraction_1.png)

Struktur der Ergebnisse:
![fraction_2](asset/fraction_2.png)

#### Return annotations

*Get sentiment:*

Sollt man hier fünf Parameters *user*, *fraction*, *minimum*, *time* and *time* eingeben.
Nach Eingabe der Parameter klicken Sie auf die Schaltfläche *Try it out*:
![sentiment_1](asset/sentiment_1.png)

Struktur der Ergebnisse:
![sentiment_2](asset/sentiment_2.png)

*Get tokens:*

Sollt man hier fünf Parameters *user*, *fraction*, *minimum*, *time* and *time* eingeben.
Nach Eingabe der Parameter klicken Sie auf die Schaltfläche *Try it out*:
![tokens_1](asset/tokens_1.png)

Struktur der Ergebnisse:
![tokens_2](asset/tokens_2.png)

*Get pos:*

Sollt man hier fünf Parameters *user*, *fraction*, *minimum*, *time* and *time* eingeben.
Nach Eingabe der Parameter klicken Sie auf die Schaltfläche *Try it out*:
![pos_1](asset/pos_1.png)

Struktur der Ergebnisse:
![pos_2](asset/pos_2.png)

*Get namedEntities:*

Sollt man hier fünf Parameters *user*, *fraction*, *minimum*, *time* and *time* eingeben.
Nach Eingabe der Parameter klicken Sie auf die Schaltfläche *Try it out*:
![namedEntities_1](asset/namedEntities_1.png)

Struktur der Ergebnisse:
![namedEntities_2](asset/namedEntities_2.png)

*Get dependencies:*

Sollt man hier fünf Parameters *user*, *fraction*, *minimum*, *time* and *time* eingeben.
Nach Eingabe der Parameter klicken Sie auf die Schaltfläche *Try it out*:
![dependencies_1](asset/dependencies_1.png)

Struktur der Ergebnisse:
![dependencies_2](asset/dependencies_2.png)

#### Return statistics for speakers and speeches

*Get statistic:*

Hier müssen keine Parameter eingegeben werden.
Klicken Sie auf die Schaltfläche *Try it out*:
![statistic_1](asset/statistic_1.png)

Struktur der Ergebnisse:
![statistic_2](asset/statistic_2.png)

# Handbuch zum Nutzung Server-Komponente und Client-Komponente

## Inhaltsverzeichnis
<details open>
    <summary>Table of Contents</summary>  

1. [Einleitung](#einleitung)
    1. [Vorwort](#vorwort)
    2. [Vorbereitung](#vorbereitung)
2. [Frontend](#frontend)
   1. [Dashboard](#dashboard) 
      1. [Chart_Visualisation](#chartvisualisation)
      2. [Filter_Funktion](#filterfunktion)
   2. [Reden](#reden)
      1. [Volltext_Funktion](#volltextfunktion)
3. [Backend](#backend)
 
</details>

### Einleitung <a name="einleitung"></a>

#### Vorwort <a name="vorwort"></a>
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

#### Vorbereitung <a name="vorbereitung"></a>
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


### Frontend <a name="frontend"></a>

Die Hauptaufgabe von Frontend-Teil dient dazu, die Ergebnisse mittels SB Admin 2 gemäß der definierten Aufgaben zu visualisieren und analysieren. Es gibt zwei Websites in unsere Frontend-Teil.

#### Main Website - Dashboard <a name="dashboard"></a>

![image](https://cdn.discordapp.com/attachments/871441710373277746/946134892041879572/a83c0bad8098d7e5a3c24aa1195cc2c.png)


##### Chart Visualisation <a name="chartvisualisation"></a>

Das Dashboard __"Übersicht"__ visualisiert die gesamte Parlament Daten aus unsere Datenbank:
```markdown
- Tokenchart
- Poschart
- Sentimentchart
- Named Entities chart
- Rednerchart mit Redner-Bild als Hover-Effekt
```

##### Filter Funktion <a name="filterfunktion"></a>
Man kann im oberen Seitenbereich je nach dem Suchfeld frei entscheiden, ob man durch Dropdown-Menü oder selbst tippen, die entsprechenden Filterergebnisse suchen:

* Speaker-ID (Format: Dropdown-Menü oder 8-stellige ID-Zahlen wie z.B. 11004908)
  ![speaker](https://cdn.discordapp.com/attachments/871441710373277746/946526903014137936/unknown.png)
* Fraktionsnamen, 
  hier ist in dem Bild als Beispiel "FDP" geschrieben (Format: Dropdown-Menü oder Fraktionszugehörigkeit)
  ![fraction](https://cdn.discordapp.com/attachments/871441710373277746/946526903278399538/unknown.png)
* minimale Anzahl z.B. für Token, Pos usw. 
  hier ist in dem Bild als Beispiel "1000" geschrieben (Format: Dropdown-Menü oder 3 bis 4-stellige Zahlen)
  ![minimum](https://cdn.discordapp.com/attachments/871441710373277746/946526903504883732/unknown.png)
* Beginn- und End-Zeitraum (Format: Dropdown-Menü)
* ![time](https://cdn.discordapp.com/attachments/871441710373277746/946526903769129010/unknown.png)
  
Es wird nach Klicken ![search](https://cdn.discordapp.com/attachments/871441710373277746/946526904029163530/unknown.png)   ein sogenannte __"Sub-Dashboard"__ angezeit (mit Suchanfragetitel), dies enthält die gleichen Visualisierungen und Optionen wie dem "Main" Dashboard, jedoch nur für die Suchanfrage.

In dem Bild sieht man, dass man kein SpeakerID aber Fraktion "FDP" und einen minimale Anzahl "1000" eingegeben hat.
![filterexample](https://cdn.discordapp.com/attachments/871441710373277746/946705721712607252/unknown.png)

Das Sub-Dashboard kann durch Button <font color =red>"Delete sub dashboard"</font> wieder entfernt werden.

#### Sub Website - Reden <a name="reden"></a>

##### Volltext Funktion <a name="volltextfunktion"></a>
Links bei der Sidebar unter "Dashboard" sieht man die Website "reden". Die Volltext-Visualisierung ist für jede einzelne Parlaments-Rede implementiert und beinhaltet folgenden Funktionen:

![fulltext](https://cdn.discordapp.com/attachments/871441710373277746/946523390867566672/rede_beispiel.png)

* Alle im Text gefundenen Named Entities werden farblich hinterlegt.
  * <font color=red>Person</font> 
  * <font color=blue>Ort</font>
  * <font color=green>Organisation</font>
* Der Sprech*innenname, Informationen zu seiner/ihrer Partei- und Fraktionszugehörigkeit, sowie ein Foto(sofern vorhanden) werden im Kopf der Rede angezeigt. (Bei dem obigen Beispiel ist der Abgeordnete Name: Marc Bernhard, Fraktionszugehörigkeit: AfD)
* Ermöglichung eines Filters über eine Navigation durch die Tagesordnung der Protokolle.
  * ![fulltextfilter](https://cdn.discordapp.com/attachments/871441710373277746/946532642097078292/unknown.png)
  * Man wählt mit dem DropDown-Menü in der Reihenfolge:
    1. Wahlperiode
    1. Sitzung
    2. Tagesordnungspunkte
    3. RedeID


### Backend <a name="backend"></a>


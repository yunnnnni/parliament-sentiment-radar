/**
 * @author Tinghuan Song
 * Shows the full text of a single speech.
 */

var globalURL = "http://localhost:5678"

/**
 * @param id
 * @returns all informations of the speech
 * Get the speech text and speaker id with speech id.
 */
function getSpeakerId(id) {
    var speakerId;
    var texts;
    var persons;
    var locations;
    var organisations;
    var sentences;
    var sentiments;
    $.ajax({
        url: globalURL + "/speech?id=" + id,
        method: 'GET',
        async: false,
        dataType: 'json',
        success: function (d) {
            speakerId = d.speaker;
            texts = d.texts;
            persons = d.annotations.persons;
            locations = d.annotations.locations;
            organisations = d.annotations.organisations;
            sentences = d.annotations.sentences;
            sentiments = d.annotations.sentiments;
        }
    });
    return [speakerId, texts, persons, locations, organisations, sentences, sentiments];
}

/**
 * @param id
 * Get the name, party and fraction of the speaker with speech id.
 */
 function getInfo(id) {
    var name;
    var fraction;
    var foto;
    $.ajax({
        url: globalURL + "/speakers?user=" + id,
        method: 'GET',
        dataType: 'json',
        async: false,
        success: function (d) {

            var result = d.result;
            name = result[0].firstname + " " + result[0].name;
            fraction = result[0].fraction;
            if (fraction == "") {
                fraction = "Keine Angabe";
            };
            foto = result[0].image.url;
        }
    });
    return [name, fraction, foto];
}

/**
 * Highlight words of persons.
 * @param str
 * @param qry
 * @returns res
 */
function markTextPerson(str, qry) {
    var rgx = new RegExp('\\b' + qry + '\\b', 'gi');
    var res = str.replace(rgx, `<mark style = "background-color : #ec7063">${qry}</mark>`);
    return res;
}

/**
 * Highlight words of locations.
 * @param str
 * @param qry
 * @returns res
 */
 function markTextLocation(str, qry) {
    var rgx = new RegExp('\\b' + qry + '\\b', 'gi');
    var res = str.replace(rgx, `<mark style = "background-color : #3498db">${qry}</mark>`);
    return res;
}

/**
 * Highlight words of organisations.
 * @param str
 * @param qry
 * @returns res
 */
 function markTextOrganisation(str, qry) {
    var rgx = new RegExp('\\b' + qry + '\\b', 'gi');
    var res = str.replace(rgx, `<mark style = "background-color : #2ecc71">${qry}</mark>`);
    return res;
}

/**
 * Show the full text.
 */
function showText(){

    // get speech id.
    speech = document.getElementById("speech-options");
    speechId = speech.options[speech.selectedIndex].text;
    info = getInfo(getSpeakerId(speechId)[0]);
    texts = getSpeakerId(speechId)[1];
    persons = getSpeakerId(speechId)[2];
    locations = getSpeakerId(speechId)[3];
    organisations = getSpeakerId(speechId)[4];
    sentences = getSpeakerId(speechId)[5];
    sentiments = getSpeakerId(speechId)[6];

    // show speaker info.
    var speaker = d3.select("#information");
    speaker.selectAll("*").remove();
    speaker.append("p").text("Name: " + info[0] + ", Fraktion: " + info[1]).style("color", "blue");
    // show speaker foto.
    var svg = d3.select("#information")
        .append("svg")
        .attr("width", 300)
        .attr("height", 300);

    var img = svg.append("svg:image")
        .attr("xlink:href", info[2])
        .attr("width", "100%")
        .attr("height", "100%");
    // show legend for named entitites.
    var svg2 = d3.select("#information")
        .append("svg")
        .attr("width", 300)
        .attr("height", 300);
    svg2.append("circle").attr("cx",200).attr("cy",130).attr("r", 6).style("fill", "#ee3b3b")
    svg2.append("circle").attr("cx",200).attr("cy",160).attr("r", 6).style("fill", "#6495ed")
    svg2.append("circle").attr("cx",200).attr("cy",190).attr("r", 6).style("fill", "#3cb371")
    svg2.append("text").attr("x", 210).attr("y", 130).text("Person").style("font-size", "15px").attr("alignment-baseline","middle")
    svg2.append("text").attr("x", 210).attr("y", 160).text("Ort").style("font-size", "15px").attr("alignment-baseline","middle")
    svg2.append("text").attr("x", 210).attr("y", 190).text("Organisation").style("font-size", "15px").attr("alignment-baseline","middle")

    // Show the speech text with comments on click of buttons.
    speechText = d3.select("#speech-visualisation");
    speechText.selectAll("*").remove();
    speechText.append("p").text(texts[0].text);
    for (let i = 1; i < texts.length; i++) {
        oldText = speechText.select("p").text(),
        speechText.select("p").text(oldText + " " + texts[i-1].text)
    }

    // Make the named entities colorful.
    // Persons will be highlighted by red.
    persons.forEach(p =>{
        var str = document.querySelector('#speech-visualisation').innerHTML;
        document.querySelector('#speech-visualisation').innerHTML = markTextPerson(str, p);
    })

    // Locations will be highlighted by blue.
    locations.forEach(l =>{
        var str = document.querySelector('#speech-visualisation').innerHTML;
        document.querySelector('#speech-visualisation').innerHTML = markTextLocation(str, l);
    })

    // Organisations will be highlighted by green.
    organisations.forEach(o =>{
        var str = document.querySelector('#speech-visualisation').innerHTML;
        document.querySelector('#speech-visualisation').innerHTML = markTextOrganisation(str, o);
    })
}
/**
 * Methods to help filter and show full texts of a single speech.
 * @author Tinghuan Song
 */

var globalURL = "http://localhost:5678"
/**
 * Get all terms, sessions and agenda item ids.
 * @returns list of terms and agenda item ids.
 */
 function getTerms() {
    var terms;
    var agendaIds;
    $.ajax({
        url: globalURL + "/protocols",
        method: 'GET',
        async: false,
        dataType: 'json',
        success: function (d) {
            terms =[[], []]
            agendaIds = [[], []]
            d.result.forEach(t => {
                if (t.term == 19) {
                    terms[0].push(t.session);
                    agendaIds[0].push(t.agendaItemIds)
                }
                else {
                    terms[1].push(t.session);
                    agendaIds[1].push(t.agendaItemIds)
                }
            });
        }
    });
    return [terms, agendaIds];
}

/**
 * Get all speeche ids in a agenda item.
 * @param session
 * @param term
 * @param agenda
 * @returns list of speech ids.
 */
 function getSpeeches(session, term, agenda) {
    var speechIds
    $.ajax({
        url: globalURL+"/agendaitem?session=" + session + "&term=" + term + "&id=" + agenda,
        method: 'GET',
        async: false,
        dataType: 'json',
        success: function (d) {
            speechIds = []
            d.speechIds.forEach(t => {
                speechIds.push(t)
            });
        }
    });
    return speechIds;
}

// Get all terms, sessions and agenda item ids.
session1 = getTerms()[0][0];
session2 = getTerms()[0][1];
agendaIds1 = getTerms()[1][0];
agendaIds2 = getTerms()[1][1];

/**
 * Show session id options.
 */
function selectSession(){
    term = document.getElementById("term-options");
    termValue = term.options[term.selectedIndex].text;
    if (termValue == "19") {
        s = session1,
        session = d3.select("#session-options");
        session.selectAll("*").remove()
        session.append("option").text("id")
        s.forEach(i =>{
            session.append("option").text(i)
        })
    } else {
        s = session2,
        session = d3.select("#session-options");
        session.selectAll("*").remove()
        session.append("option").text("id")
        s.forEach(i =>{
            session.append("option").text(i)
        })
    };

}

/**
 * Show agenda item id options.
 */
 function selectAgenda(){
    term = document.getElementById("term-options");
    termValue = term.options[term.selectedIndex].text;
    session = document.getElementById("session-options");
    sessionValue = parseInt(session.options[term.selectedIndex].text);
    if (termValue == "19") {
        a = agendaIds1[session1.indexOf(sessionValue)],
        agendaId = d3.select("#agenda-options")
        agendaId.selectAll("*").remove()
        agendaId.append("option").text("id")
        a.forEach(i =>{
            agendaId.append("option").text(i)
        })
    } else {
        a = agendaIds2[session2.indexOf(sessionValue)],
        agendaId = d3.select("#agenda-options");
        agendaId.selectAll("*").remove()
        agendaId.append("option").text("id")
        a.forEach(i =>{
            agendaId.append("option").text(i)
        })
    };
}

/**
 * Show speech id options.
 */
 function selectSpeech(){
    term = document.getElementById("term-options");
    termValue = term.options[term.selectedIndex].text;
    session = document.getElementById("session-options");
    sessionValue = session.options[term.selectedIndex].text;
    agenda = document.getElementById("agenda-options");
    agendaValue = agenda.options[agenda.selectedIndex].text;
    speechIds = getSpeeches(sessionValue, termValue, agendaValue)

    speechId = d3.select("#speech-options");
    speechId.selectAll("*").remove()
    speechId.append("option").text("id")
    speechIds.forEach(i =>{
        speechId.append("option").text(i)
    })
}
/**
 * @author Tinghuan Song
 */

function getSpeech(id) {
	var text;
	var speakerId;
	$.ajax({
		url: globalURL+"/speech?id=" + id,
		method: 'GET',
		async: false,
		dataType: 'json',
		success: function (d) {

            text = d.content;
			speakerId = d.speaker;

		}
	});
	return [text, speakerId];
}

function getInfo(id) {
	var name;
	var party;
	var fraction;
	$.ajax({
		url: globalURL+"/speakers?id=" + id,
		method: 'GET',
		dataType: 'json',
		async: false,
		success: function (d) {

			var result = d.result;
			name = result[0].firstname + " " + result[0].name;
			party = result[0].party;
			fraction = result[0].fraction;
		}
	});
	return [name, party, fraction];
}

document.getElementById("speech-visualisation").innerHTML = getSpeech("ID1916400100")[0];
info = getInfo(getSpeech("ID1916400100")[1]);
document.getElementById("information").innerHTML = "Name: " + info[0] + ", Partei: " + info[1] + ", Fraktion: " +info[2];
/**
 * @author Tinghuan Song
 * Shows the full text of a single speech.
 */

/**
 * @param id
 * Get the speech text and speaker with speech id.
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

/**
 * @param id
 * Get the name, party and fraction of the speaker with speech id.
 */
function getInfo(id) {
	var name;
	var fraction;
	$.ajax({
		url: globalURL+"/speakers?user=" + id,
		method: 'GET',
		dataType: 'json',
		async: false,
		success: function (d) {

			var result = d.result;
			name = result[0].firstname + " " + result[0].name;
			fraction = result[0].fraction;
		}
	});
	return [name, fraction];
}

function testId() {
	var sentiment;
	$.ajax({
		url: globalURL+"/sentiment",
		method: 'GET',
		dataType: 'json',
		async: false,
		success: function (d) {

			sentiment = d.result;
		}
	});
	return sentiment;
}

// Shows the speaker information and the speech text.
document.getElementById("speech-visualisation").innerHTML = getSpeech("ID1916400100")[0];
info = getInfo(getSpeech("ID1916400100")[1]);
document.getElementById("information").innerHTML = "Name: " + info[0] + ", Fraktion: " + info[2];
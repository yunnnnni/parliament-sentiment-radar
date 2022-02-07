/**
 * @author Tinghuan Song
 */

function getSpeech(id) {
	$.ajax({
		url: globalURL+"/speech?id=" + id,
		method: 'GET',
		dataType: 'json',
		success: function (d) {

			var content = d.content;
            showSpeech(content);

		}
	});
}

function showSpeech(text) {
    d3.select("h1").append("p").text(text);
}

getSpeech("ID1916400100");
/**
 * @author Tinghuan Song
 */

text = d3.select("#speech-visualisation");
text.style("color", "red");

function getNamedEntities() {

	let person = [];
	let organisation = [];
	let location = []; 
	$.ajax({
		url: globalURL+"/namedEntities",
		method: 'GET',
		async: false,
		dataType: 'json',
		success: function (d) {

			d.result[0].persons.forEach(t => {
				person.push(t.element);
			});

			d.result[1].organisations.forEach(t => {
				organisation.push(t.element);
			});

			d.result[2].locations.forEach(t => {
				location.push(t.element);
			});

		}
	});
	return [person, organisation, location];
}

test = getNamedEntities();
per = test[0];
org = test[1];
loc = test[2];
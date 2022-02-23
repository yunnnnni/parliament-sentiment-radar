/**
 * @author Tinghuan Song
 */


var speakerUrl = "http://localhost:5678/speakers?"
function createSpeakerChart(node, query){
    // create chart
    var speakerChart = new Chart(node, {
	type: 'bar',
	data: {
		labels: [],
		datasets: [{
			label: "Reden",
			lineTension: 0.3,
			backgroundColor: "rgba(78, 115, 223, 0.85)",
			borderColor: "rgba(78, 115, 223, 1)",
			pointRadius: 3,
			pointBackgroundColor: "rgba(78, 115, 223, 1)",
			pointBorderColor: "rgba(78, 115, 223, 1)",
			pointHoverRadius: 3,
			pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
			pointHoverBorderColor: "rgba(78, 115, 223, 1)",
			pointHitRadius: 10,
			pointBorderWidth: 2,
			data: [],
		}],
	},
	options: {
		maintainAspectRatio: false,
		layout: {
			padding: {
				left: 10,
				right: 25,
				top: 25,
				bottom: 0
			}
		},
		scales: {
			xAxes: [{
				time: {
					unit: 'date'
				},
				gridLines: {
					display: false,
					drawBorder: false
				},
			}],
			yAxes: [{
				ticks: {
					maxTicksLimit: 5,
					padding: 10,
					// Include a dollar sign in the ticks
					callback: function(value, index, values) {
						return '' + number_format(value);
					}
				},
				gridLines: {
					color: "rgb(234, 236, 244)",
					zeroLineColor: "rgb(234, 236, 244)",
					drawBorder: false,
					borderDash: [2],
					zeroLineBorderDash: [2]
				}
			}],
		},
		legend: {
			display: false
		},
		tooltips: {
			backgroundColor: "rgb(255,255,255)",
			bodyFontColor: "#858796",
			titleMarginBottom: 10,
			titleFontColor: '#6e707e',
			titleFontSize: 14,
			borderColor: '#dddfeb',
			borderWidth: 1,
			xPadding: 15,
			yPadding: 15,
			displayColors: false,
			mode: 'index',
			intersect: false,
			caretPadding: 10,
			callbacks: {
				label: function(tooltipItem, chart) {
					var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
					return datasetLabel + ': ' + number_format(tooltipItem.yLabel);
				}
			}
		},
        hover: {
            mode: 'index',
            intersect: false
        }
	}
    });

    let url = speakerUrl + query;
	$.ajax({
		url: url,
		method: 'GET',
		dataType: 'json',
		success: function (d) {

			let labels = [];//d.labels;
			let values = [];//d.values;

			d.result.forEach(s => {
				labels.push(s.firstname + " " + s.name);
				values.push(s.speechIds.length);
			});

			speakerChart.data.labels = labels;
			speakerChart.data.datasets[0].data = values;
			speakerChart.update();

		}
	});
}

/**
 * Method to query the speakers and their quantity
 */

function getName(id) {
	var name;
	$.ajax({
		url: globalURL+"/speakers?id=" + id,
		method: 'GET',
		dataType: 'json',
		async: false,
		success: function (d) {

			var result = d.result;
			name = result[0].firstname + " " + result[0].name;
		}
	});
	return name;
}
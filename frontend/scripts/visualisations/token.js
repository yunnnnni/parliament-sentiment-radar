/**
 * @author Tinghuan Song
 */
// Select HTML-Element
var token = document.getElementById("token-visualisation");

// create chart
var tokenChart = new Chart(token, {
	type: 'line',
	data: {
		labels: [],
		datasets: [{
			label: "Token",
			lineTension: 0.3,
			backgroundColor: "transparent",
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
			intersect: false,
			mode: 'index',
			caretPadding: 10,
			callbacks: {
				label: function(tooltipItem, chart) {
					var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
					return datasetLabel + ': ' + number_format(tooltipItem.yLabel);
				}
			}
		}
	}
});

/**
 * Method to query the tokens and their quantity.
 * Without parameter 10000 is used as miniumum.
 * @param iMinimum
 * @param data
 */
getTokenChart = function token(iMinimum=$("#tokenLimit").val(), data={}) {
	$.ajax({
		url: globalURL+"/tokens?minimum=" + iMinimum,
		method: 'GET',
		data: data,
		dataType: 'json',
		success: function (d) {

			let labels = [];//d.labels;
			let values = [];//d.values;

			d.result.forEach(t => {
				labels.push(t.token);
				values.push(t.count);
			});

			tokenChart.data.labels = labels;
			tokenChart.data.datasets[0].data = values;
			tokenChart.update();

		}
	});
}


// run on startup
getTokenChart();

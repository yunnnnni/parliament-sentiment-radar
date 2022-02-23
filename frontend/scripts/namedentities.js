// Select HTML-Element
var namedEntitiesUrl = "http://localhost:5678/namedEntities?"

function createNamedEntityChart(node, query){
    // create chart
    var namedEntitiesChart = new Chart(node, {
	    type: 'line',
	    data: {
		    labels: [],
		    datasets: [
			    {
				    label: "Person",
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
			    },
			    {
				    label: "Organisation",
				    lineTension: 0.3,
				    backgroundColor: "transparent",
				    borderColor: "rgba(255, 99, 132, 1)",
				    pointRadius: 3,
				    pointBackgroundColor: "rgba(255, 99, 132, 1)",
				    pointBorderColor: "rgba(255, 99, 132, 1)",
				    pointHoverRadius: 3,
				    pointHoverBackgroundColor: "rgba(255, 99, 132, 1)",
				    pointHoverBorderColor: "rgba(255, 99, 132, 1)",
				    pointHitRadius: 10,
				    pointBorderWidth: 2,
				    data: [],
			    },
			    {
				    label: "Ort",
				    lineTension: 0.3,
				    backgroundColor: "transparent",
				    borderColor: "rgba(255, 206, 86, 1)",
				    pointRadius: 3,
				    pointBackgroundColor: "rgba(255, 206, 86, 1)",
				    pointBorderColor: "rgba(255, 206, 86, 1)",
				    pointHoverRadius: 3,
				    pointHoverBackgroundColor: "rgba(255, 206, 86, 1)",
				    pointHoverBorderColor: "rgba(255, 206, 86, 1)",
				    pointHitRadius: 10,
				    pointBorderWidth: 2,
				    data: [],
			    }
		    ],
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
				    ticks: {
					    display: false
				    }
			    }],
			    yAxes: [{
				    ticks: {
					    maxTicksLimit: 10,
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
		    },
            plugins: {
                legend: {
                    display: false
                }
            }
	    }
    });

    /**
     * Method to query the named entities and their quantity
     * Without parameter 100 is used as miniumum.
     */

    let url = namedEntitiesUrl + query;
	$.ajax({
		url: url,
		method: 'GET',
		dataType: 'json',
		success: function (d) {

			let labels1 = [];//d.labels;
			let values1 = [];//d.values;

			d.result[0].persons.forEach(t => {
				labels1.push(t.element);
				values1.push(t.count);
			});

			let values2 = [];//d.values;

			d.result[1].organisations.forEach(t => {
				labels1.push(t.element);
				values2.push(t.count);
			});
		
			let values3 = [];//d.values;


			d.result[2].locations.forEach(t => {
				labels1.push(t.element);
				values3.push(t.count);
			});

			namedEntitiesChart.data.labels = labels1;
			namedEntitiesChart.data.datasets[0].data = values1;
			namedEntitiesChart.data.datasets[1].data = values2;
			namedEntitiesChart.data.datasets[2].data = values3;
			namedEntitiesChart.update();
		}
	});
}

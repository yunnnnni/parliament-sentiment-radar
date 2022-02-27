/**
 * @author Tinghuan Song
 */
// Select HTML-Element

var POSUrl = "http://localhost:4567/pos?"

// create chart
function createPosChart(node, query){
    var posChart = new Chart(node, {
        type: 'bar',
        data: {
            labels: [],
            datasets: [{
                label: "Pos",
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
     * Method to query the pos and their quantity
     * Without parameter 2000 is used as miniumum.
     */

    let url = POSUrl + query;
    $.ajax({
            url: url,
            method: 'GET',
            dataType: 'json',
            success: function (d) {

                let labels = [];//d.labels;
                let values = [];//d.values;

                d.result.forEach(t => {
                    values.push(t.count);
                    labels.push(t.pos);
                });

                posChart.data.labels = labels;
                posChart.data.datasets[0].data = values;
                posChart.update();

            }
        });
}


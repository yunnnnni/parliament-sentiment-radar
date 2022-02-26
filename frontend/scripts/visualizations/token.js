var tokenUrl = "http://localhost:5678/tokens?"

function createTokenChart(node, query){
    // create chart
    var tokenChart = new Chart(node, {
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
    let url = tokenUrl + query;
    $.ajax({
        url: url,
        method: 'GET',
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
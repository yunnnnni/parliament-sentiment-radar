/**
 * @author Tinghuan Song
 */

var sentimentUrl = "http://localhost:4567/sentiment?"

function createSentimentChart(node, query){
    var sentimentChart = new Chart(node, {
        type: "radar",
        data: {
            labels: [],
            datasets: [{
                data: [],
                fill: true,
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderColor: 'rgb(54, 162, 235)',
                pointBackgroundColor: 'rgb(54, 162, 235)',
                pointBorderColor: '#fff',
                pointHoverBackgroundColor: '#fff',
                pointHoverBorderColor: 'rgb(54, 162, 235)'
            }],
        },
        options: {
            maintainAspectRatio: false,
            tooltips: {
                backgroundColor: "rgb(255,255,255)",
                bodyFontColor: "#858796",
                borderColor: '#dddfeb',
                borderWidth: 1,
                xPadding: 15,
                yPadding: 15,
                displayColors: false,
                caretPadding: 10,
            },
        },
        plugins: {
            legend: {
                display: false
            }
        }
    });

    let url = sentimentUrl + query;
    $.ajax({
        url: url,
        method: 'GET',
        dataType: 'json',
        success: function (d) {

            let labels =["negative Sentiments", "neutrale Sentiments", "positive Sentiments"];//d.labels;
            let values =[0,0,0];//d.values;

            d.result.forEach(s => {
                if (s.sentiment > 0) {
                    values[2] += (s.count);
                }
                else if (s.sentiment == 0) {
                    values[1] += (s.count);
                }
                else {
                    values[0] += (s.count);
                }
            });

            sentimentChart.data.labels = labels;
            sentimentChart.data.datasets[0].data = values;
            sentimentChart.update();
        }
    });
}
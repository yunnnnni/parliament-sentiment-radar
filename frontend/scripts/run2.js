/**
 * @author Lu.Yunni, 6563468, s0425513@stud.uni-frankfurt.de
 */

// const Chart = require("chart.js");

Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';


/**
 * function to create bar chart
 * @param {String} elementId, id for html element
 * @returns 
 */
function createBarChart(elementId){
    let ctx = document.getElementById(elementId);
    return new Chart(ctx, {
        type: 'bar',
        data: {
            labels: [],
            datasets: [{
                label: "count",
                data: [],
                backgroundColor: "rgba(78, 115, 223, 1)",
                borderColor: "rgba(78, 115, 223, 1)",
                // barPercentage: 0.4
            }],
        },
        options: {
            maintainAspectRatio: false,
            scales: {
                xAxes: [{
                    min: -1,
                    max: 1,
                    // ticks: {
                    //     maxTicksLimit: 20,
                    // }
                }]
            },
            legend: {
                display: false
            },
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
        }
    });
}


/**
 * function to create radar chart
 * @param {String} elementId, id for html element
 * @returns 
 */
function createRadarChart(elementId){
    let ctx = document.getElementById(elementId);
    return new Chart(ctx, {
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
            legend: {
                display: false
                // position: 'right',
            }
        }
    });
}


/**
 * function to create pie chart
 * @param {String} elementId, id for html element
 * @returns 
 */
function createPieChart(elementId){
    let ctx = document.getElementById(elementId);
    return new Chart(ctx, {
        type: "pie",
        data: {
            labels: [],
            datasets: [{
                data: [],
                backgroundColor: []
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
            legend: {
                display: true,
                position: 'right',
            },
        },
    });
}


/**
 * function to create doughnut chart
 * @param {String} elementId, id for html element
 * @returns 
 */
function createDoughnutChart(elementId){
    let ctx = document.getElementById(elementId);
    return new Chart(ctx, {
        type: "doughnut",
        data: {
            labels: [],
            datasets: [{
                data: [],
                backgroundColor: [],
                borderWidth: 0.2
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
            cutoutPercentage: 70,
            legend: {
                display: true,
                position: 'right',
            },
        },
    });
}


/**
 * button onclick function
 * used to query and update charts
 */
function updateChartsOnClick(){
    const speakerId = document.getElementById('speaker-id').value;
    const fractionId = document.getElementById('fraction-id').value;
    const partyId = document.getElementById('party-id').value;
    const conditions = "user=" + speakerId + "&" + "fraction=" + fractionId + "&" + "party=" + partyId

    document.getElementById("query-title").textContent = "Query data: \'" + conditions + "\'";
    // getData("http://api.prg2021.texttechnologylab.org/sentiment?" + conditions)
    getData("http://38.242.210.53:4567/sentiment?" + conditions)
    .then(
        (data) => {visualizeSentimentDistribution(data, sentimentChart);})
    .catch(
        // e => {alert("Invalid query for sentiment distribution!");}
        e => {updateBarChart([], [], chart);}
    );

    // getData("http://api.prg2021.texttechnologylab.org/pos?" + conditions)
    getData("http://38.242.210.53:4567/pos?" + conditions)
    .then(
        (data) => {visualizePosDistribution(data, posChart);})
    .catch(
        // e => {alert("Invalid query for pos distribution!");}
        e => {updatePieChart([], [], chart);}
    );

    // getData("http://api.prg2021.texttechnologylab.org/tokens?" + conditions)
    getData("http://38.242.210.53:4567/tokens?" + conditions)
    .then(
        (data) => {visualizeTokenDistribution(data, tokenChart);})
    .catch(
        // e => {alert("Invalid query for token distribution!");}
        e => {updatePieChart([], [], chart);}
    );

    // getData("http://api.prg2021.texttechnologylab.org/namedEntities?" + conditions)
    getData("http://38.242.210.53:4567/namedEntities?" + conditions)
    // getData("http://localhost:4567/namedEntities?" + conditions)
    .then(
        (data) => {visualizeEntitiesDistribution(data, entityCharts);})
    .catch(
        // e => {alert("Invalid query for entities distribution!");}
        e => {
            for (let id in entityCharts){
                    updatePieChart([], [], entityCharts[id]);
                }
        }
    );
}


/**
 * get data with ajax request
 * @param {String} url 
 * @returns 
 */
function getData(url) {
    return new Promise(function(res,rej){
        $.ajax({
            type: "GET",
            url: url,
            async: true,
            success: data => {
                res(data);
            },
            error: data => {
                rej(data);
            }
        });
    })
}


/**
 * update data for bar chart
 * @param {Array[String]} labels
 * @param {Array[Float]} values 
 * @param {Chart} chart 
 */
function updateBarChart(labels, values, chart){
    chart.data.labels = labels;
    chart.data.datasets[0].data = values;
    chart.update();
}


/**
 * create random color, used to set colors for charts
 * @returns color string
 */
function randomColor() {
    var r = Math.floor(Math.random() * 255);
    var g = Math.floor(Math.random() * 255);
    var b = Math.floor(Math.random() * 255);
    return "rgb(" + r + "," + g + "," + b + ")";
};


/**
 * update data for pie chart, works also for doughnut chart
 * @param {Array[String]} labels 
 * @param {Array[Float]} values 
 * @param {Chart} chart 
 */
function updatePieChart(labels, values, chart){
    const colorList = [];
    for (let i = 0; i < labels.length; i++){
        colorList.push(randomColor());
    }
    chart.data.labels = labels;
    chart.data.datasets[0].data = values;
    chart.data.datasets[0].backgroundColor = colorList;
    chart.update();
}


/**
 * sort dictionary by keys
 * @param {Object} o , dictionary to sort
 * @returns sorted dictionary
 */
function sortObjectByKeys(o) {
    return Object.keys(o).sort().reduce((r, k) => (r[k] = o[k], r), {});
}


/**
 * update distribution chart for sentiment
 * @param {Object} data json returned by request
 * @param {Chart} chart chart to update
 * @returns 
 */
function visualizeSentimentDistribution(data, chart){
    if (data.success == false){
        // alert("Invalid query for sentiment distribution!");
        updateBarChart([], [], chart);
        return;
    }
    const sentimentsRaw = data.result;
    let sentimentData = {}
    for (let i = 0; i < sentimentsRaw.length; i++){
        let sentimentElement = sentimentsRaw[i];
        if (sentimentElement.sentiment in sentimentData){
            sentimentData[sentimentElement.sentiment] += sentimentElement.count;
        }
        else{
            sentimentData[sentimentElement.sentiment] = sentimentElement.count;
        }
    }
    sentimentData = sortObjectByKeys(sentimentData);
    updateBarChart(Object.keys(sentimentData), Object.values(sentimentData), chart);
}


/**
 * update distribution chart for POS
 * @param {Object} data json returned by request
 * @param {Chart} chart chart to update
 * @returns 
 */
function visualizePosDistribution(data, chart){
    if (data.success == false){
        // alert("Invalid query for sentiment distribution!");
        updateBarChart([], [], chart);
        return;
    }
    posRaw = data.result;
    const posList = [];
    const posCountList = [];
    for (let i = 0; i < posRaw.length; i++){
        posList.push(posRaw[i].pos);
        posCountList.push(posRaw[i].count);
    }

    const [labelsSliced, countsSliced] = buildDataPieChart(posList, posCountList, 0.0001, 27);
    updateBarChart(labelsSliced, countsSliced, chart);
}


/**
 * update distribution chart for tokens
 * @param {Object} data json returned by request
 * @param {Chart} chart chart to update
 * @returns 
 */
function visualizeTokenDistribution(data, chart){
    if (data.success == false){
        // alert("Invalid query for sentiment distribution!");
        updatePieChart([], [], chart);
        return;
    }
    dataRaw = data.result;
    const labels = [];
    const counts = [];
    for (let i = 0; i < dataRaw.length; i++){
        labels.push(dataRaw[i].token);
        counts.push(dataRaw[i].count);
    }

    const [labelsSliced, countsSliced] = buildDataPieChart(labels, counts, 0.0001, 41);
    updatePieChart(labelsSliced, countsSliced, chart);
}


/**
 * slice labels and counts by percentageThreshold and maxCount
 * labels with low frequency(defined by percentageThreshold) will be merged into label "others"
 * total number of elements in the array will also be limited by maxCount
 * @param {Array[String]} labels 
 * @param {Array[Int]} counts 
 * @param {Float} percentageThreshold 
 * @param {Int} maxCount 
 * @returns {[Array[String], Array[Int]]} sliced input arrays
 */
function buildDataPieChart(labels, counts, percentageThreshold, maxCount){
    // sort input arrays
    // 1) combine arrays:
    let list = [];
    for (let i = 0; i < labels.length && i < counts.length; i++) 
        list.push({'label': labels[i], 'count': counts[i]});

    // 2) sort:
    list.sort(function(a, b) {
        return ((a.count > b.count) ? -1 : ((a.count == b.count) ? 0 : 1));
    });

    // 3) separate them back out:
    let sum = 0;  // calculate total count
    for (let i = 0; i < list.length; i++) {
        labels[i] = list[i].label;
        counts[i] = list[i].count;
        sum += list[i].count;
    }

    for (let i = 0; i < labels.length && i < counts.length; i++){
        counts[i] = counts[i] / sum;
    }

    const labelsSliced = [];
    const countsSliced = [];
    let sumSliced = 0;
    for (let i = 0; i < labels.length && i < counts.length && counts[i] > percentageThreshold && i < maxCount; i++){
        labelsSliced.push(labels[i]);
        countsSliced.push(counts[i].toFixed(4));
        sumSliced += counts[i];
    }

    labelsSliced.push("others");
    countsSliced.push((1-sumSliced).toFixed(4));

    return [labelsSliced, countsSliced];
}

/**
 * udpate chart for name entity distributions
 * @param {Object} data 
 * @param {Array[Chart]} charts 
 * @returns 
 */
function visualizeEntitiesDistribution(data, charts){
    if (data.success == false){
        // if can't get data, abort visualization
        // alert("Invalid query for sentiment distribution!");
        updatePieChart([], [], chart);
        return;
    }
    const entityRaw = data.result;
    const elementIdDict = {  // name mapping
        "persons": "person-distribution-chart",
        "organisations": "organisation-distribution-chart",
        "locations": "location-distribution-chart",
    }
    let test_var = entityRaw.length;
    // process 3 types of name entities one by one
    for (let i = 0; i < entityRaw.length; i++){
        const dataSet = Object.values(entityRaw[i])[0];  // get dataset
        const dataLabels = [];
        const dataCounts = [];

        // extract results into dict
        for (let j = 0; j < dataSet.length; j++){
            dataLabels.push(dataSet[j].element);
            dataCounts.push(dataSet[j].count);
        }

        // slice data
        const [labelsSliced, countsSliced] = buildDataPieChart(dataLabels, dataCounts, 0.0001, 27);
        
        // update chart
        updatePieChart(labelsSliced, countsSliced, charts[elementIdDict[Object.keys(entityRaw[i])[0]]]);
    }
}

// create charts by init
var sentimentChart = createBarChart("sentiment-distribution-chart");
var posChart = createRadarChart("POS-distribution-chart");
var tokenChart = createDoughnutChart("token-distribution-chart");
var personChart = createDoughnutChart("person-distribution-chart");
var organisationChart = createDoughnutChart("organisation-distribution-chart");
var locationChart = createDoughnutChart("location-distribution-chart");

// put charts for name entities together for process convenience
var entityCharts = {
    "person-distribution-chart": personChart,
    "organisation-distribution-chart": organisationChart,
    "location-distribution-chart": locationChart,
}

// fetch available parties and update options in party selection
getData("http://api.prg2021.texttechnologylab.org/parties")
    .then(
        (data) => {
            if (data.success == false){
                return;
            }
            select = document.getElementById('party-options');
            content = data.result;
            for (let i = 0; i < content.length; i++){
                var opt = document.createElement('option');
                opt.innerHTML = content[i].id;
                select.appendChild(opt);
            }
        });

// fetch available fractions and update options in fraction selection
// getData("http://api.prg2021.texttechnologylab.org/fractions")
getData("http://38.242.210.53:4567/fractions")
    .then(
        (data) => {
            if (data.success == false){
                return;
            }
            select = document.getElementById('fraction-options');
            content = data.result;
            for (let i = 0; i < content.length; i++){
                var opt = document.createElement('option');
                opt.innerHTML = content[i].id;
                select.appendChild(opt);
            }
        });
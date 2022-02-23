/**
 * @author Tinghuan Song
 */

const getOrCreateTooltip = (chart) => {
  let tooltipEl = chart.canvas.parentNode.querySelector('div');

  if (!tooltipEl) {
    tooltipEl = document.createElement('div');
    tooltipEl.style.background = 'rgba(0, 0, 0, 0.7)';
    tooltipEl.style.borderRadius = '3px';
    tooltipEl.style.color = 'white';
    tooltipEl.style.opacity = 1;
    tooltipEl.style.pointerEvents = 'none';
    tooltipEl.style.position = 'absolute';
    tooltipEl.style.transform = 'translate(-50%, 0)';
    tooltipEl.style.transition = 'all .1s ease';

    const table = document.createElement('table');
    table.style.margin = '0px';
    tooltipEl.appendChild(table);

    const img = document.createElement('img');
    img.style.margin = '0px';
    tooltipEl.appendChild(img);

    chart.canvas.parentNode.appendChild(tooltipEl);
  }

  return tooltipEl;
};

const externalTooltipHandler = (context) => {

    console.log("test_hover.externalTolltipHandler");
    // Tooltip Element
    const {chart, tooltip} = context;
    const tooltipEl = getOrCreateTooltip(chart);

    const idx = tooltip.dataPoints[0].dataIndex;
    const imgUrl = tooltip.dataPoints[0].dataset.imageUrls[idx];
    const imgElement = tooltip.dataPoints[0].dataset.imageElements[idx];

    // Set Text
    if (tooltip.body) {
        const tableHead = document.createElement('thead');
        {
            // create table head
            const title = tooltip.title;
            const tr = document.createElement('tr');  // create table row
            tr.style.borderWidth = 0;
            const th = document.createElement('th');  // create table cell
            th.style.borderWidth = 0;
            const text = document.createTextNode(title);
            th.appendChild(text);
            tr.appendChild(th);
            tableHead.appendChild(tr);
        }
        
        const tableBody = document.createElement('tbody');
        {
            // create table body
            const body = tooltip.body[0];
            const colors = tooltip.labelColors[0];

            const span = document.createElement('span');
            span.style.background = colors.backgroundColor;
            span.style.borderColor = colors.borderColor;
            span.style.borderWidth = '2px';
            span.style.marginRight = '10px';
            span.style.height = '10px';
            span.style.width = '10px';
            span.style.display = 'inline-block';

            // create text for table body
            const tr = document.createElement('tr');
            tr.style.backgroundColor = 'inherit';
            tr.style.borderWidth = 0;

            const td = document.createElement('td');
            td.style.borderWidth = 0;

            const text = document.createTextNode(body.lines[0]);

            td.appendChild(span);
            td.appendChild(text);
            tr.appendChild(td);
            tableBody.appendChild(tr);
        }
        const tableRoot = tooltipEl.querySelector('table');

        // Remove old children
        while (tableRoot.firstChild) {
        tableRoot.firstChild.remove();
        }

        // Add new children
        tableRoot.appendChild(tableHead);
        tableRoot.appendChild(tableBody);

        const imgElement = tooltipEl.querySelector("img");
        imgElement.onload = resizeImage; 
        // tooltipEl.querySelector('img').remove();
        // tooltipEl.appendChild(imgElement);
        // imgRoot.onload = function(){
        //     const imgH = this.naturalHeight;
        //     const imgW = this.naturalWidth;
        //     console.log(imgH, imgW);
        //     if ((imgH < imgW) && (imgH > 200)){
        //         const factor = 200 / imgH;
        //         this.style.height = imgH * factor + "px";
        //         this.style.width = imgW * factor + "px";
        //     }
        //     else if ((imgH > imgW) && (imgH > 250)){
        //         const factor = 250 / imgH;
        //         this.style.height = imgH * factor + "px";
        //         this.style.width = imgW * factor + "px";
        //     }
        //     else{
        //         this.style.height = imgH + "px";
        //         this.style.width = imgW + "px";
        //     }
        //     setToolTipElSize(tooltipEl, tooltip, chart);
        // }
        imgElement.src = imgUrl;
        if (imgUrl != null){
            imgElement.style.visibility = 'visible';
        } else{
            imgElement.style.visibility = 'hidden';
            imgElement.style.height = 0;
            imgElement.style.width = 0;
        }
    }

    const {offsetLeft: positionX, offsetTop: positionY} = chart.canvas;

    setToolTipElSize(tooltipEl, tooltip, chart);
    // Display, position, and set styles for font
    // let left = chart.canvas.offsetLeft + tooltip.caretX;
    // if (left < 207){
    //     left = 207;
    // }
    // tooltipEl.style.left = left + 'px';
    // // tooltipEl.style.top = positionY + tooltip.caretY - height + 'px';
    // tooltipEl.style.top = - tooltipEl.clientHeight + 240 + 'px';
    // tooltipEl.style.font = tooltip.options.bodyFont.string;
    // tooltipEl.style.padding = tooltip.options.padding + 'px ' + tooltip.options.padding + 'px';
}

function resizeImage(){
    const imgH = this.naturalHeight;
    const imgW = this.naturalWidth;
    console.log(imgH, imgW);
    if ((imgH < imgW) && (imgH > 200)){
        const factor = 200 / imgH;
        this.style.height = imgH * factor + "px";
        this.style.width = imgW * factor + "px";
    }
    else if ((imgH > imgW) && (imgH > 250)){
        const factor = 250 / imgH;
        this.style.height = imgH * factor + "px";
        this.style.width = imgW * factor + "px";
    }
    else{
        this.style.height = imgH + "px";
        this.style.width = imgW + "px";
    }
    var delayInMilliseconds = 100; //1 second

    setTimeout(function() {
        //your code to be executed after 1 second
    }, delayInMilliseconds);
    setToolTipElSize(tooltipEl, tooltip, chart);
}

function setToolTipElSize(tooltipEl, tooltip, chart){
    // Display, position, and set styles for font
    let left = chart.canvas.offsetLeft + tooltip.caretX;
    if (left < 207){
        left = 207;
    }
    tooltipEl.style.left = left + 'px';
    // tooltipEl.style.top = positionY + tooltip.caretY - height + 'px';
    tooltipEl.style.top = - tooltipEl.clientHeight + 240 + 'px';
    tooltipEl.style.font = tooltip.options.bodyFont.string;
    tooltipEl.style.padding = tooltip.options.padding + 'px ' + tooltip.options.padding + 'px';
}

var speakerUrl = "http://localhost:5678/speakers?"
function createTestChart(node, query){
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
            imageUrls: [],
            imageElements: [],
		}],
	},
	options: {
		maintainAspectRatio: false,
        interaction: {
            mode: 'index',
            intersect: false,
        },
        plugins: {
            tooltip: {
                enabled: false,
                position: 'nearest',
                external: externalTooltipHandler
            }
        },
		// layout: {
		// 	padding: {
		// 		left: 10,
		// 		right: 25,
		// 		top: 25,
		// 		bottom: 0
		// 	}
		// },
		// scales: {
		// 	yAxes: [{
		// 		ticks: {
		// 			maxTicksLimit: 5,
		// 			padding: 10,
		// 			// Include a dollar sign in the ticks
		// 			callback: function(value, index, values) {
		// 				return '' + number_format(value);
		// 			}
		// 		},
		// 		gridLines: {
		// 			color: "rgb(234, 236, 244)",
		// 			zeroLineColor: "rgb(234, 236, 244)",
		// 			drawBorder: false,
		// 			borderDash: [2],
		// 			zeroLineBorderDash: [2]
		// 		}
		// 	}],
		// },
		legend: {
			display: false
		},
		// tooltips: {
		// 	backgroundColor: "rgb(255,255,255)",
		// 	bodyFontColor: "#858796",
		// 	titleMarginBottom: 10,
		// 	titleFontColor: '#6e707e',
		// 	titleFontSize: 14,
		// 	borderColor: '#dddfeb',
		// 	borderWidth: 1,
		// 	xPadding: 15,
		// 	yPadding: 15,
		// 	displayColors: false,
		// 	mode: 'index',
		// 	intersect: false,
		// 	caretPadding: 10,
		// 	callbacks: {
		// 		label: function(tooltipItem, chart) {
		// 			var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
		// 			return datasetLabel + ': ' + number_format(tooltipItem.yLabel);
		// 		}
		// 	}
		// }
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
            let urls = [];
            let images = [];
			d.result.forEach(s => {
				labels.push(s.firstname + " " + s.name);
				values.push(s.speechIds.length);
                urls.push(s.image.url);
                // let img = new Image();
                // img.onload = resizeImage;
                // img.src = s.image.url;
                // images.push(img);
			});

			speakerChart.data.labels = labels;
			speakerChart.data.datasets[0].data = values;
			speakerChart.data.datasets[0].imageUrls = urls;
			speakerChart.data.datasets[0].imageElements = images;
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
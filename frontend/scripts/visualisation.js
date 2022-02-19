
var globalURL = "http://localhost:5678"

/**
 * Method for formatting numbers
 * @param number
 * @param decimals
 * @param dec_point
 * @param thousands_sep
 * @returns {string}
 * @author Tinghuan Song
 */
 function number_format (number, decimals, dec_point, thousands_sep) {
    // *     example: number_format(1234.56, 2, ',', ' ');
    // *     return: '1 234,56'
    number = (number + '').replace(',', '').replace(' ', '');
    var n = !isFinite(+number) ? 0 : +number,
        prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
        sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
        dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
        s = '',
        toFixedFix = function(n, prec) {
            var k = Math.pow(10, prec);
            return '' + Math.round(n * k) / k;
        };
    // Fix for IE parseFloat(0.55).toFixed(0) = 0;
    s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
    if (s[0].length > 3) {
        s[0] = s[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, sep);
    }
    if ((s[1] || '').length < prec) {
        s[1] = s[1] || '';
        s[1] += new Array(prec - s[1].length + 1).join('0');
    }
    return s.join(dec);
}

function addNavItem(){
    var contentNode = document.getElementById("content");
    clone = document.getElementById("charts_template").cloneNode(true);
    clone.style.display = "";
    charts = clone.getElementsByTagName("canvas");

    const speakerId = document.getElementById('speaker-id').value;
    const fractionId = document.getElementById('fraction-id').value;
    const minimum = document.getElementById('minimum-id').value;

    const conditions = "user=" + speakerId + "&" + "fraction=" + fractionId + "&" + "minimum=" + minimum;
    h1 = clone.getElementsByTagName("h2")[0];
    h1.innerHTML = conditions;

    createTokenChart(charts[0], conditions)
    createPosChart(charts[1], conditions)
    createSentimentChart(charts[2], conditions)
    createNamedEntityChart(charts[3], conditions)
    createSpeakerChart(charts[4], conditions)

    // contentNode.appendChild(clone);
    insertAfter(clone, contentNode.children[0]);
    // contentNode.insertAfter(clone, contentNode.children[0]);
}

function insertAfter(newNode, existingNode) {
    existingNode.parentNode.insertBefore(newNode, existingNode.nextSibling);
}

getData("http://localhost:5678/fractions")
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
/**
 * @author Lu.Yunni, 6563468, s0425513@stud.uni-frankfurt.de
 */


/**
 * get firstname + lastname of the speaker with a given target id
 * @param {String} targetId id of the target speaker
 * @param {Array[Object]} speakers
 * @returns {String} name of the target speaker
 */
function getSpeakerNameById(targetId, speakers){
    for (let i = 0; i < speakers.length; i++) {
        if (speakers[i].id == targetId){
            const nameElements = [];
            if (speakers[i].hasOwnProperty("title")){
                nameElements.push(speakers[i].title);
            }
            nameElements.push(speakers[i].firstname);
            nameElements.push(speakers[i].name);
            return nameElements.join(" ");
        }
    }
}

/**
 * function to set card content for speaker with the most speeches
 * @param {Object} speakerStatistic json data returned by request
 * @param {Array[Object]} speakers 
 */
function setSpeakerWithMostSpeeches(speakerStatistic, speakers) {
    let maxSpeechCount = 0;
    let targetSpeakerId;
    for (let i = 0; i < speakerStatistic.length; i++) {
        if (speakerStatistic[i].count > maxSpeechCount){
            maxSpeechCount = speakerStatistic[i].count;
            targetSpeakerId = speakerStatistic[i].id;
        }
    }
    
    speakerName = getSpeakerNameById(targetSpeakerId, speakers);
    setElementText(speakerName, "speaker-most-speeches")
}


/**
 * get full name of the speaker of a given speech
 * set card content
 * @param {Object} speech json data returned by request
 * @param {Array[Object]} speakers 
 */
function setSpeakerWithLongestSpeech(speech, speakers){
    speakerName = getSpeakerNameById(speech.speaker, speakers);
    setElementText(speakerName, "speaker-longest-speech")
}


/**
 * find the longest speech from speech statistic fetched with rest api
 * @param {Array[Object]} speechStatistic 
 * @returns {String} id of the longest speech
 */
function getLongestSpeech(speechStatistic){
    let maxSpeechLen = 0;
    let speechId;
    for (let i = 0; i < speechStatistic.length; i++) {
        if (speechStatistic[i].length > maxSpeechLen){
            maxSpeechLen = speechStatistic[i].length;
            speechId = speechStatistic[i].id;
        }
    }
    return speechId;
}


/**
 * set card content for the average speech length
 * @param {Array[Object]} speechStatistics 
 */
function setAverageSpeechLength(speechStatistics){
    let sum = 0;
    for(let i = 0; i < speechStatistics.length; i++ ){
        sum += speechStatistics[i].length;
    }

    let avg = sum / speechStatistics.length;

    setElementText(Math.round(avg), "average-speech-length");
}


/**
 * core function to update cards
 * @param {Object} statisticRaw json data returned by request
 * @param {Object} speakersRaw json data returned by request
 * @returns {Promise} return promise to bind callback functions
 */
function processData(statisticRaw, speakersRaw){
    let speakers = speakersRaw.result;
    setSpeakerWithMostSpeeches(statisticRaw.result.speakers, speakersRaw.result);
    setAverageSpeechLength(statisticRaw.result.speeches);
    setElementText(statisticRaw.result.speeches.length, "number-speeches");
    // setElementText(statisticRaw.result.comments.length, "number-comments");

    let speechId = getLongestSpeech(statisticRaw.result.speeches);
    return new Promise(
        function (res, rej){
            res([speechId, speakers]);
        }
    )
}

function error(input){

}


/**
 * funciton to send ajax request
 * use promise to bind callback function
 * @param {String} url 
 * @returns 
 */
function getData(url) {
    return new Promise(function(res,rej){
        $.ajax({
            type: "GET",
            url: url,
            async: true,
            success: (data) => res(data),
            error: (data) => rej(data)
        });
    })
}


/**
 * given target speech id
 * send ajax request to query speech data
 * pass speech data and speakers data to the next function
 * @param {String} speechId target id of the speech
 * @param {Array[Object]} speakers list of speaker data
 * @returns 
 */
function findSpeech(speechId, speakers){
    return new Promise(function(res,rej){
        $.ajax({
            type: "GET",
            url: "http://localhost:5678/speech?id="+speechId,
            async: true,
            success: (speech) => res([speech, speakers]),
            error: (speech) => rej([speech, speakers])
        });
    })
}


/**
 * common function to set content of the target html element
 * @param {String} text content to set
 * @param {String} elementId id of html element
 */
function setElementText(text, elementId){
    ele = document.getElementById(elementId);
    ele.textContent = text;
}


/**
 * wait to get statistic and speaker data
 * after that, update contents of the cards
 */
Promise.all([
    // getData("http://api.prg2021.texttechnologylab.org/statistic"),
    getData("http://localhost:5678/statistic"),
    // getData("http://api.prg2021.texttechnologylab.org/speakers")
    getData("http://localhost:5678/speakers")
])
.then(([statisticData, speakerData]) => processData(statisticData, speakerData))
.then(([speechId, speakers]) => findSpeech(speechId, speakers))
.then(([speechId, speakers]) => setSpeakerWithLongestSpeech(speechId, speakers));

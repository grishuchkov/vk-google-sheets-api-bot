const BASE_URL = "http://45.12.75.36:80";
const SEND_CHECK_METHOD = "/api/v1/check-notification"

const sheetName = "Активный месяц";
const CONFIGURATION_SHEET_NAME = "Конфигурация";

const USER_LINKS_COLLUMN = 2; // Номер колонки с ссылками на рабочем листе
const EXAMINER_COLLUMN = 5;
const FILE_URL_COLLUMN = 6;
const FIRST_HOMEWORK_COLLUMN = 7;
const LAST_HOMEWORK_COLLUMN = 14;
const MAX_NUMBER_OF_HOMEWORK = 8;

var ss = SpreadsheetApp.getActiveSpreadsheet();

var configurationSheet = ss.getSheetByName(CONFIGURATION_SHEET_NAME);
const EXAMINER_NAME_COLLUMN = 3;
const CHAT_ID_COLLUMN = 4;
const CONFIRM_TOGGLE_COLLUMN = 5;


const BAD_STATUS_CODE = 400;
const CONFLICT_STATUS_CODE = 409;
const OK_STATUS_CODE = 200;

var activeSheet = ss.getSheetByName(sheetName);
var activeCell = activeSheet.getActiveCell();
var activeRow = activeCell.getRow();
var activeColumn = activeCell.getColumn();
var colorOfCell = activeCell.getBackground();
var actualSheetName = ss.getActiveSheet().getName();


function doPost(request) {
    var action = request.parameter.action;

    switch (action) {
        case "addHomework":
            return addHomework(request);
    }

    return ContentService.createTextOutput(JSON.stringify({statusCode: BAD_STATUS_CODE}))
        .setMimeType(ContentService.MimeType.JSON);
}

function addHomework(request) {

    var userScreenName = request.parameter.userScreenName;
    var numberOfHomeworkString = request.parameter.numberOfHomework;

    var numberOfHomework = parseInt(numberOfHomeworkString, 10)
    var userRow = findUserRowBySceenName(userScreenName);

    if (numberOfHomework < 1 || numberOfHomework > MAX_NUMBER_OF_HOMEWORK) {
        return ContentService.createTextOutput(JSON.stringify({statusCode: BAD_STATUS_CODE}))
            .setMimeType(ContentService.MimeType.JSON);
    }
    if (userRow < 1) {
        return ContentService.createTextOutput(JSON.stringify({statusCode: BAD_STATUS_CODE}))
            .setMimeType(ContentService.MimeType.JSON);
    }

    if (activeSheet.getRange(userRow, FIRST_HOMEWORK_COLLUMN - 1 + numberOfHomework).getValue() === "-") {
        return ContentService.createTextOutput(JSON.stringify({statusCode: CONFLICT_STATUS_CODE}))
            .setMimeType(ContentService.MimeType.JSON);
    }

    activeSheet.getRange(userRow, FIRST_HOMEWORK_COLLUMN - 1 + numberOfHomework).setValue("+");

    var studentName = activeSheet.getRange(userRow, USER_LINKS_COLLUMN).getValue();
    var examinerName = activeSheet.getRange(userRow, EXAMINER_COLLUMN).getValue();
    var fileUrl = activeSheet.getRange(userRow, FILE_URL_COLLUMN).getValue();

    var chatId = getTelegramChatIdByExaminer(examinerName);

    var data = {
        'statusCode': OK_STATUS_CODE,
        'telegramChatId': chatId,
        'numberOfWork': numberOfHomework,
        'studentFileUrl': fileUrl,
        'studentName': studentName
    };

    return ContentService.createTextOutput(JSON.stringify(data))
        .setMimeType(ContentService.MimeType.JSON);
}

function getTelegramChatIdByExaminer(examinerName) {
    let configurationLastRow = configurationSheet.getLastRow() + 1;

    for (let i = 2; i < configurationLastRow; i++) {
        var name = configurationSheet.getRange(i, EXAMINER_NAME_COLLUMN).getValue();
        var toggle = configurationSheet.getRange(i, CONFIRM_TOGGLE_COLLUMN).getValue();
        var id = configurationSheet.getRange(i, CHAT_ID_COLLUMN).getValue();

        if (name === examinerName && toggle === "Да") {
            return id;
        }
    }

    return "";
}

function findUserRowBySceenName(userScreenName) {
    let row = 0;
    const lastRow = activeSheet.getLastRow() + 1;

    for (let i = 1; i < lastRow; i++) {
        var cell = activeSheet.getRange(i, USER_LINKS_COLLUMN);
        var userScreenNameFromTable = "";

        if (cell.getRichTextValue().getLinkUrl() != null) {
            userScreenNameFromTable = cell.getRichTextValue().getLinkUrl().split('/')[3];
        }

        if (userScreenNameFromTable === userScreenName) {
            row = i;
            break
        }
    }

    return row;
}

function returnScreenNameByActiveRow() {
    var cell = activeSheet.getRange(activeRow, USER_LINKS_COLLUMN);
    var url = cell.getRichTextValue().getLinkUrl();
    var screenName = "";

    if (!url)
        throw new Error('The cell in ' + address + ' does not contain a link.');

    if (cell.getRichTextValue().getLinkUrl() != null) {
        screenName = cell.getRichTextValue().getLinkUrl().split('/')[3];
    }

    return screenName;
}

function homeworkCheckProcess() {

    //проверка на границы окна работы и рабочего листа
    if ((actualSheetName === activeSheet.getName()) && (activeColumn >= FIRST_HOMEWORK_COLLUMN && activeColumn <= LAST_HOMEWORK_COLLUMN)) {

        let userScreenName = returnScreenNameByActiveRow();
        let numberOfHomework = activeColumn - FIRST_HOMEWORK_COLLUMN + 1;

        if ((activeCell.getValue() === "+" || activeCell.getValue() === "+-" || activeCell.getValue() === "-+") && colorOfCell !== "#ffffff") {
            sendCheckNotification(userScreenName, numberOfHomework)
        }
        if (typeof (activeCell.getValue()) == "number" && colorOfCell !== "#ffffff") {
            var score = parseInt(activeCell.getValue(), 10);
            sendCheckNotification(userScreenName, numberOfHomework, score)
        }
    }
}


function sendCheckNotification(screenName, numberOfHomework, score) {

    var data = {};

    if (score === undefined) {
        data = {
            'studentScreenName': screenName,
            'numberOfWork': numberOfHomework,
            'type': "WITHOUT_SCORE"
        };
    } else {
        data = {
            'studentScreenName': screenName,
            'numberOfWork': numberOfHomework,
            'type': "WITH_SCORE",
            'score': score
        };
    }

    var ui = SpreadsheetApp.getUi();
    var response = ui.alert('Уведомить ученика о завершении проверки?', ui.ButtonSet.YES_NO);
    if (response == ui.Button.YES) {

        var options = {
            'method': 'post',
            'contentType': 'application/json',
            'payload': JSON.stringify(data)
        };
        UrlFetchApp.fetch(BASE_URL + SEND_CHECK_METHOD, options);
    } else {
        return
    }
}

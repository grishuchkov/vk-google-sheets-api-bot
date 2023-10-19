const USER_LINKS_COLLUMN = 2;
const FIRST_HOMEWORK_COLLUMN = 6;
const LAST_HOMEWORK_COLLUMN = 13;
const MAX_NUMBER_OF_HOMEWORK = 8;


var ss = SpreadsheetApp.getActiveSpreadsheet();


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

    return ContentService.createTextOutput(JSON.stringify({statusCode: 500}))
        .setMimeType(ContentService.MimeType.JSON);
}

function addHomework(request) {

    var userScreenName = request.parameter.userScreenName;
    var numberOfHomeworkString = request.parameter.numberOfHomework;

    var numberOfHomework = parseInt(numberOfHomeworkString, 10)
    var userRow = findUserRowBySceenName(userScreenName);

    if (numberOfHomework < 1 && numberOfHomework > MAX_NUMBER_OF_HOMEWORK) {
        return ContentService.createTextOutput(JSON.stringify({statusCode: 500}))
            .setMimeType(ContentService.MimeType.JSON);
    }
    if (userRow < 1) {
        return ContentService.createTextOutput(JSON.stringify({statusCode: 500}))
            .setMimeType(ContentService.MimeType.JSON);
    }

    activeSheet.getRange(userRow, FIRST_HOMEWORK_COLLUMN - 1 + numberOfHomework).setValue("+");

    return ContentService.createTextOutput(JSON.stringify({statusCode: 200}))
        .setMimeType(ContentService.MimeType.JSON);
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

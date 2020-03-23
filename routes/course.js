'user strict';

var sql = require('./db.js');

var stateCourse = false;
var jsonResultCourse;

const getResult = () => {
    return jsonResultCourse;
};

function loadCourse() {
    if (stateCourse == false) {
        true;
    } else {
        return;
    }
    sql.query('SELECT * FROM type_course ', function (error, resultCourse) {
        jsonResultCourse = resultCourse;
    });
}

loadCourse();

module.exports = getResult;


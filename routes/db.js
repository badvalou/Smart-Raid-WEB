'user strict';

var mysql = require('mysql');

// mysql db connection
var connection = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "",
    database: "testnodejs"
});

module.exports = connection;


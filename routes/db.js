'user strict';

var mysql = require('mysql');

// mysql db connection
var connection = mysql.createConnection({
    host: "",
    user: "",
    password: "",
    database: ""
});

module.exports = connection;


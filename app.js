var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
const busboy = require('connect-busboy');
const i18n = require('i18n');
const common = require('./lib/common');

// require routes
var indexRouter = require('./routes/index');
var tagRouter = require('./routes/tag');
var profileRouter = require('./routes/user');
var apiRouter = require('./routes/api');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({extended: false}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/tag', tagRouter);
app.use('/profile', profileRouter);
app.use('/api', apiRouter);

app.use(function(req, res, next) {
    res.locals.__ = res.__ = function() {
        return i18n.__.apply(req, arguments);
    };
    next();
});


app.use(busboy());
app.use(logger('dev'));

i18n.configure({
    locales: common.getAvailableLanguages(),
    defaultLocale: common.getDefaultLocale(),
    cookie: 'i18n',
    queryParameter: 'lang',
    directory: `${__dirname}/locales`,
    directoryPermissions: '755',
    register: global
});

app.use(i18n.init);
// catch 404 and forward to error handler
app.use(function (req, res, next) {
    next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.render('error');
});

module.exports = app;

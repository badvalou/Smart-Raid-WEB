const express = require('express');
const mysql = require('mysql');
const crypto = require('crypto');
const uuid = require('uuid');
let router = express.Router();
var sql = require('./db.js');
const session = require('express-session');

String.prototype.isEmpty = function () {
    return (this.length === 0 || !this.trim());
};

router.use(session({
    secret: '',
    saveUninitialized: false,
    resave: false,
    cookie: {maxAge: 2 * 60 * 60 * 1000, sameSite: true}
}));

router.use(function (req, res, next) {
    res.locals.session = req.session;
    next();
});

let genRandomString = function (length) {
    return crypto.randomBytes(Math.ceil(length / 2)).toString('hex').slice(0, length);
};

let sha512 = function (password, salt) {
    let hash = crypto.createHmac('sha512', salt);
    hash.update(password);
    let value = hash.digest('hex');
    return {
        salt: salt,
        passwordHash: value
    };
};

function saltHashPassword(userPassword) {
    let salt = genRandomString(16);
    return sha512(userPassword, salt);
}

function checkHashPassword(userPassword, salt) {
    return sha512(userPassword, salt);
}


router.get('/', function (req, res) {
    if(req.cookies.i18n) {
        setLocale(req.cookies.i18n);
    }
    let photoUser = "SELECT Photo.uri,Photo.name,Photo.photo_date,Utilisateur.nom,Utilisateur.prenom FROM Photo JOIN course ON Photo.course_id = course.course_id JOIN Utilisateur ON Utilisateur.id = course.user_id WHERE public = 1";
    sql.query(photoUser, function (error, photoResult) {
        res.render('index', {photoList: photoResult});
    });
    loadCourse();

});

router.get('/projet', function (req, res) {
    res.render('project');
});

router.get('/login', function (req, res) {
    res.render('loginPortal');
});

router.get('/register', function (req, res) {
    res.render('register');
});

router.get('/lang/:lang', function (req, res) {

    res.cookie('i18n', req.params.lang);
    res.redirect("/");


});


router.post('/register', function (req, res) {
    var login = req.body.login;
    sql.query('SELECT * FROM Utilisateur WHERE mail=?', [login], function (error, result) {
        if (result && result.length >= 1) {
            res.render('register', {warningMessage: 'Un compte existe deja.'});
        } else {
            // creation du compte
            let uid = uuid.v4();
            let plaint_password = req.body.password;
            let hash_data = saltHashPassword(plaint_password);
            let password = hash_data.passwordHash;
            let salt = hash_data.salt;
            let name = req.body.surname;
            let surname = req.body.name;

            sql.query("INSERT INTO Utilisateur (uid, prenom, nom, mail, password, salt, inscription) VALUES (?,?,?,?,?,?, NOW() )",
                [uid, name, surname, login, password, salt], function (err, result, fields) {
                    sql.on('error', function (err) {
                        console.log('MYSQL ERROR', err);
                        res.json('Register error: ', err);
                    });
                    res.render('loginPortal', {successMessage: 'Compte creer, connectez vous : '})
                });
        }
    });
});

router.post('/login', function (req, res) {
    var login = req.body.login;
    var password = req.body.password;

    if (login.isEmpty() || password.isEmpty()) {
        res.render('loginPortal', {warningMessage: 'Identifiant ou mot de passe manquant'});
    } else {
        sql.query('SELECT * FROM Utilisateur WHERE mail=?', [login], function (error, result) {
            sql.on('error', function (err) {
                console.log('MYSQL ERROR', err);
            });
            if (result && result.length) {
                let salt = result[0].salt;
                let encrypted_password = result[0].password;
                let hashed_password = checkHashPassword(password, salt).passwordHash;
                if (encrypted_password === hashed_password) {
                    let userId = result[0].id;
                    let userUUID = result[0].uid;
                    req.session.userId = userId;
                    req.session.userUUID = userUUID;
                    res.redirect('/profile');
                } else {
                    res.render('loginPortal', {warningMessage: 'Mauvais mot de passe'});
                }
            } else {
                res.render('loginPortal', {warningMessage: 'Aucune adresse mail inscrite dans le serveur pour : ' + login});
            }
        });
    }
});

router.get('/course/stat', function (req, res) {
    sql.query('SELECT type_course.nom as nom_course, COUNT(course.course_id) as nombre_course FROM course JOIN type_course ON course.course_type = type_course.type_course_id GROUP BY course.course_type', function (error, result, fields) {
        sql.query('SELECT type_course.nom as nom_course, AVG(course.vitmoyenne) as moy FROM type_course JOIN course on course.course_type = type_course.type_course_id GROUP BY type_course_id', function (error3, result3) {
            sql.query('SELECT Location.country, COUNT(Location.country) as nombre FROM Location GROUP BY Location.country', function (error4, result4) {
                res.render('statistique', {chartCourses: result, chartVit: result3, map: result4});
            });
        });
    });
});

router.get('/annotations-year/:date', function (req, res) {
    let date = req.params.date;
    sql.query("SELECT type_course.nom as nom_course, COUNT(course.course_id) as nombre_course FROM course JOIN type_course ON " +
        "course.course_type = type_course.type_course_id WHERE course.date_debut >=" +
        " '" + date + "-1-1' AND course.date_debut <= '" + date + "-12-31' GROUP BY course.course_type", function (error, result) {
        res.json(result);
    });
});

router.get('/course/list', function (req, res) {
    sql.query('SELECT * FROM type_course ', function (error, result) {
        res.render('courseList', {courses: result});
    });

});

router.get('/logout', function (req, res) {
    req.session.destroy(error => {
        if (error) {
            res.redirect('/login');
        }
    });

    res.clearCookie();
    res.redirect('/login');
});


var stateCourse = false;
var jsonResultCourse;

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


module.exports = router;

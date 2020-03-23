var express = require('express');
var sql = require('./db.js');

var router = express.Router();

const redirectLogin = (req, res, next) => {
    if (!req.session.userId) {
        res.redirect('/login');
    } else {
        next();
    }
};


router.get('/', redirectLogin, function (req, res) {
    let uuid = req.session.userUUID;
    let queryCourseUser = "SELECT type_course.nom, course_id FROM type_course JOIN course c on type_course.type_course_id = c.course_type JOIN Utilisateur U on c.user_id = U.id WHERE U.uid = '" + uuid + "'";
    let queryUserInfo = "SELECT nom,prenom,mail,inscription FROM Utilisateur WHERE uid = '" + uuid + "'";
    let badgeLocationUser = "SELECT Location.country, UNIX_TIMESTAMP(Location.location_time) AS epoch_time FROM Location JOIN course c on Location.course_id = c.course_id JOIN Utilisateur U on c.user_id = U.id WHERE U.uid = '" + uuid + "' ORDER BY location_time DESC";
    sql.query(queryUserInfo, function (error, result) {
        sql.query(queryCourseUser, function (error, resultCourseUser) {
            sql.query(badgeLocationUser, function (error, resultBadgeLocation) {
                res.render('profile', {
                    uidStatus: 'true', user: result[0],
                    courseList: jsonResultCourse,
                    userCourseList: resultCourseUser,
                    userBadgeLocation: resultBadgeLocation
                });
            });
        });
    });
});

router.get('/photo', redirectLogin, function (req, res) {
    let uuid = req.session.userUUID;
    let photoUser = "SELECT Photo.uri FROM Photo JOIN course c on Photo.course_id = c.course_id JOIN Utilisateur U on c.user_id = U.id WHERE uid = '" + uuid + "'";
    sql.query(photoUser, function (error, photoResult) {
        res.render('photoSlideShow', {photoList: photoResult});
    });
});


router.post('/edit/mail/:mail', redirectLogin, function (req, res) {
    var mail = req.params.mail;
    var uuid = req.session.userUUID;

    sql.query("UPDATE Utilisateur SET mail = '" + mail + "' WHERE uid = '" + uuid + "'", function (error, result) {
    });
    res.sendStatus(201);
});

router.post('/edit/name/:name', redirectLogin, function (req, res) {
    var name = req.params.name;
    var uuid = req.session.userUUID;

    sql.query("UPDATE Utilisateur SET nom = '" + name + "' WHERE uid = '" + uuid + "'", function (error, result) {
    });
    res.sendStatus(201);
});

router.post('/edit/surname/:surname', redirectLogin, function (req, res) {
    var surname = req.params.surname;
    var uuid = req.session.userUUID;

    sql.query("UPDATE Utilisateur SET prenom = '" + surname + "' WHERE uid = '" + uuid + "'", function (error, result) {
    });
    res.sendStatus(201);
});

router.post('/add/:id', redirectLogin, function (req, res) {
    var id = req.params.id;
    var userId = req.session.userId;

    sql.query("INSERT INTO course (user_id, date_debut, course_type, vitmoyenne) VALUES ('" + userId + "', NOW(), '" + id + "', '0')", function (error, result) {
    });

    res.sendStatus(201);
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

loadCourse();


module.exports = router;
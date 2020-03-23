var express = require('express');
var sql = require('./db.js');

var router = express.Router();

/**
 * GET /tag
 */
router.get('/', function (req, res) {
    if (req.session.tag == null) {
        res.render('teamTag', {team: true})
    } else {
        sql.query("SELECT * FROM Message JOIN course c on Message.course_id = c.course_id WHERE c.course_id = '" + req.session.tag + "'", function (error, result) {
            sql.query("SELECT Location.* FROM Location JOIN course c on Location.course_id = c.course_id WHERE c.course_id = '" + req.session.tag + "'", function (error, locationResult) {
                sql.query("SELECT Photo.* FROM Photo JOIN course c on Photo.course_id = c.course_id WHERE c.course_id = '" + req.session.tag + "'", function (error, photoResult) {
                    res.render('teamTag', {
                        messages: result,
                        locations : locationResult,
                        team: (req.session.tag == null),
                        name: req.session.tag,
                        photos : photoResult
                    });
                });
            });
        });
    }
});

router.post('/search_query', function (req, res) {
    var id = req.body.id;

    sql.query("SELECT * FROM course WHERE course_id = '" + id + "'", function (error, result) {

        if (result && result.length) {
            req.session.tag = id;
            res.redirect('/tag');
        } else {
            res.render('teamTag', {
                information: "Le numéro d'équipe n'existe pas",
                team: (req.session.tag == null)
            })
        }
    });

});

router.post('/unfollow', function (req, res) {
    req.session.tag = null;
    res.redirect('/tag');
});

module.exports = router;
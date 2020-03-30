const express = require('express');
const uuid = require('uuid');
const crypto = require('crypto');

var app = express.Router();

var con = require('./db.js');

/**
 * genere une chaine aléatoire
 * @param length
 * @returns {*}
 */
let genRandomString = function (length) {
    return crypto.randomBytes(Math.ceil(length / 2)).toString('hex').slice(0, length);
};

/**
 * Chiffrement du mot de passe avec un sel
 * @param password
 * @param salt
 * @returns {{salt: *, passwordHash: PromiseLike<ArrayBuffer>}}
 */
let sha512 = function (password, salt) {
    let hash = crypto.createHmac('sha512', salt);
    hash.update(password);
    let value = hash.digest('hex');
    return {
        salt: salt,
        passwordHash: value
    };
};

/**
 * Chiffrement du mot de passe et generation d'un sel aleatoire
 * @param userPassword
 * @returns {{salt, passwordHash}}
 */
function saltHashPassword(userPassword) {
    let salt = genRandomString(16);
    return sha512(userPassword, salt);
}

function checkHashPassword(userPassword, salt) {
    return sha512(userPassword, salt);
}


app.get('', (req, res) => {
    res.json('ok');
});

app.post('/register/', (req, res) => {
    let post_data = req.body;

    let uid = uuid.v4();
    let plaint_password = req.body.pass;
    let hash_data = saltHashPassword(plaint_password);
    let password = hash_data.passwordHash;
    let salt = hash_data.salt;

    let name = post_data.name;
    let surname = post_data.surname;
    let email = post_data.email;

    con.query('SELECT * FROM Utilisateur WHERE mail=?', [email], function (err, result) {
        con.on('error', function (err) {
            console.log('MYSQL ERROR', err);
        });
        if (result && result.length) {
            res.json('user already exists');
        } else {
            con.query("INSERT INTO Utilisateur (uid, prenom, nom, mail, password, salt, inscription) VALUES (?,?,?,?,?,?, NOW() )", [uid, name, surname, email, password, salt], function (err, result, fields) {
                con.on('error', function (err) {
                    console.log('MYSQL ERROR', err);
                    res.json('Register error: ', err);
                });
                res.json('Register succes');
            });
        }
    });
});

app.post('/login/', (req, res) => {
    let post_data = req.body;
    let user_password = post_data.password;
    let email = post_data.email;

    con.query('SELECT * FROM Utilisateur WHERE mail=?', [email], function (error, result) {
        con.on('error', function (err) {
            console.log('MYSQL ERROR', err);
        });

        if (result && result.length) {
            let salt = result[0].salt;
            let encrypted_password = result[0].password;
            let hashed_password = checkHashPassword(user_password, salt).passwordHash;
            if (encrypted_password === hashed_password) {
                res.end(JSON.stringify(result[0]));
            } else {
                res.end(JSON.stringify('Wrong password'));
            }
        } else {
            res.json('user not exists');
        }

    });
});

app.get('/list/courses/', (req, res) => {
    con.query('SELECT * FROM type_course', function (error, result) {
        res.json(result);
    });
});

/**
 * ajout de la derniere position du pilote
 */
app.post('/insert/position/', (req, res) => {
    let post_data = req.body;
    let course_id = post_data.id;
    let latitude = post_data.latitude;
    let longitude = post_data.longitude;

    con.query("INSERT INTO Location (course_id, latitude, longitude, location_time) VALUES (?, ?, ?, NOW() )", [course_id, latitude, longitude], function () {
        con.on('error', function (err) {
            console.log('MYSQL ERROR', err);
            res.json('Postion update error ', err);
        });
        res.json("Position update succes");
    });
});

app.post('/insert/course/', (req, res) => {
    let post_data = req.body;
    let player = post_data.id;
    let coursetype = post_data.idcourse;

    con.query("INSERT INTO course (user_id, date_debut, course_type) VALUES (?, NOW(), ?)", [player, coursetype], function () {
        con.on('error', function (err) {
            console.log('MYSQL ERROR', err);
            res.json('Postion update error ', err);
        });
        res.json("Course add to player");
    });
});

/**
 * ajout d'un message du pilote par rapport a la course
 */
app.post('/insert/message/', (req, res) => {
    let post_data = req.body;
    let course_id = post_data.id;
    let message = post_data.message;

    con.query("INSERT INTO Message (course_id, contenu, message_time) VALUES (?, ?, NOW())", [course_id, message], function () {
        con.on('error', function (err) {
            console.log('MYSQL ERROR', err);
            res.json('Message insert fail', err);
        });
        res.json("Message upload succes");
    });
});

/*
app.post('/insert/image/', upload.single('myFile'), (req, res, next) => {
    const file = req.file;
    if (!file) {
        const error = new Error('Please upload a file');
        error.httpStatusCode = 400;
        return next(error);
    }
    res.send(file)

});*/

app.get('/profile/:id/all/', (req, res) => {
    let user_id = req.params.id;
    con.query('SELECT id,uid,nom,prenom,password FROM Utilisateur WHERE id=?', [user_id], function (error, result) {
        res.json(result);
    });
});

app.get('/profile/:id/courses/', (req, res) => {
    let user_id = req.params.id;

    con.query('SELECT course_id,course_type,UNIX_TIMESTAMP(date_debut) as date FROM course WHERE user_id=?', [user_id], function (error, result) {
        res.json(result);
    });
});

app.get('/profile/:id/photos/', (req, res) => {
    let course_id = req.params.id;
    con.query('SELECT Photo.name,Photo.uri,UNIX_TIMESTAMP(Photo.photo_date) as date_photo FROM Photo WHERE course_id=?', [course_id], function (error, result) {
        res.json(result);
    });
});

app.get('/profile/:id/messages/', (req, res) => {
    let course_id = req.params.id;
    con.query('SELECT UNIX_TIMESTAMP(Message.message_time) as date_message,contenu FROM Message WHERE course_id=?', [course_id], function (error, result) {
        res.json(result);
    });

});

app.get('/profile/:id/positions/', (req, res) => {
    let course_id = req.params.id;
    con.query('SELECT UNIX_TIMESTAMP(location_time) as date_location,latitude,longitude FROM Location WHERE course_id=?', [course_id], function (error, result) {
        res.json(result);
    });
});

module.exports = app;
package fr.cv.projetiut.smart_raid.user;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import fr.cv.projetiut.smart_raid.data.Course;
import fr.cv.projetiut.smart_raid.data.Message;
import fr.cv.projetiut.smart_raid.data.Photo;
import fr.cv.projetiut.smart_raid.data.Position;
import fr.cv.projetiut.smart_raid.data.retrofit.LoginResponce;
import fr.cv.projetiut.smart_raid.data.retrofit.RestInterface;
import fr.cv.projetiut.smart_raid.data.retrofit.RetrofitClient;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by valentincroset on November,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class User {

    static User userInstance;

    public static void create(LoginResponce loginResponce) {
        if (userInstance == null) {
            userInstance = new User(loginResponce);
        }
    }

    public static User getUserInstance() {
        return userInstance;
    }

    private LoginResponce loginResponce;
    private Course currentCourse;
    private List<Photo> photoList;
    private List<Message> messageList;
    private List<Position> positionList;
    private RecyclerView messageRecyclerView;

    public User(LoginResponce loginResponce) {
        this.loginResponce = loginResponce;
    }

    public void setCurrentCourse(Course currentCourse) {
        this.currentCourse = currentCourse;
    }

    public LoginResponce getLoginResponce() {
        return loginResponce;
    }

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public List<Position> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    public Course getCurrentCourse() {
        return currentCourse;
    }

}

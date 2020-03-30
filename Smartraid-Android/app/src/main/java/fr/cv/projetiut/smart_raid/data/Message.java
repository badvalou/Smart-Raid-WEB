package fr.cv.projetiut.smart_raid.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by valentincroset on November,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class Message {

    @SerializedName("date_message")
    private Long date;

    @SerializedName("contenu")
    private String message;

    public void setDate(Long date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getDate() {
        return date * 1000;
    }

    public String getMessage() {
        return message;
    }

}

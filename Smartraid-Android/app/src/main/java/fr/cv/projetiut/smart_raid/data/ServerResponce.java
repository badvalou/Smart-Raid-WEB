package fr.cv.projetiut.smart_raid.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by valentincroset on January,2020
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class ServerResponce {

    @SerializedName("success")
    boolean success;

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

}

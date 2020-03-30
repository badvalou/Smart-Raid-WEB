package fr.cv.projetiut.smart_raid.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by valentincroset on November,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class Photo {

    @SerializedName("date_photo")
    private Long date;

    @SerializedName("name")
    private String name;

    @SerializedName("uri")
    private String uri;

    public Long getDate() {
        return date * 1000;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }
}

package fr.cv.projetiut.smart_raid.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by valentincroset on November,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class Position {

    @SerializedName("latitude")
    private Long latitude;

    @SerializedName("longitude")
    private Long longitude;

    @SerializedName("date_location")
    private Long date;

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getLatitude() {
        return latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public Long getDate() {
        return date * 1000;
    }
}

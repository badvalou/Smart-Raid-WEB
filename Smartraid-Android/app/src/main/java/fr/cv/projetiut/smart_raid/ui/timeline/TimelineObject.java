package fr.cv.projetiut.smart_raid.ui.timeline;

/**
 * Created by valentincroset on November,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class TimelineObject {
    long timestamp;
    String name, url;

    public TimelineObject(long timestamp, String name, String url) {
        this.timestamp = timestamp;
        this.name = name;
        this.url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getTitle() {
        return name;
    }

    public String getImageUrl() {
        return url;
    }
}
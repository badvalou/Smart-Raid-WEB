package fr.cv.projetiut.smart_raid.data;

import android.os.Build;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;
import java.util.function.Predicate;

/**
 * Created by valentincroset on November,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class Course {

    @SerializedName("course_id")
    private int id;

    @SerializedName("course_type")
    private int courseType;

    @SerializedName("date")
    private Long date;

    public int getId() {
        return id;
    }

    public Long getDate() {
        return date * 1000;
    }

    public String getDateString() {
        return new Date(getDate()).toString();
    }

    public CourseType getCourseType() {
        for (CourseType type : CourseType.getCourseTypeList()) {
            if (type.getId() == courseType) {
                return type;
            }
        }
        return null;
    }

}

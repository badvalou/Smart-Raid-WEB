package fr.cv.projetiut.smart_raid.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by valentincroset on November,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class CourseType {

    private static List<CourseType> courseTypeList;

    public static List<CourseType> getCourseTypeList() {
        return courseTypeList;
    }

    public static void setCourseTypeList(List<CourseType> courseTypeList) {
        CourseType.courseTypeList = courseTypeList;
    }

    @SerializedName("type_course_id")
    private int id;

    @SerializedName("nom")
    private String name;

    @SerializedName("description")
    private String desc;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

}

package fr.cv.projetiut.smart_raid.data.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by valentincroset on December,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class LoginResponce {

    @SerializedName("id")
    private int id;

    @SerializedName("uid")
    private String uid;

    @SerializedName("nom")
    private String nom;

    @SerializedName("prenom")
    private String prenom;

    @SerializedName("password")
    private String password;

    public int getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPassword() {
        return password;
    }
}

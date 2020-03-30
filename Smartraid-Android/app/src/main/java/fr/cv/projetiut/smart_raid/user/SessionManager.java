package fr.cv.projetiut.smart_raid.user;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by valentincroset on December,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class SessionManager {

    private static final String KEY_LOGGED = "user_logged";
    private static final String KEY_USER_ID = "user_unique_id";

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        this.context = context;
        this.sharedPreferences = this.context.getSharedPreferences("login", Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    public void login(int userId) {
        this.editor.putBoolean(KEY_LOGGED, true);
        this.editor.putInt(KEY_USER_ID, userId);
        this.editor.commit();
    }

    public void logout() {
        this.editor.clear();
        this.editor.commit();
    }

    public int getUserId() {
        return this.sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public boolean isLoggedIn() {
        return this.sharedPreferences.getBoolean(KEY_LOGGED, false);
    }

}

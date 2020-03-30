package fr.cv.projetiut.smart_raid.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


import java.util.List;

import fr.cv.projetiut.smart_raid.R;

import fr.cv.projetiut.smart_raid.data.retrofit.LoginResponce;
import fr.cv.projetiut.smart_raid.data.retrofit.RestInterface;
import fr.cv.projetiut.smart_raid.data.retrofit.RetrofitClient;
import fr.cv.projetiut.smart_raid.user.SessionManager;
import fr.cv.projetiut.smart_raid.user.User;
import fr.cv.projetiut.smart_raid.util.NetworkUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by valentincroset on November,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class LoginActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText passwordText;
    private TextView infoView;
    private Button loginButton;
    private Button registerButton;
    private CheckBox checkBox;
    private RestInterface restInterface;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameText = findViewById(R.id.userId);
        passwordText = findViewById(R.id.password);
        infoView = findViewById(R.id.tvInfo);
        loginButton = findViewById(R.id.btnLogin);
        registerButton = findViewById(R.id.btnRegister);
        checkBox = findViewById(R.id.checkBoxLogin);
        this.restInterface = RetrofitClient.getClient().create(RestInterface.class);
        this.sessionManager = new SessionManager(getApplicationContext());

        login(null, null, true, false);

        initEvents();


    }

    private void initEvents() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(nameText.getText().toString(), passwordText.getText().toString(), false, checkBox.isChecked());
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(String mail, String password, boolean autoOnly, final boolean checked) {
        /** L'utilisateur est enregistré localement */
        if (sessionManager.isLoggedIn()) {
            int userId = sessionManager.getUserId();
            restInterface.autologin(userId).enqueue(new Callback<List<LoginResponce>>() {

                @Override
                public void onResponse(Call<List<LoginResponce>> call, Response<List<LoginResponce>> response) {
                    User.create(response.body().get(0));
                    courseView();
                }

                @Override
                public void onFailure(Call<List<LoginResponce>> call, Throwable t) {
                }
            });
        } else if (!autoOnly) { /** l'utilisateur n'est pas connus */
            restInterface.loginUser(mail, password).enqueue(new Callback<LoginResponce>() {
                @Override
                public void onResponse(Call<LoginResponce> call, Response<LoginResponce> response) {
                    LoginResponce loginResponce = response.body();
                    if (loginResponce.getPassword() != null) {
                        User.create(loginResponce);
                        if (checked) {
                            sessionManager.login(loginResponce.getId());
                        }
                        courseView();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponce> call, Throwable t) {
                    restInterface.test().enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            infoView.setText("identifiant incorrect");
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            infoView.setText("Aucune connexion au serveur possible");
                            System.out.println(t.getLocalizedMessage());
                        }
                    });
                }
            });
        }
    }

    private void courseView() {
        Intent intent = new Intent(LoginActivity.this, CourseActivity.class);
        startActivity(intent);
    }

}













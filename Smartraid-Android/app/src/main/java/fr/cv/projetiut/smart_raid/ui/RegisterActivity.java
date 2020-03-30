package fr.cv.projetiut.smart_raid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fr.cv.projetiut.smart_raid.R;
import fr.cv.projetiut.smart_raid.data.retrofit.RestInterface;
import fr.cv.projetiut.smart_raid.data.retrofit.RetrofitClient;
import fr.cv.projetiut.smart_raid.user.User;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by valentincroset on November,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class RegisterActivity extends AppCompatActivity {

    // strings
    private static final String mail_error = "Adresse mail manquante !";
    private static final String mail_invalid = "Adresse mail invalide";
    private static final String password_error = "Mot de passe manquant !";
    private static final String password_error_length = "Mot de passe trop petit !";
    private static final String account_already_exist = "Adresse mail déja utilisé.";
    private static final int min_password = 4;

    RestInterface api;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RestInterface restInterface;

    Button registerButton;
    EditText mailText;
    TextView mailInfoView;
    EditText passwordText;
    TextView passwordInfoView;
    EditText name;
    EditText surname;

    private User user = User.getUserInstance();

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // api
        RestInterface apiService =
                RetrofitClient.getClient().create(RestInterface.class);
        this.restInterface = apiService;
        // view
        registerButton = findViewById(R.id.btnRegister);
        mailText = findViewById(R.id.userMail);
        mailInfoView = findViewById(R.id.textViewMail);
        passwordText = findViewById(R.id.userPassword);
        passwordInfoView = findViewById(R.id.textViewMdpPrivate);
        surname = findViewById(R.id.userSurmane);
        name = findViewById(R.id.userName);

        // events
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    private void registerUser() {
        if (!valideInput()) return;
        compositeDisposable.add(api.registerUser(mailText.getText().toString()
                , name.getText().toString()
                , surname.getText().toString()
                , passwordText.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String content) {
                        if (content.contains("Register succes")) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else if (content.contains("user already exists")) {
                            mailInfoView.setText("Un compte existe deja.¬");
                        } else {
                            mailInfoView.setText("Une erreur est survenue.");
                        }
                    }
                })
        );
    }

    private boolean valideInput() {
        if (TextUtils.isEmpty(mailText.getText().toString())) {
            mailInfoView.setText(mail_error);
            return false;
        } else if (!mailText.getText().toString().contains("@")) {
            mailInfoView.setText(mail_invalid);
            return false;
        }
        if (TextUtils.isEmpty(passwordText.getText().toString())) {
            passwordInfoView.setText(password_error);
            return false;
        } else if (passwordText.length() < min_password) {
            passwordInfoView.setText(password_error_length);
            return false;
        }
        return true;
    }

}

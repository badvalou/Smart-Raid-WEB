package fr.cv.projetiut.smart_raid.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import fr.cv.projetiut.smart_raid.R;
import fr.cv.projetiut.smart_raid.data.Message;
import fr.cv.projetiut.smart_raid.data.Photo;
import fr.cv.projetiut.smart_raid.data.Position;
import fr.cv.projetiut.smart_raid.data.retrofit.RestInterface;
import fr.cv.projetiut.smart_raid.data.retrofit.RetrofitClient;
import fr.cv.projetiut.smart_raid.user.User;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by valentincroset on November,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class SplashScreenConnexionActivity extends AppCompatActivity {

    private boolean messageDowload = false;
    private boolean photoDownload = false;
    private boolean positionDowload = false;
    private Runnable mainTask;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RestInterface restInterface;
    private User user = User.getUserInstance();


    private Runnable activityRunnable;
    private TextView textView;
    private LinearLayout linearLayout;
    private Button refreshButton;
    private long lastClick = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_connexion);

        textView = findViewById(R.id.infoLoginSplashTextView);
        linearLayout = findViewById(R.id.linearLayoutSplash);
        mainTask = getActivityRunnable();

        RestInterface apiService =
                RetrofitClient.getClient().create(RestInterface.class);
        this.restInterface = apiService;

        download();
        startSchedule();
    }

    private void download() {
        int courseId = user.getCurrentCourse().getId();
        textView.setText("Telechargement du fil d'actualité");
        restInterface.userPhotos(courseId).enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                user.setPhotoList(response.body());
                photoDownload = true;
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                textView.setText("Impossible de telecharger les photos sur le serveur.");
            }
        });
        restInterface.userMessages(courseId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                user.setMessageList(response.body());
                messageDowload = true;
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                textView.setText("Impossible de telecharger votre fil d'actualité depuis le serveur.");
            }
        });
        restInterface.userPositions(courseId).enqueue(new Callback<List<Position>>() {
            @Override
            public void onResponse(Call<List<Position>> call, Response<List<Position>> response) {
                positionDowload = true;
                user.setPositionList(response.body());
            }

            @Override
            public void onFailure(Call<List<Position>> call, Throwable t) {
                textView.setText("Impossible de telecharger la carte depuis le serveur.");
            }
        });
    }

    private Runnable getActivityRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                if (photoDownload && messageDowload && positionDowload) {
                    Intent intent = new Intent(SplashScreenConnexionActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    startSchedule();
                }
            }
        };
    }

    private void startSchedule() {
        new Handler().postDelayed(mainTask, 1500);
    }

}

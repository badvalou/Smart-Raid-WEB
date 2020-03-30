package fr.cv.projetiut.smart_raid.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.cv.projetiut.smart_raid.R;
import fr.cv.projetiut.smart_raid.data.Message;
import fr.cv.projetiut.smart_raid.data.Photo;
import fr.cv.projetiut.smart_raid.data.Position;
import fr.cv.projetiut.smart_raid.data.ServerResponce;
import fr.cv.projetiut.smart_raid.data.retrofit.RestInterface;
import fr.cv.projetiut.smart_raid.data.retrofit.RetrofitClient;
import fr.cv.projetiut.smart_raid.ui.menu.Option;
import fr.cv.projetiut.smart_raid.ui.timeline.TimeLineConfig;
import fr.cv.projetiut.smart_raid.ui.timeline.TimelineFragment;
import fr.cv.projetiut.smart_raid.ui.timeline.TimelineObject;
import fr.cv.projetiut.smart_raid.user.SessionManager;
import fr.cv.projetiut.smart_raid.user.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by valentincroset on November,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class MainActivity extends AppCompatActivity {

    private static final String SHARE_LINK = "https://localhost:3000/folow/";
    static final int REQUEST_TAKE_PHOTO = 1;

    private RestInterface restInterface;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView courseNameTextView;
    private final User user = User.getUserInstance();
    private List<Option> optionList = new ArrayList<>();

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        optionList.add(new Option("Obtenir un lien de partage", new Runnable() {
            @Override
            public void run() {
                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                Uri copyUri = Uri.parse(SHARE_LINK + user.getLoginResponce().getId());
                ClipData clipData = ClipData.newUri(getContentResolver(), "URI", copyUri);
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(MainActivity.this, "Lien copié dans le clipboard", Toast.LENGTH_LONG).show();
            }
        }));

        optionList.add(new Option("Ajouter une photo", new Runnable() {
            @Override
            public void run() {
                dispatchTakePictureIntent();
            }
        }));

        optionList.add(new Option("Position", "MAJ de la position", new Runnable() {
            @Override
            public void run() {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            updateLoc(location);
                        } else {
                            Toast.makeText(MainActivity.this, "Impossible de récupérer votre position", Toast.LENGTH_LONG);
                        }
                    }
                });
            }
        }));

        optionList.add(new Option("Position", "Carte de vos positions", new Runnable() {
            @Override
            public void run() {
                for (Position position : user.getPositionList()) {
                    System.out.println("POSITION : lat : " + position.getLatitude() + " long : " + position.getLongitude());
                }
                Intent intent = new Intent(MainActivity.this, MapsActivityUser.class);
                startActivity(intent);
            }
        }));

        optionList.add(new Option("Message", "Poster un message", new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Publiez un message");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
                builder.setView(input);
                builder.setPositiveButton("Poster", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Le message est vide :(", Toast.LENGTH_LONG).show();
                        } else {
                            sendMessage(input.getText().toString());
                            Toast.makeText(MainActivity.this, "Message envoyé !", Toast.LENGTH_LONG);
                        }
                    }
                });
                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        }));

        optionList.add(new Option("Message", "Liste des messages", new Runnable() {
            @Override
            public void run() {

                // test
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent);
            }
        }));

        optionList.add(new Option("Se deconnecter", new Runnable() {
            @Override
            public void run() {
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                sessionManager.logout();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }));

        for (Option option : optionList) {
            if (!option.getMenu().isEmpty()) {
                SubMenu subMenu = null;
                for (int i = 1; i < menu.size(); i++) {
                    if (menu.getItem(i) != null && menu.getItem(i).hasSubMenu() && menu.getItem(i).getTitle().equals(option.getMenu())) {
                        subMenu = menu.getItem(i).getSubMenu();
                    }
                }
                if (subMenu == null) {
                    subMenu = menu.addSubMenu(option.getMenu());
                }
                subMenu.add(option.getTitle());
            } else {
                menu.add(option.getTitle());
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String text = String.valueOf(item.getTitle());
        for (Option option : optionList) {
            if (option.getTitle().equals(text)) {
                option.getRunnable().run();
            }
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // api
        RestInterface apiService =
                RetrofitClient.getClient().create(RestInterface.class);
        this.restInterface = apiService;

        requestPermissions();

        courseNameTextView = findViewById(R.id.course_title_tv);
        courseNameTextView.setText("Course : " + user.getCurrentCourse().getCourseType().getName());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        TimelineFragment timelineFragment = new TimelineFragment();
        TimeLineConfig.setData(

                loadDataInTimeline());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, timelineFragment);
        transaction.commit();
    }

    private ArrayList<TimelineObject> loadDataInTimeline() {
        final ArrayList<TimelineObject> objs = new ArrayList<>();
        objs.add(new TimelineObject(user.getCurrentCourse().getDate(), "Départ", "https://s1.qwant.com/thumbr/0x380/8/5/ce4d85d2f751480afafbdf2c623a3a2a409f69fadce47c4041b47611de6757/depositphotos_5705182-stock-illustration-start-shiny-red-button.jpg?u=http%3A%2F%2Fstatic6.depositphotos.com%2F1003153%2F570%2Fv%2F950%2Fdepositphotos_5705182-stock-illustration-start-shiny-red-button.jpg&q=0&b=1&p=0&a=1"));

        for (Photo photo : user.getPhotoList()) {
            objs.add(new TimelineObject(photo.getDate(), photo.getName(), photo.getUri()));
        }
        return objs;
    }

    private void updateLoc(Location location) {
        restInterface.position(user.getCurrentCourse().getId(), location.getLatitude(), location.getLongitude())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
        // ajout de la position dans le cache
        Position positionBuilder = new Position();
        positionBuilder.setDate(System.currentTimeMillis() / 1000);
        positionBuilder.setLatitude((long) location.getLatitude());
        positionBuilder.setLongitude((long) location.getLongitude());
    }

    private void sendMessage(String message) {
        restInterface.sendMessage(user.getCurrentCourse().getId(), message).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Impossible envoyer le message", Toast.LENGTH_LONG);
            }
        });
        // ajout du message dans le cache de l'utilisateur
        Message messageBuilder = new Message();
        messageBuilder.setDate(System.currentTimeMillis() / 1000);
        messageBuilder.setMessage(message);
        user.getMessageList().add(messageBuilder);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA_SERVICE}, 1);
    }

    private void dispatchTakePictureIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_TAKE_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK && null != data) {

            Toast.makeText(getApplicationContext(), "Image en traitement...", Toast.LENGTH_SHORT).show();

            Uri imageUri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(imageUri, projection, null, null, null);

            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                String path = cursor.getString(columnIndex);

                try {
                    FileInputStream fs = new FileInputStream(path);
                    File file = new File(path);


                    RequestBody requestBody = RequestBody.create(MediaType.parse(""), file);
                    MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                    RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                    restInterface.uploadImage(fileToUpload, filename).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });


                    byte[] imageBytes = new byte[fs.available()];
                    fs.read(imageBytes);

                    StringBuilder stringBuilder = new StringBuilder();
                    for (byte imageByte : imageBytes) {
                        stringBuilder.append(imageByte);
                    }

                    // envoi de l'image sur ftp


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

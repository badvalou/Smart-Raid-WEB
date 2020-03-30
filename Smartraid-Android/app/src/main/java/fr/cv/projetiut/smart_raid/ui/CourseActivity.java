package fr.cv.projetiut.smart_raid.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import fr.cv.projetiut.smart_raid.R;
import fr.cv.projetiut.smart_raid.data.Course;
import fr.cv.projetiut.smart_raid.data.CourseType;
import fr.cv.projetiut.smart_raid.data.retrofit.RestInterface;
import fr.cv.projetiut.smart_raid.data.retrofit.RetrofitClient;
import fr.cv.projetiut.smart_raid.ui.list.course.dummy.ItemFragment;
import fr.cv.projetiut.smart_raid.ui.list.course.dummy.ListViewAdapter;
import fr.cv.projetiut.smart_raid.ui.list.course.dummy.CourseObjectContent;
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
public class CourseActivity extends AppCompatActivity {

    private User user = User.getUserInstance();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RestInterface restInterface;
    private Button addCourseButton;

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        RestInterface apiService =
                RetrofitClient.getClient().create(RestInterface.class);
        this.restInterface = apiService;

        addCourseButton = findViewById(R.id.button_add_course);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddCourse();
            }
        });
        loadData();
    }


    private void loadData() {
        restInterface.coursesList().enqueue(new Callback<List<CourseType>>() {
            @Override
            public void onResponse(Call<List<CourseType>> call, Response<List<CourseType>> response) {
                CourseType.setCourseTypeList(response.body());
            }

            @Override
            public void onFailure(Call<List<CourseType>> call, Throwable t) {
                CourseType.setCourseTypeList(new ArrayList<CourseType>());
            }
        });
        restInterface.userCourses(user.getLoginResponce().getId()).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                createList(response.body());
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {

            }
        });
    }

    private void dialogAddCourse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisir une course : ");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);
        for (CourseType courseType : CourseType.getCourseTypeList()) {
            adapter.add(courseType.getName());
        }
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strName = adapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(CourseActivity.this);
                builderInner.setMessage(strName);
                builderInner.setTitle("Ajouter la course");
                builderInner.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addCourse(strName);
                        dialog.dismiss();
                    }
                });
                builderInner.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builder.show();
    }

    private void addCourse(String courseName) {
        CourseType courseType = null;
        for (CourseType type : CourseType.getCourseTypeList()) {
            if (type.getName().equals(courseName)) {
                courseType = type;
            }
        }
        if (courseType == null)
            return;

        restInterface.course(user.getLoginResponce().getId(), courseType.getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                CourseObjectContent.ITEM_MAP.clear();
                CourseObjectContent.ITEMS.clear();
                loadData();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void createList(List<Course> courseList) {
        RecyclerView recyclerView = findViewById(R.id.course_list);
        for (Course course : courseList) {
            CourseObjectContent.addItem(new CourseObjectContent.DummyItem(course, course.getDateString(),
                    course.getCourseType().getName(), ""));
        }

        ListViewAdapter listViewAdapter = new ListViewAdapter(CourseObjectContent.ITEMS, new ItemFragment.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(CourseObjectContent.DummyItem item) {
                selectCourse(item.getCourseId());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listViewAdapter);
        if (addCourseButton != null)
            addCourseButton.refreshDrawableState();
    }

    private void selectCourse(Course idCourse) {
        user.setCurrentCourse(idCourse);
        Intent intent = new Intent(CourseActivity.this, SplashScreenConnexionActivity.class);
        startActivity(intent);
    }

}
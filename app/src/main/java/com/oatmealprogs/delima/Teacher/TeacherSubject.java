package com.oatmealprogs.delima.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oatmealprogs.delima.R;
import com.oatmealprogs.delima.SessionManagerTeacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherSubject extends AppCompatActivity {

    SessionManagerTeacher sessionManagerTeacher;
    LinearLayout layout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String className;
    ArrayList<QueryDocumentSnapshot> snapshots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_subject);

        layout = (LinearLayout) findViewById(R.id.teacherSubject_LinearLayout);

        sessionManagerTeacher = new SessionManagerTeacher(getApplicationContext(), SessionManagerTeacher.SESSION_LOGIN);
        HashMap<String, String> hashMap = sessionManagerTeacher.getLoginSession();
        String userID = String.valueOf(hashMap.get(SessionManagerTeacher.KEY_USERID));

        db.collection("Teachers")
                .document(userID)
                .collection("Subjects")
                .document("Алгебра")
                .collection("Classes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                snapshots.add(document);
                            }

                            for (QueryDocumentSnapshot entry : snapshots) {
                                className = entry.getId();
                                addButton(className);
                            }


                        } else {
                            return;
                        }
                    }
                });

    }

    public void goBack(View view) {
        Intent i = new Intent(TeacherSubject.this, TeacherMainScreen.class);
        startActivity(i);
        finish();
    }

    // should receive some data from DB to
    // show label and store onClickListener with action to open the Subject
    public void addButton(String name) {

        View view = LayoutInflater.from(this).inflate(R.layout.teacher_class_button, null);
        Button button = (Button) view.findViewById(R.id.teacher_class_buttonLayout);
        button.setText(name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherSubject.this, TeacherClassChosen.class);
                i.putExtra("className", name);
                startActivity(i);
                finish();
            }
        });
        layout.addView(view);
    }
}
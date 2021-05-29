package com.oatmealprogs.delima.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oatmealprogs.delima.R;
import com.oatmealprogs.delima.Shared.HomeworkDetails;
import com.oatmealprogs.delima.Teacher.TeacherClassHomework;
import com.oatmealprogs.delima.Teacher.TeacherEditTask;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class StudentClassHomework extends AppCompatActivity {

    Intent parentIntent;
    String className;
    HashMap<String, QueryDocumentSnapshot> hashMap = new HashMap<>();
    LinearLayout linearLayout;
    ArrayDeque<QueryDocumentSnapshot> snapshots = new ArrayDeque<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_class_homework);

        parentIntent = getIntent();
        className = parentIntent.getStringExtra("className");

        linearLayout = findViewById(R.id.studentClassHomework_LinearLayout);

        String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        db.collection("Year")
                .document(currentYear)
                .collection("Classes")
                .document(className)
                .collection("Subjects")
                .document("Алгебра")
                .collection("Homework")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                snapshots.add(document);
                            }

                            String dateTime, description;
                            while (!snapshots.isEmpty()) {
                                dateTime = String.valueOf(snapshots.peekLast().get("datetime"));
                                description = String.valueOf(snapshots.peekLast().get("Description"));
                                addTaskToList(dateTime, description);
                                snapshots.pollLast();
                            }

                        } else {
                            return;
                        }
                    }
                });

    }

    public void addTaskToList(String dateTime, String description) {

        View view = LayoutInflater.from(this).inflate(R.layout.homework_item, null); // which view to add
        TextView dateText = view.findViewById(R.id.dateLabel);
        TextView descriptionText = view.findViewById(R.id.homeworkDescription);
        ImageButton buttonOpen = view.findViewById(R.id.detailsButton);

        dateText.setText(dateTime);
        descriptionText.setText(description);
        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentClassHomework.this, HomeworkDetails.class);
                i.putExtra("datetime", dateTime);
                i.putExtra("description", description);
                i.putExtra("className", className);
                i.putExtra("intentID", 1);
                startActivity(i);
                finish();
            }
        });
        linearLayout.addView(view);
    }

    public void goBack(View view) {
        Intent i = new Intent(StudentClassHomework.this, StudentSubjectChosen.class);
        startActivity(i);
        finish();

    }
}
package com.oatmealprogs.delima.Teacher;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class TeacherClassMarks extends AppCompatActivity {

    Intent parentIntent;
    String className;
    HashMap<String, QueryDocumentSnapshot> hashMap = new HashMap<>();
    LinearLayout linearLayout;
    ArrayList<QueryDocumentSnapshot> snapshots = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_marks);

        parentIntent = getIntent();
        className = parentIntent.getStringExtra("className");

        linearLayout = findViewById(R.id.teacherClassMarks_LinearLayout);

        String currentYear = "2021";
        // следующая строка берёт текущий год, а в БД только 2021 год есть
        // = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        db.collection("Year")
                .document(currentYear)
                .collection("Classes")
                .document(className)
                .collection("Students")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                snapshots.add(document);
                            }

                            String fullName, firstName, lastName, studentID;
                            for (QueryDocumentSnapshot entry : snapshots) {
                                firstName = String.valueOf(entry.get("First Name"));
                                lastName = String.valueOf(entry.get("Last Name"));
                                fullName = firstName + " " + lastName;
                                studentID = entry.getId();
                                addStudentToList(fullName, studentID);
                            }


                        } else {
                            return;
                        }
                    }
                });

    }

    public void addStudentToList(String fullName, String studentID) {

        View view = LayoutInflater.from(this).inflate(R.layout.students_teacher, null); // which view to add
        TextView fullNameView = view.findViewById(R.id.studentFullName);
        ImageButton buttonOpen = view.findViewById(R.id.studentDetailsButton);

        fullNameView.setText(fullName);
        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherClassMarks.this, TeacherClassStudentMarks.class);
                i.putExtra("fullName", fullName);
                i.putExtra("studentID", studentID);
                i.putExtra("className", className);
                startActivity(i);
                finish();
            }
        });
        linearLayout.addView(view);
    }


    public void goBack(View view) {
        Intent intent = new Intent(TeacherClassMarks.this, TeacherClassChosen.class);
        intent.putExtra("className", className);
        startActivity(intent);
        finish();
    }
}
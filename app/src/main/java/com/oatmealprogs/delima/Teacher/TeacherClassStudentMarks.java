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

public class TeacherClassStudentMarks extends AppCompatActivity {

    Intent parentIntent;
    String className, fullName, studentId;

    LinearLayout linearLayout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<QueryDocumentSnapshot> snapshots = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_student_marks);

        parentIntent = getIntent();
        className = parentIntent.getStringExtra("className");
        fullName = parentIntent.getStringExtra("fullName");
        studentId = parentIntent.getStringExtra("studentID");

        linearLayout = findViewById(R.id.teacherClassStudentMarks_LinearLayout);

        String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        db.collection("Year")
                .document(currentYear)
                .collection("Classes")
                .document(className)
                .collection("Students")
                .document(studentId)
                .collection("Marks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                snapshots.add(document);
                            }

                            String mark, datetime, description;
                            for (QueryDocumentSnapshot entry : snapshots) {
                                mark = String.valueOf(entry.get("Mark"));
                                datetime = String.valueOf(entry.get("datetime"));
                                description = String.valueOf(entry.get("Description"));
                                addMarkToList(mark, datetime, description);
                            }


                        } else {
                            return;
                        }
                    }
                });

    }

    public void goBack(View view) {
        Intent intent = new Intent(TeacherClassStudentMarks.this, TeacherClassMarks.class);
        intent.putExtra("className", className);
        startActivity(intent);
        finish();

    }

    public void addMarkToList(String mark, String dateTimeString, String description) {

        View view = LayoutInflater.from(this).inflate(R.layout.marks_item_teacher, null); // which view to add

        TextView dateText = view.findViewById(R.id.dateLabel);
        TextView descriptionText = view.findViewById(R.id.markDescription);
        TextView markText = view.findViewById(R.id.markLabel);
        ImageButton buttonEdit = view.findViewById(R.id.editMarkButton);

        dateText.setText(dateTimeString);
        markText.setText(mark);
        descriptionText.setText(description);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherClassStudentMarks.this, TeacherEditMark.class);
                i.putExtra("datetime", dateTimeString);
                i.putExtra("description", description);
                i.putExtra("mark", mark);
                i.putExtra("className", className);
                i.putExtra("studentID", studentId);
                i.putExtra("fullName", fullName);
                startActivity(i);
                finish();

            }
        });
        linearLayout.addView(view);

    }

    public void addNewMark(View view) {
        Intent i = new Intent(TeacherClassStudentMarks.this, TeacherAddNewMark.class);
        i.putExtra("className", className);
        i.putExtra("studentID", studentId);
        startActivity(i);
        finish();
    }
}
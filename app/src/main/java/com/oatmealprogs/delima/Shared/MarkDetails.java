package com.oatmealprogs.delima.Shared;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.oatmealprogs.delima.R;
import com.oatmealprogs.delima.Student.StudentClassHomework;
import com.oatmealprogs.delima.Student.StudentMyMarks;
import com.oatmealprogs.delima.Teacher.TeacherClassHomework;
import com.oatmealprogs.delima.Teacher.TeacherClassStudentMarks;

public class MarkDetails extends AppCompatActivity {

    Intent parentIntent;
    TextView date, description, mark;
    int parentIntentId;
    String className, studentID, fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_details);

        parentIntent = getIntent();
        parentIntentId = parentIntent.getIntExtra("intentID", -1);
        className = parentIntent.getStringExtra("className");
        studentID = parentIntent.getStringExtra("studentID");
        fullName = parentIntent.getStringExtra("fullName");

        date = findViewById(R.id.markDetails_date);
        description = findViewById(R.id.markDetails_description);
        mark = findViewById(R.id.markDetails_mark);

        date.setText("Дата: " + parentIntent.getStringExtra("datetime"));
        mark.setText("Балл: " + parentIntent.getStringExtra("mark"));
        description.setText("Завдання: " + parentIntent.getStringExtra("description"));

    }

    public void goBack(View view) {
        switch (parentIntentId) {
            case -1:
                finish();
                break;
            case 0:
                Intent intentTeacher = new Intent(MarkDetails.this, TeacherClassStudentMarks.class);
                intentTeacher.putExtra("className", className);
                intentTeacher.putExtra("studentID", studentID);
                intentTeacher.putExtra("fullName", fullName);

                startActivity(intentTeacher);
                finish();
                break;
            case 1:
                Intent intentStudent = new Intent(MarkDetails.this, StudentMyMarks.class);
                intentStudent.putExtra("className", className);
                intentStudent.putExtra("studentID", studentID);

                startActivity(intentStudent);
                finish();
                break;
        }
        finish();
    }
}
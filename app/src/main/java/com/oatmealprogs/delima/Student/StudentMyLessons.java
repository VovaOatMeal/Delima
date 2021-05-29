package com.oatmealprogs.delima.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oatmealprogs.delima.R;

public class StudentMyLessons extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_my_lessons);
    }

    public void openSubject(View view) {
        Intent i = new Intent(StudentMyLessons.this, StudentSubjectChosen.class);
        startActivity(i);
        finish();

    }

    public void goBack(View view) {
        Intent i = new Intent(StudentMyLessons.this, StudentMainScreen.class);
        startActivity(i);
        finish();

    }
}
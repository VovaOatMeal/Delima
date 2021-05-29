package com.oatmealprogs.delima.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oatmealprogs.delima.R;
import com.oatmealprogs.delima.SessionManagerStudent;
import com.oatmealprogs.delima.Teacher.TeacherClassChosen;
import com.oatmealprogs.delima.Teacher.TeacherClassHomework;
import com.oatmealprogs.delima.Teacher.TeacherClassMarks;

import java.util.HashMap;

public class StudentSubjectChosen extends AppCompatActivity {

    SessionManagerStudent sessionManagerStudent;
    String className, studentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subject_chosen);

        sessionManagerStudent = new SessionManagerStudent(getApplicationContext(), SessionManagerStudent.SESSION_LOGIN);
        HashMap<String, String> hashMap = sessionManagerStudent.getLoginSession();

        className = hashMap.get(SessionManagerStudent.KEY_CLASSNAME);
        studentID = hashMap.get(SessionManagerStudent.KEY_USERID);

    }

    public void goBack(View view) {
        Intent i = new Intent(StudentSubjectChosen.this, StudentMyLessons.class);
        startActivity(i);
        finish();

    }

    public void chooseHomework(View view) {
        Intent intent = new Intent(StudentSubjectChosen.this, StudentClassHomework.class);
        intent.putExtra("className", className);

        startActivity(intent);
        finish();
    }

    public void chooseMarks(View view) {
        Intent intent = new Intent(StudentSubjectChosen.this, StudentMyMarks.class);
        intent.putExtra("className", className);
        intent.putExtra("studentID", studentID);

        startActivity(intent);
        finish();

    }
}
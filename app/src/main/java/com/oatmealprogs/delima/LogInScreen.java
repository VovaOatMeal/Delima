package com.oatmealprogs.delima;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oatmealprogs.delima.Student.StudentLogIn;
import com.oatmealprogs.delima.Student.StudentMainScreen;
import com.oatmealprogs.delima.Teacher.TeacherLogIn;
import com.oatmealprogs.delima.Teacher.TeacherMainScreen;

public class LogInScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

    }

    public void studentSignIn(View view) {
        Intent intent = new Intent(this, StudentLogIn.class);
        startActivity(intent);
    }

    public void teacherSignIn(View view) {
        Intent intent = new Intent(this, TeacherLogIn.class);
        startActivity(intent);
    }

}
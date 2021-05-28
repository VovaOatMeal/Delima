package com.oatmealprogs.delima.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.oatmealprogs.delima.LogInScreen;
import com.oatmealprogs.delima.R;
import com.oatmealprogs.delima.SessionManagerStudent;

import java.util.HashMap;

public class StudentUserDetails extends AppCompatActivity {


    TextView studentFirstName, studentLastName, studentClass, studentID;

    SessionManagerStudent sessionManagerStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_user_details);

        studentFirstName = findViewById(R.id.studentUserDetails_studentFirstName);
        studentLastName = findViewById(R.id.studentUserDetails_studentLastName);
        studentClass = findViewById(R.id.studentUserDetails_studentClassName);
        studentID = findViewById(R.id.studentUserDetails_studentID);

        sessionManagerStudent = new SessionManagerStudent(getApplicationContext(), SessionManagerStudent.SESSION_LOGIN);
        HashMap<String, String> hashMap = sessionManagerStudent.getLoginSession();

        studentFirstName.setText((hashMap.get(SessionManagerStudent.KEY_FIRSTNAME)));
        studentLastName.setText((hashMap.get(SessionManagerStudent.KEY_LASTNAME)));
        studentClass.setText(hashMap.get(SessionManagerStudent.KEY_CLASSNAME));
        studentID.setText("Мій ID: " + hashMap.get(SessionManagerStudent.KEY_USERID));

    }

    public void goBack(View view) {
        Intent i = new Intent(StudentUserDetails.this, StudentMainScreen.class);
        startActivity(i);
        finish();

    }

    public void logoutUser(View view) {
        sessionManagerStudent.logoutUserFromSession();
        Intent i = new Intent(StudentUserDetails.this, LogInScreen.class);
        startActivity(i);
        finish();
    }
}
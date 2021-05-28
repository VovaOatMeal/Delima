package com.oatmealprogs.delima.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.oatmealprogs.delima.LogInScreen;
import com.oatmealprogs.delima.R;
import com.oatmealprogs.delima.SessionManagerTeacher;
import com.oatmealprogs.delima.SessionManagerTeacher;

import java.util.HashMap;

public class TeacherUserDetails extends AppCompatActivity {

    TextView teacherFirstName, teacherLastName, teacherID;

    SessionManagerTeacher sessionManagerTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_user_details);
        
        teacherFirstName = findViewById(R.id.teacherUserDetails_teacherFirstName);
        teacherLastName = findViewById(R.id.teacherUserDetails_teacherLastName);
        teacherID = findViewById(R.id.teacherUserDetails_teacherID);

        sessionManagerTeacher = new SessionManagerTeacher(getApplicationContext(), SessionManagerTeacher.SESSION_LOGIN);
        HashMap<String, String> hashMap = sessionManagerTeacher.getLoginSession();

        teacherFirstName.setText((hashMap.get(SessionManagerTeacher.KEY_FIRSTNAME)));
        teacherLastName.setText((hashMap.get(SessionManagerTeacher.KEY_LASTNAME)));
        teacherID.setText("Мій ID: " + hashMap.get(SessionManagerTeacher.KEY_USERID));

    }

    public void goBack(View view) {
        Intent i = new Intent(TeacherUserDetails.this, TeacherMainScreen.class);
        startActivity(i);
        finish();
    }

    public void logoutUser(View view) {
        sessionManagerTeacher.logoutUserFromSession();
        Intent i = new Intent(TeacherUserDetails.this, LogInScreen.class);
        startActivity(i);
        finish();
    }

}
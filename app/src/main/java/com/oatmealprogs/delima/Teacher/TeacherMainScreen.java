package com.oatmealprogs.delima.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.oatmealprogs.delima.R;
import com.oatmealprogs.delima.SessionManagerStudent;
import com.oatmealprogs.delima.SessionManagerTeacher;
import com.oatmealprogs.delima.Student.StudentMainScreen;
import com.oatmealprogs.delima.Student.StudentUserDetails;

import java.util.HashMap;

public class TeacherMainScreen extends AppCompatActivity {


    TextView teacherFirstName, teacherLastName, teacherID;

    SessionManagerTeacher sessionManagerTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main_screen);
        teacherFirstName = findViewById(R.id.teacherMainScreen_studentFirstName);
        teacherLastName = findViewById(R.id.teacherMainScreen_studentLastName);
        teacherID = findViewById(R.id.teacherMainScreen_teacherID);

        sessionManagerTeacher = new SessionManagerTeacher(getApplicationContext(), SessionManagerTeacher.SESSION_LOGIN);
        HashMap<String, String> hashMap = sessionManagerTeacher.getLoginSession();

        if (!isConnectedToInternet(getApplicationContext())) {
            Snackbar snackbar = Snackbar.make(getCurrentFocus(),
                    "Перевірте підключення до інтернету", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        teacherFirstName.setText(((hashMap.get(SessionManagerTeacher.KEY_FIRSTNAME))));
        teacherLastName.setText(((hashMap.get(SessionManagerTeacher.KEY_LASTNAME))));
        teacherID.setText("Мій ID: " + hashMap.get(SessionManagerStudent.KEY_USERID));

    }

    public static boolean isConnectedToInternet(@NonNull Context _context) {
        ConnectivityManager cm = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void showUserDetails(View view) {
        Intent i = new Intent(TeacherMainScreen.this, TeacherUserDetails.class);
        startActivity(i);
        finish();

    }

    public void openSubject(View view) {
        Intent i = new Intent(TeacherMainScreen.this, TeacherSubject.class);
        startActivity(i);
        finish();
    }
}
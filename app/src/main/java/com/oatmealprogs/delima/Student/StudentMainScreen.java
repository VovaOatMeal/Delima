package com.oatmealprogs.delima.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oatmealprogs.delima.R;
import com.oatmealprogs.delima.SessionManagerStudent;

import java.util.Calendar;
import java.util.HashMap;

public class StudentMainScreen extends AppCompatActivity {

    TextView studentFirstName, studentLastName, studentClass, studentID;
    String studentIdString;
    SessionManagerStudent sessionManagerStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main_screen);

        studentFirstName = findViewById(R.id.studentMainScreen_studentFirstName);
        studentLastName = findViewById(R.id.studentMainScreen_studentLastName);
        studentClass = findViewById(R.id.studentMainScreen_studentClassName);
        studentID = findViewById(R.id.studentMainScreen_studentID);

        sessionManagerStudent = new SessionManagerStudent(getApplicationContext(), SessionManagerStudent.SESSION_LOGIN);
        HashMap<String, String> hashMap = sessionManagerStudent.getLoginSession();

        if (!isConnectedToInternet(getApplicationContext())) {
            Snackbar snackbar = Snackbar.make(getCurrentFocus(),
                    "Перевірте підключення до інтернету", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        studentFirstName.setText(((hashMap.get(SessionManagerStudent.KEY_FIRSTNAME))));
        studentLastName.setText(((hashMap.get(SessionManagerStudent.KEY_LASTNAME))));
        studentClass.setText(hashMap.get(SessionManagerStudent.KEY_CLASSNAME));
        studentID.setText("Мій ID: " + hashMap.get(SessionManagerStudent.KEY_USERID));

    }

    public static boolean isConnectedToInternet(@NonNull Context _context) {
        ConnectivityManager cm = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void showUserDetails(View view) {
        Intent i = new Intent(StudentMainScreen.this, StudentUserDetails.class);
        startActivity(i);
        finish();
    }

    public void openMyLessons(View view) {
        Intent i = new Intent(StudentMainScreen.this, StudentMyLessons.class);
        i.putExtra("studentID", studentIdString);
        startActivity(i);
        finish();

    }
}
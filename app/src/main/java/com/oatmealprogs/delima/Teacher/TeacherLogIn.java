package com.oatmealprogs.delima.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oatmealprogs.delima.LogInScreen;
import com.oatmealprogs.delima.R;
import com.oatmealprogs.delima.SessionManagerTeacher;
import com.oatmealprogs.delima.Student.StudentMainScreen;

import java.util.Calendar;
import java.util.HashMap;

public class TeacherLogIn extends AppCompatActivity {

    TextInputLayout userIDLayout;
    TextInputLayout passwordIDLayout;
    String userID, password;
    Button signInButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_log_in);

        userIDLayout = findViewById(R.id.teacherLogIn_idField);
        passwordIDLayout = findViewById(R.id.teacherLogIn_passwordField);

        signInButton = findViewById(R.id.teacherLogIn_logInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn(v);
            }
        });

    }

    public void SignIn(View view) {
        if (!isConnectedToInternet(getApplicationContext())) {
            Snackbar snackbar = Snackbar.make(view,
                    "Перевірте підключення до інтернету", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }

        userID = userIDLayout.getEditText().getText().toString();
        password = passwordIDLayout.getEditText().getText().toString();

        String currentYear = "2021";
        // следующая строка берёт текущий год, а в БД только 2021 год есть
        // = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        db.collection("Teachers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        HashMap<String, QueryDocumentSnapshot> hashMap = new HashMap<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                hashMap.put(document.getId(), document);
                            }
                            if (hashMap.containsKey(userID)) {
                                if (hashMap.get(userID).get("Password").toString()
                                        .equals(password)) {

                                    SessionManagerTeacher sessionManagerTeacher = new SessionManagerTeacher(getApplicationContext(), SessionManagerTeacher.SESSION_LOGIN);
                                    sessionManagerTeacher.createLoginSession(userID, password, hashMap.get(userID).get("First Name").toString(),
                                            hashMap.get(userID).get("Last Name").toString());

                                    Intent i = new Intent(TeacherLogIn.this, TeacherMainScreen.class);
                                    startActivity(i);
                                    finish();

                                } else {
                                    Snackbar snackbar = Snackbar.make(view,
                                            "Невірно введено пароль!", Snackbar.LENGTH_SHORT);
                                    snackbar.show();

                                }
                            } else {
                                Snackbar snackbar = Snackbar.make(view,
                                        "Такого користувача не існує!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        } else {
                            Snackbar snackbar = Snackbar.make(view,
                                    "Сталася помилка", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }

                });
    }

    public static boolean isConnectedToInternet(@NonNull Context _context) {
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void goBack(View view) {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SessionManagerTeacher sessionManagerTeacher = new SessionManagerTeacher
                (getApplicationContext(), SessionManagerTeacher.SESSION_LOGIN);

        if (sessionManagerTeacher.isLoggedIn()) {
            Intent i = new Intent(TeacherLogIn.this, TeacherMainScreen.class);
            startActivity(i);
            finish();
        }

    }
}
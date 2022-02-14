package com.oatmealprogs.delima.Student;


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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oatmealprogs.delima.LogInScreen;
import com.oatmealprogs.delima.R;
import com.oatmealprogs.delima.SessionManagerStudent;
import com.oatmealprogs.delima.Teacher.TeacherMainScreen;

import java.util.Calendar;
import java.util.HashMap;

public class StudentLogIn extends AppCompatActivity {

    TextInputLayout userIDLayout;
    TextInputLayout passwordIDLayout;
    String userID, password;
    String classNumber, classLetter;
    String className;
    Button signInButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_log_in);

        userIDLayout = findViewById(R.id.studentLogIn_idField);
        passwordIDLayout = findViewById(R.id.studentLogIn_passwordField);

        Spinner spinnerNumbers = (Spinner) findViewById(R.id.spinnerNumbers);
        Spinner spinnerLetters = (Spinner) findViewById(R.id.spinnerLetters);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.class_numbers, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.class_letters, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerNumbers.setAdapter(adapter1);
        spinnerLetters.setAdapter(adapter2);

        // устанавливаем обработчик нажатия
        spinnerNumbers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // code
                classNumber = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spinnerLetters.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // code
                classLetter = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        signInButton = findViewById(R.id.studentLogIn_logInButton);
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

        className = classNumber + "-" + classLetter;
        String currentYear = "2021";
                // следующая строка берёт текущий год, а в БД только 2021 год есть
                // = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        db.collection("Year")
                .document(currentYear)
                .collection("Classes")
                .document(className)
                .collection("Students")
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

                                    SessionManagerStudent sessionManagerStudent = new SessionManagerStudent(getApplicationContext(), SessionManagerStudent.SESSION_LOGIN);
                                    sessionManagerStudent.createLoginSession(userID, password, hashMap.get(userID).get("First Name").toString(),
                                            hashMap.get(userID).get("Last Name").toString(), className);

                                    Intent i = new Intent(StudentLogIn.this, StudentMainScreen.class);
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
        ConnectivityManager cm = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        SessionManagerStudent sessionManagerStudent = new SessionManagerStudent
                (getApplicationContext(), SessionManagerStudent.SESSION_LOGIN);

        if (sessionManagerStudent.isLoggedIn()) {
            Intent i = new Intent(StudentLogIn.this, StudentMainScreen.class);
            startActivity(i);
            finish();
        }
    }

}
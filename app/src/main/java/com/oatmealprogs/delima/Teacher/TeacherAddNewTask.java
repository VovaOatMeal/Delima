package com.oatmealprogs.delima.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oatmealprogs.delima.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TeacherAddNewTask extends AppCompatActivity {

    Intent parentIntent;
    String className, dateString;
    TextInputLayout description;
    TextView taskDate;
    Calendar date = Calendar.getInstance();
    Map<String, String> task = new HashMap<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_new_task);

        parentIntent = getIntent();
        className = parentIntent.getStringExtra("className");

        description = findViewById(R.id.teacherAddNewTask_descriptionField);
        taskDate = findViewById(R.id.teacherAddNewTask_date);

        dateString = null;
    }

    public void setDate(View v) {
        new DatePickerDialog(TeacherAddNewTask.this, d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setInitialDateTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateString = dateFormat.format(date.getTime());
        taskDate.setText(dateString);
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, monthOfYear);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    public void goBack(View view) {
        Intent intent = new Intent(TeacherAddNewTask.this, TeacherClassHomework.class);
        intent.putExtra("className", className);
        startActivity(intent);
        finish();
    }

    public void addTask(View view) {

        if (dateString == null) {
            Snackbar snackbar = Snackbar.make(view,
                    "Виберіть дату", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }

        task.put("Description", description.getEditText().getText().toString());
        task.put("datetime", dateString);

        String currentYear = "2021";
        // следующая строка берёт текущий год, а в БД только 2021 год есть
        // = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        db.collection("Year")
                .document(currentYear)
                .collection("Classes")
                .document(className)
                .collection("Subjects")
                .document("Алгебра")
                .collection("Homework")
                .document(dateString)
                .set(task)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(TeacherAddNewTask.this, TeacherClassHomework.class);
                        intent.putExtra("className", className);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackbar = Snackbar.make(view,
                                "Виникла помилка. Спробуйте пізніше.", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });


    }

}
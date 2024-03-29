package com.oatmealprogs.delima.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oatmealprogs.delima.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TeacherEditMark extends AppCompatActivity {

    Intent parentIntent;
    String className, dateString, fullName, mark, studentID;
    TextInputLayout description;
    TextView markDate;
    Calendar date = Calendar.getInstance();
    Map<String, String> markMap = new HashMap<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner spinnerMarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_edit_mark);

        spinnerMarks = (Spinner) findViewById(R.id.marksSpinner);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.marks, android.R.layout.simple_spinner_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMarks.setAdapter(adapter1);

        spinnerMarks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // code
                mark = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        parentIntent = getIntent();
        className = parentIntent.getStringExtra("className");
        studentID = parentIntent.getStringExtra("studentID");
        fullName = parentIntent.getStringExtra("fullName");
        mark = parentIntent.getStringExtra("mark");
        dateString = parentIntent.getStringExtra("datetime");

        description = findViewById(R.id.teacherEditMark_descriptionField);
        markDate = findViewById(R.id.teacherEditMark_date);

        description.getEditText().setText(parentIntent.getStringExtra("description"));
        markDate.setText(dateString);
        spinnerMarks.setSelection(Integer.parseInt(mark));

    }

    public void setDate(View v) {
        new DatePickerDialog(TeacherEditMark.this, d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setInitialDateTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateString = dateFormat.format(date.getTime());
        markDate.setText(dateString);
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
        Intent i = new Intent(TeacherEditMark.this, TeacherClassStudentMarks.class);
        i.putExtra("fullName", fullName);
        i.putExtra("studentID", studentID);
        i.putExtra("className", className);
        startActivity(i);
        finish();
    }

    public void editMark(View view) {

        if (dateString == null || spinnerMarks == null || spinnerMarks.getSelectedItem().toString().equals("--")) {
            Snackbar snackbar = Snackbar.make(view,
                    "Виберіть дату та оцінку", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }

        markMap.put("Description", description.getEditText().getText().toString());
        markMap.put("datetime", dateString);
        markMap.put("Mark", spinnerMarks.getSelectedItem().toString());

        String currentYear = "2021";
        // следующая строка берёт текущий год, а в БД только 2021 год есть
        // = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        db.collection("Year")
                .document(currentYear)
                .collection("Classes")
                .document(className)
                .collection("Students")
                .document(studentID)
                .collection("Marks")
                .document(dateString)
                .set(markMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent i = new Intent(TeacherEditMark.this, TeacherClassStudentMarks.class);
                        i.putExtra("className", className);
                        i.putExtra("fullName", fullName);
                        i.putExtra("studentID", studentID);

                        startActivity(i);
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

    public void deleteTask(View view) {
        String currentYear = "2021";
        // следующая строка берёт текущий год, а в БД только 2021 год есть
        // = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        db.collection("Year")
                .document(currentYear)
                .collection("Classes")
                .document(className)
                .collection("Students")
                .document(studentID)
                .collection("Marks")
                .document(dateString)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent i = new Intent(TeacherEditMark.this, TeacherClassStudentMarks.class);
                        i.putExtra("className", className);
                        i.putExtra("fullName", fullName);
                        i.putExtra("studentID", studentID);
                        startActivity(i);
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
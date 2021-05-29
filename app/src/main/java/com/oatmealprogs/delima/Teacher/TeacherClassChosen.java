package com.oatmealprogs.delima.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oatmealprogs.delima.R;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TeacherClassChosen extends AppCompatActivity {

    Intent parentIntent;
    String className;
    TextView title, studentsCount;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    HashMap<String, QueryDocumentSnapshot> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_chosen);

        title = findViewById(R.id.teacherClassChosen_Title);
        studentsCount = findViewById(R.id.teacherClassChosen_studentsCount);

        parentIntent = getIntent();
        className = parentIntent.getStringExtra("className");
        title.setText(className);

        String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        db.collection("Year")
                .document(currentYear)
                .collection("Classes")
                .document(className)
                .collection("Students")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                hashMap.put(document.getId(), document);
                            }

                            studentsCount.setText("Кількість учнів: " + String.valueOf(hashMap.size()));
                        }
                    }
        });

    }

    public void goBack(View view) {
        Intent intent = new Intent(TeacherClassChosen.this, TeacherSubject.class);
        startActivity(intent);
        finish();
    }

    public void chooseHomework(View view) {
        Intent intent = new Intent(TeacherClassChosen.this, TeacherClassHomework.class);
        intent.putExtra("className", className);

        startActivity(intent);
        finish();
    }

    public void chooseMarks(View view) {
        Intent intent = new Intent(TeacherClassChosen.this, TeacherClassMarks.class);
        intent.putExtra("className", className);

        startActivity(intent);
        finish();

    }
}

package com.oatmealprogs.delima.Shared;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.oatmealprogs.delima.R;
import com.oatmealprogs.delima.Teacher.TeacherClassChosen;
import com.oatmealprogs.delima.Teacher.TeacherClassHomework;

public class HomeworkDetails extends AppCompatActivity {

    Intent parentIntent;
    TextView date, description;
    int parentIntentId;
    String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_details);

        parentIntent = getIntent();
        parentIntentId = parentIntent.getIntExtra("intentID", -1);
        className = parentIntent.getStringExtra("className");

        date = findViewById(R.id.homeworkDetails_date);
        description = findViewById(R.id.homeworkDetails_description);

        date.setText("Дата: " + parentIntent.getStringExtra("datetime"));
        description.setText("Завдання: " + parentIntent.getStringExtra("description"));
    }

    public void goBack(View view) {
        switch (parentIntentId) {
            case -1:
                finish();
                break;
            case 0:
                Intent intent = new Intent(HomeworkDetails.this, TeacherClassHomework.class);
                intent.putExtra("className", className);

                startActivity(intent);
                finish();
                break;
            case 1:
                // for student
                break;
        }
        finish();
    }
}
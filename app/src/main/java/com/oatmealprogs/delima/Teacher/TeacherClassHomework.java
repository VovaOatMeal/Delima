package com.oatmealprogs.delima.Teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.oatmealprogs.delima.R;
import com.oatmealprogs.delima.Shared.HomeworkDetails;
import com.oatmealprogs.delima.Shared.HomeworkTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherClassHomework extends AppCompatActivity {

    Intent parentIntent;
    String className;
    HashMap<String, QueryDocumentSnapshot> hashMap = new HashMap<>();
    LinearLayout linearLayout;
    ArrayList<QueryDocumentSnapshot> snapshots = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
/*
    private FirestoreRecyclerAdapter<HomeworkTask, ViewHolder> adapter;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_homework);

        parentIntent = getIntent();
        className = parentIntent.getStringExtra("className");

        linearLayout = findViewById(R.id.teacherClassHomework_LinearLayout);

        String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));


/*
        // Here starts the procedure of filling the RecyclerView that is the same for both Teacher and Student
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);

        Query query = db.collection("Year")
                .document(currentYear)
                .collection("Classes")
                .document(className)
                .collection("Subjects")
                .document("Алгебра")
                .collection("Homework")
                .orderBy("datetime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<HomeworkTask> options = new FirestoreRecyclerOptions.Builder<HomeworkTask>()
                .setQuery(query, HomeworkTask.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<HomeworkTask, ViewHolder> (options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder productViewHolder, int position, @NonNull HomeworkTask productModel) {
                // write what it should do
                productViewHolder.dateLabelView.setText(productModel.getDatetime());
                productViewHolder.homeworkDescriptionView.setText(productModel.getDescription());
                productViewHolder.detailsButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), HomeworkDetails.class);
                        intent.putExtra("date", productModel.getDatetime());
                        intent.putExtra("description", productModel.getDescription());
                        v.getContext().startActivity(intent);
                        ((Activity) v.getContext()).finish();
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homework_item, parent, false);
                return new ViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
*/

        db.collection("Year")
                .document(currentYear)
                .collection("Classes")
                .document(className)
                .collection("Subjects")
                .document("Алгебра")
                .collection("Homework")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                snapshots.add(document);
                            }

                            String dateTime, description;
                            for (QueryDocumentSnapshot entry : snapshots) {
                                dateTime = String.valueOf(entry.get("datetime"));
                                description = String.valueOf(entry.get("Description"));
                                addTaskToList(dateTime, description);
                            }


                        } else {
                            return;
                        }
                    }
                });


    }

    public void goBack(View view) {
        Intent intent = new Intent(TeacherClassHomework.this, TeacherClassChosen.class);
        intent.putExtra("className", className);
        startActivity(intent);
        finish();
    }

    public void addTaskToList(String dateTime, String description) {

        View view = LayoutInflater.from(this).inflate(R.layout.homework_item_teacher, null); // which view to add
        TextView dateText = view.findViewById(R.id.dateLabel);
        TextView descriptionText = view.findViewById(R.id.homeworkDescription);
        ImageButton buttonOpen = view.findViewById(R.id.detailsButton);
        ImageButton buttonEdit = view.findViewById(R.id.editButton);

        dateText.setText(dateTime);
        descriptionText.setText(description);
        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherClassHomework.this, HomeworkDetails.class);
                i.putExtra("datetime", dateTime);
                i.putExtra("description", description);
                i.putExtra("className", className);
                i.putExtra("intentID", 0);
                startActivity(i);
                finish();
            }
        });
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherClassHomework.this, TeacherEditTask.class);
                i.putExtra("datetime", dateTime);
                i.putExtra("description", description);
                i.putExtra("className", className);
                startActivity(i);
                finish();

            }
        });
        linearLayout.addView(view);
    }

    public void addNewTask(View view) {
        Intent i = new Intent(TeacherClassHomework.this, TeacherAddNewTask.class);
        i.putExtra("className", className);
        startActivity(i);
        finish();

    }

/*
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView dateLabelView, homeworkDescriptionView;
        Button detailsButtonView;
        ViewHolder(View view){
            super(view);
            dateLabelView = (TextView) view.findViewById(R.id.dateLabel);
            homeworkDescriptionView = (TextView) view.findViewById(R.id.homeworkDescription);
            detailsButtonView = (Button) view.findViewById(R.id.detailsButton);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }
*/
}
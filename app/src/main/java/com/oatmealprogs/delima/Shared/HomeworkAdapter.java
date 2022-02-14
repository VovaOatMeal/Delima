package com.oatmealprogs.delima.Shared;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.oatmealprogs.delima.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<HomeworkTask> tasks;
    FirestoreRecyclerOptions<HomeworkTask> options;

    public HomeworkAdapter(Context context, HashMap<String, QueryDocumentSnapshot> hashMap, String className) {
        this.tasks = new ArrayList<>();

        String currentYear = "2021";
        // следующая строка берёт текущий год, а в БД только 2021 год есть
        // = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("Year")
                .document(currentYear)
                .collection("Classes")
                .document(className)
                .collection("Subjects")
                .document("Алгебра")
                .collection("Homework")
                .orderBy("productName", Query.Direction.ASCENDING);

        options = new FirestoreRecyclerOptions.Builder<HomeworkTask>()
                .setQuery(query, HomeworkTask.class)
                .build();

        for (Map.Entry<String, QueryDocumentSnapshot> entry : hashMap.entrySet()) {
            tasks.add(new HomeworkTask(entry.getKey(), entry.getValue().get("Description").toString(), entry.getValue().get("datetime").toString()));
        }
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public HomeworkAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.homework_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeworkAdapter.ViewHolder holder, int position) {
        HomeworkTask homeworkTask = tasks.get(position);
        holder.dateLabelView.setText(homeworkTask.getTaskID());
        holder.homeworkDescriptionView.setText(homeworkTask.getDescription());
        holder.detailsButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HomeworkDetails.class);
                intent.putExtra("date", homeworkTask.getTaskID());
                intent.putExtra("description", homeworkTask.getDescription());
                v.getContext().startActivity(intent);
                ((Activity) v.getContext()).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

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
}

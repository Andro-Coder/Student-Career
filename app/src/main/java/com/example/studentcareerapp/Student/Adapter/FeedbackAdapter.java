package com.example.studentcareerapp.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentcareerapp.Domains.FacultyFeedbackDomain;
import com.example.studentcareerapp.Domains.StudentNotificationDomain;
import com.example.studentcareerapp.R;

import java.util.ArrayList;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder>{


    ArrayList<FacultyFeedbackDomain> details;
    Context context;

    public FeedbackAdapter(Context context,ArrayList<FacultyFeedbackDomain> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_faculty_feedback, parent,false);
        return new FeedbackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.ViewHolder holder, int position) {

        final FacultyFeedbackDomain facultyFeedbackDomain = details.get(position);


        holder.emailId.setText(facultyFeedbackDomain.getEmail());
        holder.semester.setText(facultyFeedbackDomain.getSemester());
        holder.msgFeedback.setText(facultyFeedbackDomain.getFeedback());

    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView emailId,semester,msgFeedback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            emailId = itemView.findViewById(R.id.emailFTV);
            semester = itemView.findViewById(R.id.semFTV);
            msgFeedback = itemView.findViewById(R.id.feedbackDescriptionTV);

        }
    }
}

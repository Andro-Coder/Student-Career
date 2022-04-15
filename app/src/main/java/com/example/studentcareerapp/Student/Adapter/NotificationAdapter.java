package com.example.studentcareerapp.Student.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.studentcareerapp.Domains.StudentNotificationDomain;
import com.example.studentcareerapp.R;


import java.util.ArrayList;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    ArrayList<StudentNotificationDomain> details;
    Context context;

    public NotificationAdapter(Context context,ArrayList<StudentNotificationDomain> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_student_genral_notification, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final StudentNotificationDomain studentNotificationDomain = details.get(position);


        holder.emailId.setText(studentNotificationDomain.getEmail());
        holder.semester.setText(studentNotificationDomain.getSemester());
        holder.msgTitle.setText(studentNotificationDomain.getMsgTitle());
        holder.msgDesc.setText(studentNotificationDomain.getMessage());

    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView emailId,semester,msgTitle,msgDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            emailId = itemView.findViewById(R.id.emailTV);
            semester = itemView.findViewById(R.id.semTV);
            msgTitle = itemView.findViewById(R.id.msgTitleTV);
            msgDesc = itemView.findViewById(R.id.msgDescriptionTV);

        }
    }
}

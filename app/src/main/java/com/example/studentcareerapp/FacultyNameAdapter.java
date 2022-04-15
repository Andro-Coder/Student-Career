package com.example.studentcareerapp;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentcareerapp.ArraylistHelper.DepartmentHelper;
import com.example.studentcareerapp.ArraylistHelper.FacultyCheckboxHelper;
import com.example.studentcareerapp.Domains.DepartmentDomain;
import com.example.studentcareerapp.Domains.FacultyDomain;
import com.example.studentcareerapp.Domains.StudentNotificationDomain;
import com.example.studentcareerapp.Faculty.Adapter.SelectedDetailsAdapter;

import java.util.ArrayList;

public class FacultyNameAdapter extends RecyclerView.Adapter<FacultyNameAdapter.ViewHolder>{

    ArrayList<String> list;

    FacultyCheckboxHelper facultyCheckboxHelper;

    Context context;

    public FacultyNameAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        facultyCheckboxHelper = new FacultyCheckboxHelper(context);
    }

    @NonNull
    @Override
    public FacultyNameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_faculty_name_checkbox,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyNameAdapter.ViewHolder holder, int position) {

        String facultyName = list.get(position);


        holder.checkBox.setText(facultyName);


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkBox.isChecked()){
                    if(!facultyCheckboxHelper.fnCheckItemsExist(holder.checkBox.getText().toString().trim())){
                        facultyCheckboxHelper.fnInsertItems(holder.checkBox.getText().toString().trim());
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.facultyNameCB);

        }
    }
}

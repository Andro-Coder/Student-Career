package com.example.studentcareerapp.Faculty.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentcareerapp.ArraylistHelper.DepartmentHelper;
import com.example.studentcareerapp.ArraylistHelper.DivisionHelper;
import com.example.studentcareerapp.ArraylistHelper.FacultyCheckboxHelper;
import com.example.studentcareerapp.ArraylistHelper.SemesterHelper;
import com.example.studentcareerapp.ArraylistHelper.SubjectHelper;
import com.example.studentcareerapp.Domains.DepartmentDomain;
import com.example.studentcareerapp.Domains.DivisionDomain;
import com.example.studentcareerapp.Domains.FacultyDomain;
import com.example.studentcareerapp.Domains.SemesterDomain;
import com.example.studentcareerapp.Domains.SubjectDomain;
import com.example.studentcareerapp.R;

import java.util.ArrayList;

public class SelectedDetailsAdapter extends RecyclerView.Adapter<SelectedDetailsAdapter.ViewHolder> {

    // dept = 1;
    // sem = 2;

    ArrayList<DepartmentDomain> deptList;
    ArrayList<SemesterDomain> semList;
    ArrayList<DivisionDomain> divList;
    ArrayList<SubjectDomain> subList;
    ArrayList<FacultyDomain> facultyList;

    DepartmentHelper departmentHelper;
    SemesterHelper semesterHelper;
    DivisionHelper divisionHelper;
    SubjectHelper subjectHelper;
    FacultyCheckboxHelper facultyCheckboxHelper;

    int type;

    Context context;



    public SelectedDetailsAdapter(Context context, ArrayList<DepartmentDomain> list,int type) {

        this.context = context;
        this.deptList = list;
        this.type = type;

    }

    public SelectedDetailsAdapter(ArrayList<SemesterDomain> list, Context context, int type) {

        this.semList = list;
        this.context = context;
        this.type = type;

    }

    public SelectedDetailsAdapter(ArrayList<DivisionDomain> list, int type,Context context) {

        this.divList = list;
        this.type = type;
        this.context = context;

    }

    public SelectedDetailsAdapter(int type,Context context,ArrayList<SubjectDomain> list) {

        this.type = type;
        this.context = context;
        this.subList = list;

    }

    public SelectedDetailsAdapter(int type,ArrayList<FacultyDomain> list,Context context) {

        this.type = type;
        this.context = context;
        this.facultyList = list;

    }

     @NonNull
     @Override
     public SelectedDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(context).inflate(R.layout.viewholder_selected_details, parent,false);
         return new ViewHolder(view);
     }

     @Override
     public void onBindViewHolder(@NonNull SelectedDetailsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        if(type == 1){

            final DepartmentDomain departmentDomain = deptList.get(position);

            holder.text.setText(departmentDomain.getItem());

            holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    deptList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,deptList.size());
                }
            });
        }

         else if(type == 2){

             final SemesterDomain semesterDomain = semList.get(position);

             holder.text.setText(semesterDomain.getItem());

             holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v)
                 {
                     semList.remove(position);
                     notifyItemRemoved(position);
                     notifyItemRangeChanged(position,semList.size());
                 }
             });
         }

        else if(type == 3){

            final DivisionDomain divisionDomain = divList.get(position);

            holder.text.setText(divisionDomain.getItem());

            holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    divList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,divList.size());
                }
            });
        }

        else if(type == 4){

            final SubjectDomain subjectDomain = subList.get(position);

            holder.text.setText(subjectDomain.getItem());

            holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    subList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,subList.size());
                }
            });
        }

        else if(type == 5){

            final FacultyDomain facultyDomain = facultyList.get(position);

            holder.text.setText(facultyDomain.getItem());

            holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    facultyList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,facultyList.size());
                }
            });
        }

     }

     @Override
     public int getItemCount() {

        if (type == 1) {
            return deptList.size();
        }

         else if (type == 2) {
             return semList.size();
         }

         else if (type == 3) {
             return divList.size();
         }

        else if (type == 5) {
            return facultyList.size();
        }

        else {
            return subList.size();
        }
     }

     public class ViewHolder extends RecyclerView.ViewHolder {

         ImageView cancelBtn;
         TextView text;

         public ViewHolder(@NonNull View itemView) {
             super(itemView);

             text = itemView.findViewById(R.id.infoTV);
             cancelBtn = itemView.findViewById(R.id.cancelButton);

         }
     }
 }

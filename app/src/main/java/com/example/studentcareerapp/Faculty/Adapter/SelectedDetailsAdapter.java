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

import com.example.studentcareerapp.R;

import java.util.ArrayList;

public class SelectedDetailsAdapter extends RecyclerView.Adapter<SelectedDetailsAdapter.ViewHolder> {

    // dept = 1;
    // sem = 2;

    ArrayList<String> deptList;
    ArrayList<String> semList;


    ArrayList<String> list;

    Context context;



    public SelectedDetailsAdapter(ArrayList<String> list, Context context, int type) {

        if(type == 1){
            this.deptList = list;
            this.list = deptList;
        }
        else  if(type == 2){
            this.semList = list;
            this.list = semList;
        }

        this.context = context;

    }




     @NonNull
     @Override
     public SelectedDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(context).inflate(R.layout.viewholder_selected_details, parent,false);
         return new ViewHolder(view);
     }

     @Override
     public void onBindViewHolder(@NonNull SelectedDetailsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


         holder.text.setText(list.get(position));

         holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v)
             {
                 list.remove(position);
                 notifyItemRemoved(position);
                 notifyItemRangeChanged(position,list.size());
             }
         });

     }

     @Override
     public int getItemCount() {
         return list.size();
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

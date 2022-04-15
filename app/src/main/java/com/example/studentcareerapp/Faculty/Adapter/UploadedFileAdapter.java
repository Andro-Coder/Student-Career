package com.example.studentcareerapp.Faculty.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentcareerapp.Domains.putPDF;
import com.example.studentcareerapp.Faculty.Activity.PDFActivity;
import com.example.studentcareerapp.R;

import java.util.ArrayList;


public class UploadedFileAdapter extends RecyclerView.Adapter<UploadedFileAdapter.ViewHolder> {

    Context context;
    ArrayList<putPDF> list;

    public UploadedFileAdapter(Context context,ArrayList<putPDF> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UploadedFileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_uploaded_file,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadedFileAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final putPDF putPDF = list.get(position);


        holder.pdfName.setText(putPDF.getName());

        holder.viewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), PDFActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("pdfUrl",putPDF.getUrl());
                view.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView pdfName,viewFile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfName = itemView.findViewById(R.id.fileNameTV);
            viewFile = itemView.findViewById(R.id.viewFileTV);
        }
    }

}


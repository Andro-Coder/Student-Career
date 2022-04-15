package com.example.studentcareerapp.Student.Adapter;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentcareerapp.Domains.putPDF;
import com.example.studentcareerapp.R;

import java.util.ArrayList;

public class getNotesAdapter extends RecyclerView.Adapter<getNotesAdapter.ViewHolder>{

    Context context;
    ArrayList<putPDF> list;


    public getNotesAdapter(Context context, ArrayList<putPDF> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public getNotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_get_notes,parent,false);
        return new getNotesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull getNotesAdapter.ViewHolder holder, int position) {


        final putPDF putPDF = list.get(position);


        holder.pdfName.setText(putPDF.getName());

        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               downloadFile(view.getContext(), putPDF.getName(),".pdf",DIRECTORY_DOWNLOADS,putPDF.getUrl());

            }
        });

    }

    public void downloadFile(Context context,String name, String fileExtension, String destinationDirectory,String url){

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, name + fileExtension);

        downloadManager.enqueue(request);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView pdfName;
        ImageView downloadBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pdfName = itemView.findViewById(R.id.fileNameTV);
            downloadBtn = itemView.findViewById(R.id.downloadImg);

        }

    }
}

package com.example.studentcareerapp.Faculty.Activity;

import static com.google.android.gms.tasks.Tasks.await;

import android.app.Dialog;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.studentcareerapp.Faculty.Adapter.SelectedDetailsAdapter;
import com.example.studentcareerapp.R;

import java.util.ArrayList;

public class FGenralNotificationFragment extends Fragment {


    private ImageView selectDept,selectSem;
    private RecyclerView deptRecyclerView,semRecyclerView;

    ArrayList<String> selectedDepartment = new ArrayList<String>();
    ArrayList<String> selectedSemester = new ArrayList<String>();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FGenralNotificationFragment() {
        // Required empty public constructor
    }


    public static FGenralNotificationFragment newInstance(String param1, String param2) {
        FGenralNotificationFragment fragment = new FGenralNotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_f_genral_notification, container, false);

        selectDept = view.findViewById(R.id.deptSelectImg);
        selectSem = view.findViewById(R.id.semSelectImg);

        deptRecyclerView = view.findViewById(R.id.deptRV);
        semRecyclerView = view.findViewById(R.id.semRV);

        selectDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    showDepartmentDialog();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        Log.d("list", selectedDepartment.toString());



        selectSem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    showSemesterDialog();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

     private void showDepartmentDialog(){

        try{
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.bottom_sheet_department);

            ImageView checkButton = dialog.findViewById(R.id.checkImgButton);

            CheckBox computerDept = (CheckBox) dialog.findViewById(R.id.ceCB);
            CheckBox itDept = (CheckBox) dialog.findViewById(R.id.itCB);
            CheckBox civilDept = (CheckBox) dialog.findViewById(R.id.clCB);
            CheckBox mechanicalDept = (CheckBox) dialog.findViewById(R.id.meCB);


            checkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    addDeptData(computerDept);
                    addDeptData(itDept);
                    addDeptData(civilDept);
                    addDeptData(mechanicalDept);


                    if(selectedDepartment.isEmpty()){
                        deptRecyclerView.setVisibility(View.GONE);
                    }
                    else {
                        deptRecyclerView.setVisibility(View.VISIBLE);
                    }

                    SelectedDetailsAdapter adapter = new SelectedDetailsAdapter(selectedDepartment, getContext(),1);
                    deptRecyclerView.setAdapter(adapter);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    deptRecyclerView.setLayoutManager(layoutManager);

                    dialog.dismiss();
                }

            });

            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.dialog_background));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

     private void addDeptData(CheckBox name){

        if(name.isChecked()){

            if(!selectedDepartment.contains(name.getText().toString())){
                selectedDepartment.add(name.getText().toString());
            }
        }
        else {
            if(selectedDepartment.contains(name.getText().toString())){
                selectedDepartment.remove(name.getText().toString());
            }
        }
    }

    private void showSemesterDialog(){

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_semester);

        ImageView checkButton = dialog.findViewById(R.id.checkImgButton);

        CheckBox sem1 = dialog.findViewById(R.id.sem1CB);
        CheckBox sem2 = dialog.findViewById(R.id.sem2CB);
        CheckBox sem3 = dialog.findViewById(R.id.sem3CB);
        CheckBox sem4 = dialog.findViewById(R.id.sem4CB);
        CheckBox sem5 = dialog.findViewById(R.id.sem5CB);
        CheckBox sem6 = dialog.findViewById(R.id.sem6CB);
        CheckBox sem7 = dialog.findViewById(R.id.sem7CB);
        CheckBox sem8 = dialog.findViewById(R.id.sem8CB);



        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addSemData(sem1);
                addSemData(sem2);
                addSemData(sem3);
                addSemData(sem4);
                addSemData(sem5);
                addSemData(sem6);
                addSemData(sem7);
                addSemData(sem8);


                if(selectedSemester.isEmpty()){
                    semRecyclerView.setVisibility(View.GONE);
                }
                else {
                    semRecyclerView.setVisibility(View.VISIBLE);
                }

                SelectedDetailsAdapter adapter = new SelectedDetailsAdapter(selectedSemester, getContext(),1);
                semRecyclerView.setAdapter(adapter);


                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                semRecyclerView.setLayoutManager(layoutManager);

                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.dialog_background));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void addSemData(CheckBox name){

        if(name.isChecked()){

            if(!selectedSemester.contains(name.getText().toString())){
                selectedSemester.add(name.getText().toString());
            }
        }
        else {
            if(selectedSemester.contains(name.getText().toString())){
                selectedSemester.remove(name.getText().toString());
            }
        }
    }

}
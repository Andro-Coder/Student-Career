package com.example.studentcareerapp.Student.Activity;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.studentcareerapp.ArraylistHelper.DepartmentHelper;
import com.example.studentcareerapp.ArraylistHelper.FacultyCheckboxHelper;
import com.example.studentcareerapp.ArraylistHelper.FacultyHelper;
import com.example.studentcareerapp.ArraylistHelper.SemesterHelper;
import com.example.studentcareerapp.Domains.DepartmentDomain;
import com.example.studentcareerapp.Domains.FacultyDomain;
import com.example.studentcareerapp.Domains.SemesterDomain;
import com.example.studentcareerapp.Domains.StudentNotificationDomain;
import com.example.studentcareerapp.Faculty.Adapter.SelectedDetailsAdapter;
import com.example.studentcareerapp.FacultyNameAdapter;
import com.example.studentcareerapp.R;
import com.example.studentcareerapp.Student.Adapter.NotificationAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SFeedbackFragment extends Fragment {

    private ImageView selectDept,selectFaculty;
    private EditText feedbackMessage;
    private Button sendBtn;
    private RecyclerView deptRecyclerView,facultyRecyclerView;

    ArrayList<DepartmentDomain> selectedDepartment = new ArrayList<DepartmentDomain>();
    ArrayList<FacultyDomain> selectedFaculty = new ArrayList<FacultyDomain>();
    ArrayList<String> facultyNames = new ArrayList<String>();

    DepartmentHelper departmentHelper;
    FacultyHelper facultyHelper;
    FacultyCheckboxHelper facultyCheckboxHelper;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    DocumentReference documentUsers;
    String userID,email,sem;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SFeedbackFragment() {
        // Required empty public constructor
    }

    public static SFeedbackFragment newInstance(String param1, String param2) {
        SFeedbackFragment fragment = new SFeedbackFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_s_feedback, container, false);


        selectDept = view.findViewById(R.id.deptSelectImgS);
        selectFaculty = view.findViewById(R.id.facultySelectImgS);
        feedbackMessage = view.findViewById(R.id.feedbackTXT);
        sendBtn = view.findViewById(R.id.feedbackBtn);

        deptRecyclerView = view.findViewById(R.id.deptSRV);
        facultyRecyclerView = view.findViewById(R.id.facultyRV);

        departmentHelper = new DepartmentHelper(getContext());
        facultyHelper = new FacultyHelper(getContext());
        facultyCheckboxHelper = new FacultyCheckboxHelper(getContext());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        email = firebaseAuth.getCurrentUser().getEmail();

        documentUsers = firebaseFirestore.collection("users").document(userID);
        documentUsers.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            sem = documentSnapshot.getString("Semester").trim();

                        } else {
                            Toast.makeText(getContext(), "Document Not Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                        Log.d("Error!",e.toString());
                    }
                });


        CollectionReference colRef = firebaseFirestore.collection("users");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        //Log.d("task","partnerID" + document.getString("partner"));
                        //Log.d("task",document.getString("title"));

                        if((document.getString("Role").trim()).equals("Faculty")){

                            if(!facultyHelper.fnCheckItemsExist(document.getString("Name").trim())){
                                facultyHelper.fnInsertItems(document.getString("Name").trim());
                            }
                        }

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error: Retrieval Faculty Name", Toast.LENGTH_SHORT).show();
            }
        });


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

        selectFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    showFacultyDialog();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String feedbackMsg = feedbackMessage.getText().toString().trim();

                sendMessage(feedbackMsg);
//
//                departmentHelper.fnDeleteAll();
//                semesterHelper.fnDeleteAll();

                deptRecyclerView.setVisibility(View.GONE);
                facultyRecyclerView.setVisibility(View.GONE);

                feedbackMessage.getText().clear();

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

                    departmentHelper.fnDeleteAll();

                    addDeptData(computerDept);
                    addDeptData(itDept);
                    addDeptData(civilDept);
                    addDeptData(mechanicalDept);

                    ArrayList<DepartmentDomain> list = departmentHelper.fnGetItems();

                    if(list.isEmpty()){
                        deptRecyclerView.setVisibility(View.GONE);
                    }
                    else {
                        deptRecyclerView.setVisibility(View.VISIBLE);
                    }


                    SelectedDetailsAdapter adapter = new SelectedDetailsAdapter(getContext(),list,1);
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

    private void addDeptData(@NonNull CheckBox name){

        if(name.isChecked()){

            if(!departmentHelper.fnCheckItemsExist(name.getText().toString())){
                departmentHelper.fnInsertItems(name.getText().toString().trim());
            }
        }
//        else {
//            if(departmentHelper.fnCheckItemsExist(name.getText().toString())){
//                departmentHelper.fnRemoveItem(name.getText().toString());
//            }
//        }
    }


    private void showFacultyDialog(){

        try{
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.bottom_sheet_faculty);

            facultyCheckboxHelper.fnDeleteAll();

            ImageView checkButton = dialog.findViewById(R.id.checkImgButton);

            RecyclerView nameRV = dialog.findViewById(R.id.facultyNameRV);

            facultyNames = facultyHelper.fnGetItems();

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            nameRV.setLayoutManager(layoutManager);

            FacultyNameAdapter adapter = new FacultyNameAdapter(getContext(),facultyNames);
            nameRV.setAdapter(adapter);


            checkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ArrayList<FacultyDomain> list = facultyCheckboxHelper.fnGetItems();

                    Log.d("list",list.toString());

                    if(list.isEmpty()){
                        facultyRecyclerView.setVisibility(View.GONE);
                    }
                    else {
                        facultyRecyclerView.setVisibility(View.VISIBLE);
                    }


                    SelectedDetailsAdapter adapter = new SelectedDetailsAdapter(5,list,getContext());
                    facultyRecyclerView.setAdapter(adapter);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    facultyRecyclerView.setLayoutManager(layoutManager);

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

    private void sendMessage(String feedbackMsg){

        selectedDepartment = departmentHelper.fnGetItems();
        selectedFaculty = facultyCheckboxHelper.fnGetItems();


        for(int j=0; j<selectedDepartment.size(); j++){

            for(int k=0; k<selectedFaculty.size();k++){

                DepartmentDomain departmentDomain = selectedDepartment.get(j);
                FacultyDomain facultyDomain = selectedFaculty.get(k);
                DocumentReference documentReference2 = firebaseFirestore.collection("Feedback").document(departmentDomain.getItem());


                Map<String,Object> message = new HashMap<>();
                message.put("Email",email);
                message.put("Dept",departmentDomain.getItem());
                message.put("Semester",sem);
                message.put("Faculty Name",facultyDomain.getItem());
                message.put("Feedback Message",feedbackMsg);


                String id = UUID.randomUUID().toString();

                documentReference2.collection(facultyDomain.getItem()).document(id)
                        .set(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getContext(), "Feedback Sent Successfully", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG","onFail: " + e.toString());
                        Toast.makeText(getContext(), "Error: Feedback not sent", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    }


}
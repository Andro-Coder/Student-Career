package com.example.studentcareerapp.Faculty.Activity;

import static com.google.android.gms.tasks.Tasks.await;

import android.app.Dialog;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.studentcareerapp.ArraylistHelper.SemesterHelper;
import com.example.studentcareerapp.Domains.DepartmentDomain;
import com.example.studentcareerapp.Domains.SemesterDomain;
import com.example.studentcareerapp.Domains.StudentNotificationDomain;
import com.example.studentcareerapp.Faculty.Adapter.SelectedDetailsAdapter;
import com.example.studentcareerapp.R;
import com.example.studentcareerapp.Student.Adapter.NotificationAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class FGenralNotificationFragment extends Fragment {


    private ImageView selectDept,selectSem;
    private RecyclerView deptRecyclerView,semRecyclerView;
    private EditText messageTitle,messageDescription;
    private Button sendBtn;


    ArrayList<DepartmentDomain> selectedDepartment = new ArrayList<DepartmentDomain>();
    ArrayList<SemesterDomain> selectedSemester = new ArrayList<SemesterDomain>();

    private DepartmentHelper departmentHelper;
    private SemesterHelper semesterHelper;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;
    String userID,email;

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
        messageTitle = view.findViewById(R.id.titleTXT);
        messageDescription = view.findViewById(R.id.msgTXT);
        sendBtn = view.findViewById(R.id.sendBtn);

        deptRecyclerView = view.findViewById(R.id.deptRV);
        semRecyclerView = view.findViewById(R.id.semRV);

        departmentHelper = new DepartmentHelper(getContext());
        semesterHelper = new SemesterHelper(getContext());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        email = firebaseAuth.getCurrentUser().getEmail();


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


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msgTitle = messageTitle.getText().toString().trim();
                String msgDescription = messageDescription.getText().toString().trim();

                sendMessage(msgTitle,msgDescription);
//
//                departmentHelper.fnDeleteAll();
//                semesterHelper.fnDeleteAll();

                deptRecyclerView.setVisibility(View.GONE);
                semRecyclerView.setVisibility(View.GONE);

                messageTitle.getText().clear();
                messageDescription.getText().clear();

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

                semesterHelper.fnDeleteAll();

                addSemData(sem1);
                addSemData(sem2);
                addSemData(sem3);
                addSemData(sem4);
                addSemData(sem5);
                addSemData(sem6);
                addSemData(sem7);
                addSemData(sem8);


                ArrayList<SemesterDomain> list = semesterHelper.fnGetItems();

                if(list.isEmpty()){
                    semRecyclerView.setVisibility(View.GONE);
                }
                else {
                    semRecyclerView.setVisibility(View.VISIBLE);
                }

                SelectedDetailsAdapter adapter = new SelectedDetailsAdapter(list, getContext(),2);
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

    private void addSemData(@NonNull CheckBox name) {

        if (name.isChecked()) {

            if (!semesterHelper.fnCheckItemsExist(name.getText().toString())) {
                semesterHelper.fnInsertItems(name.getText().toString());
            }
        }
//        else {
//            if(semesterHelper.fnCheckItemsExist(name.getText().toString())){
//                semesterHelper.fnRemoveItem(name.getText().toString());
//            }
//        }
    }


    private void sendMessage(String msgTitle,String msgDescription){

        selectedDepartment = departmentHelper.fnGetItems();
        selectedSemester = semesterHelper.fnGetItems();


        for(int j=0; j<selectedDepartment.size(); j++){

            for(int k=0; k<selectedSemester.size();k++){

                DepartmentDomain departmentDomain = selectedDepartment.get(j);
                SemesterDomain semesterDomain = selectedSemester.get(k);
                DocumentReference documentReference2 = firebaseFirestore.collection("GeneralNotification").document(departmentDomain.getItem());


                Map<String,Object> message = new HashMap<>();
                message.put("Email",email);
                message.put("Dept",departmentDomain.getItem());
                message.put("Semester",semesterDomain.getItem());
                message.put("title",msgTitle);
                message.put("Message",msgDescription);


                String id = UUID.randomUUID().toString();

                documentReference2.collection(semesterDomain.getItem()).document(id)
                        .set(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getContext(), "Message Sent Successfully", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG","onFail: " + e.toString());
                        Toast.makeText(getContext(), "Error: Message not sent", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    }
}
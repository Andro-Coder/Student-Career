package com.example.studentcareerapp.Faculty.Activity;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentcareerapp.ArraylistHelper.DepartmentHelper;
import com.example.studentcareerapp.ArraylistHelper.DivisionHelper;
import com.example.studentcareerapp.ArraylistHelper.SemesterHelper;
import com.example.studentcareerapp.ArraylistHelper.SubjectHelper;
import com.example.studentcareerapp.Domains.DepartmentDomain;
import com.example.studentcareerapp.Domains.DivisionDomain;
import com.example.studentcareerapp.Domains.SemesterDomain;
import com.example.studentcareerapp.Domains.SubjectDomain;
import com.example.studentcareerapp.Domains.putPDF;
import com.example.studentcareerapp.Faculty.Adapter.SelectedDetailsAdapter;
import com.example.studentcareerapp.Faculty.Adapter.UploadedFileAdapter;
import com.example.studentcareerapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FNotesFragment extends Fragment {

    private TextView chooseFile,text;
    private ImageView selectDepartment,selectSemester,selectDivision,selectSubject;
    private RecyclerView deptRV,semRV,divRV,subRV,uploadedFileRV;

    private DepartmentHelper departmentHelper;
    private SemesterHelper semesterHelper;
    private DivisionHelper divisionHelper;
    private SubjectHelper subjectHelper;

    ArrayList<DepartmentDomain> selectedDept = new ArrayList<DepartmentDomain>();
    ArrayList<SemesterDomain> selectedSem = new ArrayList<SemesterDomain>();
    ArrayList<DivisionDomain> selectedDiv = new ArrayList<DivisionDomain>();
    ArrayList<SubjectDomain> selectedSub = new ArrayList<SubjectDomain>();
    ArrayList<putPDF> pdfDetails = new ArrayList<putPDF>();

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID,email,pdfName,name;
    DocumentReference documentUsers;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FNotesFragment() {
        // Required empty public constructor
    }


    public static FNotesFragment newInstance(String param1, String param2) {
        FNotesFragment fragment = new FNotesFragment();
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

        View view =  inflater.inflate(R.layout.fragment_f_notes, container, false);


        selectDepartment = view.findViewById(R.id.deptSelectFNImg);
        selectSemester = view.findViewById(R.id.semSelectFNImg);
        selectDivision = view.findViewById(R.id.divSelectFNImg);
        selectSubject = view.findViewById(R.id.subSelectFNImg);
        text = view.findViewById(R.id.Text);

        chooseFile = view.findViewById(R.id.chooseFileTV);

        deptRV = view.findViewById(R.id.deptFNRV);
        semRV = view.findViewById(R.id.semFNRV);
        divRV = view.findViewById(R.id.divFNRV);
        subRV = view.findViewById(R.id.subFNRV);
        uploadedFileRV = view.findViewById(R.id.uploadedFileRV);

        departmentHelper = new DepartmentHelper(getContext());
        semesterHelper = new SemesterHelper(getContext());
        divisionHelper = new DivisionHelper(getContext());
        subjectHelper = new SubjectHelper(getContext());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        email = firebaseAuth.getCurrentUser().getEmail();


        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploadPDF");

        documentUsers = firebaseFirestore.collection("users").document(userID);
        documentUsers.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            name = documentSnapshot.getString("Name").trim();

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


        selectDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    showDeptDialog();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


        selectSemester.setOnClickListener(new View.OnClickListener() {
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

        selectDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    showDivDialog();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        selectSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    showSubDialog();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectPDF();

            }
        });

        return view;
    }

    private void showDeptDialog(){

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

                    ArrayList<DepartmentDomain> deptList = departmentHelper.fnGetItems();

                    Log.d("list",deptList.toString());

                    if(deptList.isEmpty()){
                        deptRV.setVisibility(View.GONE);
                    }
                    else {
                        deptRV.setVisibility(View.VISIBLE);
                    }

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    deptRV.setLayoutManager(layoutManager);

                    SelectedDetailsAdapter adapter = new SelectedDetailsAdapter(getContext(),deptList,1);
                    deptRV.setAdapter(adapter);

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

            if(!departmentHelper.fnCheckItemsExist(name.getText().toString().trim())){
                departmentHelper.fnInsertItems(name.getText().toString().trim());
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
                    semRV.setVisibility(View.GONE);
                }
                else {
                    semRV.setVisibility(View.VISIBLE);
                }

                SelectedDetailsAdapter adapter = new SelectedDetailsAdapter(list, getContext(),2);
                semRV.setAdapter(adapter);


                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                semRV.setLayoutManager(layoutManager);

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
    }

    private void showDivDialog(){

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_division);

        ImageView checkButton = dialog.findViewById(R.id.checkImgButton);

        CheckBox div1 = dialog.findViewById(R.id.div1CB);
        CheckBox div2 = dialog.findViewById(R.id.div2CB);
        CheckBox div3 = dialog.findViewById(R.id.div3CB);



        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                divisionHelper.fnDeleteAll();

                addDivData(div1);
                addDivData(div2);
                addDivData(div3);

                ArrayList<DivisionDomain> list = divisionHelper.fnGetItems();

                if(list.isEmpty()){
                    divRV.setVisibility(View.GONE);
                }
                else {
                    divRV.setVisibility(View.VISIBLE);
                }

                SelectedDetailsAdapter adapter = new SelectedDetailsAdapter(list,3,getContext());
                divRV.setAdapter(adapter);


                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                divRV.setLayoutManager(layoutManager);

                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.dialog_background));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void addDivData(@NonNull CheckBox name) {

        if (name.isChecked()) {

            if (!divisionHelper.fnCheckItemsExist(name.getText().toString())) {
                divisionHelper.fnInsertItems(name.getText().toString());
            }
        }
    }

    private void showSubDialog(){

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_subject);

        ImageView checkButton = dialog.findViewById(R.id.checkImgButton);

        CheckBox cLang = dialog.findViewById(R.id.cLangCB);
        CheckBox physics = dialog.findViewById(R.id.physCB);
        CheckBox maths = dialog.findViewById(R.id.mathsCB);
        CheckBox cpp = dialog.findViewById(R.id.cppLangCB);
        CheckBox os = dialog.findViewById(R.id.osCB);
        CheckBox java = dialog.findViewById(R.id.javaLangCB);



        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                subjectHelper.fnDeleteAll();

                addSubData(cLang);
                addSubData(physics);
                addSubData(maths);
                addSubData(cpp);
                addSubData(os);
                addSubData(java);

                ArrayList<SubjectDomain> list = subjectHelper.fnGetItems();

                if(list.isEmpty()){
                    subRV.setVisibility(View.GONE);
                }
                else {
                    subRV.setVisibility(View.VISIBLE);
                }

                SelectedDetailsAdapter adapter = new SelectedDetailsAdapter(4,getContext(),list);
                subRV.setAdapter(adapter);


                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                subRV.setLayoutManager(layoutManager);

                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.dialog_background));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void addSubData(@NonNull CheckBox name) {

        if (name.isChecked()) {

            if (!subjectHelper.fnCheckItemsExist(name.getText().toString())) {
                subjectHelper.fnInsertItems(name.getText().toString());
            }
        }
    }

    private void SelectPDF(){

        Intent i = new Intent();
        i.setType("application/pdf");
        i.setAction(i.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select PDF File "),1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            pdfName = data.getDataString().substring(data.getDataString().lastIndexOf("/") + 1 );

            UploadPDFFileFirebase(data.getData());

        }
    }

    private void UploadPDFFileFirebase(Uri data){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("File is loading...");
        progressDialog.show();

        StorageReference reference = storageReference.child("uploadPDF/" + System.currentTimeMillis() + ".pdf");

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri uri = uriTask.getResult();

                        putPDF putPDF = new putPDF(pdfName,uri.toString());


                        selectedDept = departmentHelper.fnGetItems();
                        selectedSem = semesterHelper.fnGetItems();
                        selectedDiv = divisionHelper.fnGetItems();
                        selectedSub = subjectHelper.fnGetItems();


                        for(int j=0; j<selectedDept.size(); j++){

                            for(int k=0; k<selectedSem.size();k++){

                                for(int p=0; p<selectedDiv.size();p++){

                                    for(int q=0; q<selectedSub.size(); q++){

                                        DepartmentDomain departmentDomain = selectedDept.get(j);
                                        SemesterDomain semesterDomain = selectedSem.get(k);
                                        DivisionDomain divisionDomain = selectedDiv.get(p);
                                        SubjectDomain subjectDomain = selectedSub.get(q);

                                        DocumentReference documentReference2 = firebaseFirestore.collection("UploadedPDF").document(departmentDomain.getItem());


                                        Map<String,Object> pdf = new HashMap<>();
                                        pdf.put("Email",email);
                                        pdf.put("Dept",departmentDomain.getItem());
                                        pdf.put("Semester",semesterDomain.getItem());
                                        pdf.put("Division",divisionDomain.getItem());
                                        pdf.put("Subject",subjectDomain.getItem());
                                        pdf.put("PDF Name",pdfName);
                                        pdf.put("Uri",uri.toString());

                                        String id = UUID.randomUUID().toString();

                                        documentReference2.collection(semesterDomain.getItem()).document(divisionDomain.getItem())
                                                .collection(subjectDomain.getItem()).document(name).collection(name).document(pdfName)
                                                .set(pdf).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                databaseReference.child(databaseReference.push().getKey()).setValue(putPDF);
                                                Toast.makeText(getContext(), "File Upload", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();

//
//                                                putPDF domain = new putPDF();
//                                                domain.setName(pdfName);
//                                                domain.setUrl(uri.toString());
//
//                                                pdfDetails.add(domain);

                                                uploadedFileRV.setVisibility(View.VISIBLE);

                                                viewFiles();

//                                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//                                                uploadedFileRV.setLayoutManager(layoutManager);
//
//                                                UploadedFileAdapter adapter = new UploadedFileAdapter(getContext(),pdfDetails);
//                                                uploadedFileRV.setAdapter(adapter);

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("TAG","onFail: " + e.toString());
                                                Toast.makeText(getContext(), "Error: Firestore Error!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        double progress = (100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        progressDialog.setMessage("File Uploaded.."+(int) progress+ "%" );

                    }
                });
    }

    private void viewFiles() {

        databaseReference = FirebaseDatabase.getInstance().getReference("uploadPDF");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for( DataSnapshot postSnapshot: snapshot.getChildren()){
                    putPDF putPDF = postSnapshot.getValue(com.example.studentcareerapp.Domains.putPDF.class);
                    pdfDetails.add(putPDF);
                }

                text.setVisibility(View.VISIBLE);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                uploadedFileRV.setLayoutManager(layoutManager);

                UploadedFileAdapter adapter = new UploadedFileAdapter(getContext(),pdfDetails);
                uploadedFileRV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
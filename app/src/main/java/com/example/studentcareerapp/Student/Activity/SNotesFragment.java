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
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentcareerapp.ArraylistHelper.DepartmentHelper;
import com.example.studentcareerapp.ArraylistHelper.DivisionHelper;
import com.example.studentcareerapp.ArraylistHelper.FacultyCheckboxHelper;
import com.example.studentcareerapp.ArraylistHelper.FacultyHelper;
import com.example.studentcareerapp.ArraylistHelper.SubjectHelper;
import com.example.studentcareerapp.Domains.DepartmentDomain;
import com.example.studentcareerapp.Domains.DivisionDomain;
import com.example.studentcareerapp.Domains.FacultyDomain;
import com.example.studentcareerapp.Domains.SubjectDomain;
import com.example.studentcareerapp.Domains.putPDF;
import com.example.studentcareerapp.Faculty.Adapter.SelectedDetailsAdapter;
import com.example.studentcareerapp.FacultyNameAdapter;
import com.example.studentcareerapp.R;
import com.example.studentcareerapp.Student.Adapter.getNotesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SNotesFragment extends Fragment {


    private ImageView selectDiv,selectFaculty,selectSub;;
    TextView noteText;
    private Button getNotesBtn;
    private RecyclerView divRV,facultyRV,subRV,showPDF;

    ArrayList<DivisionDomain> selectedDivision = new ArrayList<DivisionDomain>();
    ArrayList<FacultyDomain> selectedFaculty = new ArrayList<FacultyDomain>();
    ArrayList<String> facultyNames = new ArrayList<String>();
    ArrayList<SubjectDomain> selectedSubject = new ArrayList<SubjectDomain>();
    ArrayList<putPDF> pdfDetails = new ArrayList<putPDF>();

    FacultyHelper facultyHelper;
    FacultyCheckboxHelper facultyCheckboxHelper;
    DivisionHelper divisionHelper;
    SubjectHelper subjectHelper;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID,email,dept,sem,div;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SNotesFragment() {
        // Required empty public constructor
    }

    public static SNotesFragment newInstance(String param1, String param2) {
        SNotesFragment fragment = new SNotesFragment();
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

        View view =  inflater.inflate(R.layout.fragment_s_notes, container, false);


        selectFaculty = view.findViewById(R.id.faultySelectImg);
        selectDiv = view.findViewById(R.id.divSelectImg);
        selectSub = view.findViewById(R.id.subSelectImg);
        getNotesBtn = view.findViewById(R.id.getBtn);
        noteText = view.findViewById(R.id.text);

        facultyRV = view.findViewById(R.id.facultyRV);
        divRV = view.findViewById(R.id.divRV);
        subRV = view.findViewById(R.id.subRV);
        showPDF = view.findViewById(R.id.showPDFRV);

        facultyHelper = new FacultyHelper(getContext());
        facultyCheckboxHelper = new FacultyCheckboxHelper(getContext());
        divisionHelper = new DivisionHelper(getContext());
        subjectHelper = new SubjectHelper(getContext());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        email = firebaseAuth.getCurrentUser().getEmail();


        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploadPDF");


        firebaseFirestore.collection("users").document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            dept = documentSnapshot.getString("Department").trim();
                            sem = documentSnapshot.getString("Semester").trim();
                            div = documentSnapshot.getString("Division").trim();

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

        selectDiv.setOnClickListener(new View.OnClickListener() {
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

        selectSub.setOnClickListener(new View.OnClickListener() {
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

        getNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedFaculty = facultyCheckboxHelper.fnGetItems();
                selectedDivision = divisionHelper.fnGetItems();
                selectedSubject = subjectHelper.fnGetItems();


                for(int j=0; j<selectedSubject.size(); j++){

                    for(int k=0; k<selectedFaculty.size();k++){

                        SubjectDomain subjectDomain = selectedSubject.get(j);
                        FacultyDomain facultyDomain = selectedFaculty.get(k);

                        firebaseFirestore.collection("UploadedPDF").document(dept).collection(sem).document(div)
                                .collection(subjectDomain.getItem()).document(facultyDomain.getItem()).collection(facultyDomain.getItem())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if(task.isSuccessful()){

                                            for(DocumentSnapshot documentSnapshot: task.getResult()){

                                                putPDF putPDF = new putPDF(documentSnapshot.getString("PDF Name"),documentSnapshot.getString("Uri"));

                                                pdfDetails.add(putPDF);

                                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                                showPDF.setLayoutManager(layoutManager);

                                                getNotesAdapter  adapter = new getNotesAdapter(getContext(),pdfDetails);
                                                showPDF.setAdapter(adapter);

                                            }
                                        }
                                        else {
                                            Toast.makeText(getContext(), "Error Here", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })

                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                }

            }
        });


        return view;

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
                        facultyRV.setVisibility(View.GONE);
                    }
                    else {
                        facultyRV.setVisibility(View.VISIBLE);
                    }


                    SelectedDetailsAdapter adapter = new SelectedDetailsAdapter(5,list,getContext());
                    facultyRV.setAdapter(adapter);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    facultyRV.setLayoutManager(layoutManager);

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


}
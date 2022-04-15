package com.example.studentcareerapp.Student.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studentcareerapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class SProfileFragment extends Fragment {

    View view;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;

    TextView name,email,dept,sem,div,id,titleName;

    public SProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_s_profile, container, false);

        titleName = (TextView) view.findViewById(R.id.name1FPTV);
        name = (TextView) view.findViewById(R.id.nameSPTV);
        email = (TextView) view.findViewById(R.id.emailSPTV);
        dept = (TextView) view.findViewById(R.id.deptSPTV);
        sem = (TextView) view.findViewById(R.id.semSPTV);
        div = (TextView) view.findViewById(R.id.divSPTV);
        id = (TextView) view.findViewById(R.id.sidSPTV);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                titleName.setText(value.getString("Name"));
                name.setText(value.getString("Name"));
                email.setText(value.getString("Email"));
                dept.setText(value.getString("Department"));
                sem.setText(value.getString("Semester"));
                div.setText(value.getString("Division"));
                id.setText(value.getString("Id No"));

            }
        });

    }
}
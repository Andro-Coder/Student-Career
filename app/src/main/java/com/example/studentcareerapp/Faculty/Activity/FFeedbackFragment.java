package com.example.studentcareerapp.Faculty.Activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentcareerapp.Domains.FacultyFeedbackDomain;
import com.example.studentcareerapp.Domains.StudentNotificationDomain;
import com.example.studentcareerapp.R;
import com.example.studentcareerapp.Student.Adapter.FeedbackAdapter;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FFeedbackFragment extends Fragment {

    RecyclerView feedbackRV;
    TextView noFeedback;

    FeedbackAdapter adapter;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    DocumentReference documentUsers;

    ArrayList<FacultyFeedbackDomain> feedbackDetails = new ArrayList<FacultyFeedbackDomain>();

    String userID,dept,name;

    int count = 0, temp = 0, temp1 =0, count1 =0;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FFeedbackFragment() {
        // Required empty public constructor
    }

    public static FFeedbackFragment newInstance(String param1, String param2) {
        FFeedbackFragment fragment = new FFeedbackFragment();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_f_feedback, container, false);

        feedbackRV = view.findViewById(R.id.facultyFeedbackRV);
        noFeedback = view.findViewById(R.id.emptyFeedbackTV);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();


        documentUsers = firebaseFirestore.collection("users").document(userID);
        documentUsers.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            dept = documentSnapshot.getString("Department").trim();
                            name = documentSnapshot.getString("Name").trim();


                            firebaseFirestore.collection("Feedback").document(dept).collection(name)
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            //Log.d("task","partnerID" + document.getString("partner"));
                                            //Log.d("task",document.getString("title"));

                                            feedbackRV.setVisibility(View.VISIBLE);
                                            noFeedback.setVisibility(View.GONE);

                                            FacultyFeedbackDomain domain = new FacultyFeedbackDomain();
                                            domain.setEmail(document.getString("Email"));

                                            String sem =document.getString("Semester");


                                            if(sem.equals("Semester - 1")){
                                                domain.setSemester("1");
                                            }
                                            else if(sem.equals("Semester - 2")){
                                                domain.setSemester("2");
                                            }
                                            else if(sem.equals("Semester - 3")){
                                                domain.setSemester("3");
                                            }
                                            else if(sem.equals("Semester - 4")){
                                                domain.setSemester("4");
                                            }
                                            else if(sem.equals("Semester - 5")){
                                                domain.setSemester("5");
                                            }
                                            else if(sem.equals("Semester - 6")){
                                                domain.setSemester("6");
                                            }
                                            else if(sem.equals("Semester - 7")){
                                                domain.setSemester("7");
                                            }
                                            else if(sem.equals("Semester - 8")){
                                                domain.setSemester("8");
                                            }

                                            domain.setFeedback(document.getString("Feedback Message"));

                                            feedbackDetails.add(domain);

                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                                            feedbackRV.setLayoutManager(linearLayoutManager);

                                            adapter = new FeedbackAdapter(getContext(),feedbackDetails);
                                            feedbackRV.setAdapter(adapter);
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(getContext(), "Failure!", Toast.LENGTH_SHORT).show();

                                    feedbackRV.setVisibility(View.GONE);
                                    noFeedback.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                        else {
                            Toast.makeText(getContext(), "User Document Not Found", Toast.LENGTH_SHORT).show();

                            feedbackRV.setVisibility(View.GONE);
                            noFeedback.setVisibility(View.VISIBLE);

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

        return view;
    }
}
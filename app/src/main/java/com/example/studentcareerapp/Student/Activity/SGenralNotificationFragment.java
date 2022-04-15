package com.example.studentcareerapp.Student.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentcareerapp.Domains.DepartmentDomain;
import com.example.studentcareerapp.Domains.StudentNotificationDomain;
import com.example.studentcareerapp.Faculty.Adapter.SelectedDetailsAdapter;
import com.example.studentcareerapp.R;
import com.example.studentcareerapp.Student.Adapter.NotificationAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SGenralNotificationFragment extends Fragment {


    private RecyclerView notificationRV;
    private TextView noNotification;

    int count = 0;

    NotificationAdapter adapter;

    ArrayList<StudentNotificationDomain> notificationDetail = new ArrayList<StudentNotificationDomain>();
    ArrayList<StudentNotificationDomain> list = new ArrayList<StudentNotificationDomain>();

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    DocumentReference documentUsers,documentNotification;

    String userID,dept,sem;
    String ceDept = "Computer (CE)";
    String itDept = "IT";
    String civilDept = "Civil (CL)";
    String mechanicalDept = "Mechanical (ME)";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SGenralNotificationFragment() {
        // Required empty public constructor
    }

    public static SGenralNotificationFragment newInstance(String param1, String param2) {
        SGenralNotificationFragment fragment = new SGenralNotificationFragment();
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

        View view = inflater.inflate(R.layout.fragment_s_genral_notification, container, false);


        notificationRV = view.findViewById(R.id.studentGNRV);
        noNotification = view.findViewById(R.id.emptyNotifyTV);

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
                            sem = documentSnapshot.getString("Semester").trim();


                             CollectionReference colRef = firebaseFirestore.collection("GeneralNotification").document(dept).collection(sem);
                             colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                 @Override
                                 public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                     if (task.isSuccessful()) {

                                         for (QueryDocumentSnapshot document : task.getResult()) {

                                             //Log.d("task","partnerID" + document.getString("partner"));
                                            // Log.d("task", document.getString("title"));

                                             notificationRV.setVisibility(View.VISIBLE);
                                             noNotification.setVisibility(View.GONE);

                                             StudentNotificationDomain domain = new StudentNotificationDomain();
                                             domain.setEmail(document.getString("Email"));

                                             String sem = document.getString("Semester");


                                             if (sem.equals("Semester - 1")) {
                                                 domain.setSemester("1");
                                             } else if (sem.equals("Semester - 2")) {
                                                 domain.setSemester("2");
                                             } else if (sem.equals("Semester - 3")) {
                                                 domain.setSemester("3");
                                             } else if (sem.equals("Semester - 4")) {
                                                 domain.setSemester("4");
                                             } else if (sem.equals("Semester - 5")) {
                                                 domain.setSemester("5");
                                             } else if (sem.equals("Semester - 6")) {
                                                 domain.setSemester("6");
                                             } else if (sem.equals("Semester - 7")) {
                                                 domain.setSemester("7");
                                             } else if (sem.equals("Semester - 8")) {
                                                 domain.setSemester("8");
                                             }

                                             domain.setMsgTitle(document.getString("title"));
                                             domain.setMessage(document.getString("Message"));

                                             notificationDetail.add(domain);

                                             LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                                             notificationRV.setLayoutManager(linearLayoutManager);

                                             adapter = new NotificationAdapter(getContext(), notificationDetail);
                                             notificationRV.setAdapter(adapter);
                                         }
                                     }
                                 }
                             })
                                     .addOnFailureListener(new OnFailureListener() {
                                         @Override
                                         public void onFailure(@NonNull Exception e) {

                                             Toast.makeText(getContext(), "Failure!", Toast.LENGTH_SHORT).show();


                                             noNotification.setVisibility(View.VISIBLE);
                                             notificationRV.setVisibility(View.GONE);
                                         }
                                     });



                        }
                        else {
                            Toast.makeText(getContext(), "User Document Not Found", Toast.LENGTH_SHORT).show();
                            noNotification.setVisibility(View.VISIBLE);
                            notificationRV.setVisibility(View.GONE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                        Log.d("Error!",e.toString());
                        noNotification.setVisibility(View.VISIBLE);
                        notificationRV.setVisibility(View.GONE);
                    }
                });

        return view;
    }

}
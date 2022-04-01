package com.example.studentcareerapp.Faculty.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.studentcareerapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FRegistration extends AppCompatActivity {

    Button Register;
    TextInputEditText name,email,designation,dept,phone,password,confPass;

    String emailPattern = "[a-zA-Z0-9._-]+@[charusat]+\\.+[ac]+\\.+[in]+";
    String phonePattern = "^[+]?[0-9]{10}$";
    String passwordPattern = "^(?=\\S+$).{6,}$";        // No white space and at least 6 digit

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_f_registration);


        String role = getIntent().getStringExtra("Role");


        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        name = findViewById(R.id.textInputEditNAME);
        email = findViewById(R.id.textInputEditEMAIL);
        designation = findViewById(R.id.textInputEditDESIGNATION);
        dept = findViewById(R.id.textInputEditDEPARTMENT);
        phone = findViewById(R.id.textInputEditContactNO);
        password = findViewById(R.id.textInputEditPASSWORD);
        confPass = findViewById(R.id.textInputEditConformPASSWORD);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        /*if(firebaseAuth.getCurrentUser() != null){
            Toast.makeText(FRegistration.this, "User Already Exist", Toast.LENGTH_SHORT).show();
            finish();
        }*/


        Register = findViewById(R.id.registerBTN);

        Register.setOnClickListener(view -> {
            StartRegistration(role);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void StartRegistration(String role){

        String userName = name.getText().toString();
        String userEmail = email.getText().toString().trim();
        String userDesig = designation.getText().toString();
        String userDept = dept.getText().toString();
        String userPhone = phone.getText().toString();
        String userPassword = password.getText().toString().trim();
        String userConfPassword = confPass.getText().toString().trim();


        if(userName.isEmpty() || userEmail.isEmpty() || userDesig.isEmpty() || userDept.isEmpty() || userPhone.isEmpty() || userConfPassword.isEmpty() || userPassword.isEmpty()){
            Toast.makeText(FRegistration.this, "All fields are Required!", Toast.LENGTH_SHORT).show();
        }
        else{
            if(CheckPattern())
            {
                if(!userPassword.equals(userConfPassword))
                {
                    Toast.makeText(FRegistration.this, "Passwords are not matching.\nPlease try again..", Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                userID = firebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);

                                Map<String,Object> user = new HashMap<>();
                                user.put("Role",role);
                                user.put("Name",userName);
                                user.put("Email",userEmail);
                                user.put("Designation",userDesig);
                                user.put("Department",userDept);
                                user.put("Phone No",userPhone);
                                user.put("Password",userPassword);

                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(FRegistration.this, "Registration Successful.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG","onFail: " + e.toString());
                                        Toast.makeText(FRegistration.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                Intent intent = new Intent(FRegistration.this, com.example.studentcareerapp.LoginActivity.class);
                                intent.putExtra("Role",2);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(FRegistration.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }
    }

    private boolean CheckPattern()
    {
        if(!email.getText().toString().trim().matches(emailPattern))
        {
            Toast.makeText(FRegistration.this, "Enter Valid Email Id", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!phone.getText().toString().trim().matches(phonePattern))
        {
            Toast.makeText(FRegistration.this, "Enter Valid Phone Number.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!password.getText().toString().trim().matches(passwordPattern))
        {
            Toast.makeText(FRegistration.this, "Make sure that password length is minimum 4 and white spaces are not allowed.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
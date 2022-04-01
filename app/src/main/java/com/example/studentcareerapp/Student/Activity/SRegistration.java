package com.example.studentcareerapp.Student.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class SRegistration extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Button register;
    TextInputEditText name,email,dept,div,id,phone,password,confPass;

    String emailPattern = "[a-zA-Z0-9._-]+@[charusat]+\\.+[edu]+\\.+[in]+";
    String phonePattern = "^[+]?[0-9]{10}$";
    String passwordPattern = "^(?=\\S+$).{6,}$";        // No white space and at least 6 digit

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_s_registration);

        String role = getIntent().getStringExtra("Role");

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        name = findViewById(R.id.textInputEditName);
        email = findViewById(R.id.textInputEditEmail);
        dept = findViewById(R.id.textInputEditDepartment);
        div = findViewById(R.id.textInputEditDvision);
        id = findViewById(R.id.textInputEditIdNo);
        phone = findViewById(R.id.textInputEditContactNo);
        password = findViewById(R.id.textInputEditPassword);
        confPass = findViewById(R.id.textInputEditConformPassword);
        register = findViewById(R.id.register);

        Spinner spinner = findViewById(R.id.semester);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.SelectSemester, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        /*if(firebaseAuth.getCurrentUser() != null){
            Toast.makeText(SRegistration.this, "User Already Exist", Toast.LENGTH_SHORT).show();
            finish();
        }*/


        register.setOnClickListener(view -> {
            StartRegistration(spinner,role);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        System.out.println(text+"Selected");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void StartRegistration(Spinner spinner, String role){

        String userName = name.getText().toString();
        String userEmail = email.getText().toString().trim();
        String userDept = dept.getText().toString();
        String userSem = spinner.getSelectedItem().toString();
        String userDiv = div.getText().toString();
        String userId = id.getText().toString();
        String userPhone = phone.getText().toString();
        String userPassword = password.getText().toString().trim();
        String userConfPassword = confPass.getText().toString().trim();


        if(userName.isEmpty() || userEmail.isEmpty() || userDept.isEmpty() ||  userSem.isEmpty() || userDiv.isEmpty() ||userId.isEmpty() ||userPhone.isEmpty() || userConfPassword.isEmpty() || userPassword.isEmpty()){
            Toast.makeText(SRegistration.this, "All fields are Required!", Toast.LENGTH_SHORT).show();
        }
        else{
            if(CheckPattern())
            {
                if(!userPassword.equals(userConfPassword))
                {
                    Toast.makeText(SRegistration.this, "Passwords are not matching.\nPlease try again..", Toast.LENGTH_SHORT).show();
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
                                user.put("Department",userDept);
                                user.put("Semester",userSem);
                                user.put("Division",userDiv);
                                user.put("Id No",userId);
                                user.put("Phone No",userPhone);
                                user.put("Password",userPassword);

                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(SRegistration.this, "Registration Successful.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG","onFail: " + e.toString());
                                        Toast.makeText(SRegistration.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                                    }
                                });


                                Intent intent = new Intent(SRegistration.this, com.example.studentcareerapp.LoginActivity.class);
                                intent.putExtra("Role",1);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(SRegistration.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SRegistration.this, "Enter Valid Email Id", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!phone.getText().toString().trim().matches(phonePattern))
        {
            Toast.makeText(SRegistration.this, "Enter Valid Phone Number.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!password.getText().toString().trim().matches(passwordPattern))
        {
            Toast.makeText(SRegistration.this, "Make sure that password length is minimum 4 and white spaces are not allowed.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
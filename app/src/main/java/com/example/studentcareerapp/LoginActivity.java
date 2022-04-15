package com.example.studentcareerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button loginBTN;
    private TextView forgotPassword, noAccount;

    private TextInputEditText email,password;

    String facultyEmailPattern = "[a-zA-Z0-9._-]+@[charusat]+\\.+[ac]+\\.+[in]+";
    String studentEmailPattern = "[a-zA-Z0-9._-]+@[charusat]+\\.+[edu]+\\.+[in]+";
    String passwordPattern = "^(?=\\S+$).{6,}$";        // No white space and at least 6 digit

    FirebaseAuth firebaseAuth;
    String TAG="role: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        int role = getIntent().getIntExtra("Role",0);


        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        email = findViewById(R.id.textInputEditTextEmail);
        password = findViewById(R.id.textInputEditTextPassword);
        forgotPassword = findViewById(R.id.textViewForgetPassword);
        loginBTN = findViewById(R.id.btnLOGIN);
        noAccount = findViewById(R.id.dontHaveAnAccount);

        Log.d(TAG,"Value" + role);

        if(role == 0){
            Toast.makeText(LoginActivity.this, "Error in Role", Toast.LENGTH_SHORT).show();
        }

        firebaseAuth = FirebaseAuth.getInstance();

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(intent1);
            }
        });


        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(role == 1){
                    Intent intent = new Intent(LoginActivity.this, com.example.studentcareerapp.Student.Activity.SRegistration.class);
                    intent.putExtra("Role","Student");
                    startActivity(intent);
                }
                else if(role == 2){
                    Intent intent = new Intent(LoginActivity.this, com.example.studentcareerapp.Faculty.Activity.FRegistration.class);
                    intent.putExtra("Role","Faculty");
                    startActivity(intent);
                }

            }
        });

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString();

                if(userEmail.isEmpty() || userPassword.isEmpty()){
                    Toast.makeText(LoginActivity.this, "All fields are Required!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(CheckPattern(role)){
                        firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, Tabs.class);
                                    intent.putExtra("Role",role);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "Login Unsuccessful.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

            }
        });

    }

    private boolean CheckPattern(int role)
    {
        if(role == 1 && (!email.getText().toString().trim().matches(studentEmailPattern))){
            Toast.makeText(LoginActivity.this, "Enter Valid Email Id", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(role == 2 && (!email.getText().toString().trim().matches(facultyEmailPattern))){
            Toast.makeText(LoginActivity.this, "Enter Valid Email Id.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!password.getText().toString().trim().matches(passwordPattern))
        {
            Toast.makeText(LoginActivity.this, "Make sure that password length is minimum 6 and white spaces are not allowed.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
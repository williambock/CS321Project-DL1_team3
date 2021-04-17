package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.util.Log;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
    private EditText email, password;
    private Button signIn, createAccount, reset;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        initialize();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPass();
            }
        });
    }

    private void registerUser(){
        Intent intent = new Intent(LoginActivity.this, RegisterUser.class);
        startActivity(intent);
    }

    private void forgotPass(){
        String userEmail;
        userEmail=email.getText().toString();
        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(getApplicationContext(), "Please Enter email", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(LoginActivity.this, "The Link has been sent to your email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    
    private void loginUser(){
        String userEmail, userPass;
        userEmail=email.getText().toString();
        userPass=password.getText().toString();

        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(getApplicationContext(), "Please Enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(userPass)){
            Toast.makeText(getApplicationContext(),"please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(getApplicationContext(), "Sign In Successful", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent MainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(MainActivityIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initialize(){
        email=findViewById(R.id.UserEmail);
        password=findViewById(R.id.UserPassword);
        signIn =findViewById(R.id.SignInButton);
        createAccount=findViewById(R.id.CreateAccount);
        reset=findViewById(R.id.ForgotButton);
    }
}
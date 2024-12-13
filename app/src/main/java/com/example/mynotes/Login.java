package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.myapplication16.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView creatAccountBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.edit_email_text);
        passwordEditText = findViewById(R.id.edit_password_text);
        loginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progressBar);
        creatAccountBtnTextView = findViewById(R.id.createAccountTxtViewBtn);

        loginBtn.setOnClickListener((v)-> loginUser());
        creatAccountBtnTextView.setOnClickListener((v)-> startActivity(new Intent(Login.this, CreateAccount.class)));
    }

    void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isValidated = validation(email, password);
        if(!isValidated) {
            return;
        }

        loginAccountInFirebase(email,password);
    }

    void loginAccountInFirebase(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){
                    //login success
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to main activity
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                    }
                    else {
                        Utlity.showToast(Login.this, "Email not verified, Please verify your email");
                    }
                }
                else{
                    //login failed
                    Utlity.showToast(Login.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validation(String email, String password){
        //Validation of input by user
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email not valid");
            return false;
        }
        if(password.length()<6){
            passwordEditText.setError("Password length error");
            return false;
        }
        return true;
    }
}
package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


public class CreateAccount extends AppCompatActivity {

    EditText emailEditText, passwordEditText, confirmPasswordEditText;
    Button createAccountbtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText = findViewById(R.id.edit_email_text);
        passwordEditText = findViewById(R.id.edit_password_text);
        confirmPasswordEditText = findViewById(R.id.edit_confirmPassword_text);
        createAccountbtn = findViewById(R.id.create_accountButton);
        progressBar = findViewById(R.id.progressBar);
        loginBtnTextView = findViewById(R.id.LoginTextView);

        createAccountbtn.setOnClickListener(v-> createAccount());
        loginBtnTextView.setOnClickListener(v-> finish());
    }


    void createAccount(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword= confirmPasswordEditText.getText().toString();

        boolean isValidated = validation(email, password, confirmPassword);
        if(!isValidated) {
            return;
        }

        createAccountFirebase(email,password);
    }

    void  createAccountFirebase(String email, String password ){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccount.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if(task.isSuccessful()){
                            Utlity.showToast(CreateAccount.this, "Successfully create account, Check Email to verify");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }
                        else{
                            Utlity.showToast(CreateAccount.this, task.getException().getLocalizedMessage());
                        }
                    }
                }
        );
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountbtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            createAccountbtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validation(String email, String password, String confirmPassword){
        //Validation of input by user
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Email not valid");
            return false;
        }
        if(password.length()<6){
            passwordEditText.setError("Password length error");
            return false;
        }
        if(!password.equals(confirmPassword)){
            confirmPasswordEditText.setError("Password not same");
            return false;
        }
        return true;
    }
}
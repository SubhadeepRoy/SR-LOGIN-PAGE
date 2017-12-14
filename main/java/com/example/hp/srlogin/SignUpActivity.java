package com.example.hp.srlogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.regex.Pattern;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
EditText edittextEmail,edittextPassword;
    ProgressBar progressBar;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        progressBar=(ProgressBar)findViewById(R.id.progressbar) ;
        edittextEmail =(EditText)findViewById(R.id.editTextEmail);
                edittextPassword=(EditText)findViewById(R.id.editTextPassword);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.buttonSignup).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
    }
    private void registerUser(){
        String email=edittextEmail.getText().toString().trim();
        String password=edittextPassword.getText().toString().trim();
        progressBar=(ProgressBar)findViewById(R.id.progressbar);

        if (email.isEmpty()){
            edittextEmail.setError("EMAIL IS REQUIRED");
            edittextEmail.requestFocus();
            return;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edittextEmail.setError("ENTER VALID EMAIL ADDRESS");
            edittextEmail.requestFocus();
            return;

        }
        if(password.isEmpty()){
            edittextPassword.setError("password is required");
                    edittextPassword.requestFocus();
            return;

        }
        if(password.length()<6){
            edittextPassword.setError("password is too short");
            edittextPassword.requestFocus();
            return;
        }
progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent intent=new Intent(SignUpActivity.this,ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"YOU ARE ALREADY REGISTERED",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSignup:
                registerUser();

                break;
            case R.id.textViewLogin:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}

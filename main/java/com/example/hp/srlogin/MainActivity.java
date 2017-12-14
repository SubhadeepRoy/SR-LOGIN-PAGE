package com.example.hp.srlogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mAuth;
    EditText edittextEmail,edittextPassword;
    ProgressBar progressBar;
    Button buttonLogin;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();

        progressBar=(ProgressBar)findViewById(R.id.progressbar) ;
        edittextEmail =(EditText)findViewById(R.id.editTextEmail);
        edittextPassword=(EditText)findViewById(R.id.editTextPassword);

        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);


    }
    private void userLogin(){
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

mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        progressBar.setVisibility(View.GONE);
        if(task.isSuccessful()){
            Intent intent=new Intent(MainActivity.this, ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

    }else{
            Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
        }}
});
    }
    @Override
    public void  onClick(View view){
        switch(view.getId()){
            case R.id.textViewSignup:

                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.buttonLogin:
                userLogin();
                break;
        }
    }

}

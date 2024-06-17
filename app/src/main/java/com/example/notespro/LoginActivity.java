package com.example.notespro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email_et,password_et;
    Button button_loginAccount;
    TextView text_createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        email_et=findViewById(R.id.et_email);
        password_et=findViewById(R.id.et_password);
        button_loginAccount=findViewById(R.id.bt_loginAccount);
        text_createAccount=findViewById(R.id.txt_createAccount);

        button_loginAccount.setOnClickListener((v)->loginUser());
        text_createAccount.setOnClickListener((v)->startActivity(new Intent(LoginActivity.this, CreateAcoountActivity.class)));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    void loginUser(){
        String email =email_et.getText().toString();
        String password =password_et.getText().toString();

        boolean isValidated = validateData(email,password);

        if(!isValidated){
            return;
        }

        loginAccountInFirebase(email,password);

    }

    void loginAccountInFirebase(String email, String password){
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    changeInProgress(false);
                    //Login is successful
                        if(task.isSuccessful()){


                            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                //go to main activity
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            }else{
                                Utility.showToast(LoginActivity.this,"Email not verified.Please verify your email.");
                            }


                        } else{
                            //Login failed
                            Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                        }
                    }
                }
        );
    }



    void changeInProgress(boolean inProgress){

        if(inProgress){
            button_loginAccount.setVisibility(View.GONE);
        }
        else{
            button_loginAccount.setVisibility((View.VISIBLE));
        }
    }



    boolean validateData(String email,String password){

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_et.setError("Email is invalid.");
            return false;
        }
        if(password.length()<6){
            password_et.setError("Password is invalid.");
            return false;
        }

        return true;

    }

}
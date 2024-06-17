package com.example.notespro;

import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAcoountActivity extends AppCompatActivity {
    EditText email_et,password_et,confirmpass_et;
    Button createAccountButton;
    TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_acoount);



//        Linking with the XML

        email_et=findViewById(R.id.et_email);
        password_et=findViewById(R.id.et_password);
        confirmpass_et=findViewById(R.id.et_cpassword);
        createAccountButton=findViewById(R.id.bt_createA);
        loginText=findViewById(R.id.txt_login);

        createAccountButton.setOnClickListener(v-> createAccount());
        loginText.setOnClickListener(v-> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    void createAccount(){
        String email =email_et.getText().toString();
        String password =password_et.getText().toString();
        String confirmPassword =confirmpass_et.getText().toString();

        boolean isValidated = validateData(email,password,confirmPassword);

        if(!isValidated){
            return;
        }

        createAccountInFirebase(email,password);
    }

    void createAccountInFirebase (String email,String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAcoountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Account is created
                            Utility.showToast(CreateAcoountActivity.this,"Account Created,Check Email.");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();

                        }
                        else{
                            //failure
                            Utility.showToast(CreateAcoountActivity.this,task.getException().getLocalizedMessage());


                        }
                    }
                }
        );


    }


    void changeInProgress(boolean inProgress){

        if(inProgress){
            createAccountButton.setVisibility(View.GONE);
        }
        else{
            createAccountButton.setVisibility((View.VISIBLE));
        }
    }






    boolean validateData(String email,String password,String confirmPassword){

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_et.setError("Email is invalid.");
            return false;
        }
        if(password.length()<6){
            password_et.setError("Password is invalid.");
            return false;
        }
        if(!password.equals(confirmPassword)){
            confirmpass_et.setError("Password doesn't matched.");
            return false;
        }
        return true;

    }





}
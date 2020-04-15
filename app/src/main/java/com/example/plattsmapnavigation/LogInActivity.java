package com.example.plattsmapnavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//sign in activity
public class LogInActivity extends AppCompatActivity {
    EditText emailID, password;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mFirebaseAuth = FirebaseAuth.getInstance(); //create firebase instance
        emailID = findViewById(R.id.editText);  //get email from textbox in SignUpActivity.xml
        password = findViewById(R.id.editText2); //get password from textbox in SignUpActivity.xml
        btnSignIn = findViewById(R.id.button);
        tvSignUp = findViewById(R.id.textView);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser != null){
                    Toast.makeText(LogInActivity.this, "You are logged in!", Toast.LENGTH_LONG).show();
                    SignInStatus.SignedIn = true;
                    SignInStatus.UserName = mFirebaseAuth.getCurrentUser().getEmail();
                    System.out.println(SignInStatus.UserName + "!!!!!!!!!!!!!!!!!!!!!!!!");
                    Intent i = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(LogInActivity.this, "Please Login", Toast.LENGTH_LONG).show();
                }
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = emailID.getText().toString();
                String pwd = password.getText().toString();
                if (email.isEmpty()) {
                    emailID.setError("Provide your email");
                    emailID.requestFocus();
                }
                else if (pwd.isEmpty()) {
                    password.setError("Provide your password");
                    password.requestFocus();
                }
                else if (email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(LogInActivity.this, "Fields are both empty!",Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LogInActivity.this, "Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(LogInActivity.this, "You are logged in!", Toast.LENGTH_LONG).show();
                                Intent inToHome = new Intent(LogInActivity.this, MainActivity.class);
                                SignInStatus.SignedIn = true;
                                SignInStatus.UserName = mFirebaseAuth.getCurrentUser().getEmail();
                                System.out.println(mFirebaseAuth.getCurrentUser().getEmail() + " !!!!!!!!!!!!!!!!!!!!!!!!");
                                startActivity(inToHome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(LogInActivity.this, "ERROR OCCURRED",Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intSignUp);
            }
        });
    }

    protected void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}

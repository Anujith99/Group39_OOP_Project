package com.example.food_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerLoginActivity extends AppCompatActivity {

    private EditText nEmail, nPassword,nphone;
    private Button nLogin,nRegister,nsavephone;
    private FirebaseAuth nAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        nAuth=FirebaseAuth.getInstance();
        firebaseAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                {
                    Intent intent=new Intent(CustomerLoginActivity.this,CustomerMapsActivity.class);

                    startActivity(intent);
                    finish();
                    return;
                }

            }
        };

        nEmail = (EditText) findViewById(R.id.email);
        nPassword = (EditText) findViewById(R.id.Password);
        nphone=(EditText)findViewById(R.id.Phoneno);
        nsavephone=(Button)findViewById(R.id.Phone) ;

        nLogin = (Button) findViewById(R.id.Login);
        nRegister = (Button) findViewById(R.id.Register);
        nRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=nEmail.getText().toString();
                final String password=nPassword.getText().toString();
                nAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CustomerLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(CustomerLoginActivity.this,"sign up error",Toast.LENGTH_SHORT).show();//check for errors
                        }
                        else{
                            String user_id=nAuth.getCurrentUser().getUid();
                            DatabaseReference current_user__db= FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user_id);
                            current_user__db.setValue(true);

                        }
                    }
                });
            }
        });

        nLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=nEmail.getText().toString();
                final String password=nPassword.getText().toString();
                nAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(CustomerLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(CustomerLoginActivity.this,"sign in error",Toast.LENGTH_SHORT).show();//check for errors
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        nAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        nAuth.addAuthStateListener(firebaseAuthListener);
    }
}

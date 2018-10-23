package com.groupscheduler.www;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

/**
 * This application demos the use of the Firebase Login feature. It currently supports logging in
 * with Google, Facebook, Twitter, Email/Password, and Anonymous providers.
 * <p/>
 * The methods in this class have been divided into sections based on providers (with a few
 * general methods).
 * <p/>
 * {@link LoginActivity}
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    String email,password;

    EditText email_et, pw_et;
    Button registerButton,loginButton;
    FirebaseAuth firebaseAuth;
    Context login_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login_context = LoginActivity.this;
        /* Load the view and display it */
        setContentView(R.layout.login);

        email_et = findViewById(R.id.login_email);
        pw_et = findViewById(R.id.login_pw);
        registerButton = findViewById(R.id.create_btn);
        loginButton = findViewById(R.id.login_btn);

        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(login_context, MainActivity.class));
        }

    }

    @Override
    public void onClick(View v) {
        email = email_et.getText().toString();
        password = pw_et.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(login_context, "Please fill in the required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        switch(v.getId()){
            case R.id.login_btn:
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(login_context, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(login_context, R.string.email_pw_error, Toast.LENGTH_SHORT).show();
                        }
                    }

                });
                break;
            case R.id.create_btn:
                if (password.length() < 6) {
                    Toast.makeText(login_context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                }
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(login_context, "You're already registed.", Toast.LENGTH_SHORT).show();
                                }
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(login_context, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(login_context, R.string.email_pw_error, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                break;
            default:
                break;
        }
    }
}

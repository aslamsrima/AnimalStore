package com.ics.animalworld.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.ics.animalworld.R;
import com.ics.animalworld.util.Common;

public class LogInActivity extends AppCompatActivity {
    private TextView register;
    private EditText UserName,Password;
    private FirebaseAuth mAuth;
    private Button SignIn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_login);
        mAuth = FirebaseAuth.getInstance();
        register = (TextView) findViewById(R.id.register_link);
        SignIn = (Button) findViewById(R.id.btnlogin);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
            }
        });
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserName = (EditText) findViewById(R.id.username);
                Password = (EditText) findViewById(R.id.password);

                mAuth.signInWithEmailAndPassword(UserName.getText().toString(), Password.getText().toString())
                        .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("", "Logged In Successfully");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(LogInActivity.this, ""+user.getEmail()+" Logged In Successfully.",
                                            Toast.LENGTH_SHORT).show();
                                    if (getIntent().hasExtra("reqCode") &&
                                            getIntent().getIntExtra("reqCode", 0) == Common.LOGIN_REQ) {

                                        Intent returnIntent = new Intent();
                                        setResult(RESULT_OK, returnIntent);
                                        finish();
                                    } else {
                                        startActivity(new Intent(LogInActivity.this, ECartHomeActivity.class));
                                        finish();
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LogInActivity.this, "Login Failed.",
                                            Toast.LENGTH_SHORT).show();

                                }

                                // ...
                            }
                        });

            }
        });

    }
}

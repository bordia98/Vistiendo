package com.example.vistiendo.vistiendo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {

    EditText emailfield;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        Toolbar toolbar =(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        emailfield = (EditText)findViewById(R.id.resetemailid);
        Button back =(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        });

        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendresetlink();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),Login.class);
        startActivity(i);
    }

    private void sendresetlink() {
        String email = emailfield.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailfield.setError("Enter  a valid email id");
            emailfield.requestFocus();
            return;
        }


        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful()){
                                                   emailfield.setText("");
                                                   emailfield.setEnabled(false);
                                                   submit.setEnabled(false);
                                                   Toast.makeText(getApplicationContext(),"Email is Send Successfully",Toast.LENGTH_SHORT).show();
                                                   Intent i = new Intent(getApplicationContext(),Login.class);
                                                   startActivity(i);
                                               }
                                               else{
                                                   emailfield.setText("");
                                                   emailfield.setEnabled(true);
                                                   submit.setEnabled(true);
                                                   Toast.makeText(getApplicationContext(),"Try again Later and check whether email exists or not ",Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       }
                );
    }

}

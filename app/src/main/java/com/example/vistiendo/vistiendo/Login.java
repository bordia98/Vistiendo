package com.example.vistiendo.vistiendo;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText email,password;
    ProgressBar pgbar;
    private FirebaseAuth mAuth;
    TextView new_user,forgotpass;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent i = new Intent(getApplicationContext(),Main_Activity.class);
            startActivity(i);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        onStart();
        setContentView(R.layout.login);
        Toolbar bar = findViewById(R.id.toolbar);
        bar.setTitleTextColor(Color.WHITE);
        bar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setSupportActionBar(bar);
        pgbar=(ProgressBar)findViewById(R.id.progressBar);
        pgbar.setVisibility(View.GONE);
        email=(EditText)findViewById(R.id.emailid);
        password=(EditText)findViewById(R.id.password);
        new_user = (TextView) findViewById(R.id.new_user);
        forgotpass = (TextView)findViewById(R.id.forgot_pass);


        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Sign_Up.class);
                startActivity(i);
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Forgot_Password.class);
                startActivity(i);
            }
        });
        Button login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logintheuser();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        System.exit(0);
    }

    private void logintheuser() {

        String emailid = email.getText().toString().trim();
        String passwordid = password.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(emailid).matches()) {
            email.setError("Enter  a valid email id");
            email.requestFocus();
            return;
        }

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            pgbar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(emailid, passwordid)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pgbar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                final FirebaseUser user = mAuth.getCurrentUser();
                                if(user!=null && user.isEmailVerified()){
                                    Intent i = new Intent(getApplicationContext(), Main_Activity.class);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Your email-id is not verifiedf", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please check your credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        } else {
            Toast.makeText(getApplicationContext(),"NO Internet Access",Toast.LENGTH_SHORT).show();
        }
    }
}

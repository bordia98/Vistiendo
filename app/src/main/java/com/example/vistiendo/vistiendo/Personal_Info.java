package com.example.vistiendo.vistiendo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Personal_Info extends AppCompatActivity {

    private FirebaseAuth mauth;
    private DatabaseReference users;
    private DatabaseReference check;
    private EditText fname,lname,sname,mobno,saddress,supi,shopgst;
    private Spinner shopcity,shopstate;
    private Button proceed;
    private String first_name,last_name,shop_name,mobile_number,shop_address,shop_upi,shop_gst;
    private ProgressBar pgbar;
    private static Map<String,Integer> states;
    private static Map<String,Integer> districts;
    private boolean exists=false;
    private String childid;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),Main_Activity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        creatmap();
        setContentView(R.layout.personal__info);
        Toolbar toolbar =(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        mauth = FirebaseAuth.getInstance();
        users = FirebaseDatabase.getInstance().getReference().child("Users").child(mauth.getCurrentUser().getUid());

        // code for bypassing the values
        // add the above snnipet in the main activity of navigation drawer
        //otherwise it will be quite hectic to figure out

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        sname = findViewById(R.id.shopname);
        mobno = findViewById(R.id.mob);
        saddress = findViewById(R.id.shopaddress);
        supi = findViewById(R.id.upiaddress);
        shopgst = findViewById(R.id.gstno);
        shopcity = findViewById(R.id.cities);
        shopstate = findViewById(R.id.states);

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Fetching Contents for you");
        pd.show();

        FirebaseDatabase.getInstance().getReference().child("Users").child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.hide();
                if (dataSnapshot.exists()){
                    for ( DataSnapshot snapshot : dataSnapshot.getChildren()){
                        childid = snapshot.getKey();
                        fname.setText(snapshot.child("fname").getValue().toString());
                        lname.setText(snapshot.child("lname").getValue().toString());
                        mobno.setText(snapshot.child("mobno").getValue().toString());
                        saddress.setText(snapshot.child("saddress").getValue().toString());
                        shopgst.setText(snapshot.child("shop_gst").getValue().toString());
                        sname.setText(snapshot.child("sname").getValue().toString());
                        supi.setText(snapshot.child("supi").getValue().toString());

                        int index;
                        index = districts.get(snapshot.child("scity").getValue().toString().trim());
                        shopcity.setSelection(index);
                        index = states.get(snapshot.child("sstate").getValue().toString().trim());
                        shopstate.setSelection(index);
                    }
                    exists = true;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.hide();
            }
        });


        pgbar=(ProgressBar)findViewById(R.id.progressBar);
        pgbar.setVisibility(View.GONE);
        proceed = findViewById(R.id.save_proceed);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savethedetails();
            }
        });

    }

    private static  void creatmap() {
         states = new HashMap<String,Integer>();
         districts = new HashMap<String,Integer>();


         // This part is done hardcoding in the starting later can be changed
         //adding the states we serves
        states.put("Rajasthan",0);

        //adding the districts wer serves
        districts.put("Banswara",0);
        districts.put("Bikaner",1);
    }

    private void savethedetails() {
        first_name = fname.getText().toString().trim();
        last_name = lname.getText().toString().trim();
        shop_name = sname.getText().toString().trim();
        mobile_number = mobno.getText().toString().trim();
        shop_address = saddress.getText().toString().trim();
        shop_upi = supi.getText().toString().trim();
        shop_gst = shopgst.getText().toString().trim();

        if(first_name.length()==0){
            fname.setError("Please write your first name");
            fname.requestFocus();
            fname.setText("");
            return;
        }

        if(last_name.length()==0){
            lname.setError("Please write your last name");
            lname.requestFocus();
            lname.setText("");
            return;
        }

        if(mobile_number.length()!=10){
            mobno.setError("Mobile number must be of 10 digits");
            mobno.requestFocus();
            mobno.setText("");
            return;
        }

        if(shop_name.length()==0){
            sname.setError("Shop name can't be empty");
            sname.requestFocus();
            sname.setText("");
            return;
        }

        if(shop_address.length()==0){
            saddress.setError("Shop address can't be empty");
            saddress.requestFocus();
            saddress.setText("");
            return;
        }

        if(shop_upi.length()==0){
            supi.setError("Shop upi can't be empty");
            supi.requestFocus();
            supi.setText("");
            return;
        }
        if(shop_gst.length()==0){
            shopgst.setError("GST number can't be empty");
            shopgst.requestFocus();
            shopgst.setText("");
            return;
        }

        String shop_state = shopstate.getSelectedItem().toString();
        String shop_city = shopcity.getSelectedItem().toString();

        if(!exists){
            final DatabaseReference newuser = users.push();
            final Map data_structure = new HashMap();
            data_structure.put("fname", first_name);
            data_structure.put("lname", last_name);
            data_structure.put("sname", shop_name);
            data_structure.put("shop_gst", shop_gst);
            data_structure.put("mobno", mobile_number);
            data_structure.put("saddress", shop_address);
            data_structure.put("scity",shop_city);
            data_structure.put("sstate",shop_state);
            data_structure.put("supi",shop_upi);
            pgbar.setVisibility(View.VISIBLE);
            Thread mainthread = new Thread(new Runnable() {
                @Override
                public void run() {
                    newuser.setValue(data_structure).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pgbar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "User details have been saved", Toast.LENGTH_SHORT).show();
                                fname.setText("");
                                lname.setText("");
                                sname.setText("");
                                saddress.setText("");
                                mobno.setText("");
                                shopgst.setText("");
                                supi.setText("");
                                Intent i = new Intent(getApplicationContext(), Main_Activity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), "Error in saving details", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

            mainthread.start();
        }else{
            final Map update_data_structure = new HashMap();
            update_data_structure.put("fname", first_name);
            update_data_structure.put("lname", last_name);
            update_data_structure.put("sname", shop_name);
            update_data_structure.put("shop_gst", shop_gst);
            update_data_structure.put("mobno", mobile_number);
            update_data_structure.put("saddress", shop_address);
            update_data_structure.put("scity",shop_city);
            update_data_structure.put("sstate",shop_state);
            update_data_structure.put("supi",shop_upi);

            users.child(childid).updateChildren(update_data_structure);
            Toast.makeText(getApplicationContext(),"Details have been updated successfully",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(),Main_Activity.class);
            startActivity(i);
        }
    }
}

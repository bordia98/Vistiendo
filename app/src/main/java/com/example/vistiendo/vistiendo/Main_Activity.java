package com.example.vistiendo.vistiendo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class Main_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Product_Upload.Upload_Click_Listner {

    private FirebaseAuth mauth;
    private FirebaseStorage storage;
    protected static Uri upload1,upload2,upload3,upload4;
    private int countbit;
    private DatabaseReference ref;
    private FirebaseFunctions mFunctions;
    private StorageReference storageref;
    private FirebaseFirestore firebaseFirestore;
    private Map<String, Object> product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFunctions = FirebaseFunctions.getInstance();
        mauth = FirebaseAuth.getInstance();

        // initialized storage
        storageref = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        upload1 = upload2 = upload3 = upload4 = null;


        // initializing progress bar
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Fetching Contents for you");
        pd.show();

        //checking whether the personal details are filled or not
        FirebaseDatabase.getInstance().getReference().child("Users").child(mauth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pd.hide();
                if (!dataSnapshot.exists()){

                    Intent i = new Intent(getApplicationContext(),Personal_Info.class);
                    startActivity(i);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.hide();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finishAffinity();
            System.exit(0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.profile) {
            Intent i = new Intent(getApplicationContext(),Personal_Info.class);
            startActivity(i);
            return true;
        }

        if(id== R.id.logout){
            mauth.signOut();
            Intent i = new Intent(getApplicationContext(),Login.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
        }
        else if(id == R.id.Add_Product){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Product_Upload fragment = new Product_Upload();
            ft.replace(R.id.mainFrame,fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void upload_image1(View v) {
        countbit=1;
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(512, 512)
                .setAspectRatio(1, 1)
                .start(this);
    }

    @Override
    public void upload_image2(View v) {
        countbit=2;
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(512, 512)
                .setAspectRatio(1, 1)
                .start(this);
    }

    @Override
    public void upload_image3(View v) {
        countbit=3;
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(512, 512)
                .setAspectRatio(1, 1)
                .start(this);
    }

    @Override
    public void upload_image4(View v) {
        countbit=4;
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(512, 512)
                .setAspectRatio(1, 1)
                .start(this);
    }

    // code for uploading the product in the backend
    @Override
    public void upload_product(String pro_name, String pro_price, String pro_qty, String pro_category, String pro_type, String pro_seller, View v) {

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Please wait while the Product gets uploaded");
        pd.show();


        product = new HashMap<>();
        product.put("product_name", pro_name);
        product.put("product_price", pro_price);
        product.put("product_quantity", pro_qty);
        product.put("product_seller", pro_seller);
        product.put("product_gender", pro_category);
        product.put("product_type",pro_type);

        final String productid=firebaseFirestore.collection("Records").document("Shopkeeper").collection(pro_seller).document("Product").collection(pro_type).document().getId();

        Bitmap image1,image2,image3,image4;
        image1=image2=image3=image4=null;
        if(upload1!=null){
            File pic1 = new File(upload1.getPath());
            try {

                image1 = new Compressor(this)
                        .setMaxHeight(720)
                        .setMaxWidth(720)
                        .setQuality(50)
                        .compressToBitmap(pic1);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();

                UploadTask filePath = storageref.child("Shopkeeper").child(pro_seller).child("Products").child(productid).child("photo1.jpg").putBytes(imageData);

                filePath.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            final String url1 = task.getResult().getDownloadUrl().toString();
                            Log.d("productid",url1);
                            product.put("photo1",url1);
                            firebaseFirestore.collection("Records").document("Shopkeeper").collection(pro_seller).document("Product").collection(pro_type).document(productid).update(product);
                        } else {
                            Toast.makeText(getApplicationContext(),"Error in uploading images",Toast.LENGTH_SHORT).show();
                            pd.hide();
                            return;
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(upload2!=null){
            File pic2 = new File(upload2.getPath());
            try {

                image2 = new Compressor(this)
                        .setMaxHeight(720)
                        .setMaxWidth(720)
                        .setQuality(50)
                        .compressToBitmap(pic2);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();

                UploadTask filePath = storageref.child("Shopkeeper").child(pro_seller).child("Products").child(productid).child("photo2.jpg").putBytes(imageData);

                filePath.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            final String url2 = task.getResult().getDownloadUrl().toString();
                            Log.d("productid",url2);
                            product.put("photo2",url2);
                            firebaseFirestore.collection("Records").document("Shopkeeper").collection(pro_seller).document("Product").collection(pro_type).document(productid).update(product);
                        } else {
                            Toast.makeText(getApplicationContext(),"Error in uploading images",Toast.LENGTH_SHORT).show();
                            pd.hide();
                            return;
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(upload3!=null){
            File pic3 = new File(upload3.getPath());
            try {
                image3 = new Compressor(this)
                        .setMaxHeight(720)
                        .setMaxWidth(720)
                        .setQuality(50)
                        .compressToBitmap(pic3);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image3.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();

                UploadTask filePath = storageref.child("Shopkeeper").child(pro_seller).child("Products").child(productid).child("photo3.jpg").putBytes(imageData);
                filePath.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            final String url3 = task.getResult().getDownloadUrl().toString();
                            Log.d("productid",url3);
                            product.put("photo3",url3);
                            firebaseFirestore.collection("Records").document("Shopkeeper").collection(pro_seller).document("Product").collection(pro_type).document(productid).update(product);
                        } else {
                            Toast.makeText(getApplicationContext(),"Error in uploading images",Toast.LENGTH_SHORT).show();
                            pd.hide();
                            return;
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(upload4!=null){
            File pic4 = new File(upload4.getPath());
            try {

                image4 = new Compressor(this)
                        .setMaxHeight(720)
                        .setMaxWidth(720)
                        .setQuality(50)
                        .compressToBitmap(pic4);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image4.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageData = baos.toByteArray();

                UploadTask filePath = storageref.child("Shopkeeper").child(pro_seller).child("Products").child(productid).child("photo4.jpg").putBytes(imageData);

                filePath.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            final String url4 = task.getResult().getDownloadUrl().toString();
                            Log.d("productid",url4);
                            product.put("photo4",url4);
                            firebaseFirestore.collection("Records").document("Shopkeeper").collection(pro_seller).document("Product").collection(pro_type).document(productid).update(product);
                        } else {
                            Toast.makeText(getApplicationContext(),"Error in uploading images",Toast.LENGTH_SHORT).show();
                            pd.hide();
                            return;
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        firebaseFirestore.collection("Records").document("Shopkeeper").collection(pro_seller).document("Product").collection(pro_type).document(productid).set(product);
        Toast.makeText(getApplicationContext(),"Prodcut added",Toast.LENGTH_SHORT).show();
        pd.hide();

        Intent i = new Intent(getApplicationContext(),Main_Activity.class);
        startActivity(i);
        finish();
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if(countbit==1){
                    upload1= result.getUri();
                }else if(countbit==2){
                    upload2=result.getUri();
                }else if(countbit==3){
                    upload3=result.getUri();
                }else{
                    upload4=result.getUri();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}

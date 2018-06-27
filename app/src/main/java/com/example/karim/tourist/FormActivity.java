package com.example.karim.tourist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FormActivity extends Activity implements View.OnClickListener {
    private static final int REQUEST_IMAGE_CAPTURE =1 ;
    private EditText title,des,price,address;
    Spinner city,cat;
     static ArrayList<Bitmap> images=new ArrayList<>();
    Dialog dialog ;
    Uri videoUri;
    Button imageButton,videoButton;
    ImageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        imageButton=findViewById(R.id.uploadImageButton);
        imageButton.setBackgroundColor(Color.GRAY);
        videoButton=findViewById(R.id.uploadVideoButton);
        videoButton.setBackgroundColor(Color.GRAY);
        title=findViewById(R.id.title);
        des=findViewById(R.id.description);
        city=findViewById(R.id.spinnercity);
        cat=findViewById(R.id.spinnercat);
        price=findViewById(R.id.price);
        address=findViewById(R.id.address);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("alex");
        categories.add("cairo");
        categories.add("giza");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        city.setAdapter(dataAdapter);
        List<String> categories2 = new ArrayList<String>();
        categories2.add("food");
        categories2.add("art");
        categories2.add("sport");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        cat.setAdapter(dataAdapter2);
    }

    public void UploadImages(View view) {
        createCustomDialog();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == RESULT_OK) {
             videoUri = data.getData();
             if(videoUri!=null) {
                 videoButton.setBackgroundColor(Color.GREEN);
             }
             else{
                 videoButton.setBackgroundColor(Color.GRAY);
             }
        }
       else  if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            images.add(imageBitmap);
        }
       else if ( resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Log.e("image",targetUri.toString());

            try {
                final InputStream imageStream = getContentResolver().openInputStream(targetUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                images.add(selectedImage);
                adapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    }
    private void createCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.addphotodialog, null);
        GridView gridView=view.findViewById(R.id.gridView);
        adapter=new ImageAdapter(getApplicationContext(),images);
        gridView.setAdapter(adapter);
        ImageView cameraButton=view.findViewById(R.id.CameraButton);
        ImageView gallery=view.findViewById(R.id.GalleryButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {dispatchTakePictureIntent();}});
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getphotoFromGallery();
            }
        });
        Button b=view.findViewById(R.id.closeDialog);

        builder.setView(view);
        dialog = builder.create();
        b.setOnClickListener(this);
        dialog.show();
    }
    public void getphotoFromGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(photoPickerIntent, 200);

    }
    public void uploadVideo(View view) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, 10);
        }
    }

    @Override
    public void onClick(View v) {
        dialog.dismiss();

        if(images.size()!=0){

            imageButton.setText("Upload Images ("+images.size()+")");
            imageButton.setBackgroundColor(Color.GREEN);
        }
        else{
            imageButton.setBackgroundColor(Color.GRAY);
            imageButton.setText("Upload Images");

        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    // upload to firebase
    public void submit(View view) {


      String key=uploadToRealtimeDatabase();
      UploadToStorage(key);
    }
    private String uploadToRealtimeDatabase() {
        String uid="K9igaf2jqFQ6F0mCgZt7hCGVyIE3";


        DatabaseReference database = FirebaseDatabase.getInstance().getReference("exp").child(city.getSelectedItem().toString()).child(cat.getSelectedItem().toString());
        String key=database.push().getKey();
        database=database.child(key);
        database.child("id").setValue(key);
         FirebaseDatabase.getInstance().getReference("users").child(uid).child("exp").push().setValue(key);

        database.child("userid").setValue(uid);

        database.child("title").setValue(title.getText().toString());
        database.child("des").setValue(des.getText().toString());
        database.child("price").setValue(Integer.parseInt( price.getText().toString()));
        database.child("address").setValue(address.getText().toString());
        return key;
    }
    private void UploadToStorage(String key) {
        StorageReference StorageRef= FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = StorageRef.child(key+".mp4");
        riversRef.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("vieo","done");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("vieo","fail");
                    }
                });
        for(int i=0;i<images.size();i++){
            StorageReference ref=StorageRef.child(key+i+".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            images.get(i).compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = ref.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.e("photos","error");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e("photos","done");
                }
            });
        }

    }


}

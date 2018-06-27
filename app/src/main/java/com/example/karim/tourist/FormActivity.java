package com.example.karim.tourist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

public class FormActivity extends Activity implements View.OnClickListener {
    private static final int REQUEST_IMAGE_CAPTURE =1 ;
    private EditText title,des,city,price,cat,address;
     static ArrayList<Bitmap> images=new ArrayList<>();
    Dialog dialog ;
    Uri videoUri;
    ImageAdapter adapter;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        title=findViewById(R.id.title);
        des=findViewById(R.id.description);
        city=findViewById(R.id.city);
        cat=findViewById(R.id.cat);
        price=findViewById(R.id.price);
        address=findViewById(R.id.address);
    }

    public void UploadImages(View view) {
        createCustomDialog();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == RESULT_OK) {
             videoUri = data.getData();
        }
       else  if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            images.add(imageBitmap);
        }
       else if ( resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Log.e("image",targetUri.toString());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), targetUri);
                images.add(bitmap);
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
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    // upload to firebase
    public void submit(View view) {
      String uid="K9igaf2jqFQ6F0mCgZt7hCGVyIE3";
      //check all filled first
      uploadToRealtimeDatabase();
      UploadToStorage();
    }
    private void uploadToRealtimeDatabase() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("exp").child(city.getText().toString()).child(cat.getText().toString());
        String key=database.push().getKey();
        database=database.child(key);
        database.child("title").setValue(title.getText().toString());
        database.child("des").setValue(des.getText().toString());
        database.child("price").setValue(Integer.parseInt( price.getText().toString()));
        database.child("address").setValue(Integer.parseInt( address.getText().toString()));
    }
    private void UploadToStorage() {
        StorageReference StorageRef= FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = StorageRef.child("key"+".mp4");
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

    }


}

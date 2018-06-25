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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class FormActivity extends Activity implements View.OnClickListener {
    private static final int REQUEST_IMAGE_CAPTURE =1 ;
    private EditText title,des;
     static ArrayList<Bitmap> images=new ArrayList<>();
    Dialog dialog ;
    ImageAdapter adapter;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        title=findViewById(R.id.title);
        des=findViewById(R.id.description);

    }

    public void UploadImages(View view) {
        createCustomDialog();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
        }
       else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            images.add((Bitmap) extras.get("data"));
            adapter.notifyDataSetChanged();
        }
        else if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();

            try {
                Bitmap b= BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                images.add(b);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
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

}

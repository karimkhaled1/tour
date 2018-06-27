package com.example.karim.tourist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

//get all data
// see the data's catiogry
//view them in hirizontal view
public class expeinces extends AppCompatActivity {
     ArrayList<String> DummyData= new ArrayList<>();
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expeinces);
        linearLayout=findViewById(R.id.Linearcat);
        getData();



    }

    private void getData() {
        // get all data
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("exp").child("alex").child("food");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<proxyItem> proxyItems =new ArrayList<>();
                ArrayList<String> keys =new ArrayList<>();
                 for(DataSnapshot snap:dataSnapshot.getChildren()){
                  proxyItems.add(snap.getValue(proxyItem.class));
                  keys.add(snap.getKey());

                     Log.e("print",snap.toString());
                 }
                showCatItems(proxyItems,database,keys,"food");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("blablabla", "Failed to read value.", error.toException());
            }
        });


        final DatabaseReference database2 = FirebaseDatabase.getInstance().getReference("exp").child("alex").child("art");
        database2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<proxyItem> proxyItems =new ArrayList<>();
                ArrayList<String> keys =new ArrayList<>();
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    proxyItems.add(snap.getValue(proxyItem.class));
                    keys.add(snap.getKey());

                    Log.e("print",snap.toString());
                }
                showCatItems(proxyItems,database2,keys,"art");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("blablabla", "Failed to read value.", error.toException());
            }
        });


        final DatabaseReference database3 = FirebaseDatabase.getInstance().getReference("exp").child("alex").child("sport");
        database3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<proxyItem> proxyItems =new ArrayList<>();
                ArrayList<String> keys =new ArrayList<>();
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    proxyItems.add(snap.getValue(proxyItem.class));
                    keys.add(snap.getKey());

                    Log.e("print",snap.toString());
                }
                showCatItems(proxyItems,database3,keys,"sport");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("blablabla", "Failed to read value.", error.toException());
            }
        });




        //loop in data
        //in each loop show its items

    }

    private void showCatItems(ArrayList<proxyItem> proxyItems , DatabaseReference myRef, final ArrayList<String> keys,String title) {
        CardView cardView=new CardView(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardView.setCardElevation(10.2f);cardView.setElevation(10.2f);
        }
        cardView.setContentPadding(0,0,0,10);
        cardView.setPreventCornerOverlap(true);
        LinearLayout linearLayout1=new LinearLayout(getApplicationContext());
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        TextView Title=new TextView(getApplicationContext());
        Title.setText(title);
        Title.setTypeface(null,Typeface.BOLD);
        Title.setTextSize(22);
        cardView.addView(linearLayout1);
        linearLayout1.addView(Title);


        HorizontalScrollView sv=new HorizontalScrollView(getApplicationContext());
        sv.setHorizontalScrollBarEnabled(false);

        sv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,260 ));
        final LinearLayout ls=new LinearLayout(getApplicationContext());
        ls.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));



for(int i=0;i<proxyItems.size();i++) {
    final LinearLayout eachItem=new LinearLayout(getApplicationContext());
    eachItem.setOrientation(LinearLayout.VERTICAL);
    eachItem.setLayoutParams(new LinearLayout.LayoutParams(200, 260));
    final ImageView imageView = new ImageView(getApplicationContext());
    StorageReference storageRef = FirebaseStorage.getInstance().getReference(keys.get(i)+"0.jpg");

    final long ONE_MEGABYTE = 1024 * 1024;
    storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
        @Override
        public void onSuccess(byte[] bytes) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            // Handle any errors
        }
    });

    imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
    TextView name = new TextView(getApplicationContext());
    name.setText(proxyItems.get(i).getTitle());
    name.setGravity(Gravity.CENTER);
    name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50));
    eachItem.addView(imageView);
    eachItem.addView(name);
    ls.addView(eachItem);


}
        linearLayout1.addView(sv);
        linearLayout.addView(cardView);
        sv.addView(ls);

        ViewGroup.MarginLayoutParams layoutParams2 =
                (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
        layoutParams2.setMargins(0, 0, 0, 9);
        cardView.requestLayout();
    }

    private static  class proxyItem {

        String title,des,address;
        int price;

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public proxyItem() {
        }


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }
}

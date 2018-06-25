package com.example.karim.tourist;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

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
        //loop in data
        //in each loop show its items
        showCatItems("art");
        showCatItems("food");
    }

    private void showCatItems(String cat) {
        CardView cardView=new CardView(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardView.setCardElevation(10.2f);cardView.setElevation(10.2f);
        }
        cardView.setContentPadding(0,0,0,10);
        cardView.setPreventCornerOverlap(true);
        LinearLayout linearLayout1=new LinearLayout(getApplicationContext());
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        TextView Title=new TextView(getApplicationContext());
        Title.setText(cat);
        Title.setTypeface(null,Typeface.BOLD);
        Title.setTextSize(22);
        cardView.addView(linearLayout1);
        linearLayout1.addView(Title);


        HorizontalScrollView sv=new HorizontalScrollView(getApplicationContext());
        sv.setHorizontalScrollBarEnabled(false);

        sv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,260 ));
        LinearLayout ls=new LinearLayout(getApplicationContext());
        ls.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

        LinearLayout eachItem=new LinearLayout(getApplicationContext());
        eachItem.setOrientation(LinearLayout.VERTICAL);
        eachItem.setLayoutParams(new LinearLayout.LayoutParams(200,260));
        ImageView imageView= new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.ronaldo);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(200,200));
        TextView name=new TextView(getApplicationContext());
        name.setText("new app");
        name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,50));
        eachItem.addView(imageView);
        eachItem.addView(name);




        ls.addView(eachItem);
        linearLayout1.addView(sv);
        linearLayout.addView(cardView);
        sv.addView(ls);
        ViewGroup.MarginLayoutParams layoutParams1 =
                (ViewGroup.MarginLayoutParams) eachItem.getLayoutParams();
        layoutParams1.setMargins(0, 0, 5, 0);
        eachItem.requestLayout();
        ViewGroup.MarginLayoutParams layoutParams2 =
                (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
        layoutParams2.setMargins(0, 0, 0, 9);
        cardView.requestLayout();
    }
}

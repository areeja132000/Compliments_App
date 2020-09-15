package com.example.compliments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class menu extends AppCompatActivity {


    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        dataBase db = new dataBase(menu.this);

        ArrayList<String> compliments = db.getNonBlockedCompliments(username);
        String compliment;
        if (compliments.size() > 0) {
            compliment = getRandomElement(compliments);
        } else {
            compliment = "";
        }


        TextView compliment_area= (TextView) findViewById(R.id.compliment);
        compliment_area.setText("Today's compliment:\n"+compliment);

        ImageButton give_comp = (ImageButton) findViewById(R.id.give_comp);
        give_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowComplimentsWindow(v, "give_compliment");
            }
        });

        ImageButton block_user = (ImageButton) findViewById(R.id.block);
        block_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowBlockWindow(v, "block");
            }
        });

        ImageButton facebook_but = (ImageButton) findViewById(R.id.facebook);
        facebook_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public String getRandomElement(ArrayList<String> array)
    {
        Random rand = new Random();
        return array.get(rand.nextInt(array.size()));
    }

    public void onButtonShowComplimentsWindow(View view, String which) {

        // inflate
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);

        final View popup = inflater.inflate(R.layout.popup_send_compliment, null);

        // make popup window
        int wid = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int hei = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        //If you tap outside, it vanishes
        boolean tap = true;
        final PopupWindow popWindow = new PopupWindow(popup, wid, hei, tap);

        // view popup window
        popWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        ImageButton exit = (ImageButton) popup.findViewById(R.id.send);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameInput = (EditText) popup.findViewById(R.id.username);
                String usernameTo = usernameInput.getText().toString();
                EditText compliment = (EditText) popup.findViewById(R.id.compliment);
                String complimentText = compliment.getText().toString();

                dataBase db = new dataBase(menu.this);
                db.insertCompliment(usernameTo, complimentText, username);
                popWindow.dismiss();
            }
        });

    }

    public void onButtonShowBlockWindow(View view, String which) {

        // inflate
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);

        final View popup = inflater.inflate(R.layout.popup_block, null);
        // make popup window
        int wid = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int hei = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        //If you tap outside, it vanishes
        boolean tap = true;
        final PopupWindow popWindow = new PopupWindow(popup, wid, hei, tap);

        // view popup window
        popWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        ImageButton exit = (ImageButton) popup.findViewById(R.id.block);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText blockInput = (EditText) popup.findViewById(R.id.username);
                String usernameBlock = blockInput.getText().toString();

                dataBase db = new dataBase(menu.this);
                db.insertBlock(username, usernameBlock);
                popWindow.dismiss();
            }
        });

    }
}

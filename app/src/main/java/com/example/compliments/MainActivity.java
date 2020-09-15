package com.example.compliments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText username = (EditText) findViewById(R.id.username);
                String usernameLogin = username.getText().toString();
                EditText password = (EditText) findViewById(R.id.password);
                String passwordLogin = password.getText().toString();

                dataBase db = new dataBase(MainActivity.this);
                if (db.check_login(usernameLogin, passwordLogin)) {
                    Intent intent = new Intent(MainActivity.this, menu.class);
                    intent.putExtra("username", usernameLogin);
                    startActivity(intent);
                    finish();
                } else {
                    failedLogin(v);
                }

            }
        });

        Button sign_up = (Button) findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonSignUpWindow(v);
            }
        });
    }

    public void onButtonSignUpWindow(View view) {

        // inflate
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popup = inflater.inflate(R.layout.sign_up, null);
        // make popup window
        int wid = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int hei = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        //If you tap outside, it vanishes
        boolean tap = true;
        final PopupWindow popWindow = new PopupWindow(popup, wid, hei, tap);

        // view popup window
        popWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button exit = (Button) popup.findViewById(R.id.sign_up);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBase db = new dataBase(MainActivity.this);
                EditText username = (EditText) popup.findViewById(R.id.username);
                String usernameLogin = username.getText().toString();
                EditText password = (EditText) popup.findViewById(R.id.password);
                String passwordLogin = password.getText().toString();
                db.insertAccount(usernameLogin, passwordLogin);
                db.insertCompliment(usernameLogin, "You might be the primary reason for global warming. Because you're so hot :)", usernameLogin);
                db.insertCompliment(usernameLogin, "Are you a beaver, because damn.", usernameLogin);
                db.insertCompliment(usernameLogin, "You are more unique and wonderful than the smell of a new book.", usernameLogin);
                db.insertCompliment(usernameLogin, "You’re more fun than bubble wrap :)", usernameLogin);
                popWindow.dismiss();
            }
        });

    }

    public void failedLogin(View view) {

        // inflate
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.login_failed, null);
        // make popup window
        int wid = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int hei = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        //If you tap outside, it vanishes
        boolean tap = true;
        final PopupWindow popWindow = new PopupWindow(popup, wid, hei, tap);

        // view popup window
        popWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        popup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popWindow.dismiss();
                return true;
            }
        });

    }
}
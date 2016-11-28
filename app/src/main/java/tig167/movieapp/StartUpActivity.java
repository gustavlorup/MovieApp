package tig167.movieapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by jnssonhugo on 2016-11-17.
 */


public class StartUpActivity extends AppCompatActivity {



    // Lägger till kommentar!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

    }

    // On Click för "Log in" knapp i StartUP
    public void promptLogIn(View v){
        Intent loginActivity = new Intent(this, LoginActivity.class);
        startActivity(loginActivity);
    }

    // On click för "Register" knapp i StartUP
    public void promptSignUp(View v){
        Intent registerActivity = new Intent(this, RegisterActivity.class);
        startActivity(registerActivity);
    }
}

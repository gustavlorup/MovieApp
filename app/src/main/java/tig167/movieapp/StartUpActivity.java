package tig167.movieapp;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

/**
 * Created by jnssonhugo on 2016-11-17.
 */


public class StartUpActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
    }

        /* Metoder som i xml-filens OnClick ska öppna (1) LoginActivity och (se nedan) */

        public void promptSignIn(View v){
            Intent loginActivity = new Intent(this, LoginActivity.class);
            startActivity(loginActivity);
        }

        /* (2) RegisterActivity */

        public void promptSignUp(View v){
            Intent registerActivity = new Intent(this, RegisterActivity.class);
            startActivity(registerActivity);
        }

    }



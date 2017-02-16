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
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class StartUpActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Vi vill se om användaren är inloggad för att automatisera inloggning om man aldrig
        // loggade ut

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Användare inloggad
                } else {
                    //Användare utloggad
                }

            }
        };

        /* Om användaren klickar på LogIn och man inte har loggat ut sen tidigare så loggas man in automatiskt utan
           att behöva ange användarinfo igen.
         */
        Button b1 = (Button) findViewById(R.id.openLogInButton);
        b1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(user!=null){
                    /* Om man aldrig loggade ut.. */
                    signInDirectly(view);
                }
                if(user==null){
                    /* Du är utloggad, snälla logga in! */
                    promptSignIn(view);
                }
            }
        });



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

        public void signInDirectly(View v){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

        }





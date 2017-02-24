package tig167.movieapp.granssnitt;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import tig167.movieapp.R;


public class RegisterActivity extends AppCompatActivity{


    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private String TAG = "";
    private AutoCompleteTextView mEmailField;
    private EditText mPasswordField;
    private EditText mSecondPasswordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //Vyer
        mEmailField = (AutoCompleteTextView) findViewById(R.id.mEmailField);
        mPasswordField = (EditText) findViewById(R.id.mPasswordField);
        mSecondPasswordField = (EditText) findViewById(R.id.mSecondPasswordField);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Användare inloggad
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // Användare utloggad
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }


    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) { /* Om fälten inte är ifyllde korrekt enlight validateForm() */
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        /* Om inloggning fallerar */

                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, R.string.register_auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        /* Här ska vi lägga in vad som händer när man loggas in (öppna MenuActivity */

                        if(task.isSuccessful()){
                            sayThankYou();
                            openApp();
                        }

                    }
                });
        /* #END createAccount() */
    }

    /* Kollar om fälten är korrekt ifyllda */

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required."); /* Ska bytas till en given String i String.xml */
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String firstPassword = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(firstPassword)) {
            mPasswordField.setError("Required."); /* Ska bytas till en given String i String.xml */
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String secondpassword = mSecondPasswordField.getText().toString();
        if (!secondpassword.equals(firstPassword)) {
            mPasswordField.setError("Passwords doesnt match"); /* Ska bytas till en given String i String.xml */
            mSecondPasswordField.setError("Passwords doesnt match"); /* Ska bytas till en given String i String.xml */
            valid = false;
        } else {
            mPasswordField.setError(null);
            mSecondPasswordField.setError(null);
        }


        CheckBox checkbox = (CheckBox) findViewById(R.id.termsBox);
        if(!checkbox.isChecked()){
            checkbox.setError("Required"); /* Ska bytas till en given String i String.xml */
            valid = false;
        }
        else{
            checkbox.setError(null);
        }

        return valid; /* Testet godkänns */
    }



    /*
     * Funktion för att öppna ett pop-up fönster i form av en AlertDialog som ska informera användaren om diverse villkor som gäller
     * för våran app
     */


    public void showTerms(View view) {
        final Dialog dialog = new Dialog(this);
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER);
        title.setAllCaps(true);
        title.setText("Terms and conditions");
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        myBuilder.setCustomTitle(title);
        myBuilder.setMessage(getString(R.string.termsText));
        myBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        }).create().show();
    }


    /* Likt showTerms() visar ett pop-up fönster som informerar användaren om att man blivit registrerad och att man nu blir inloggad */

    public void sayThankYou() {
        final Dialog dialog = new Dialog(this);
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER);
        title.setAllCaps(true);
        title.setText("Cheers mate!");
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        myBuilder.setCustomTitle(title);
        myBuilder.setMessage("Thank you for your registration! We'll now proceed to log-in.");
        myBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                finish();
            }
        }).create().show();
    }

    /* Funktion för att öppna MenuActivity */
    public void openApp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /* onStart och onStop funktioner för att starta och stoppa interaktionen med Firease */
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /* Funktion som i knappens OnClick (xml) används för att skapa användaren */

    public void promptRegister(View v) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }




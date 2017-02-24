package tig167.movieapp.granssnitt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import tig167.movieapp.R;

/**
 *
 LoginAktivitet som använder sig utav FireBase för att autensiera en redan registrerad anmäld.
 Ska öppna MenuActivity vid lyckad autensiering
 *
 */

public class LoginActivity extends AppCompatActivity  {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG ="";

    EditText mEmailField;
    EditText mPasswordField;

    Button SignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /* Hämtar signal från Firebase */
        mAuth = FirebaseAuth.getInstance();

        mEmailField = (EditText) findViewById(R.id.mEmail);
        mPasswordField = (EditText)findViewById(R.id.mPassword);
        SignInButton = (Button) findViewById(R.id.signInButton);
        SignInButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

    }

    /* För at logga in på appen (öppna MenuActivity) */

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) { /* Om fälten inte är ifyllde korrekt enlight validateForm() */
            return;
        }

        /* Här görs inloggning med eget email och lösenord */
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        /* Om inloggning fallerar */

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.login_auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        /* Här ska vi lägga in vad som händer när man loggas in (öppna MenuActivity */

                        if(task.isSuccessful()){
                            openApp();
                        }


                    }
                });
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

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required."); /* Ska bytas till en given String i String.xml */
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    public void forgotPassword(View v){
        Intent newIntent = new Intent(this, PasswordResetActivity.class);
        startActivity(newIntent);
    }

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

    public void openApp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
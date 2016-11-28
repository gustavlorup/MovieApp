package tig167.movieapp;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

/**
 * Created by jnssonhugo on 2016-11-17.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

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
        mEmailField = (AutoCompleteTextView) findViewById(R.id.mEmailView);
        mPasswordField = (EditText) findViewById(R.id.mPasswordView);
        mSecondPasswordField = (EditText) findViewById(R.id.mSecondPasswordView);

        //Registerknapp
        findViewById(R.id.registerButton).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

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

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        //Om sign in misslyckas så får användaren veta detta.

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        //Om sign in lyckas, säg tack för registration och visa inloggninsruta

                        if(task.isSuccessful()){
                            sayThankYou();
                        }
                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String firstPassword = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(firstPassword)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String secondpassword = mSecondPasswordField.getText().toString();
        if (!secondpassword.equals(firstPassword)) {
            mPasswordField.setError("Passwords doesnt match");
            mSecondPasswordField.setError("Passwords doesnt match");
            valid = false;
        } else {
            mPasswordField.setError(null);
            mSecondPasswordField.setError(null);
        }


        CheckBox checkbox = (CheckBox) findViewById(R.id.termsBox);
        if(!checkbox.isChecked()){
            checkbox.setError("Required");
            valid = false;
        }
        else{
            checkbox.setError(null);
        }

        return valid;
    }








    /*
        Metod för att visa villkor för vår app.

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


    /*
            Metod för att säga tack för registrering av användare. Kommer att öppna log-in ruta.

     */
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
                promptLogin();
                finish();
            }
        }).create().show();
    }


    /*

                Öppnar log in ruta

     */
    public void promptLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.registerButton) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());

        }
    }
}

       /* testText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();

            }

        });



    /*

    public void showTerms(View view) {
        TextView title = new TextView(this);
        Button button = new Button(this);
        button.setGravity(Gravity.CENTER);
        title.setText("Terms and conditions");
        title.setAllCaps(true);
        title.setTextSize(20);
        title.setGravity(Gravity.CENTER);
        final Dialog dialog = new Dialog(this);
        AlertDialog.Builder b1 = new AlertDialog.Builder(this);
                b1.setCustomTitle(title);
                b1.setMessage("teshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhheshhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhht");
                b1.setNeutralButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                }).create().show();


    }

    */




    /*

        public void showTerms(View v) {

            FrameLayout rootLayout = (FrameLayout) findViewById(android.R.id.content);
            View.inflate(this, R.layout.test, rootLayout);

                regButton.setVisibility(View.GONE);
        }

        public void dismissTerms(View v){
            FrameLayout rootLayout = (FrameLayout) findViewById(android.R.id.content);
            View.inflate(this, R.layout.activity_register, rootLayout);


        }

        */



















        /* Fungerar för att få upp pop-up



        final Button registerButton = (Button) findViewById(R.id.button5);
        final ColorDrawable dwDim = new ColorDrawable(800000000);
        final ColorDrawable dwNorm = new ColorDrawable(800000000);
        dwDim.setAlpha(50);
        dwNorm.setAlpha(255);

        linearLayout = (LinearLayout) findViewById(R.id.registeractivity);
        test = (TextView) findViewById(R.id.termsView);

        Button confirmButton = (Button) findViewById(R.id.confirmButton);

        layoutInFlater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) layoutInFlater.inflate(R.layout.test, null);
        popupWindow = new PopupWindow(container, 1000, 1500, true);
        popupWindow.setBackgroundDrawable(null);


        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerButton.setVisibility(View.GONE);
                linearLayout.setBackgroundDrawable(dwDim);
                popupWindow.showAtLocation(linearLayout, Gravity.CENTER, 0, 0);


            }

        });

        */



















    //private String m_Text ="By accepting the terms... hhhhhhhhhhhhhhhhhh" +
      //      "gggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg" +
        //    "";

    /*

            Metod för att visa villkor i en skrollbar pop-up ruta


        public void showTerms(View view){
            // Skapar Textview utifrån ID:termsView i customDialog.xml
            final TextView termsOnClick = (TextView) findViewById(R.id.termsView);

            termsOnClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContentView(R.layout.customdialog);



                }


            }); */













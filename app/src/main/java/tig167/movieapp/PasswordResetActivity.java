package tig167.movieapp;

//Syftet med denna är endast att kunna erbjuda byte av lösenord!!

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    String yourEmail;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        Button resetButton = (Button) findViewById(R.id.resetButton);
        email = (EditText)findViewById(R.id.emailAdress);

        resetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(validateForm()==true){
                auth.sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public static final String TAG = "";

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    yourEmail = email.getText().toString();
                                    showTerms();
                                }
                            }
                        });
            }
            }
        });


    }

    public void showTerms() {
        final Dialog dialog = new Dialog(this);
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER);
        title.setAllCaps(true);
        title.setText("Password reset");
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        myBuilder.setCustomTitle(title);
        myBuilder.setMessage("An email has been sent to "+yourEmail+". Please proceed to your email and you will be able to reset your password");
        myBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                finish();
            }
        }).create().show();
    }

    public boolean validateForm() {
        boolean valid = false;
        String yourEmail = email.getText().toString();
        if(TextUtils.isEmpty(yourEmail)){
            email.setError("Required.");
            valid = false;
        }
        if(!TextUtils.isEmpty(yourEmail)){
            valid = true;
        }
        return valid;
    }

}

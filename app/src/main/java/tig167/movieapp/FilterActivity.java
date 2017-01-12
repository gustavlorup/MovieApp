package tig167.movieapp;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.youtube.player.internal.v;
import static tig167.movieapp.R.id.ratingBar;
import static tig167.movieapp.R.id.ratingView;

public class FilterActivity extends AppCompatActivity implements View.OnTouchListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        /* Deklarera filterknappar */
        ImageButton btn1 = (ImageButton)findViewById(R.id.criminalbutton);
        ImageButton btn2 = (ImageButton)findViewById(R.id.fantasybutton);
        ImageButton btn3 = (ImageButton)findViewById(R.id.dramabutton);
        ImageButton btn4 = (ImageButton)findViewById(R.id.romancebutton);
        ImageButton btn5 = (ImageButton)findViewById(R.id.adventurebutton);
        ImageButton btn6 = (ImageButton)findViewById(R.id.comedybutton);
        ImageButton btn7 = (ImageButton)findViewById(R.id.documentarybutton);
        ImageButton btn8 = (ImageButton)findViewById(R.id.westernbutton);

        final RatingBar bar = (RatingBar) findViewById(ratingBar);
        final TextView ratingView = (TextView)findViewById(R.id.ratingView);

        ratingView.setText("At least: " + (String.format("%2.1f", bar.getRating()*2)));

          /* Lägg på en OnTouchListener på knapparna */
        btn1.setOnTouchListener(this);
        btn2.setOnTouchListener(this);
        btn3.setOnTouchListener(this);
        btn4.setOnTouchListener(this);
        btn5.setOnTouchListener(this);
        btn6.setOnTouchListener(this);
        btn7.setOnTouchListener(this);
        btn8.setOnTouchListener(this);

        Spinner spinner = (Spinner)findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.year_array,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating  = rating*2;

                ratingView.setText("At least: " + String.format("%2.1f", rating));
            }
        });


        //btn1.setTag("pressed");
        /*btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    btn1.setPressed(!btn1.isPressed());
                }
                return true;
            }
        });
*/



    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            switch(v.getId()){
                case R.id.criminalbutton:
                    v.setPressed(!v.isPressed());
                    break;
                case R.id.fantasybutton:
                    v.setPressed(!v.isPressed());
                    break;
                case R.id.romancebutton:
                    v.setPressed(!v.isPressed());
                    break;
                case R.id.dramabutton:
                    v.setPressed(!v.isPressed());
                    break;
                case R.id.adventurebutton:
                    v.setPressed(!v.isPressed());
                    break;
                case R.id.comedybutton:
                    v.setPressed(!v.isPressed());
                    break;
                case R.id.documentarybutton:
                    v.setPressed(!v.isPressed());
                    break;
                case R.id.westernbutton:
                    v.setPressed(!v.isPressed());
                    break;
                default:
                    break;
            }
        }
        return true;
    }
}





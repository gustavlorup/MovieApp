package tig167.movieapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
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

import java.io.IOException;

import static tig167.movieapp.R.id.ratingBar;
import static tig167.movieapp.R.id.ratingView;

public class FilterActivity extends AppCompatActivity implements View.OnTouchListener {
    ImageButton genres[] = new ImageButton[8];
    TextView warningView;
    DBHelper myDbHelper;
    String videoUrl;
    String title;
    String rating;
    String plot;
    String year;
    String genre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


        Button applyFilter = (Button) findViewById(R.id.applyFilterOnMovie);

        /* Deklarera filterknappar */
        genres[0] = (ImageButton) findViewById(R.id.criminalbutton);
        genres[0].setTag("criminal");
        genres[1] = (ImageButton) findViewById(R.id.fantasybutton);
        genres[1].setTag("fantasy");
        genres[2] = (ImageButton) findViewById(R.id.dramabutton);
        genres[2].setTag("drama");
        genres[3] = (ImageButton) findViewById(R.id.romancebutton);
        genres[3].setTag("romance");
        genres[4] = (ImageButton) findViewById(R.id.adventurebutton);
        genres[4].setTag("adventure");
        genres[5] = (ImageButton) findViewById(R.id.comedybutton);
        genres[5].setTag("comedy");
        genres[6] = (ImageButton) findViewById(R.id.documentarybutton);
        genres[6].setTag("documentary");
        genres[7] = (ImageButton) findViewById(R.id.westernbutton);
        genres[7].setTag("western");


        final RatingBar bar = (RatingBar) findViewById(ratingBar);
        final TextView ratingView = (TextView) findViewById(R.id.ratingView);
        warningView = (TextView) findViewById(R.id.warningText);
        ratingView.setText("At least: " + (String.format("%2.1f", bar.getRating() * 2)));

          /* Lägg på en OnTouchListener på knapparna */
        genres[0].setOnTouchListener(this);
        genres[1].setOnTouchListener(this);
        genres[2].setOnTouchListener(this);
        genres[3].setOnTouchListener(this);
        genres[4].setOnTouchListener(this);
        genres[5].setOnTouchListener(this);
        genres[6].setOnTouchListener(this);
        genres[7].setOnTouchListener(this);

        bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating = rating * 2;

                ratingView.setText("At least: " + String.format("%2.1f", rating));
            }
        });


        Spinner s1 = (Spinner) findViewById(R.id.year_from);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.year_from, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner s2 = (Spinner) findViewById(R.id.year_to);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.year_to, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s2.setAdapter(adapter2);
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        myDbHelper = new DBHelper(this);

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }

        Movie a = myDbHelper.getRandomMovie();
        videoUrl = a.getUrl();
        title = a.getTitle();
        rating = (Double.toString(a.getRating()));
        plot = a.getDesc();
        year = (Integer.toString(a.getYear()));
        genre = ("Inte än implementerat");

        System.out.println(title);

    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.criminalbutton:
                    if (validateGenre()) {
                        v.setPressed(!v.isPressed());
                    }
                    if (!validateGenre()) {

                        v.setPressed(!v.isPressed());
                    }
                    break;
                case R.id.fantasybutton:
                    if (validateGenre()) {
                        v.setPressed(!v.isPressed());
                    }
                    if (!validateGenre()) {

                        v.setPressed(!v.isPressed());
                    }
                    break;
                case R.id.romancebutton:
                    if (validateGenre()) {
                        v.setPressed(!v.isPressed());
                    }
                    if (!validateGenre()) {

                        v.setPressed(!v.isPressed());
                    }
                    break;
                case R.id.dramabutton:
                    if (validateGenre()) {
                        v.setPressed(!v.isPressed());
                    }
                    if (!validateGenre()) {

                        v.setPressed(!v.isPressed());
                    }
                    break;
                case R.id.adventurebutton:
                    if (validateGenre()) {
                        v.setPressed(!v.isPressed());
                    }
                    if (!validateGenre()) {

                        v.setPressed(!v.isPressed());
                    }
                    break;
                case R.id.comedybutton:
                    if (validateGenre()) {
                        v.setPressed(!v.isPressed());
                    }
                    if (!validateGenre()) {

                        v.setPressed(!v.isPressed());
                    }
                    break;
                case R.id.documentarybutton:
                    if (validateGenre()) {
                        v.setPressed(!v.isPressed());
                    }
                    if (!validateGenre()) {
                        v.setPressed(!v.isPressed());
                    }
                    break;
                case R.id.westernbutton:
                    if (validateGenre()) {
                        v.setPressed(!v.isPressed());
                    }
                    if (!validateGenre()) {
                        v.setPressed(!v.isPressed());
                    }
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    public boolean validateGenre() {
        boolean valid = true;
        int nr = 0;

        for (ImageButton genre1 : genres) {
            if (genre1.isPressed()) {
                nr++;
                if (nr > 3) {
                    valid = false;
                    warningView.setText("Max 3 genres!");
                } else {
                    warningView.setText(null);
                    valid = true;
                }
            }
        }

        return valid;
    }

    public void sendMovie(View v) {
        Intent movieIntent = new Intent(FilterActivity.this, MainActivity.class);
        movieIntent.putExtra("url", videoUrl);
        movieIntent.putExtra("title", title);
        movieIntent.putExtra("rating", rating);
        movieIntent.putExtra("plot", plot);
        movieIntent.putExtra("year", year);
        movieIntent.putExtra("genre", "Inte än implementerat");
        setResult(RESULT_OK, movieIntent);
        finish();
    }

    public String getGenre(View v) {
        String searchGenre = "";
        int nr = 0;
        for (int i = 0; i < genres.length; i++) {
            if (validateGenre() && genres[i].isPressed() && nr==0) {
                searchGenre = (String) genres[i].getTag();
                nr++;
            }
            else if(validateGenre() && genres[i].isPressed() && nr>0) {
                searchGenre = searchGenre + "," + genres[i].getTag();
            }
        }
        System.out.println(searchGenre);
        return searchGenre;
    }
}





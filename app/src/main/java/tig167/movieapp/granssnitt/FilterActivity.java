package tig167.movieapp.granssnitt;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import tig167.movieapp.logik.DBHelper;
import tig167.movieapp.logik.Movie;
import tig167.movieapp.R;

import static tig167.movieapp.R.id.ratingBar;

public class FilterActivity extends AppCompatActivity implements View.OnTouchListener {
    ImageButton genres[] = new ImageButton[8]; // Genreknapperna
    TextView warningView;
    TextView defaultGenreView;
    DBHelper myDbHelper;
    TextView ratingView;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapter2;
    Spinner s1;
    Spinner s2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        /* Deklarerar filterknappar */
        genres[0] = (ImageButton) findViewById(R.id.actionbutton);
        genres[0].setTag("1");
        genres[1] = (ImageButton) findViewById(R.id.fantasybutton);
        genres[1].setTag("10");
        genres[2] = (ImageButton) findViewById(R.id.dramabutton);
        genres[2].setTag("8");
        genres[3] = (ImageButton) findViewById(R.id.romancebutton);
        genres[3].setTag("17");
        genres[4] = (ImageButton) findViewById(R.id.adventurebutton);
        genres[4].setTag("2");
        genres[5] = (ImageButton) findViewById(R.id.comedybutton);
        genres[5].setTag("5");
        genres[6] = (ImageButton) findViewById(R.id.documentarybutton);
        genres[6].setTag("7");
        genres[7] = (ImageButton) findViewById(R.id.westernbutton);
        genres[7].setTag("22");


        final RatingBar bar = (RatingBar) findViewById(ratingBar);
        ratingView = (TextView) findViewById(R.id.ratingView);
        warningView = (TextView) findViewById(R.id.warningText);
        defaultGenreView = (TextView)findViewById(R.id.defaultview);

        ratingView.setText(String.format("%2.1f", bar.getRating() * 2).replace(",", "."));

          /* Lägger på en OnTouchListener på knapparna */
        genres[0].setOnTouchListener(this);
        genres[1].setOnTouchListener(this);
        genres[2].setOnTouchListener(this);
        genres[3].setOnTouchListener(this);
        genres[4].setOnTouchListener(this);
        genres[5].setOnTouchListener(this);
        genres[6].setOnTouchListener(this);
        genres[7].setOnTouchListener(this);


        /* Visar ratingen och dubblar denna då värdet endast går från 1-5. */

        bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating = rating * 2;
                ratingView.setText((String.format("%2.1f", rating).replace(",", ".")));
            }
        });




        s1 = (Spinner) findViewById(R.id.year_from);

        adapter = ArrayAdapter.createFromResource(this,
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

        s2 = (Spinner) findViewById(R.id.year_to);

        adapter2 = ArrayAdapter.createFromResource(this,
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

    }

    /* Hantering av genre-knapparna. I XML är det två olika genre-bilder beroende på vilken state den har.
       Den tittar först om validateGenres är false eller true och gör beroende på detta bilderna itryckta eller tvärtom
     */

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.actionbutton:
                    if (validateGenre()) {
                        v.setSelected(!v.isSelected());
                        updateGenres(genres[0].getTag());
                    }
                    if (!validateGenre()) {
                        v.setSelected(!v.isSelected());
                        updateGenres(genres[0].getTag());
                    }
                    break;
                case R.id.fantasybutton:
                    if (validateGenre()) {
                        v.setSelected(!v.isSelected());
                        updateGenres(genres[1].getTag());
                    }
                    if (!validateGenre()) {

                        v.setSelected(!v.isSelected());
                        updateGenres(genres[1].getTag());
                    }
                    break;
                case R.id.romancebutton:
                    if (validateGenre()) {
                        v.setSelected(!v.isSelected());
                        updateGenres(genres[2].getTag());
                    }
                    if (!validateGenre()) {

                        v.setSelected(!v.isSelected());
                        updateGenres(genres[2].getTag());
                    }
                    break;
                case R.id.dramabutton:
                    if (validateGenre()) {
                        v.setSelected(!v.isSelected());
                        updateGenres(genres[3].getTag());
                    }
                    if (!validateGenre()) {

                        v.setSelected(!v.isSelected());
                        updateGenres(genres[3].getTag());
                    }
                    break;
                case R.id.adventurebutton:
                    if (validateGenre()) {
                        v.setSelected(!v.isSelected());
                        updateGenres(genres[4].getTag());
                    }
                    if (!validateGenre()) {

                        v.setSelected(!v.isSelected());
                        updateGenres(genres[4].getTag());
                    }
                    break;
                case R.id.comedybutton:
                    if (validateGenre()) {
                        v.setSelected(!v.isSelected());
                        updateGenres(genres[5].getTag());
                    }
                    if (!validateGenre()) {

                        v.setSelected(!v.isSelected());
                        updateGenres(genres[5].getTag());
                    }
                    break;
                case R.id.documentarybutton:
                    if (validateGenre()) {
                        v.setSelected(!v.isSelected());
                        updateGenres(genres[6].getTag());
                    }
                    if (!validateGenre()) {
                        v.setSelected(!v.isSelected());
                        updateGenres(genres[6].getTag());
                    }
                    break;
                case R.id.westernbutton:
                    if (validateGenre()) {
                        v.setSelected(!v.isSelected());
                        updateGenres(genres[7].getTag());
                    }
                    if (!validateGenre()) {
                        v.setSelected(!v.isSelected());
                        updateGenres(genres[7].getTag());
                    }
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    /* Säkerställer att användaren endast klickar i max tre st genres som sök-kriterie */
    public boolean validateGenre() {
        boolean valid = true;
        int nr = 0;

        for (ImageButton genre1 : genres) {
            if (genre1.isSelected()) {
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

    /* Här skapas en film som skickas över till MainActivity med tag: "RESULT_OK".
       Den tittar hur många genres som är itrycka och gör anrop till DBHelper
       beroende på hur många som är itryckta
     */

    public void sendMovie(View v) {
        Intent movieIntent = new Intent();
        ArrayList<String> searchGenres = getGenres(v);
        if(searchGenres.size()==3){
            Movie a = myDbHelper.getFilteredMovie(searchGenres.get(0), searchGenres.get(1), searchGenres.get(2), ratingView.getText().toString(),
                    s1.getSelectedItem().toString(), s2.getSelectedItem().toString());

            if(!DBHelper.foundMovie)
            {
                movieError();
            }
            if(DBHelper.foundMovie) {
                movieIntent.putExtra("url", a.getUrl());
                movieIntent.putExtra("title", a.getTitle());
                movieIntent.putExtra("rating", (String.valueOf(a.getRating())));
                movieIntent.putExtra("plot", a.getDesc());
                movieIntent.putExtra("year", (String.valueOf(a.getYear())));
                movieIntent.putExtra("genre", a.getGenres());
                setResult(RESULT_OK, movieIntent);

                finish();
            }
        }

        else if(searchGenres.size()==2){
            Movie a = myDbHelper.getFilteredMovie(searchGenres.get(0), searchGenres.get(1), null, ratingView.getText().toString(),
                    s1.getSelectedItem().toString(), s2.getSelectedItem().toString());


            if(!DBHelper.foundMovie)
            {
                movieError();
            }
            if(DBHelper.foundMovie) {
                movieIntent.putExtra("url", a.getUrl());
                movieIntent.putExtra("title", a.getTitle());
                movieIntent.putExtra("rating", (String.valueOf(a.getRating())));
                movieIntent.putExtra("plot", a.getDesc());
                movieIntent.putExtra("year", (String.valueOf(a.getYear())));
                movieIntent.putExtra("genre", a.getGenres());
                setResult(RESULT_OK, movieIntent);

                finish();
            }
        }

        else if(searchGenres.size()==1){
                Movie a = myDbHelper.getFilteredMovie(searchGenres.get(0), null, null, ratingView.getText().toString(),
                        s1.getSelectedItem().toString(), s2.getSelectedItem().toString());

            if(!DBHelper.foundMovie)
            {
                movieError();
            }

            if(DBHelper.foundMovie){
                movieIntent.putExtra("url", a.getUrl());
                movieIntent.putExtra("title", a.getTitle());
                movieIntent.putExtra("rating", (String.valueOf(a.getRating())));
                movieIntent.putExtra("plot", a.getDesc());
                movieIntent.putExtra("year", (String.valueOf(a.getYear())));
                movieIntent.putExtra("genre", a.getGenres());
                setResult(RESULT_OK, movieIntent);
                finish();
            }

        }


        else if(searchGenres.size()==0){
            Movie a = myDbHelper.getFilteredMovie(null, null, null, ratingView.getText().toString(),
                    s1.getSelectedItem().toString(), s2.getSelectedItem().toString());
            if(!DBHelper.foundMovie)
            {
                movieError();
            }
            if(DBHelper.foundMovie) {
                movieIntent.putExtra("url", a.getUrl());
                movieIntent.putExtra("title", a.getTitle());
                movieIntent.putExtra("rating", (String.valueOf(a.getRating())));
                movieIntent.putExtra("plot", a.getDesc());
                movieIntent.putExtra("year", (String.valueOf(a.getYear())));
                movieIntent.putExtra("genre", a.getGenres());
                setResult(RESULT_OK, movieIntent);
                finish();
            }
        }

    }

    /* Funktion som hämtar de genres som är iklickade som sen ska användas för att bedöma vilken SQL-statement som ska göras */
    public ArrayList<String> getGenres(View v) {
        ArrayList<String> searchGenre = new ArrayList<>();
        for (ImageButton genre1 : genres) {
            if (validateGenre() && genre1.isSelected()) {
                searchGenre.add(genre1.getTag().toString());
            }
        }
        return searchGenre;
    }

    /* Preliminär lista som visar användaren vilka kriterier man valt
     *
     */
    public void updateGenres(Object b) {
        String total = "";
        String current = defaultGenreView.getText().toString();

        if (b == "1" && !current.contains("Action")) {
            total = defaultGenreView.getText().toString() + "Action,";
            defaultGenreView.setText(total);
        }

        if (b == "1" && current.contains("Action")) {
            current = current.replace("Action,", "");
            defaultGenreView.setText(current);
        }

        if (b == "10" && !current.contains("Fantasy")) {
            total = defaultGenreView.getText().toString() + "Fantasy,";
            defaultGenreView.setText(total);
        }

        if (b == "10" && current.contains("Fantasy")) {
            current = current.replace("Fantasy,", "");
            defaultGenreView.setText(current);
        }
        if (b == "8" && !current.contains("Romance")) {
            total = defaultGenreView.getText().toString() + "Romance,";
            defaultGenreView.setText(total);
        }
        if (b == "8" && current.contains("Romance")) {
            current = current.replace("Romance,", "");
            defaultGenreView.setText(current);
        }
        if (b == "17" && !current.contains("Drama")) {
            total = defaultGenreView.getText().toString() + "Drama,";
            defaultGenreView.setText(total);
        }
        if (b == "17" && current.contains("Drama")) {
            current = current.replace("Drama,", "");
            defaultGenreView.setText(current);
        }
        if (b == "2" && !current.contains("Adventure")) {
            total = defaultGenreView.getText().toString() + "Adventure,";
            defaultGenreView.setText(total);
        }
        if (b == "2" && current.contains("Adventure")) {
            current = current.replace("Adventure,", "");
            defaultGenreView.setText(current);
        }
        if (b == "5" && !current.contains("Comedy")) {
            total = defaultGenreView.getText().toString() + "Comedy,";
            defaultGenreView.setText(total);
        }
        if (b == "5" && current.contains("Comedy")) {
            current = current.replace("Comedy,", "");
            defaultGenreView.setText(current);
        }
        if (b == "7" && !current.contains("Documentary")) {
            total = defaultGenreView.getText().toString() + "Documentary,";
            defaultGenreView.setText(total);
        }
        if (b == "7" && current.contains("Documentary")) {
            current = current.replace("Documentary,", "");
            defaultGenreView.setText(current);
        }
        if (b == "22" && !current.contains("Western")) {
            total = defaultGenreView.getText().toString() + "Western,";
            defaultGenreView.setText(total);
        }
        if (b == "22" && current.contains("Western")) {
            current = current.replace("Western,", "");
            defaultGenreView.setText(current);
        }
    }

    public void movieError() {
        final Dialog dialog = new Dialog(this);
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER);
        title.setAllCaps(true);
        title.setText("Whops!");
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        myBuilder.setCustomTitle(title);
        myBuilder.setMessage("We did not find a movie according to your specifications.");
        myBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        }).create().show();
    }


}





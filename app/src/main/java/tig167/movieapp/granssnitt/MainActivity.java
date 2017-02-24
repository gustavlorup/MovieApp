package tig167.movieapp.granssnitt;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.IOException;

import tig167.movieapp.logik.DBHelper;
import tig167.movieapp.logik.Movie;
import tig167.movieapp.R;

/* Vår MainAktivitet. Det är här de slumpade filmerna visas och det är härifrån man navigerar sig */


public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    private String API_KEY = "AIzaSyAS-LOzdzRz1ni16kvTKrPU_60HQd_IYeo";
    public String url;
    private static final int RECOVERY_REQUEST = 1;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    TextView titleView;
    TextView ratingView;
    TextView plotView;
    TextView genreView;
    TextView yearView;
    DBHelper myDbHelper;
    public YouTubePlayer YPlayer;
    public YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);
        youTubePlayerView.initialize(API_KEY, this);


        titleView = (TextView) findViewById(R.id.titleView);
        ratingView = (TextView)findViewById(R.id.ratingView);
        plotView = (TextView)findViewById(R.id.plotView);
        genreView = (TextView)findViewById(R.id.genreView);
        yearView = (TextView)findViewById(R.id.yearView);

        Button filterButton = (Button) findViewById(R.id.newFilterButton);
        filterButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openFilter();
            }
        });

        Button newMovie = (Button) findViewById(R.id.newMoviePlease);
        newMovie.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                newRandomMovie(view);

            }
        });
        url = "R2GKVtWsXKY";

        /* Skapar en instans av vår databashelper och använder den för att hämta en slumpvald film redan vid start av appen */

        myDbHelper = new DBHelper(this);

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        }catch(SQLException sqle){

            throw sqle;

        }


        Movie a = myDbHelper.getRandomMovie();
        url = a.getUrl();
        titleView.setText(a.getTitle());
        ratingView.setText(Double.toString(a.getRating()));
        plotView.setText(a.getDesc());
        yearView.setText(Integer.toString(a.getYear()));
        genreView.setText(a.getGenres());

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Användare inloggad
                } else {
                    //Användare utloggad
                }

            }
        };

        Button b1 = (Button) findViewById(R.id.signOutButton);
        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                logOutQuestion();
            }

        });

        System.out.println(a.getTitle());


    } /* Slut på onCreate */


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            // För att kunna styra vår player
            YPlayer = player;
            player.loadVideo(url);

            // Vi vill inte att den ska spelas automatiskt, därav pause();
            player.pause();
        }
        player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
    }


    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format("error", errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    public void openFilter(){
        Intent askIntent = new Intent(this, FilterActivity.class);
        startActivityForResult(askIntent, 1);
}

    /* Används i OnClick på XML. Skapar en helt ny random film, endast! */
    public void newRandomMovie(View v){
        Movie a = myDbHelper.getRandomMovie();
        url = a.getUrl();
        titleView.setText(a.getTitle());
        ratingView.setText(Double.toString(a.getRating()));
        plotView.setText(a.getDesc());
        yearView.setText(Integer.toString(a.getYear()));
        genreView.setText(a.getGenres());
        changeVideo(url);
    }

    /* Loggar ut och tar dig till början av appen */
    public void signOut(){
        mAuth.signOut();
        Intent intent = new Intent(this, StartUpActivity.class);
        startActivity(intent);
        this.finish();
    }

    /* En fråga om man verkligen vill logga ut */
    public void logOutQuestion() {
        final Dialog dialog = new Dialog(this);
        TextView title = new TextView(this);
        title.setGravity(Gravity.CENTER);
        title.setAllCaps(true);
        title.setText("Signing out!");
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        myBuilder.setCustomTitle(title);
        myBuilder.setMessage("Are you sure you want to log out?");
        myBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                finish();
                signOut();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                dialog.dismiss();
            }
        }).create().show();

    }

    /* Hämtar data från FilterActivity, i detta fallet filmens alla Strängar */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data){
        if(resultCode == RESULT_OK){
            titleView.setText(data.getStringExtra("title"));
            ratingView.setText(data.getStringExtra("rating"));
            plotView.setText(data.getStringExtra("plot"));
            yearView.setText(data.getStringExtra("year"));
            genreView.setText(data.getStringExtra("genre"));
            url = data.getStringExtra("url");
            YPlayer.loadVideo(url);
        }


    }

    public void changeVideo(String s){
        YPlayer.loadVideo(s);
        YPlayer.pause();

    }
}






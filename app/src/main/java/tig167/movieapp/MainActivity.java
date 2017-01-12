package tig167.movieapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;


public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    private String API_KEY = "AIzaSyAS-LOzdzRz1ni16kvTKrPU_60HQd_IYeo";
    public static String videoUrl;
    public static String movieTitle;
    public static int year;
    public static int movieId;
    public static double rating;
    public static String desc;
    public static String url;

    YouTubePlayer player;

    private static final int RECOVERY_REQUEST = 1;

    TextView titleView;
    TextView ratingView;
    TextView plotView;
    TextView genreView;
    TextView yearView;
    DBHelper myDbHelper;

    YouTubePlayerView youTubePlayerView;
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
        videoUrl = a.getUrl();
        titleView.setText(a.getTitle());
        ratingView.setText(Double.toString(a.getRating()));
        plotView.setText(a.getDesc());
        yearView.setText(Integer.toString(a.getYear()));
        genreView.setText("Inte än implementerat");


    } /* Slut på onCreate */

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(videoUrl);
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        }
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
        Intent intent = new Intent(this, FilterActivity.class);
        startActivity(intent);
    }

    public void newRandomMovie(View v){
        Movie a = myDbHelper.getRandomMovie();
        videoUrl = a.getUrl();
        titleView.setText(a.getTitle());
        ratingView.setText(Double.toString(a.getRating()));
        plotView.setText(a.getDesc());
        yearView.setText(Integer.toString(a.getYear()));
        genreView.setText("Inte än implementerat");
    }



}



package tig167.movieapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by GustavL on 2016-12-05.
 */

 public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "";
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/tig167.movieapp/databases/";

    private static String DB_NAME = "movies.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DBHelper(Context context) {

        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            System.out.println("Database exists");
            this.getWritableDatabase();
            //do nothing - database already exist
        }
        dbExist = checkDataBase();

        if (!dbExist){

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

        try {

            copyDataBase();

        } catch (IOException e) {

            throw new Error("Error copying database");

        }
    }

}



    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException{

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    public Movie getRandomMovie() {

        String query = "SELECT movies.*, genre.moviegenre" +
                " FROM movies INNER JOIN (genre INNER JOIN movies_genre ON genre._id = movies_genre.idgenre) ON movies._id = movies_genre.idmovies" +
                " ORDER BY RANDOM()" +
                " LIMIT 1;";
        Cursor c = myDataBase.rawQuery(query, null);
        c.moveToFirst();
        String id = c.getString(c.getColumnIndex("_id"));
        String title = c.getString(c.getColumnIndex("title"));
        String year = c.getString(c.getColumnIndex("year"));
        String rating = c.getString(c.getColumnIndex("movieRating"));
        String desc = c.getString(c.getColumnIndex("movieDesc"));
        String url = c.getString(c.getColumnIndex("movieURL"));
        c.close ();

        /* String query2 = "SELECT moviegenre from GENRE where _ID =" + id;
        Cursor c2 = myDataBase.rawQuery(query2, null);
        String genre = c2.getString(c.getColumnIndex("moviegenre"));
        System.out.println(genre);

*/
        Movie randomMovie = new Movie(Integer.parseInt(id), title, Integer.parseInt(year), Double.parseDouble(rating), desc, url);
        return randomMovie;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       if(newVersion>oldVersion){
           System.out.println("Database version..");
           myContext.deleteDatabase(DB_NAME);
       }


    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

}

    /*
    private static final int DATABASE_VERSION = 1;
    private static String TAG ="DBHelper";
    private static final String DB_NAME = "movies";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    private static final String TABLE_MOVIES = "movies";

    private static final String KEY_ID = "idmovies";
    private static final String KEY_TITLE ="title";
    private static final String KEY_YEAR = "year";
    private static final String KEY_RATING = "movieRating";
    private static final String KEY_DESC = "movieDesc";
    private static final String KEY_URL ="movieURL";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }


    public Movie getMovie(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        /* Cursor cursor = db.query(TABLE_MOVIES, new String[] { KEY_ID,
                        KEY_TITLE, KEY_YEAR, KEY_RATING, KEY_DESC, KEY_URL }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);



        String sql = "SELECT movies.title " + "FROM movies INNER JOIN (genre INNER JOIN movies_genre ON genre.genreID = movies_genre.idgenre) ON movies.idmovies = movies_genre.idmovies " +
                "ORDER BY RANDOM() " + "LIMIT 1;";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null)
            cursor.moveToFirst();
        Movie randomMovie = new Movie(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getInt(2), cursor.getDouble(3), cursor.getString(4), cursor.getString(5));
// return shop
        return randomMovie;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

    /**
     * Hjälpmetoder för att hämta data från databsen. Exempelvis med cursors
     * */

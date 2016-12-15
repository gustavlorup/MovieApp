package tig167.movieapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by GustavL on 2016-12-05.
 */

public class DBHelper extends SQLiteOpenHelper{

    private static String TAG ="DBHelper";
    private static final String DB_PATH = "/data/data/tig167.movieapp/databases/";
    private static final String DB_NAME = "movies";
    private SQLiteDatabase myDataBase;
    private final Context myContext;





    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Skapar en tom databas och skriver om den med vår egna databas.
     * */
    public void createDataBase() throws IOException{

        boolean mDataBaseExist = checkDataBase();

        System.out.println("checkDatabase");

        if(mDataBaseExist){

        }

        else{
            this.getReadableDatabase();

            try
            {
                //Kopierar databasen från assets\\databases
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
            }
            catch (IOException mIOException)
            {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    /**
     * Kollar om databasen redan finns för att undvika att kopiera om filen varje gång vi startar appen.
     * returnar true om den finns, false om den inte finns.
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;



        try{

            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


        }catch(SQLiteException e){


        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    /**
     * Koperiar vår databasen (movies.db i assets\database) till den tomma databasen vi har skapat
     * som sedan kan behandlas.
     * Detta görs genom att överföra bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Öppnar vår databas som inputstream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path till den nyskapta tomma databasen
        String outFileName = DB_PATH + DB_NAME;


        //Öppnar den tomma databasen som output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //överför bytes från inputfilen (vår databas) till outputfilen (den nya tomma)
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Stänger strömmarna
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Öppnar databasen
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


    }


    public Cursor getRandomMovie() {

        try {

            String sql = "SELECT movies.title " +
                    "FROM movies INNER JOIN (genre INNER JOIN movies_genre ON genre.genreID = movies_genre.idgenre)ON movies.idmovies = movies_genre.idmovies" +
                    " ORDER BY RANDOM()" +
                    " LIMIT 1;";


            Cursor cursor =  myDataBase.rawQuery(sql, null);

            System.out.println(cursor);

            cursor.close();
            return cursor;


        } catch (SQLiteException mSQLException) {
            Log.e(TAG, "getRandomMovie >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

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

    

}
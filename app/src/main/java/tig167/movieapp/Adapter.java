package tig167.movieapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.IOException;

/**
 * Created by GustavL on 2016-12-15.
 */

public class Adapter {

    protected static final String TAG = "DataAdapter";

    private final Context myContext;
    private SQLiteDatabase myDb;
    private DBHelper myDBHelper;





    public Adapter(Context context) {

        this.myContext = context;
        myDBHelper = new DBHelper(myContext);

    }

    public Adapter createDatabase() throws SQLiteException {

        try {
            System.out.println("SKJDHFJKSDHGJKSDHG");
            myDBHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "Unable To Create Database");
            throw new Error("Unable To Create Database");
        }
        return this;
    }

    public Adapter open() throws SQLiteException {

        try {
            myDBHelper.openDataBase();
            myDBHelper.close();
            myDb = myDBHelper.getReadableDatabase();
        } catch (SQLiteException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {

        myDBHelper.close();
    }

    public Cursor getRandomMovie() {

        try {

           String sql = "SELECT movies.title " +
                    "FROM movies INNER JOIN (genre INNER JOIN movies_genre ON genre.genreID = movies_genre.idgenre)ON movies.idmovies = movies_genre.idmovies" +
                    " ORDER BY RANDOM()" +
                    " LIMIT 1;";


            Cursor cursor = myDb.rawQuery(sql, null);

            System.out.println(cursor);

            cursor.close();
            return cursor;


        } catch (SQLiteException mSQLException) {
            Log.e(TAG, "getRandomMovie >>" + mSQLException.toString());
            throw mSQLException;
        }
    }



}
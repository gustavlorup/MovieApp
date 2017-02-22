package tig167.movieapp.Logik;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "";
    /* Default systemväg för applikations databas */
    private static String DB_PATH = "/data/data/tig167.movieapp/databases/";

    private static String DB_NAME = "movies.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    public DBHelper(Context context) {

        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    /**
     * Skapar en tom databas på systemet och gör om det till vår egna */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            System.out.println("Database exists");
            this.getWritableDatabase();
            /* Databasen existerar, gör inget! */
        }
        dbExist = checkDataBase();

        if (!dbExist) {

            /* Om databasen inte finns så skapar vi en till som overridar vår default i systemet*/

            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }


    /**
     * Kollar om databasen redan finns så vi inte kopierar databasen varje start av app
     *
     *
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            //Databasen finns inte

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Kopierar databasen från asset till den tomma, nyskapade databsen i systemmappen.
     *
     *
     */
    private void copyDataBase() throws IOException {

        //Lokal data som inputstream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path till vår lokala
        String outFileName = DB_PATH + DB_NAME;

        //Öppnar tom databas
        OutputStream myOutput = new FileOutputStream(outFileName);

        //För över datan från lokal till nya
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Stänger
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Öppna databasen
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
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
        c.close();

        String query2 = "SELECT genre.moviegenre FROM movies " +
                "INNER JOIN (genre INNER JOIN movies_genre ON genre.[_id] = movies_genre.idgenre) ON movies.[_id] = movies_genre.idmovies " +
                "WHERE movies._id =" + id + ";";
        Cursor c1 = myDataBase.rawQuery(query2,null);

        ArrayList<String> genres = new ArrayList<>();

        String row[] = new String[c1.getCount()];
        while(c1.moveToNext()){
            for(int i = 0; i < c1.getCount(); i++){
                row[i] = c1.getString(0);
                if(!genres.contains(row[i])) {
                    genres.add(row[i]);
                }
            }
        }

        c1.close();

        Movie randomMovie = new Movie(Integer.parseInt(id), title, Integer.parseInt(year), Double.parseDouble(rating), desc, url, genres);
        return randomMovie;
    }


    public Movie getFilteredMovie(String genre1, String genre2, String genre3, String Rating, String year_from, String year_to) {
        Movie FilteredMovie = null;
        String query = "";
        String query2 = "";
        Cursor c;
        Cursor c1;

        if (genre1!=null && genre2!=null && genre3==null) {
            query = "SELECT movies.*\n" +
                    "FROM movies INNER JOIN (genre INNER JOIN movies_genre ON genre.[_id] = movies_genre.idgenre) ON movies.[_id] = movies_genre.idmovies\n" +
                    "WHERE ((((movies_genre.idgenre)=" + genre1 + ") OR ((movies_genre.idgenre) = " + genre2 + ")) AND (movies.movieRating)>=" + Rating + ")" +
                    " AND ((movies.year) BETWEEN " + year_from + " AND " + year_to + ")\n" +
                    "ORDER BY RANDOM()\n" +
                    "LIMIT 1;";
            c = myDataBase.rawQuery(query, null);
            c.moveToFirst();
            String id = c.getString(c.getColumnIndex("_id"));
            String title = c.getString(c.getColumnIndex("title"));
            String year = c.getString(c.getColumnIndex("year"));
            String rating = c.getString(c.getColumnIndex("movieRating"));
            String desc = c.getString(c.getColumnIndex("movieDesc"));
            String url = c.getString(c.getColumnIndex("movieURL"));
            c.close();

            query2 = "SELECT genre.moviegenre FROM movies " +
                    "INNER JOIN (genre INNER JOIN movies_genre ON genre.[_id] = movies_genre.idgenre) ON movies.[_id] = movies_genre.idmovies " +
                    "WHERE movies._id =" + id + ";";
            c1 = myDataBase.rawQuery(query2, null);

            ArrayList<String> genres = new ArrayList<>();

            String row[] = new String[c1.getCount()];
            while (c1.moveToNext()) {
                for (int i = 0; i < c1.getCount(); i++) {
                    row[i] = c1.getString(0);
                    if (!genres.contains(row[i])) {
                        genres.add(row[i]);
                    }
                }
            }


            c1.close();
            FilteredMovie = new Movie(Integer.parseInt(id), title, Integer.parseInt(year), Double.parseDouble(rating), desc, url, (ArrayList<String>) genres);
        }


        else if(genre1!=null && genre2==null && genre3==null) {

            query = "SELECT movies.*\n" +
                    "FROM movies INNER JOIN (genre INNER JOIN movies_genre ON genre.[_id] = movies_genre.idgenre) ON movies.[_id] = movies_genre.idmovies\n" +
                    "WHERE ((((movies_genre.idgenre)=" + genre1 + ") AND (movies.movieRating)>=" + Rating + ")" +
                    " AND ((movies.year) BETWEEN " + year_from + " AND " + year_to + "))\n" +
                    "ORDER BY RANDOM()\n" +
                    "LIMIT 1;";
            c = myDataBase.rawQuery(query, null);
            c.moveToFirst();
            String id = c.getString(c.getColumnIndex("_id"));
            String title = c.getString(c.getColumnIndex("title"));
            String year = c.getString(c.getColumnIndex("year"));
            String rating = c.getString(c.getColumnIndex("movieRating"));
            String desc = c.getString(c.getColumnIndex("movieDesc"));
            String url = c.getString(c.getColumnIndex("movieURL"));
            c.close();


            query2 = "SELECT genre.moviegenre FROM movies " +
                    "INNER JOIN (genre INNER JOIN movies_genre ON genre.[_id] = movies_genre.idgenre) ON movies.[_id] = movies_genre.idmovies " +
                    "WHERE movies._id =" + id + ";";
            c1 = myDataBase.rawQuery(query2, null);


            ArrayList<String> genres = new ArrayList<>();

            String row[] = new String[c1.getCount()];
            while(c1.moveToNext()){
                for(int i = 0; i < c1.getCount(); i++){
                    row[i] = c1.getString(0);
                    if(!genres.contains(row[i])) {
                        genres.add(row[i]);
                    }
                }
            }

            c1.close();

            FilteredMovie = new Movie(Integer.parseInt(id), title, Integer.parseInt(year), Double.parseDouble(rating), desc, url, (ArrayList<String>) genres);


        }

        else if (genre1==null && genre2==null && genre3==null) {
            query = "SELECT movies.*\n" +
                    "FROM movies INNER JOIN (genre INNER JOIN movies_genre ON genre.[_id] = movies_genre.idgenre) ON movies.[_id] = movies_genre.idmovies\n" +
                    "WHERE ((movies.movieRating)>=" + Rating + ")" +
                    " AND ((movies.year) BETWEEN " + year_from + " AND " + year_to + ")\n" +
                    "ORDER BY RANDOM()\n" +
                    "LIMIT 1;";
            c = myDataBase.rawQuery(query, null);
            c.moveToFirst();
            String id = c.getString(c.getColumnIndex("_id"));
            String title = c.getString(c.getColumnIndex("title"));
            String year = c.getString(c.getColumnIndex("year"));
            String rating = c.getString(c.getColumnIndex("movieRating"));
            String desc = c.getString(c.getColumnIndex("movieDesc"));
            String url = c.getString(c.getColumnIndex("movieURL"));
            c.close();


            query2 = "SELECT genre.moviegenre FROM movies " +
                    "INNER JOIN (genre INNER JOIN movies_genre ON genre.[_id] = movies_genre.idgenre) ON movies.[_id] = movies_genre.idmovies " +
                    "WHERE movies._id =" + id + ";";
            c1 = myDataBase.rawQuery(query2, null);
            ArrayList<String> genres = new ArrayList<>();
            String row[] = new String[c1.getCount()];
            while(c1.moveToNext()){
                for(int i = 0; i < c1.getCount(); i++){
                    row[i] = c1.getString(0);
                    if(!genres.contains(row[i])) {
                        genres.add(row[i]);
                    }
                }
            }

            c1.close();

            FilteredMovie = new Movie(Integer.parseInt(id), title, Integer.parseInt(year), Double.parseDouble(rating), desc, url, genres);


    }


        else if(genre1!=null && genre2!=null && genre3!=null){

            query = "SELECT movies.*\n" +
                    "FROM movies INNER JOIN (genre INNER JOIN movies_genre ON genre.[_id] = movies_genre.idgenre) ON movies.[_id] = movies_genre.idmovies\n" +
                    "WHERE ((((movies_genre.idgenre)=" + genre1 + ") OR ((movies_genre.idgenre) = " + genre2 + ") AND ((movies_genre.idgenre) = " + genre3 + ")) AND (movies.movieRating)>=" + Rating + ")" +
                    " AND ((movies.year) BETWEEN " + year_from + " AND " + year_to + ")\n" +
                    "ORDER BY RANDOM()\n" +
                    "LIMIT 1;";
            c = myDataBase.rawQuery(query, null);
            c.moveToFirst();
            String id = c.getString(c.getColumnIndex("_id"));
            String title = c.getString(c.getColumnIndex("title"));
            String year = c.getString(c.getColumnIndex("year"));
            String rating = c.getString(c.getColumnIndex("movieRating"));
            String desc = c.getString(c.getColumnIndex("movieDesc"));
            String url = c.getString(c.getColumnIndex("movieURL"));
            c.close();


            query2 = "SELECT genre.moviegenre FROM movies " +
                    "INNER JOIN (genre INNER JOIN movies_genre ON genre.[_id] = movies_genre.idgenre) ON movies.[_id] = movies_genre.idmovies " +
                    "WHERE movies._id =" + id + ";";
            c1 = myDataBase.rawQuery(query2, null);


            ArrayList<String> genres = new ArrayList<>();

            String row[] = new String[c1.getCount()];
            while (c1.moveToNext()) {
                for (int i = 0; i < c1.getCount(); i++) {
                    row[i] = c1.getString(0);
                    if (!genres.contains(row[i])) {
                        genres.add(row[i]);
                    }
                }
            }
            c1.close();
            FilteredMovie = new Movie(Integer.parseInt(id), title, Integer.parseInt(year), Double.parseDouble(rating), desc, url, (ArrayList<String>) genres);
        }
            return FilteredMovie;
        }





    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            System.out.println("Database version..");
            myContext.deleteDatabase(DB_NAME);
        }
    }
}


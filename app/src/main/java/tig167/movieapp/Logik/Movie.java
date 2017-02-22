package tig167.movieapp.Logik;

import java.util.ArrayList;

public class Movie {

    private int id;
    private String title;
    private int year;
    private double rating;
    private String desc;
    private String url;
    private String genre1;
    private String genre2;
    private String genre3;
    private String[] arraywithgenres;
    private ArrayList<String> arraylistwithgenres;

    public Movie(){

    }


    public Movie(int id, String title, int year, double rating, String desc, String url, ArrayList<String> genre){
        this.id=id;
        this.title=title;
        this.year = year;
        this.rating=rating;
        this.desc = desc;
        this.url = url;
        this.arraylistwithgenres = genre;
    }

    public String getTitle(){
        return this.title;
    }

    public int getId(){
        return this.id;
    }

    public int getYear(){
        return this.year;
    }

    public String getGenre1(){
        return this.genre1;
    }

    public String getGenre2(){
        return this.genre2;
    }

    public String getGenre3(){
        return this.genre3;
    }

    public double getRating(){
        return this.rating;
    }
    public String getDesc(){
        return this.desc;
    }
    public String getUrl(){
        return this.url;
    }
    public String getGenres(){
        return this.arraylistwithgenres.toString();
    }

    @Override
    public String toString(){
        return "id: " + this.id + "\n" + "title: " + this.title + "\n" + "year of realease: " + this.year + "\n" + "rating: " + this.rating + "\n" + "plot: " + this.desc +"\n" + "youtube url: " + this.url;
    }


}
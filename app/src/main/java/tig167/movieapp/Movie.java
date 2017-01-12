package tig167.movieapp;

/**
 * Created by Hugo on 2016-12-16.
 */

public class Movie {

    private int id;
    private String title;
    private int year;
    private double rating;
    private String desc;
    private String url;

    public Movie(){

    }

    public Movie(int id, String title, int year, double rating, String desc, String url){
        this.id = id;
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.desc = desc;
        this.url = url;
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

    public double getRating(){
        return this.rating;
    }
    public String getDesc(){
        return this.desc;
    }
    public String getUrl(){ return this.url; }

    @Override
    public String toString(){
        return "id: " + this.id + "\n" + "title: " + this.title + "\n" + "year of realease: " + this.year + "\n" + "rating: " + this.rating + "\n" + "plot: " + this.desc +"\n" + "youtube url: " + this.url;
    }


}


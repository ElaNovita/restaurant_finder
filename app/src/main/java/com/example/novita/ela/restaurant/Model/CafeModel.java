package com.example.novita.ela.restaurant.Model;

/**
 * Created by elaa on 03/04/17.
 */

public class CafeModel {
    int id, likes, bookmark, have_here;
    String nama, alamat, tlp, email, fb, ig, jam, deskripsi, lokasi, gambar;
    double lat, lng, rating;
    boolean status;


    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTlp() {
        return tlp;
    }

    public String getEmail() {
        return email;
    }

    public String getFb() {
        return fb;
    }

    public String getIg() {
        return ig;
    }

    public String getJam() {
        return jam;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getGambar() {
        return gambar;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getBookmark() {
        return bookmark;
    }

    public void setBookmark(int bookmark) {
        this.bookmark = bookmark;
    }

    public int getHave_here() {
        return have_here;
    }

    public void setHave_here(int have_here) {
        this.have_here = have_here;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

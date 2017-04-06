package com.example.novita.ela.restaurant.Model;

/**
 * Created by elaa on 03/04/17.
 */

public class CafeModel {
    int id, likes;
    String nama, alamat, tlp, email, fb, ig, jam, deskripsi, lokasi, gambar, logo;
    double lat, lng;

    public CafeModel() {
    }

    public CafeModel(int id, String nama, String alamat, String tlp, String email, String fb,
                     String ig, String jam, String deskripsi, String lokasi, String gambar,
                     double lat, double lng) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.tlp = tlp;
        this.email = email;
        this.fb = fb;
        this.ig = ig;
        this.jam = jam;
        this.deskripsi = deskripsi;
        this.lokasi = lokasi;
        this.gambar = gambar;
        this.lat = lat;
        this.lng = lng;
    }

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}

package com.example.novita.ela.restaurant.Model;

/**
 * Created by elaa on 03/04/17.
 */

public class GalleryModel {
    int id, cafeModel;
    String gambar;

    public GalleryModel() {
    }

    public GalleryModel(int id, int cafeModel, String gambar) {
        this.id = id;
        this.cafeModel = cafeModel;
        this.gambar = gambar;
    }

    public int getId() {
        return id;
    }

    public int getCafeModel() {
        return cafeModel;
    }

    public String getGambar() {
        return gambar;
    }
}

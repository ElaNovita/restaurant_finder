package com.example.novita.ela.restaurant.helper;

import java.io.Serializable;

/**
 * Created by elaa on 06/04/17.
 */

public class Image implements Serializable {
    String name;
    String images;

    public Image() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}

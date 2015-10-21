package ru.thevhod.picquest.data;

public class GridItem {

    private String imageUrl;
    private int id;

    public GridItem(String imageUrl, int id) {
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getId() {
        return id;
    }
}

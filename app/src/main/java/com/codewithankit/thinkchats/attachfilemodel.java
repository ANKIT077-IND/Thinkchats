package com.codewithankit.thinkchats;

public class attachfilemodel {
    String filename;
    int image;

    public attachfilemodel(String filename, int image) {
        this.filename = filename;
        this.image = image;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}

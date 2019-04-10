package proitappsolutions.com.rumosstore.revistas;

public class Kiosque {
    private String Name, ImageURL;

    public Kiosque(String name, String imageURL) {
        Name = name;
        ImageURL = imageURL;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }


}

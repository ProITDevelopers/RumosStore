package proitappsolutions.com.rumosstore.QUIZ.Model;

public class Category {

    private String Name;
    private String Image;
    private String Status;

    public Category() {}

    public Category(String name, String image, String status) {
        Name = name;
        Image = image;
        Status = status;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}

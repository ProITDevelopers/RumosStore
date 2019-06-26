package proitappsolutions.com.rumosstore.modelo;

public class UsuarioFire {
    private String userName;
    private String email;

    public UsuarioFire(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public UsuarioFire() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package proitappsolutions.com.rumosstore.communs;

import android.util.Patterns;

import java.util.regex.Pattern;

public class MetodosComuns {

    public static boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

}

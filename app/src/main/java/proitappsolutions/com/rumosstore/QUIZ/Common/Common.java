package proitappsolutions.com.rumosstore.QUIZ.Common;

import java.util.ArrayList;
import java.util.List;

import proitappsolutions.com.rumosstore.QUIZ.Model.PerguntaErrada;
import proitappsolutions.com.rumosstore.QUIZ.Model.Question;
import proitappsolutions.com.rumosstore.QUIZ.Model.User;

public class Common {

    public static String categoryId,categoryName;
    public static User currentUser;
    public static List<Question> questionList = new ArrayList<>();
    public static List<PerguntaErrada> questErradasList = new ArrayList<>();

}

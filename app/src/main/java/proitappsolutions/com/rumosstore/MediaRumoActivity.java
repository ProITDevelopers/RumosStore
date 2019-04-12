package proitappsolutions.com.rumosstore;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import proitappsolutions.com.rumosstore.telasIniciais.HomeInicialActivity;

public class MediaRumoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmailLogin,editTextPasslLogin;
    private ImageView btnLogFb;
    private Button btnEntrar,btnRegistrate;

    private LoginButton loginButton;
    private AVLoadingIndicatorView loader;
    private CallbackManager callbackManager;
    private AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.statuscolor));
        setContentView(R.layout.activity_media_rumo);
        //comit
        inicializar();

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));

        btnLogFb.setEnabled(true);
        btnLogFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
                btnLogFb.setEnabled(false);
//                loader.setVisibility(View.VISIBLE);

            }
        });


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();

                btnLogFb.setEnabled(false);
                loader.setVisibility(View.VISIBLE);

                if (!Common.isConnected(10000)) {
                    loader.setVisibility(View.INVISIBLE);
                    LoginManager.getInstance().logOut();

                    btnLogFb.setEnabled(true);

                } else{
                    loaduserProfile(accessToken);
                }


            }

            @Override
            public void onCancel() {
                loader.setVisibility(View.INVISIBLE);
                btnLogFb.setEnabled(true);

            }

            @Override
            public void onError(FacebookException error) {
                loader.setVisibility(View.INVISIBLE);
                btnLogFb.setEnabled(true);

            }
        });
    }

    private void inicializar() {
        editTextEmailLogin = findViewById(R.id.editTextEmaiLogin);
        editTextPasslLogin= findViewById(R.id.editTextPasslLogin);
        btnLogFb = findViewById(R.id.btnLogFb);
        btnEntrar= findViewById(R.id.btnEntrar);
        btnRegistrate = findViewById(R.id.btnRegistrate);
        btnEntrar.setOnClickListener(MediaRumoActivity.this);
        btnRegistrate.setOnClickListener(MediaRumoActivity.this);
//        btnLogFb.setOnClickListener(MediaRumoActivity.this);

        loginButton = findViewById(R.id.loginBtn);
        loader = findViewById(R.id.loader);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnEntrar:
                Toast.makeText(this, "Login com facebook", Toast.LENGTH_SHORT).show();
//                Intent intentEntrar = new Intent(MediaRumoActivity.this,HomeInicialActivity.class);
//                intentEntrar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intentEntrar);
                break;

            case R.id.btnRegistrate:
                Intent intentRegistrar = new Intent(MediaRumoActivity.this,RegistroActivity.class);
                startActivity(intentRegistrar);
                break;

//            case R.id.btnLogFb:
//                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void loaduserProfile(AccessToken newAccessToken){

        loader.setVisibility(View.VISIBLE);

        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String name = first_name + " "+last_name;

                    String image_url = "https://graph.facebook.com/"+id+ "/picture?type=normal";

                    Common.mCurrentUser = new Usuario(id,email,name,image_url);

                    AppDatabase.saveUser(Common.mCurrentUser);

                    AppPref.getInstance().saveAuthToken(newAccessToken.getToken());
                    launchHomeScreen();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void launchHomeScreen() {
        Intent intent = new Intent(MediaRumoActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}

package proitappsolutions.com.rumosstore;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.realm.RealmResults;
import proitappsolutions.com.rumosstore.testeRealmDB.CartItemRevistas;
import proitappsolutions.com.rumosstore.testeRealmDB.Revistas;

public class Common {


    public static List<Revistas> revistasArrayList = new ArrayList<>();

    //

    public static final int GALLERY_PICK = 1;

    public static final String PAYPAL_CLIENT_ID = "AcWb_OrCdYDpYwI0UL_jQ3Az-dj0akEzIxFLj8yVWCDP9gW8d36a38sfyPHhDC4DNZjZ5ORtzj6rXPUh";
    public static final int PAYPAL_REQUEST_CODE = 123;
    public static final String ORDER_STATUS_COMPLETED = "approved";
    public static Usuario mCurrentUser;

    public static final String DB_REALM = "rumos_store_db";

    //Your social Page ID or Username here. Not links....
    public static String SOCIAL_FACEBOOK = "Media-Rumo";
    public static String SOCIAL_INSTAGRAM = "revistarumo";





    public static void changeStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(color);
        }
    }




    public static boolean isConnected(int timeOut) {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress != null && !inetAddress.equals("");
    }
}

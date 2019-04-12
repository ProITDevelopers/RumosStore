package proitappsolutions.com.rumosstore.communs;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.http.Url;

public class HTTPDataHandler {

    static String stream = null;

    public HTTPDataHandler(){

    }

    public String GetHTTPData(String urlString){


        try {
            URL url = new URL(urlString);

            HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = r.readLine()) != null){
                    sb.append(line);
                    stream = sb.toString();
                    urlConnection.disconnect();
                }

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stream;

    }

}

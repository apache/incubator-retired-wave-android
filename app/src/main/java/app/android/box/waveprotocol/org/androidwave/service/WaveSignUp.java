package app.android.box.waveprotocol.org.androidwave.service;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import app.android.box.waveprotocol.org.androidwave.util.Util;

public class WaveSignUp {

    private static final String CHARSET = "utf-8";

    /**
     * This method get Wave server name, Wave user's username and Wave user's password as input parameters
     * and it will invoke UserRegistrationServlet in the Wave server. If sign up get success the method
     * will return true if not it return false
     *
     * @param host     Apache Wave hostname
     * @param username Apache Wave user's username
     * @param password Apache Wave user's password
     * @return True or false
     */

    public boolean waveSignUp(String host, String username, String password) {

        String servlet = "auth/register";
        String hostURL = Util.hostCreator(host, servlet);
        String httpQuery = "";
        HttpURLConnection connection = null;

        try {
            httpQuery = "address=" + URLEncoder.encode(username, CHARSET) + "&password="
                    + URLEncoder.encode(password, CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            URL url = new URL(hostURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", CHARSET);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset="
                    + CHARSET);

            OutputStream out = connection.getOutputStream();
            out.write(httpQuery.getBytes(CHARSET));

            return connection.getResponseCode() == 200;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            connection.disconnect();
        }
    }

}

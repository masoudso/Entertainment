package com.androidapplication.entertainmentmedia.Utilities;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class API implements Serializable{

    private final String URL = "https://cop4331.herokuapp.com/api/";

    private JSONObject sendData;
    private JSONObject respData;

    private String username;
    private String authtoken;
    private int responseCode;
    private boolean loggedIn = false;
    private String action;

    public API() {

    }

    private void clearPriors() {
        this.responseCode = 422;
        this.sendData = null;
        this.respData = null;
    }

    private boolean getResponse(int ourResponse, int expResponse, HttpURLConnection conn) {
        if (ourResponse == expResponse) {
            if (this.action == "logout") {
                this.respData = null;
                return true;
            }

            try {
                InputStream stream = new BufferedInputStream(conn.getInputStream());

                BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();
                String input;


                while ((input = streamReader.readLine()) != null) {
                    responseStrBuilder.append(input);
                }
                this.respData = new JSONObject(responseStrBuilder.toString());
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("Response", String.valueOf(ourResponse));
            return false;
        }

        return true;
    }

    private HttpURLConnection setupConnection(String append, String requestMethod) {
        URL url = null;
        HttpURLConnection conn = null;

        try {
            url = new URL(this.URL + append);

            conn = (HttpURLConnection) url.openConnection();

            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod(requestMethod);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (this.loggedIn) {
            conn.setRequestProperty("X-Auth-Username", username);
            conn.setRequestProperty("X-Auth-Token", authtoken);
        }

        return conn;
    }


    public boolean login(Map<String, String> post) {
        this.action = "login";
        clearPriors();
        this.sendData = new JSONObject(post);

        HttpURLConnection conn = this.setupConnection("sessions", "POST");

        conn.setRequestProperty("Content-Type", "application/json");

        try {
            if (this.sendData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

                writer.write(this.sendData.toString());
                writer.flush();
            }

            this.responseCode = conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (getResponse(this.responseCode, HttpURLConnection.HTTP_CREATED, conn) && this.respData != null) {

            try {
                this.username = this.respData.getJSONObject("data").getJSONObject("user").getString("username");
                this.authtoken = this.respData.getJSONObject("data").getJSONObject("user").getString("authentication_token");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            this.loggedIn = true;
            return true;
        }
        return false;
    }

    public boolean register(Map<String, String> post) {
        this.action = "register";
        clearPriors();
        this.sendData = new JSONObject(post);

        HttpURLConnection conn = this.setupConnection("sessions", "POST");

        conn.setRequestProperty("Content-Type", "application/json");

        try {
            if (this.sendData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

                writer.write(this.sendData.toString());
                writer.flush();
            }

            this.responseCode = conn.getResponseCode();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (getResponse(this.responseCode, HttpURLConnection.HTTP_CREATED, conn) && this.respData != null) {

            try {
                this.username = this.respData.getJSONObject("data").getJSONObject("user").getString("username");
                this.authtoken = this.respData.getJSONObject("data").getJSONObject("user").getString("authentication_token");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            this.loggedIn = true;
            return true;
        }
        return false;
    }

    public boolean logout() {
        this.action = "logout";
        clearPriors();

        HttpURLConnection conn = this.setupConnection("sessions", "DELETE");

        try {
            this.responseCode = conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (getResponse(this.responseCode, HttpURLConnection.HTTP_OK, conn) && this.respData == null) {
            this.username = "";
            this.authtoken = "";
            this.loggedIn = false;
            return true;
        }
        return false;
    }

    public JSONObject search(String query) {
        this.action = "search";
        clearPriors();

        String append = "search?q=" + query.replace(" ", "+");
        HttpURLConnection conn = this.setupConnection(append, "GET");

        try {
            this.responseCode = conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (getResponse(this.responseCode, HttpURLConnection.HTTP_OK, conn) && this.respData != null) {
            return this.respData;
        }
        return null;
    }

    public boolean getLoginStatus()
    {
        return loggedIn;
    }

}

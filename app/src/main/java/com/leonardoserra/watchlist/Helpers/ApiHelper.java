package com.leonardoserra.watchlist.Helpers;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.leonardoserra.watchlist.Models.Message;
import com.leonardoserra.watchlist.Models.User;
import com.leonardoserra.watchlist.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHelper {

    private Context gContext;
    private String gToken;
    private String gAction;

    private final String SEARCH = "search";
    private final String CREATEUSER = "createuser";
    private final String ADDMOVIE = "addmovie";
    private final String REMOVEMOVIE = "removemovie";
    private final String OBTERMYLISTT = "obtermylistt";
    private final String OBTERFILMESRECOMENDADOS = "obterfilmesrecomendados";

    public ApiHelper(Context current) {
        gContext = current;
    }

    public ApiHelper() {
    }

    private Resources getResources(){
        return gContext.getResources();
    }

    public Message obterFilmesRecomendados(String pHash) {
        String[] lParameters = {OBTERFILMESRECOMENDADOS, pHash};
        Message msg = call(true, lParameters);

        return msg;
    }
    public Message obterMyListt(String pHash) {
        String[] lParameters = {OBTERMYLISTT, pHash};
        Message msg = call(true, lParameters);

        return msg;
    }

    public Message createuser(String pHash) {
        String[] lParameters = {CREATEUSER, pHash};
        Message msg = call(true, lParameters);

        return msg;
    }

    public Message addMovie(String pHash, String pMovieId) {
        String[] lParameters = {ADDMOVIE, pHash, pMovieId };
        Message msg = call(true, lParameters);

        return msg;
    }

    public Message removeMovie(String pHash, String pMovieId) {
        String[] lParameters = {REMOVEMOVIE, pHash, pMovieId };
        Message msg = call(true, lParameters);

        return msg;
    }

    public Message search(User user, String term) {
        String[] lParameters = { SEARCH, user.getHash(), term};
        Message msg = call(true, lParameters);

        return msg;
    }

    public Message call(boolean sync, String... params) {
        Message msg;
        try {
            WLWebApi api = new WLWebApi();
            String response = sync ? api.execute(params).get() : null;

            JSONObject hashToken = new JSONObject(response);

            boolean success = Boolean.parseBoolean(hashToken.getString("success"));
            String message = hashToken.getString("message");
            JSONObject object = hashToken.getJSONObject("object");

            msg = new Message(success, message, object);
        } catch (Exception e) {
            msg = new Message(false, getResources().getString(R.string.some_error_occurred), null);
            e.printStackTrace();
        }

        return msg;
    }

    private class WLWebApi extends AsyncTask<String, Void, String> {

        private String searchTerm;

        @Override
        protected String doInBackground(String... params) {

            String responseStr = "", lHash = "";
            String baseUrl = "http://10.0.2.2:8080/api/";

            if (!Singleton.getInstance().isEmulator())
                baseUrl = "http://192.168.1.5:8080/api/";

            try {

                gAction = params[0];
                lHash = (gAction == OBTERFILMESRECOMENDADOS || gAction == OBTERMYLISTT || gAction == CREATEUSER || gAction == SEARCH || gAction == ADDMOVIE || gAction == REMOVEMOVIE) ? params[1] : "";
                String uri = baseUrl + gAction;

                URL url = new URL(uri);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                JSONObject jsonParam = new JSONObject();

                if (gAction == SEARCH) {
                    if (params[2].equals("")) {
                        return null;
                    }
                    searchTerm = params[2].trim().replace(",", "").replace("-", "").replace(".", "");
                    jsonParam.put("searchterm", searchTerm);
                    jsonParam.put("hash", lHash);
                } else if (gAction == ADDMOVIE || gAction == REMOVEMOVIE) {
                    jsonParam.put("movieid", params[2]);
                    jsonParam.put("hash", lHash);
                } else if (gAction == CREATEUSER || gAction == OBTERMYLISTT || gAction == OBTERFILMESRECOMENDADOS) {
                    jsonParam.put("hash", lHash);
                }

                byte[] outputBytes = jsonParam.toString().getBytes();
                OutputStream os = connection.getOutputStream();
                os.write(outputBytes);

                if (connection.getResponseCode() == 200) {
                    BufferedReader stream =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String line = "";
                    StringBuilder response = new StringBuilder();

                    while ((line = stream.readLine()) != null) {
                        response.append(line);
                    }

                    connection.disconnect();

                    responseStr = response.toString();

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return responseStr;
            }
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }
}
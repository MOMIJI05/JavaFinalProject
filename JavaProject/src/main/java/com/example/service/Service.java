package com.example.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.*;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Service{

    private static String apiKey = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMzRkNDMxNjIyYTFlMmJjYzFkYWM5NGRhMjc4OWVlMCIsIm5iZiI6MS43NDY2MjExOTg2MjMwMDAxZSs5LCJzdWIiOiI2ODFiNTMwZTgxOGFjNTQ1ODI1YWYzNmUiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.YbqCPAqqg38JsIFWBWZh63H0CO9o7eKbeesgGi0zmu8";
    private static String token;
    private static String sessionID;
    private static int accountID;
    
    public static void getToken() throws IOException{
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
        .url("https://api.themoviedb.org/3/authentication/token/new")
        .get()
        .addHeader("accept", "application/json")
        .addHeader("Authorization", "Bearer " + apiKey)
        .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                token = jsonObject.getString("request_token");
                System.out.println("Get Token Request success: " + token);
            }
            else {
                System.out.println("Get Token Request failed: " + response.code());
            }
        }

    }

    public static boolean vaildLogin(String userName, String password) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Boolean success = false;
        
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"username\":\"" + userName + "\",\"password\":\"" + password + "\",\"request_token\":\"" + token + "\"}");
        Request request = new Request.Builder()
        .url("https://api.themoviedb.org/3/authentication/token/validate_with_login")
        .post(body)
        .addHeader("accept", "application/json")
        .addHeader("content-type", "application/json")
        .addHeader("Authorization", "Bearer " + apiKey)
        .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                success = jsonObject.getBoolean("success");
            }
        }

        return success;
    }

    public static void getSession() throws IOException{
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"request_token\":\"" + token + "\"}");
        Request request = new Request.Builder()
        .url("https://api.themoviedb.org/3/authentication/session/new")
        .post(body)
        .addHeader("accept", "application/json")
        .addHeader("content-type", "application/json")
        .addHeader("Authorization", "Bearer " + apiKey)
        .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                sessionID = jsonObject.getString("session_id");
                System.out.println("Get Session Request success: " + sessionID);
            }
            else{
                System.out.println("Get Session Request failed: " + response.code());
            }
        }
    }

    public static void getAccountID() throws IOException{
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
            .url("https://api.themoviedb.org/3/account?session_id=" + sessionID)
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer " + apiKey)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                accountID = jsonObject.getInt("id");

                System.out.println("accountId: " + accountID);
            }
            else {
                System.out.println("Get Account ID Request failed: " + response.code());
            }
        }
    }

    public static void addFavorite(String movieID) throws IOException{
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"media_type\":\"movie\",\"media_id\":" + movieID + ",\"favorite\":true}");
        Request request = new Request.Builder()
        .url("https://api.themoviedb.org/3/account/" + accountID + "/favorite?session_id=" + sessionID)
        .post(body)
        .addHeader("accept", "application/json")
        .addHeader("content-type", "application/json")
        .addHeader("Authorization", "Bearer " + apiKey)
        .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
            } else {
                System.out.println("Request failed: " + response.code());
            }
        }
    }

    public static void removeFavorite(String movieID) throws IOException{
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"media_type\":\"movie\",\"media_id\":" + movieID + ",\"favorite\":false}");
        Request request = new Request.Builder()
        .url("https://api.themoviedb.org/3/account/" + accountID + "/favorite?session_id=" + sessionID)
        .post(body)
        .addHeader("accept", "application/json")
        .addHeader("content-type", "application/json")
        .addHeader("Authorization", "Bearer " + apiKey)
        .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
            } else {
                System.out.println("Request failed: " + response.code());
            }
        }
    }

    public static ArrayList<Movie> listFavorite() throws IOException{
        Map<Integer, String> genreMap = new HashMap<>();
        genreMap.put(28   , "#動作");
        genreMap.put(12   , "#冒險");
        genreMap.put(16   , "#動畫");
        genreMap.put(35   , "#喜劇");
        genreMap.put(80   , "#犯罪");
        genreMap.put(99   , "#紀錄");
        genreMap.put(18   , "#劇情");
        genreMap.put(10751, "#家庭");
        genreMap.put(14   , "#奇幻");
        genreMap.put(36   , "#歷史");
        genreMap.put(27   , "#恐怖");
        genreMap.put(10402, "#音樂");
        genreMap.put(9648 , "#懸疑");
        genreMap.put(10749, "#浪漫");
        genreMap.put(878  , "#科幻");
        genreMap.put(10770, "#電視電影");
        genreMap.put(53   , "#驚悚");
        genreMap.put(10752, "#戰爭");
        genreMap.put(37   , "#西部");

        OkHttpClient client = new OkHttpClient();
        ArrayList<Movie> favorieMovies = new ArrayList<Movie>();
        
        Request request = new Request.Builder()
        .url("https://api.themoviedb.org/3/account/" + accountID + "/favorite/movies?language=zh-TW&page=1&sort_by=created_at.asc&session_id=" + sessionID)
        .get()
        .addHeader("accept", "application/json")
        .addHeader("Authorization", "Bearer " + apiKey)
        .build();

        try (Response response = client.newCall(request).execute();) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                int total_results = jsonObject.getInt("total_results");
                for(int i = 0 ; i < total_results ; i++){
                    String title = jsonObject.getJSONArray("results").getJSONObject(i).getString("title");
                    
                    String posterPath = "https://image.tmdb.org/t/p/w440_and_h660_face" + jsonObject.getJSONArray("results").getJSONObject(i).getString("poster_path");
                    
                    int id = jsonObject.getJSONArray("results").getJSONObject(i).getInt("id");
                    
                    String link = "https://www.themoviedb.org/movie/" + id;
                    
                    String overview = jsonObject.getJSONArray("results").getJSONObject(i).getString("overview");
                    
                    JSONArray genresJSONArray = jsonObject.getJSONArray("results").getJSONObject(i).getJSONArray("genre_ids");
                    ArrayList<String> genres = new ArrayList<String>();
                    for(int j = 0 ; j < genresJSONArray.length() ; j++){
                        String genre = genreMap.getOrDefault(genresJSONArray.getInt(j), "Other");
                        genres.add(genre);
                    }

                    Movie movie = new Movie(title, posterPath, id, link, overview, genres);
                    favorieMovies.add(movie);
                }
                
            } else {
                System.out.println("Request failed: " + response.code());
            }
        }

        return favorieMovies;
    }

    public static Movie getMovieDetail(String movieID) throws IOException{
        Map<Integer, String> genreMap = new HashMap<>();
        genreMap.put(28   , "#動作");
        genreMap.put(12   , "#冒險");
        genreMap.put(16   , "#動畫");
        genreMap.put(35   , "#喜劇");
        genreMap.put(80   , "#犯罪");
        genreMap.put(99   , "#紀錄");
        genreMap.put(18   , "#劇情");
        genreMap.put(10751, "#家庭");
        genreMap.put(14   , "#奇幻");
        genreMap.put(36   , "#歷史");
        genreMap.put(27   , "#恐怖");
        genreMap.put(10402, "#音樂");
        genreMap.put(9648 , "#懸疑");
        genreMap.put(10749, "#浪漫");
        genreMap.put(878  , "#科幻");
        genreMap.put(10770, "#電視電影");
        genreMap.put(53   , "#驚悚");
        genreMap.put(10752, "#戰爭");
        genreMap.put(37   , "#西部");
        OkHttpClient client = new OkHttpClient();

        Movie movie = new Movie("", "", 0, "", "", null);
        Request request = new Request.Builder()
        .url("https://api.themoviedb.org/3/movie/" + movieID + "?language=zh-TW")
        .get()
        .addHeader("accept", "application/json")
        .addHeader("Authorization", "Bearer " + apiKey)
        .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                // System.out.println(responseBody);
                String title = jsonObject.getString("title");

                ArrayList<String> genres = new ArrayList<String>();
                JSONArray genresJSONArray = jsonObject.getJSONArray("genres");
                for(int i = 0 ; i < genresJSONArray.length() ; i++){
                    String genre = genreMap.getOrDefault(genresJSONArray.getJSONObject(i).getInt("id"), "Other");
                    genres.add(genre);
                }

                String posterPath = "https://image.tmdb.org/t/p/w440_and_h660_face" + jsonObject.getString("poster_path");
                int ID = jsonObject.getInt("id");
                String link = "https://www.themoviedb.org/movie/" + ID;
                String overview = jsonObject.getString("overview");
                movie.setDetail(title, posterPath, ID, link, overview, genres);
            } else {
                System.out.println("Request failed: " + response.code());
            }
        }

        return movie;
    }

    public static ArrayList<Movie> recommendWithGenres(ArrayList<String> genres) throws IOException{
        ArrayList<Movie> Movies = new ArrayList<Movie>();
        Map<Integer, String> genreMap = new HashMap<>();
        genreMap.put(28   , "#動作");
        genreMap.put(12   , "#冒險");
        genreMap.put(16   , "#動畫");
        genreMap.put(35   , "#喜劇");
        genreMap.put(80   , "#犯罪");
        genreMap.put(99   , "#紀錄");
        genreMap.put(18   , "#劇情");
        genreMap.put(10751, "#家庭");
        genreMap.put(14   , "#奇幻");
        genreMap.put(36   , "#歷史");
        genreMap.put(27   , "#恐怖");
        genreMap.put(10402, "#音樂");
        genreMap.put(9648 , "#懸疑");
        genreMap.put(10749, "#浪漫");
        genreMap.put(878  , "#科幻");
        genreMap.put(10770, "#電視電影");
        genreMap.put(53   , "#驚悚");
        genreMap.put(10752, "#戰爭");
        genreMap.put(37   , "#西部");

        //String page = "1";
        String genresString = "";
        for(int i = 0 ; i < genres.size() ; i++){
            if(i == 0) genresString += genres.get(i);
            else genresString += "," + genres.get(i);
        }

        OkHttpClient client = new OkHttpClient();

        for (int page = 1; page <= 5; page++) {
            Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=zh-TW&page=" + page + "&sort_by=popularity.desc&with_genres=" + genresString)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);

                    JSONArray results = jsonObject.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movieObj = results.getJSONObject(i);
                        String title = movieObj.getString("title");
                        String posterPathCheck = movieObj.optString("poster_path");
                        if(posterPathCheck == null | posterPathCheck.equals("null")){
                            continue;
                        }
                        String posterPath = "https://image.tmdb.org/t/p/w440_and_h660_face" + posterPathCheck;
                        int id = movieObj.getInt("id");
                        String link = "https://www.themoviedb.org/movie/" + id;
                        String overview = movieObj.getString("overview");

                        JSONArray genresJSONArray = movieObj.getJSONArray("genre_ids");
                        ArrayList<String> movie_genres = new ArrayList<>();
                        for (int j = 0; j < genresJSONArray.length(); j++) {
                            String genre = genreMap.getOrDefault(genresJSONArray.getInt(j), "Other");
                            movie_genres.add(genre);
                        }

                        Movie movie = new Movie(title, posterPath, id, link, overview, movie_genres);
                        Movies.add(movie);
                    }

                }
                else {
                    System.err.println("Request failed (Page " + page + "): " + response.code());
                }
            } catch (Exception e) {
                System.err.println("Exception at page " + page + ": " + e.getMessage());
            }
        }


        return Movies;
    }

    public static boolean checkIfFavorite(String movieID) throws IOException{
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
        .url("https://api.themoviedb.org/3/movie/" + movieID + "/account_states?session_id=" + sessionID)
        .get()
        .addHeader("accept", "application/json")
        .addHeader("Authorization", "Bearer " + apiKey)
        .build();

        try(Response response = client.newCall(request).execute()){
            if(response.isSuccessful()){
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                return jsonObject.getBoolean("favorite");
            }
            else{
                System.out.println("Request failed: " + response.code());
                return false;
            }
        }
    }
}
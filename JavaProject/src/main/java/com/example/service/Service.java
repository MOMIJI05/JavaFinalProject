package com.example.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.*;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Service {

    private static String apiKey = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMzRkNDMxNjIyYTFlMmJjYzFkYWM5NGRhMjc4OWVlMCIsIm5iZiI6MS43NDY2MjExOTg2MjMwMDAxZSs5LCJzdWIiOiI2ODFiNTMwZTgxOGFjNTQ1ODI1YWYzNmUiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.YbqCPAqqg38JsIFWBWZh63H0CO9o7eKbeesgGi0zmu8";
    private static String token;
    private static String sessionID;
    private static int accountID;

    public static void getToken() throws IOException, UnknownHostException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("https://api.themoviedb.org/3/authentication/token/new").get()
                .addHeader("accept", "application/json").addHeader("Authorization", "Bearer " + apiKey).build();

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
        catch (UnknownHostException e) {
            System.err.println("Unknown Host Exception!! " + e);
        }

    }

    public static boolean vaildLogin(String userName, String password) throws IOException, UnknownHostException {
        OkHttpClient client = new OkHttpClient();
        Boolean success = false;

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"username\":\"" + userName + "\",\"password\":\"" + password
                + "\",\"request_token\":\"" + token + "\"}");
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/authentication/token/validate_with_login").post(body)
                .addHeader("accept", "application/json").addHeader("content-type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                success = jsonObject.getBoolean("success");
            }
        }
        catch (UnknownHostException e) {
            System.err.println("Unknown Host Exception!! " + e);
        }

        return success;
    }

    public static void getSession() throws IOException, UnknownHostException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"request_token\":\"" + token + "\"}");
        Request request = new Request.Builder().url("https://api.themoviedb.org/3/authentication/session/new")
                .post(body).addHeader("accept", "application/json").addHeader("content-type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                sessionID = jsonObject.getString("session_id");
                System.out.println("Get Session Request success: " + sessionID);
            }
            else {
                System.out.println("Get Session Request failed: " + response.code());
            }
        }
        catch (UnknownHostException e) {
            System.err.println("Unknown Host Exception!! " + e);
        }
    }

    public static void getAccountID() throws IOException, UnknownHostException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("https://api.themoviedb.org/3/account?session_id=" + sessionID)
                .get().addHeader("accept", "application/json").addHeader("Authorization", "Bearer " + apiKey).build();

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
        catch (UnknownHostException e) {
            System.err.println("Unknown Host Exception!! " + e);
        }
    }

    public static void addFavorite(String movieID) throws IOException, UnknownHostException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "{\"media_type\":\"movie\",\"media_id\":" + movieID + ",\"favorite\":true}");
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/account/" + accountID + "/favorite?session_id=" + sessionID)
                .post(body).addHeader("accept", "application/json").addHeader("content-type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
            }
            else {
                System.out.println("Request failed: " + response.code());
            }
        }
        catch (UnknownHostException e) {
            System.err.println("Unknown Host Exception!! " + e);
        }
    }

    public static void removeFavorite(String movieID) throws IOException, UnknownHostException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "{\"media_type\":\"movie\",\"media_id\":" + movieID + ",\"favorite\":false}");
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/account/" + accountID + "/favorite?session_id=" + sessionID)
                .post(body).addHeader("accept", "application/json").addHeader("content-type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
            }
            else {
                System.out.println("Request failed: " + response.code());
            }
        }
        catch (UnknownHostException e) {
            System.err.println("Unknown Host Exception!! " + e);
        }
    }

    public static ArrayList<Movie> listFavorite() throws IOException, UnknownHostException {
        Map<Integer, String> genreMap = new HashMap<>();
        genreMap.put(28, "#動作");
        genreMap.put(12, "#冒險");
        genreMap.put(16, "#動畫");
        genreMap.put(35, "#喜劇");
        genreMap.put(80, "#犯罪");
        genreMap.put(99, "#紀錄");
        genreMap.put(18, "#劇情");
        genreMap.put(10751, "#家庭");
        genreMap.put(14, "#奇幻");
        genreMap.put(36, "#歷史");
        genreMap.put(27, "#恐怖");
        genreMap.put(10402, "#音樂");
        genreMap.put(9648, "#懸疑");
        genreMap.put(10749, "#浪漫");
        genreMap.put(878, "#科幻");
        genreMap.put(10770, "#電視電影");
        genreMap.put(53, "#驚悚");
        genreMap.put(10752, "#戰爭");
        genreMap.put(37, "#西部");

        OkHttpClient client = new OkHttpClient();
        ArrayList<Movie> favorieMovies = new ArrayList<Movie>();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/account/" + accountID
                        + "/favorite/movies?language=zh-TW&page=1&sort_by=created_at.asc&session_id=" + sessionID)
                .get().addHeader("accept", "application/json").addHeader("Authorization", "Bearer " + apiKey).build();

        try (Response response = client.newCall(request).execute();) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                int total_results = jsonObject.getInt("total_results");
                for (int i = 0; i < total_results; i++) {
                    String title = jsonObject.getJSONArray("results").getJSONObject(i).getString("title");

                    String posterPath = "https://image.tmdb.org/t/p/w440_and_h660_face"
                            + jsonObject.getJSONArray("results").getJSONObject(i).getString("poster_path");

                    int id = jsonObject.getJSONArray("results").getJSONObject(i).getInt("id");

                    String link = "https://www.themoviedb.org/movie/" + id;

                    String overview = jsonObject.getJSONArray("results").getJSONObject(i).getString("overview");

                    JSONArray genresJSONArray = jsonObject.getJSONArray("results").getJSONObject(i)
                            .getJSONArray("genre_ids");
                    ArrayList<String> genres = new ArrayList<String>();
                    for (int j = 0; j < genresJSONArray.length(); j++) {
                        String genre = genreMap.getOrDefault(genresJSONArray.getInt(j), "Other");
                        genres.add(genre);
                    }

                    Movie movie = new Movie(title, posterPath, id, link, overview, genres);
                    favorieMovies.add(movie);
                }

            }
            else {
                System.out.println("Request failed: " + response.code());
            }
        }
        catch (UnknownHostException e) {
            System.err.println("Unknown Host Exception!! " + e);
        }

        return favorieMovies;
    }

    public static ArrayList<String> getFavoriteGenres() throws UnknownHostException, IOException {
        Map<String, Integer> genreMap = new HashMap<>();
        genreMap.put("#動作", 28);
        genreMap.put("#冒險", 12);
        genreMap.put("#動畫", 16);
        genreMap.put("#喜劇", 35);
        genreMap.put("#犯罪", 80);
        genreMap.put("#紀錄", 99);
        genreMap.put("#劇情", 18);
        genreMap.put("#家庭", 10751);
        genreMap.put("#奇幻", 14);
        genreMap.put("#歷史", 36);
        genreMap.put("#恐怖", 27);
        genreMap.put("#音樂", 10402);
        genreMap.put("#懸疑", 9648);
        genreMap.put("#浪漫", 10749);
        genreMap.put("#科幻", 878);
        genreMap.put("#電視電影", 10770);
        genreMap.put("#驚悚", 53);
        genreMap.put("#戰爭", 10752);
        genreMap.put("#西部", 37);
        ArrayList<String> genres = new ArrayList<String>();
        ArrayList<Movie> movies = listFavorite();

        for (int i = 0; i < movies.size(); i++) {
            for (int j = 0; j < movies.get(i).getGenres().size(); j++) {
                genres.add(Integer.toString(genreMap.getOrDefault(movies.get(i).getGenres().get(j), 0)));
            }
        }

        return genres;
    }

    public static Movie getMovieDetail(String movieID) throws IOException, UnknownHostException {
        Map<Integer, String> genreMap = new HashMap<>();
        genreMap.put(28, "#動作");
        genreMap.put(12, "#冒險");
        genreMap.put(16, "#動畫");
        genreMap.put(35, "#喜劇");
        genreMap.put(80, "#犯罪");
        genreMap.put(99, "#紀錄");
        genreMap.put(18, "#劇情");
        genreMap.put(10751, "#家庭");
        genreMap.put(14, "#奇幻");
        genreMap.put(36, "#歷史");
        genreMap.put(27, "#恐怖");
        genreMap.put(10402, "#音樂");
        genreMap.put(9648, "#懸疑");
        genreMap.put(10749, "#浪漫");
        genreMap.put(878, "#科幻");
        genreMap.put(10770, "#電視電影");
        genreMap.put(53, "#驚悚");
        genreMap.put(10752, "#戰爭");
        genreMap.put(37, "#西部");
        OkHttpClient client = new OkHttpClient();

        Movie movie = new Movie("", "", 0, "", "", null);
        Request request = new Request.Builder().url("https://api.themoviedb.org/3/movie/" + movieID + "?language=zh-TW")
                .get().addHeader("accept", "application/json").addHeader("Authorization", "Bearer " + apiKey).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                // System.out.println(responseBody);
                String title = jsonObject.getString("title");

                ArrayList<String> genres = new ArrayList<String>();
                JSONArray genresJSONArray = jsonObject.getJSONArray("genres");
                for (int i = 0; i < genresJSONArray.length(); i++) {
                    String genre = genreMap.getOrDefault(genresJSONArray.getJSONObject(i).getInt("id"), "Other");
                    genres.add(genre);
                }

                String posterPath = "https://image.tmdb.org/t/p/w440_and_h660_face"
                        + jsonObject.getString("poster_path");
                int ID = jsonObject.getInt("id");
                String link = "https://www.themoviedb.org/movie/" + ID;
                String overview = jsonObject.getString("overview");
                if (overview.isEmpty()) {
                    movie = getMovieDetailEng(movieID);
                }
                else {
                    movie.setDetail(title, posterPath, ID, link, overview, genres);
                }
            }
            else {
                System.out.println("Request failed: " + response.code());
            }
        }
        catch (UnknownHostException e) {
            System.err.println("Unknown Host Exception!! " + e);
        }

        return movie;
    }

    public static Movie getMovieDetailEng(String movieID) throws IOException, UnknownHostException {
        Map<Integer, String> genreMap = new HashMap<>();
        genreMap.put(28, "#動作");
        genreMap.put(12, "#冒險");
        genreMap.put(16, "#動畫");
        genreMap.put(35, "#喜劇");
        genreMap.put(80, "#犯罪");
        genreMap.put(99, "#紀錄");
        genreMap.put(18, "#劇情");
        genreMap.put(10751, "#家庭");
        genreMap.put(14, "#奇幻");
        genreMap.put(36, "#歷史");
        genreMap.put(27, "#恐怖");
        genreMap.put(10402, "#音樂");
        genreMap.put(9648, "#懸疑");
        genreMap.put(10749, "#浪漫");
        genreMap.put(878, "#科幻");
        genreMap.put(10770, "#電視電影");
        genreMap.put(53, "#驚悚");
        genreMap.put(10752, "#戰爭");
        genreMap.put(37, "#西部");
        OkHttpClient client = new OkHttpClient();

        Movie movie = new Movie("", "", 0, "", "", null);
        Request request = new Request.Builder().url("https://api.themoviedb.org/3/movie/" + movieID + "?language=en-US")
                .get().addHeader("accept", "application/json").addHeader("Authorization", "Bearer " + apiKey).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                // System.out.println(responseBody);
                String title = jsonObject.getString("title");

                ArrayList<String> genres = new ArrayList<String>();
                JSONArray genresJSONArray = jsonObject.getJSONArray("genres");
                for (int i = 0; i < genresJSONArray.length(); i++) {
                    String genre = genreMap.getOrDefault(genresJSONArray.getJSONObject(i).getInt("id"), "Other");
                    genres.add(genre);
                }

                String posterPath = "https://image.tmdb.org/t/p/w440_and_h660_face"
                        + jsonObject.getString("poster_path");
                int ID = jsonObject.getInt("id");
                String link = "https://www.themoviedb.org/movie/" + ID;
                String overview = jsonObject.getString("overview");
                movie.setDetail(title, posterPath, ID, link, overview, genres);
            }
            else {
                System.out.println("Request failed: " + response.code());
            }
        }
        catch (UnknownHostException e) {
            System.err.println("Unknown Host Exception!! " + e);
        }

        return movie;
    }

    public static ArrayList<Movie> recommendWithGenres(ArrayList<String> genres)
            throws IOException, UnknownHostException {
        ArrayList<Movie> Movies = new ArrayList<Movie>();
        Map<Integer, String> genreMap = new HashMap<>();
        genreMap.put(28, "#動作");
        genreMap.put(12, "#冒險");
        genreMap.put(16, "#動畫");
        genreMap.put(35, "#喜劇");
        genreMap.put(80, "#犯罪");
        genreMap.put(99, "#紀錄");
        genreMap.put(18, "#劇情");
        genreMap.put(10751, "#家庭");
        genreMap.put(14, "#奇幻");
        genreMap.put(36, "#歷史");
        genreMap.put(27, "#恐怖");
        genreMap.put(10402, "#音樂");
        genreMap.put(9648, "#懸疑");
        genreMap.put(10749, "#浪漫");
        genreMap.put(878, "#科幻");
        genreMap.put(10770, "#電視電影");
        genreMap.put(53, "#驚悚");
        genreMap.put(10752, "#戰爭");
        genreMap.put(37, "#西部");

        // String page = "1";
        String genresString = "";
        for (int i = 0; i < genres.size(); i++) {
            if (i == 0)
                genresString += genres.get(i);
            else
                genresString += "," + genres.get(i);
        }

        OkHttpClient client = new OkHttpClient();

        for (int page = 1; page <= 5; page++) {
            Request request = new Request.Builder().url(
                    "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=zh-TW&page="
                            + page + "&sort_by=popularity.desc&with_genres=" + genresString)
                    .get().addHeader("accept", "application/json").addHeader("Authorization", "Bearer " + apiKey)
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
                        if (posterPathCheck == null | posterPathCheck.equals("null")) {
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
            }
            catch (UnknownHostException e) {
                System.err.println("Unknown Host Exception!! " + e);
            }
        }

        System.out.println("movie length: " + Movies.size());
        return Movies;
    }

    public static ArrayList<Movie> recommendWithFavorite() throws IOException, UnknownHostException {
        ArrayList<Movie> Movies = new ArrayList<Movie>();
        ArrayList<Movie> favoriteMovies = listFavorite();
        Map<Integer, String> genreMap = new HashMap<>();
        genreMap.put(28, "#動作");
        genreMap.put(12, "#冒險");
        genreMap.put(16, "#動畫");
        genreMap.put(35, "#喜劇");
        genreMap.put(80, "#犯罪");
        genreMap.put(99, "#紀錄");
        genreMap.put(18, "#劇情");
        genreMap.put(10751, "#家庭");
        genreMap.put(14, "#奇幻");
        genreMap.put(36, "#歷史");
        genreMap.put(27, "#恐怖");
        genreMap.put(10402, "#音樂");
        genreMap.put(9648, "#懸疑");
        genreMap.put(10749, "#浪漫");
        genreMap.put(878, "#科幻");
        genreMap.put(10770, "#電視電影");
        genreMap.put(53, "#驚悚");
        genreMap.put(10752, "#戰爭");
        genreMap.put(37, "#西部");

        OkHttpClient client = new OkHttpClient();
        for (int i = 0; i < favoriteMovies.size(); i++) {
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/" + favoriteMovies.get(i).getID()
                            + "/recommendations?language=zh-TW&page=1")
                    .get().addHeader("accept", "application/json").addHeader("Authorization", "Bearer " + apiKey)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);

                    JSONArray results = jsonObject.getJSONArray("results");
                    for (int j = 0; j < results.length(); j++) {
                        JSONObject movieObj = results.getJSONObject(j);
                        String title = movieObj.getString("title");
                        String posterPathCheck = movieObj.optString("poster_path");
                        if (posterPathCheck == null | posterPathCheck.equals("null")) {
                            continue;
                        }
                        String posterPath = "https://image.tmdb.org/t/p/w440_and_h660_face" + posterPathCheck;
                        int id = movieObj.getInt("id");
                        String link = "https://www.themoviedb.org/movie/" + id;
                        String overview = movieObj.getString("overview");

                        JSONArray genresJSONArray = movieObj.getJSONArray("genre_ids");
                        ArrayList<String> movie_genres = new ArrayList<>();
                        for (int k = 0; k < genresJSONArray.length(); k++) {
                            String genre = genreMap.getOrDefault(genresJSONArray.getInt(k), "Other");
                            movie_genres.add(genre);
                        }

                        Movie movie = new Movie(title, posterPath, id, link, overview, movie_genres);
                        Movies.add(movie);
                    }

                }
                else {
                    System.err.println("Request failed (Page " + 1 + "): " + response.code());
                }
            }
            catch (UnknownHostException e) {
                System.err.println("Unknown Host Exception!! " + e);
            }
        }

        if (Movies.size() == 0) {
            // recommendWithTop
            Movies = recommendWithTop();
        }
        return Movies;
    }

    public static ArrayList<Movie> recommendWithTop() throws IOException {
        ArrayList<Movie> Movies = new ArrayList<Movie>();
        Map<Integer, String> genreMap = new HashMap<>();
        genreMap.put(28, "#動作");
        genreMap.put(12, "#冒險");
        genreMap.put(16, "#動畫");
        genreMap.put(35, "#喜劇");
        genreMap.put(80, "#犯罪");
        genreMap.put(99, "#紀錄");
        genreMap.put(18, "#劇情");
        genreMap.put(10751, "#家庭");
        genreMap.put(14, "#奇幻");
        genreMap.put(36, "#歷史");
        genreMap.put(27, "#恐怖");
        genreMap.put(10402, "#音樂");
        genreMap.put(9648, "#懸疑");
        genreMap.put(10749, "#浪漫");
        genreMap.put(878, "#科幻");
        genreMap.put(10770, "#電視電影");
        genreMap.put(53, "#驚悚");
        genreMap.put(10752, "#戰爭");
        genreMap.put(37, "#西部");

        OkHttpClient client = new OkHttpClient();
        for (int page = 0; page <= 5; page++) {
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/popular?language=zh-TW&page=" + page).get()
                    .addHeader("accept", "application/json").addHeader("Authorization", "Bearer " + apiKey).build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);

                    JSONArray results = jsonObject.getJSONArray("results");
                    for (int j = 0; j < results.length(); j++) {
                        JSONObject movieObj = results.getJSONObject(j);
                        String title = movieObj.getString("title");
                        String posterPathCheck = movieObj.optString("poster_path");
                        if (posterPathCheck == null | posterPathCheck.equals("null")) {
                            continue;
                        }
                        String posterPath = "https://image.tmdb.org/t/p/w440_and_h660_face" + posterPathCheck;
                        int id = movieObj.getInt("id");
                        String link = "https://www.themoviedb.org/movie/" + id;
                        String overview = movieObj.getString("overview");

                        JSONArray genresJSONArray = movieObj.getJSONArray("genre_ids");
                        ArrayList<String> movie_genres = new ArrayList<>();
                        for (int k = 0; k < genresJSONArray.length(); k++) {
                            String genre = genreMap.getOrDefault(genresJSONArray.getInt(k), "Other");
                            movie_genres.add(genre);
                        }

                        Movie movie = new Movie(title, posterPath, id, link, overview, movie_genres);
                        Movies.add(movie);
                    }

                }
                else {
                    System.err.println("Request failed (Page " + 1 + "): " + response.code());
                }
            }
            catch (UnknownHostException e) {
                System.err.println("Unknown Host Exception!! " + e);
            }
        }

        return Movies;
    }

    public static boolean checkIfFavorite(String movieID) throws IOException, UnknownHostException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/" + movieID + "/account_states?session_id=" + sessionID).get()
                .addHeader("accept", "application/json").addHeader("Authorization", "Bearer " + apiKey).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                return jsonObject.getBoolean("favorite");
            }
            else {
                System.out.println("Request failed: " + response.code());
                return false;
            }
        }
        catch (UnknownHostException e) {
            System.err.println("Unknown Host Exception!! " + e);
            return false;
        }
    }

    public static ArrayList<String> getComment(String movieID) throws IOException {
        ArrayList<String> comments = new ArrayList<String>();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/" + movieID + "/reviews?language=en-US&page=1").get()
                .addHeader("accept", "application/json").addHeader("Authorization", "Bearer " + apiKey).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                JSONArray results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieObj = results.getJSONObject(i);
                    String content = movieObj.getString("content");
                    comments.add(content);
                }
            }
        }
        catch (UnknownHostException e) {
            System.err.println("Unknown Host Exception!! " + e);
        }

        return comments;
    }

    public static ArrayList<Movie> getUpComing() throws IOException {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("https://api.themoviedb.org/3/movie/upcoming?language=zh-TW&page=1")
                .get().addHeader("accept", "application/json")
                .addHeader("Authorization",
                        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxMzRkNDMxNjIyYTFlMmJjYzFkYWM5NGRhMjc4OWVlMCIsIm5iZiI6MTc0NjYyMTE5OC42MjMwMDAxLCJzdWIiOiI2ODFiNTMwZTgxOGFjNTQ1ODI1YWYzNmUiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.KaiFev6_YspC7Sr_Zr6BBAGR4hbm4el2umlq4C4itUo")
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
                    if (posterPathCheck == null | posterPathCheck.equals("null")) {
                        continue;
                    }
                    String posterPath = "https://image.tmdb.org/t/p/w440_and_h660_face" + posterPathCheck;
                    int id = movieObj.getInt("id");
                    String link = "https://www.themoviedb.org/movie/" + id;
                    String overview = movieObj.getString("overview");

                    JSONArray genresJSONArray = movieObj.getJSONArray("genre_ids");
                    ArrayList<String> movie_genres = new ArrayList<>();
                    for (int j = 0; j < genresJSONArray.length(); j++) {
                        String genre = Integer.toString(genresJSONArray.getInt(j));
                        movie_genres.add(genre);
                    }

                    Movie movie = new Movie(title, posterPath, id, link, overview, movie_genres);
                    movies.add(movie);
                }
            }
        }

        return movies;
    }

    public static void downloadPoster(String movieID) throws IOException, URISyntaxException, FileNotFoundException {
        Movie movie = getMovieDetail(movieID);
        URL url = new URI("https://image.tmdb.org/t/p/w500_face" + movie.getPosterPath()).toURL();
        String destinationPath = "C:\\Users\\Addmin\\Downloads\\" + movie.getTitle().replaceAll("[<>:\"/\\\\|?*]", "")
                + "_海報.jpg";
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream in = conn.getInputStream(); FileOutputStream out = new FileOutputStream(destinationPath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            catch (FileNotFoundException e) {
                System.out.println("File Not Found Exception!!" + e);
            }
        }
        else {
            System.out.println("HTTP error: " + conn.getResponseCode());
        }
    }
}

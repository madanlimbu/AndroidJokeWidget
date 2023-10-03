package jokes.com.widget.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import jokes.com.widget.MainActivity;
import jokes.com.widget.WidgetProvider;
import jokes.com.widget.database.DatabaseInterface;
import jokes.com.widget.theme.ThemeData;

public class Service {
    public static String JOKES_ASSETS = "jokes";
    // Todo: once api to get random joke is create replace this.
    public static String JOKES_URL = "https://raw.githubusercontent.com/madanlimbu/AndroidJokeWidget/master/app/src/main/assets/jokes";
    public static String UPDATE_JOKE = "jokes.com.widget.ACTION_UPDATE_JOKE";
    public static String UPDATE_JOKE_UI = "jokes.com.widget.ACTION_UPDATE_JOKE_UI";
//    public static String UPDATE_THEME = "ACTION_UPDATE_WIDGET_THEME";
//    public static String UPDATE_WIDGET = "JOKES_WIDGET_ACTION_UPDATE_WIDGET";
//    public static String JOKES_FILENAME = "jokes";
    public static String TAG = "JOKES_WIDGET";

    /**
     * Returns theme data object with data populated from db.
     *
     * @param db
     * @return
     * @throws IOException
     */
    public static ThemeData getThemeData(DatabaseInterface db) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(db.getData(), ThemeData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ThemeData();
    }

    /**
     * Persist the theme data as json string in db.
     *
     * @param db
     * @param theme
     * @throws JsonProcessingException
     */
    public static void saveThemeData(DatabaseInterface db, ThemeData theme) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            db.setData(objectMapper.writeValueAsString(theme));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidColor(String color) {
        if(!color.isEmpty()) {
            try {
                Color.parseColor(color);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Make online request to get the latest jokes and save it for later use as a offline.
     * Todo: Once every 24 hours check before making the request
     * Todo: Use rest API cms ?? vs Full List of JOKES ??
     *
     * @param con
     */
//    public static void makeRequest(Context con) {
//        Log.d(TAG, "makeRequest()");
//        Thread thread = new Thread(() -> {
//            try {
//                URL url = new URL(JOKES_URL);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//                try {
//                    InputStream inputStream = connection.getInputStream();
//                    InputStream stream = new BufferedInputStream(inputStream);
//                    Log.d(TAG, stream.toString());
//
////                    StringBuilder sb = new StringBuilder();
////                    for (int ch; (ch = stream.read()) != -1; ) {
////                        sb.append((char) ch);
////                    }
////                    Log.d(TAG, sb.toString());
//
//                    saveFile(stream, con);
//                } catch (IOException e) {
//                    Log.d(TAG, "Found issue with IOException");
//                    InputStream inputStream = new BufferedInputStream(connection.getErrorStream());
//                    StringBuilder sb = new StringBuilder();
//                    for (int ch; (ch = inputStream.read()) != -1; ) {
//                        sb.append((char) ch);
//                    }
//                    Log.d(TAG, sb.toString());
//                }
//                finally {
//                    connection.disconnect();
//                }
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        thread.start();
//    }


    /**
     * Save online jokes for later use.
     *
     * @param stream
     * @param con
     */
//    public static void saveFile(InputStream stream, Context con) {
//        Log.d(TAG, "saveFile()");
//
//        FileOutputStream outputStream;
//        try {
//            outputStream = con.openFileOutput(JOKES_FILENAME, con.MODE_PRIVATE);
//            outputStream.write(inputstreamToStringWithNewLine(stream).getBytes());
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    /**
     * Return inputstream to string with new lines.
     *
     * @param stream
     * @return
     */
//    public static String inputstreamToStringWithNewLine(InputStream stream) {
//        Log.d(TAG, "inputstreamToStringWithNewLine()");
//        String line;
//        StringBuilder sb = new StringBuilder();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//        try {
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + '\n');
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return sb.toString();
//    }


    /**
     * get List of jokes from downloaded file.
     *
     * @param con
     * @return
     */
//    public static ArrayList<String> getStringsFromFile(Context con) {
//        Log.d(TAG, "getStringsFromFile()");
//
//        String line;
//        ArrayList<String> listOfJokes = new ArrayList<>();
//
//        File directory = con.getFilesDir();
//        File file = new File(directory, JOKES_FILENAME);
//
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(file));
//            while ((line = reader.readLine()) != null) {
//                listOfJokes.add(line);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return listOfJokes;
//    }

    /**
     * Get single random joke from assets.
     *
     * @param con
     * @return
     */
    public static String getJokeFromLocalAsset(Context con) {
        Log.d(TAG, "getJokeFromLocalAsset()");

        String line;
        ArrayList<String> listOfJokes = new ArrayList<>();

        try {
            InputStream stream = con.getResources().getAssets().open(JOKES_ASSETS);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            while ((line = reader.readLine()) != null) {
                listOfJokes.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        return String.valueOf(listOfJokes.size());
        return listOfJokes.size() > 0  ? listOfJokes.get(new Random().nextInt(listOfJokes.size() - 0) + 0) : "Task failed successfully.";
    }

    /**
     * Get random joke from the list.
     *
     * @return
     */
//    public static String getRandomJoke(Context context) {
//        Log.d(Service.TAG, "getRandomJoke");
//        ArrayList<String> jokes = new ArrayList<>();
//
////       Service.makeRequest(context); // Todo: once app is sorted fix api call....
////       jokes = Service.getStringsFromFile(con);
//
//        if (jokes.isEmpty()) {
//            jokes = Service.getJokesFromAssets(context);
//        }
//        String joke = jokes.get(new Random().nextInt(jokes.size() - 0) + 0);
//        Log.d(Service.TAG, joke);
//        return joke;
//    }

    public static void broadcastNewJoke(Context context) {
        Log.d(Service.TAG, "broadcastNewJoke");
        Thread thread = new Thread(() -> {
            String joke = new String("");
            try {
                Log.d(Service.TAG, JOKES_URL);
                URL url = new URL(JOKES_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream inputStream = connection.getInputStream();
                    InputStream stream = new BufferedInputStream(inputStream);
                    StringBuilder sb = new StringBuilder();
                    for (int ch; (ch = stream.read()) != -1; ) {
                        sb.append((char) ch);
                    }
                    joke = sb.toString();
                    Log.d(Service.TAG, "Using API Joke.");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    connection.disconnect();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (joke.isEmpty()) {
                    joke = Service.getJokeFromLocalAsset(context);
                    Log.d(Service.TAG, "Using local joke.");
                }
                Log.d(Service.TAG, joke);
                Intent intent = new Intent(context, WidgetProvider.class);
                intent.setAction(Service.UPDATE_JOKE_UI);
                intent.putExtra("joke", joke);
                PendingIntent pendingIntent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                } else {
                    pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                }

                try {
                    pendingIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}

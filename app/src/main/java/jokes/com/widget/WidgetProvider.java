package jokes.com.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import jokes.com.widget.database.DataStore;
import jokes.com.widget.theme.ThemeData;
import jokes.com.widget.utils.Service;

public class WidgetProvider extends AppWidgetProvider {
    public static String UPDATE_JOKE = "ACTION_UPDATE_WIDGET_JOKE";
    public static String UPDATE_THEME = "ACTION_UPDATE_WIDGET_THEME";

    Context con;

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(MainActivity.TAG, "On receive is fired.");
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WidgetProvider.class.getName());
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(thisAppWidget);

        if (intent.getAction().equals(WidgetProvider.UPDATE_THEME)){
            updateTheme(context, AppWidgetManager.getInstance(context), appWidgetIds);
        }
        else {
            onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
        }
    }

    /**
     * Proxy onUpdate() to update only theme of widget instead of updating content aswell.
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    public void updateTheme(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(MainActivity.TAG, "Updating the theme views.");
        ThemeData theme = Service.getThemeData(new DataStore(context));
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setInt(R.id.textView, "setBackgroundColor", Color.parseColor(theme.getBgValue()));
            views.setTextColor(R.id.textView, Color.parseColor(theme.getFgValue()));
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(MainActivity.TAG, "Main onUpdate Running");
        updateTheme(context, appWidgetManager, appWidgetIds); //New widgets needs to be themed as-well.
        this.con = context;
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setTextViewText(R.id.textView, getRandomJoke());

            createIntentForWidgetClick(context, appWidgetIds, views);
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
   }

    /**
     * Overriding the default behaviour of widget on click to create another intent which updates the content.
     *
     * @param context
     * @param appWidgetIds
     * @param views
     */
   public void createIntentForWidgetClick(Context context, int[] appWidgetIds, RemoteViews views) {
       Intent intent = new Intent(context, WidgetProvider.class);
       intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
       intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
       PendingIntent pendingIntent = PendingIntent.getBroadcast(
               context,
               0,
               intent,
               PendingIntent.FLAG_UPDATE_CURRENT
       );
       views.setOnClickPendingIntent(R.id.textView, pendingIntent);
   }

    /**
     * Get random joke from the list.
     *
     * @return
     */
   public String getRandomJoke() {
       String joke = null;
       String[] letter;
       try {
           letter = readTxt("text");
           Random r = new Random();
           int j = r.nextInt(letter.length - 0) + 0;
           joke = letter[j].toString();
       }
       catch (IOException e) {
           e.printStackTrace();
       }
       return joke;
   }

    /**
     * React text form the resource.
     *
     * @param FileName
     * @return
     * @throws IOException
     */
    private String[] readTxt(String FileName) throws IOException {
        Log.d("App", "Reading the text file.");
        InputStream inputStream = con.getResources().getAssets().open(FileName);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ok = byteArrayOutputStream.toString();
        String[] g = ok.split("\n");
        return g;
    }
}
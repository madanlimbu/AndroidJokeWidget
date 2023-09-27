package jokes.com.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.se.omapi.SEService;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Random;

import jokes.com.widget.database.DataStore;
import jokes.com.widget.theme.ThemeData;
import jokes.com.widget.utils.Service;

public class WidgetProvider extends AppWidgetProvider {

    Context con;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(Service.TAG, "On receive is fired.");
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WidgetProvider.class.getName());
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(thisAppWidget);

        Log.d(Service.TAG, "Intent is to : " + intent.getAction());
        if (intent.getAction().equals(Service.UPDATE_THEME)){
            updateTheme(context, AppWidgetManager.getInstance(context), appWidgetIds);
        }
        else if (intent.getAction().equals(Service.UPDATE_JOKE)){
            onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
        }
        else {
            onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
            Log.d(Service.TAG, "Some different intent.");
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
        Log.d(Service.TAG, "Updating the theme views.");
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
        Log.d(Service.TAG, "Main onUpdate Running");
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
       intent.setAction(Service.UPDATE_JOKE);
       intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
       PendingIntent pendingIntent;
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
           pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);
       } else {
           pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
       }
       views.setOnClickPendingIntent(R.id.textView, pendingIntent);
   }

    /**
     * Get random joke from the list.
     *
     * @return
     */
   public String getRandomJoke() {
       ArrayList<String> jokes = new ArrayList<>();

//       Service.makeRequest(con); // Todo: once app is sorted fix api call....
//       jokes = Service.getStringsFromFile(con);

       if (jokes.isEmpty()) {
           jokes = Service.getJokesFromAssets(con);
       }
       String joke = jokes.get(new Random().nextInt(jokes.size() - 0) + 0);
       Log.d(Service.TAG, joke);
       return joke;
   }
}
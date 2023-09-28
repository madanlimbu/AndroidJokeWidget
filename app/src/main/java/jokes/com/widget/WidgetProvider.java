package jokes.com.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import jokes.com.widget.database.DataStore;
import jokes.com.widget.theme.ThemeData;
import jokes.com.widget.utils.Service;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Service.TAG, "On receive intent is fired.");
        Log.d(Service.TAG, "Intent is to : " + intent.getAction());
        super.onReceive(context, intent);

        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WidgetProvider.class.getName());
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(thisAppWidget);
        this.updateWidget(context, AppWidgetManager.getInstance(context), appWidgetIds);
    }

    public static PendingIntent getUpdateJokesWidgetPendingIntent(Context context) {
       Intent intent = new Intent(context, WidgetProvider.class);
       intent.setAction(Service.UPDATE_JOKE);
       PendingIntent pendingIntent;
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
           pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);
       } else {
           pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
       }
       return pendingIntent;
    }

    /**
     * Single entry to update widget.
     *
     * @param context
     */
    public static void sendUpdateWidgetIntent(Context context) {
        Log.d(Service.TAG, "sendUpdateWidgetIntent");
        PendingIntent pendingIntent = WidgetProvider.getUpdateJokesWidgetPendingIntent(context);
        try {
            pendingIntent.send();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Overriding the default behaviour of widget on click to create another intent which updates the content.
     *
     * @param context
     */
    public void createIntentForWidgetClick(Context context, RemoteViews views) {
        Log.d(Service.TAG, "createIntentForWidgetClick");
        PendingIntent pendingIntent = WidgetProvider.getUpdateJokesWidgetPendingIntent(context);
        views.setOnClickPendingIntent(R.id.textView, pendingIntent);
    }

    /**
     * Single place to Update the widget (data, theme) & add on click listener.
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    public void updateWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(Service.TAG, "updateWidget");
        ThemeData theme = Service.getThemeData(new DataStore(context));
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setInt(R.id.textView, "setBackgroundColor", Color.parseColor(theme.getBgValue()));
            views.setTextColor(R.id.textView, Color.parseColor(theme.getFgValue()));
            views.setTextViewText(R.id.textView, Service.getRandomJoke(context));
            this.createIntentForWidgetClick(context, views);
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }
}
package jokes.com.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Main extends AppWidgetProvider {

    Context con;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Log.d("update", "Main onUpdate Running");
        String answer = null;
        this.con = context;
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            String[] letter;
            try {
                letter = readTxt("text");
                Random r = new Random();
                int j = r.nextInt(letter.length - 0) + 0;
                answer = letter[j].toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.activity_main);
        rv.setTextViewText(R.id.textView, answer);

            Intent intent = new Intent(context, Main.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.textView, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
   }

    private String[] readTxt(String FileName) throws IOException {
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
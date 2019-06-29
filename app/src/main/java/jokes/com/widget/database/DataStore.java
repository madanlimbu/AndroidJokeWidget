package jokes.com.widget.database;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

public class DataStore extends ContextWrapper implements DatabaseInterface{
    public static String DB_CONTEXT = "jokes.com.widget.database";
    private SharedPreferences db;

    public DataStore(Context context){
        super(context);
    }

    @Override
    public void setData(String data) {
        this.db = getBaseContext().getSharedPreferences(getBaseContext().getPackageName(), Context.MODE_PRIVATE);
        db.edit().putString("data", data).apply();
    }

    @Override
    public String getData() {
        this.db = getBaseContext().getSharedPreferences(getBaseContext().getPackageName(), Context.MODE_PRIVATE);
        return db.getString("data", "");
    }
}

package jokes.com.widget;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import jokes.com.widget.database.DataStore;
import jokes.com.widget.theme.ThemeData;
import jokes.com.widget.utils.Service;

public class MainActivity extends AppCompatActivity {

    private DataStore db;
    private TextView bgValue;
    private TextView fgValue;
    private TextView errorLabel;

    protected void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
        setContentView(R.layout.main);
        bootstrap();
    }

    /**
     * Bootstrap the applicaiton.
     *
     */
    public void bootstrap() {
        Log.d(Service.TAG, this.getBaseContext().toString());
        db = new DataStore(getBaseContext());
        this.bgValue = (TextView) findViewById(R.id.bg_value);
        this.fgValue = (TextView) findViewById(R.id.fg_value);
        this.errorLabel = (TextView) findViewById(R.id.error_label);

        populateFieldsValueFromDB();
    }

    /**
     * Save new data to database on save-btn click.
     *
     * @param view
     */
    public void saveData(View view) {
        Log.d(Service.TAG, "Saved Btn Clicked.");
        String bgColor = bgValue.getText().toString();
        String fgColor = fgValue.getText().toString();
        if(Service.isValidColor(bgColor) && Service.isValidColor(fgColor)) {
            Log.d(Service.TAG, "Both color valid.");
            Service.saveThemeData(db, new ThemeData(bgColor, fgColor));
            this.updateWidgets();
            this.errorLabel.setText("");
        }
        else {
            Log.d(Service.TAG, "Invalid color.");
            this.errorLabel.setText("Please enter a valid hex color. Google hex color if not sure.");
        }
    }

    /**
     * Get data from database ans restore to fields.
     *
     */
    public void populateFieldsValueFromDB() {
        Log.d(Service.TAG, "Populating fields value from db.");
        ThemeData theme = Service.getThemeData(db);
        this.bgValue.setText(theme.getBgValue());
        this.fgValue.setText(theme.getFgValue());
    }

    /**
     * Update the theming of currently active widgets.
     *
     */
    public void updateWidgets() {
        Log.d(Service.TAG, "Updating widgets by sending intent.");
        WidgetProvider.sendUpdateWidgetIntent(getBaseContext());
    }
}

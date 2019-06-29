package jokes.com.widget.theme;

import android.util.Log;

import jokes.com.widget.MainActivity;

public class ThemeData {
    private String DEFAULT_BG = "#262425";
    private String DEFAULT_FG = "#D79F90";

    private String bgValue = "#262425";
    private String fgValue = "#D79F90";

    public ThemeData(){}

    public ThemeData(String bgValue, String fgValue) {
        this.bgValue = bgValue;
        this.fgValue = fgValue;
    }

    public String getBgValue() {
        Log.d(MainActivity.TAG, "Current BG value : " + bgValue);
        return (bgValue.isEmpty()) ? DEFAULT_BG : bgValue;
    }

    public void setBgValue(String bgValue) {
        this.bgValue = bgValue;
    }

    public String getFgValue() {
        return fgValue;
    }

    public void setFgValue(String fgValue) {
        Log.d(MainActivity.TAG, "Current FG value : " + fgValue);
        this.fgValue = (fgValue.isEmpty()) ? DEFAULT_FG : fgValue;
    }
}

package jokes.com.widget.utils;

import android.graphics.Color;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import jokes.com.widget.R;
import jokes.com.widget.database.DatabaseInterface;
import jokes.com.widget.theme.ThemeData;

public class Service {
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
}

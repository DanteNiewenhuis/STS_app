package e.dante.sts;

import android.util.Log;

import java.util.HashMap;
import java.util.List;

public class DatabaseHelper {
    public DatabaseHelper() {

    }

    public void init_database() {
        Log.d("DatabaseHelper", "init_database() => start");
    }

    public HashMap<String, String> get_expendable_data(String type) {
        HashMap<String, String> data = new HashMap<>();
        data.put("Innate", "Always begin combat with this card in your hard");
        data.put("Exhaust", "Removes the Exhausted card from the deck until end of combat.");


        return data;
    }
}

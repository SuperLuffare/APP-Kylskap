package com.example.smartfridge;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ListHistory {
    private final Context context;
    private static final int MAX_HISTORY = 20;

    public ListHistory(Context context){
        this.context = context;

    }

    /**
     * Sparar den nuvarande listan till historiken
     * Sparar bara ifall listan inte är samma som förra listan som sparades
     * Kollar vilken tid listan sparas
     * @param data
     * @author Jakob
     */
    public void saveListToHistory(String data) {
        List<String> history = readHistory();
        if (!history.isEmpty()) {
            String[] parts = history.get(history.size() - 1).split("\\|", 2);
            if (parts.length >= 2 && data.equals(parts[1])) {
                Log.d("ListHistory", "Samma som förra – sparar inte.");
                return;
            }
        }
        String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm",
                java.util.Locale.getDefault()).format(new java.util.Date());
        history.add(timestamp + "|" + data);
        while (history.size() > MAX_HISTORY) {
            history.remove(0);
        }
        writeHistory(history);
    }

    /**
     * Läser History.txt
     * @return lista med varor
     * @author Jakob
     */
    private List<String> readHistory(){
        List<String> history = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput("History.txt")));
                String line;
                while((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        history.add(line);
                    }
                }
                reader.close();
            } catch (IOException e) {
                Log.d("ListHistory", "Ingen historikfil hittades");
            }
            return history;
        }

    /**
     * Skriver in den nuvarande listan till History.txt
     * @param history list<String>
     * @author Jakob
     */
    private void writeHistory(List<String> history) {
        try {
            FileOutputStream fos = context.openFileOutput("History.txt", Context.MODE_PRIVATE);
            for (String entry : history) {
                fos.write(entry.getBytes());
                fos.write("\n".getBytes());
            }
            fos.flush();
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

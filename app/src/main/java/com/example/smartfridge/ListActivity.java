package com.example.smartfridge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {
    private AskHandler askHandler;
    private ListHistory listHistory;

    /**
     * Hämta listan från servern. Formatera listan vi får från servern så att det är en vara per rad. Vi byter bort "," med "\n" (ny rad)
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     * @author Jakob
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        askHandler = new AskHandler();
        listHistory = new ListHistory(this);


        askHandler.sendMessage("LIST", response -> {
            runOnUiThread(() -> {
                TextView listContent = findViewById(R.id.listContent);
                if (response != null && !response.equals("Connection failed")) {
                    String formatted = response.replace(",", "\n");
                    listContent.setText(formatted);
                    listHistory.saveListToHistory(response);
                } else {
                    listContent.setText("Kunde inte hämta listan.");
                }
            });
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> finish());


        FloatingActionButton historyBtn = findViewById(R.id.viewHistoryButton);
        historyBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ListActivity.this, ListHistoryButtonsActivity.class);
            startActivity(intent);
        });
    }
}
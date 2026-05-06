package com.example.smartfridge;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListHistoryActivity extends AppCompatActivity {
    /**
     * Visar en listan med varor som tillhör den historikknappen användaren har tryckt på.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_history);

        String listData = getIntent().getStringExtra("LIST_DATA");
        TextView listContent = findViewById(R.id.listContent);
        TextView listTitle = findViewById(R.id.listTitle);

        listTitle.setText("Historik");

        if (listData != null) {
            String formatted = listData.replace(",", "\n");
            listContent.setText(formatted);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> finish());
    }
}
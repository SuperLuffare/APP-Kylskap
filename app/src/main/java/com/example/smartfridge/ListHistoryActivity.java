package com.example.smartfridge;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListHistoryActivity extends AppCompatActivity {
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
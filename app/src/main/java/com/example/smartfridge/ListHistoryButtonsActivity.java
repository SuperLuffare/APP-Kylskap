package com.example.smartfridge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ListHistoryButtonsActivity extends AppCompatActivity {
    private LinearLayout buttonContainer;
    private AskHandler askHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_history_buttons);

        buttonContainer = findViewById(R.id.buttonContainer);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        List<String> history = readHistory();
        createButtons(history);
    }
    private List<String> readHistory(){
        List<String> history = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(openFileInput("History.txt")));
            String line;
            while((line = reader.readLine()) != null){
                if(!line.trim().isEmpty()){
                    history.add(line);
                }
            }
            reader.close();
        } catch(IOException e){

        }
        return history;
    }
    private void createButtons(List<String> history){
        if(history.isEmpty()){
            Button btn = new Button(this);
            btn.setText("Ingen historik sparad");
            btn.setEnabled(false);
            buttonContainer.addView(btn);
            return;
        }
        for (int i = history.size() - 1; i >= 0; i--) {
            String entry = history.get(i);
            String[] parts = entry.split("\\|", 2);

            if(parts.length < 2) continue;

            String timestamp = parts[0];
            String listData = parts[1];

            Button btn = new Button(this);
            btn.setText(timestamp);

            btn.setOnClickListener(v -> {
                Intent intent = new Intent(this, ListHistoryActivity.class);
                intent.putExtra("LIST_DATA", listData);
                startActivity(intent);
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,16);
            btn.setLayoutParams(params);

            buttonContainer.addView(btn);

        }
    }
}

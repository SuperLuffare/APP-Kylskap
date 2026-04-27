package com.example.smartfridge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class AskActivity extends AppCompatActivity {
    private TextView userOutput;
    private EditText userInput;
    private AskHandler askHandler;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ask);

        userInput = findViewById(R.id.userInput);
        userOutput = findViewById(R.id.userOutput);

        askHandler = new AskHandler();

        ButtonBuilder.setupNavigationButtons(this);

        // Överskriver ButtonBuilders lyssnare och aktiverar knappen
        Button askBtn = findViewById(R.id.askButton);
        askBtn.setEnabled(true);
        askBtn.setOnClickListener(v -> handleText(v));

        Button backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> finish());
    }
    public void handleText(View v) {
        String message = userInput.getText().toString();

        userInput.setText("");
        hideKeyBoard(v);

        askHandler.sendMessage(message, response -> {
            runOnUiThread(() -> {
                userOutput.setText(response);
            });
        });

        userInput.clearFocus();
    }
    public void hideKeyBoard(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }
}
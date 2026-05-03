
package com.example.smartfridge;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AskActivity extends AppCompatActivity {
    private TextView userOutput;
    private EditText userInput;
    private AskHandler askHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ask);

        userInput = findViewById(R.id.userInput);
        userOutput = findViewById(R.id.userOutput);

        askHandler = new AskHandler();

        ButtonBuilder.setupNavigationButtons(this);

        Button askBtn = findViewById(R.id.askButton);
        askBtn.setEnabled(true);
        askBtn.setOnClickListener(v -> handleText(v));

        Button backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(v -> finish());

        // NYTT (snabbknappar)
        Button btnMilk = findViewById(R.id.btnMilk);
        Button btnEggs = findViewById(R.id.btnEggs);
        Button btnJuice = findViewById(R.id.btnJuice);

        btnMilk.setOnClickListener(v -> sendQuick("milk"));
        btnEggs.setOnClickListener(v -> sendQuick("eggs"));
        btnJuice.setOnClickListener(v -> sendQuick("juice"));
    }

    public void handleText(View v) {
        String message = userInput.getText().toString();

        if (message.trim().isEmpty()) {
            userOutput.setText("Skriv in en vara");
            return;
        }

        userInput.setText("");
        hideKeyBoard(v);

        Button askBtn = findViewById(R.id.askButton);
        askBtn.setEnabled(false);

        userOutput.setText("Loading...");

        askHandler.sendMessage(message, response -> {
            runOnUiThread(() -> {

                if (response.startsWith("ERROR")) {
                    userOutput.setText("Kunde inte ansluta till servern");
                } else {
                    userOutput.setText(response);
                }

                askBtn.setEnabled(true);
            });
        });

        userInput.clearFocus();
    }

    private void sendQuick(String product) {
        Button askBtn = findViewById(R.id.askButton);
        askBtn.setEnabled(false);

        userOutput.setText("Loading...");

        askHandler.sendMessage(product, response -> {
            runOnUiThread(() -> {
                if (response.startsWith("ERROR")) {
                    userOutput.setText("Kunde inte ansluta till servern");
                } else {
                    userOutput.setText(response);
                }

                askBtn.setEnabled(true);
            });
        });
    }

    public void hideKeyBoard(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }
}

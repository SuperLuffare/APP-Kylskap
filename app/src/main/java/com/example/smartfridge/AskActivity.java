
package com.example.smartfridge;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Gravity; // NYTT
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout; // NYTT
import android.widget.ScrollView; // NYTT

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AskActivity extends AppCompatActivity {
    private EditText userInput;
    private AskHandler askHandler;
    private LinearLayout chatContainer; // NYTT
    private ScrollView chatScroll; // NYTT

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ask);

        userInput = findViewById(R.id.userInput);

        chatContainer = findViewById(R.id.chatContainer); // NYTT
        chatScroll = findViewById(R.id.chatScroll); // NYTT

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
            return;
        }

        addMessage(message, true); // NYTT

        userInput.setText("");
        hideKeyBoard(v);

        sendMessageToServer(message); //nytt

        userInput.clearFocus();
    }

    private void sendQuick(String product) { //nytt
        addMessage(product, true);
        sendMessageToServer(product);
    }

    private void sendMessageToServer(String message) {
        Button askBtn = findViewById(R.id.askButton);
        askBtn.setEnabled(false);

        askHandler.sendMessage(message, response -> {
            runOnUiThread(() -> {
                try {

                    if (response == null) {
                        addMessage("Inget svar från servern", false); // NYTT
                    } else if (response.startsWith("ERROR")) {
                        addMessage("Kunde inte ansluta till servern", false); // NYTT
                    } else {
                        addMessage(response, false); // NYTT
                    }

                } finally {
                    askBtn.setEnabled(true);
                    scrollToBottom(); // NYTT
                }
            });
        });
    }

    private void addMessage(String text, boolean isUser) { // Hela metoden är ny
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(16);
        tv.setPadding(24, 16, 24, 16);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(8, 8, 8, 8);

        if (isUser) { // NYTT
            tv.setBackgroundResource(R.drawable.user_bubble);
            params.gravity = Gravity.END;
        } else {
            tv.setBackgroundResource(R.drawable.bot_bubble);
            params.gravity = Gravity.START;
        }

        tv.setLayoutParams(params);
        chatContainer.addView(tv);
    }

    private void scrollToBottom() { // NYTT
        chatScroll.post(() -> chatScroll.fullScroll(View.FOCUS_DOWN));
    }

    public void hideKeyBoard(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }
}


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

    /**
     * Skapar Askactivity sidan och lägger till alla knappar och textfält
     * @param savedInstanceState
     * @author Jakob och Sana
     */
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

    /**
     * Tar det användaren skriver in och skickar till servern.
     * @param v View
     * @author Jakob
     */
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
        android.util.Log.d("DEBUG", "sendQuick anromed med: " + product);
        addMessage(product, true);
        android.util.Log.d("DEBUG", "addMessage klar");
        sendMessageToServer(product);
        android.util.Log.d("DEBUG", "sendmessageToServer klar");
    }

    private void sendMessageToServer(String message) {
        Button askBtn = findViewById(R.id.askButton);
        askBtn.setEnabled(false);

        askHandler.sendMessage(message, response -> {
            android.util.Log.d("DEBUG", "svar från servern: " + response);
            runOnUiThread(() -> {
                android.util.Log.d("DEBUG", "UI Trod körs för respons: " + response);
                try {

                    if (response == null) {
                        addMessage("Inget svar från servern", false); // NYTT
                    } else if (response.startsWith("ERROR")) {
                        addMessage("Kunde inte ansluta till servern", false); // NYTT
                    } else {
                        addMessage(response, false); // NYTT
                    }
                } catch(Exception e){
                    android.util.Log.d("DEBUG", "Fel i UI-uppdatering",e);
                }finally {
                    askBtn.setEnabled(true);
                    scrollToBottom(); // NYTT
                }
            });
        });
    }

    private void addMessage(String text, boolean isUser) { // Hela metoden är ny
        android.util.Log.d("DEBUG", "addMessage körs med text: " + text);
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(16);
        tv.setTextColor(android.graphics.Color.RED);
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

    /**
     * Tar ner tangentbordet från skärmen
     * @param v View
     * @author Jakob
     */
    public void hideKeyBoard(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }
}

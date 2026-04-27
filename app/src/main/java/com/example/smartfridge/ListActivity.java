package com.example.smartfridge;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {
    private AskHandler askHandler;
    private ListHistory listHistory;
    private String currentList;

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
                    currentList = response;
                    listHistory.saveListToHistory(response);
                } else {
                    listContent.setText("Kunde inte hämta listan.");
                }
            });
        });
        EditText searchInput = findViewById(R.id.listSearchinput);
        TextView searchResult = findViewById(R.id.searchResult);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String toSearch = s.toString().toLowerCase().trim();
                if(toSearch.isEmpty()){
                    searchResult.setText("");
                    return;
                }
                int amount = findAmountInList(toSearch, currentList);
                searchResult.setText(String.valueOf(amount));
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> finish());


        FloatingActionButton historyBtn = findViewById(R.id.viewHistoryButton);
        historyBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ListActivity.this, ListHistoryButtonsActivity.class);
            startActivity(intent);
        });
    }

    /**
     *
     * @param toSearch
     * @param list
     * @return
     * Tar listan (en lång sträng), tar mort mellanslag med trim, delar upp den långa strängen med split och lägger de enskillda varorna i en array.
     * Från: " milk (1), egg (12), butter (1)"
     * Till: ["milk (1)", "egg (12)", "butter (1)"]
     * Kollar ifall det vi söker efter finns i listan
     */
    private int findAmountInList(String toSearch, String list){
        if(list.isEmpty())return 0;
        String[] items = list.split(",");
        for(String item : items){
            String[] parts = item.trim().split("\\(");
            if(parts.length >= 2){
                String name = parts[0].trim().toLowerCase();
                if(name.equals(toSearch)){
                    String amountStr = parts[1].replace(")","").trim();
                    try{
                        return Integer.parseInt(amountStr);
                    }catch (NumberFormatException e){
                        return 0;
                    }
                }
            }
        }
        return 0;
    }
}
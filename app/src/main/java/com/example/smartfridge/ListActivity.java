package com.example.smartfridge;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;



public class ListActivity extends AppCompatActivity {
    private AskHandler askHandler;
    private ListHistory listHistory;
    private String currentList;

    /**
     * Hämta listan från servern. Formatera listan vi får från servern så att det är en vara per rad. Vi byter bort "," med "\n" (ny rad)
     * @param savedInstanceState
     * @author Jakob
     * @author Sana
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        askHandler = new AskHandler();
        listHistory = new ListHistory(this);

        TextView listContent = findViewById(R.id.listContent);
        listContent.setText("Loading..."); // NYTT

        AutoCompleteTextView searchInput = findViewById(R.id.listSearchinput);
        TextView searchResult = findViewById(R.id.searchResult);

        ArrayAdapter<String> autocompleteAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                new ArrayList<>()
        );
        searchInput.setAdapter(autocompleteAdapter);

        askHandler.sendMessage("LIST", response -> {
            runOnUiThread(() -> {

                if (response == null) { // NYTT
                    listContent.setText("Ingen data"); // NYTT
                    return; // NYTT
                }

                if (!response.startsWith("ERROR")) { // NYTT
                    String formatted = response.replace(",", "\n");
                    listContent.setText(formatted);
                    currentList = response;
                    listHistory.saveListToHistory(response);

                    List<String> itemName = extractItemNames(response);
                    autocompleteAdapter.clear();
                    autocompleteAdapter.addAll(itemName);
                } else {
                    listContent.setText("Kunde inte ansluta till servern."); // NYTT
                }
            });
        });

        searchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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
     * Tar listan (en lång sträng), tar mort mellanslag med trim, delar upp den långa strängen med split och lägger de enskillda varorna i en array.
     * Kollar ifall det vi söker efter finns i listan
     * @param toSearch
     * @param list
     * @return antal
     * @author Jakob
     */
    private int findAmountInList(String toSearch, String list){
        if(list == null || list.isEmpty()) return 0; // NYTT
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
    private List<String> extractItemNames(String list){
        List<String> names = new ArrayList<>();
        if(list == null || list.isEmpty())return names;
        for(String item : list.split(",")){
            String[] parts = item.trim().split("\\(");
            if(parts.length >= 1){
                String name = parts[0].trim();
                if(!name.isEmpty()){
                    names.add(name);
                }
            }
        }
        return names;
    }
}
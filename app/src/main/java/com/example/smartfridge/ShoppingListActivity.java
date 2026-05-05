package com.example.smartfridge;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    private ArrayList<String> shoppingItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    /**
     * Skapar ShoppingListActivity med en lista, ett inmatningsfält och knappar
     * för att lägga till och rensa varor. Listan sparas lokalt och försvinner när man stänger appen.
     * @param savedInstanceState Sparad instans om aktiviteten återstartas
     * @author Jakob
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        //fixar knapparna
        EditText shoppingInput = findViewById(R.id.shoppingInput);
        ListView shoppingListView = findViewById(R.id.shoppingListView);
        Button addButton = findViewById(R.id.addButton);
        Button clearButton = findViewById(R.id.clearButton);
        Button backButton = findViewById(R.id.backButton);


        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                shoppingItems);
        shoppingListView.setAdapter(adapter);

        addButton.setOnClickListener(v -> {
            String item = shoppingInput.getText().toString().trim();
            if (!item.isEmpty()) {
                shoppingItems.add(item);
                adapter.notifyDataSetChanged(); //uppdaterar skärmen (typ)
                shoppingInput.setText("");
            }
        });

        // Tryck länge på en vara i listan för att ta bort den
        shoppingListView.setOnItemLongClickListener((parent, view, position, id) -> {
            shoppingItems.remove(position);
            adapter.notifyDataSetChanged();
            return true;
        });

        clearButton.setOnClickListener(v -> {
            shoppingItems.clear();
            adapter.notifyDataSetChanged();
        });

        backButton.setOnClickListener(v -> finish());
    }
}
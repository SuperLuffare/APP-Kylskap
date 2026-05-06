package com.example.smartfridge;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button disabledButton = null;

    /**
     * Skapar main activity och använder ButtonBuilder för att sätta upp navigationsknapparna
     * @param savedInstanceState
     * @author Jakob
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        EditText ipInput = findViewById(R.id.ipInput);
        EditText portInput = findViewById(R.id.portInput);

        ipInput.setText("10.0.2.2");
        portInput.setText("5100");

        ButtonBuilder.setupNavigationButtons(this);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            String ip = ipInput.getText().toString().trim();
            String portStr = portInput.getText().toString().trim();

            if (ip.isEmpty() || portStr.isEmpty()) return;

            int port; // NYTT

            try { // NYTT
                port = Integer.parseInt(portStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Ogiltig port", Toast.LENGTH_SHORT).show();
                return;
            }

            AskHandler.setConnection(ip, port);
        });
    }

    /**
     * Stänger av knapparna
     * @param v View
     * @author Jakob
     */
    public void disable(View v) {
        if (disabledButton != null) {
            disabledButton.setEnabled(true);
            disabledButton.setText("Enabled");
        }
        Button button = (Button) v;
        button.setEnabled(false);
        disabledButton = button;
        disabledButton.setText("Disabled");
    }
}
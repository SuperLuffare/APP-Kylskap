package com.example.smartfridge;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.smartfridge.ui.login.LoginActivity;

public class ButtonBuilder {

    /**
     * Builderklass för att sätta upp navigationsknapparna till aktiviteterna
     * @param activity akvivitenen vi är på
     * @author Jakob
     */
    public static void setupNavigationButtons(Activity activity){
        if(!(activity instanceof MainActivity)){
            Button backBtn = activity.findViewById(R.id.backButton);
            if(backBtn != null) {
                backBtn.setOnClickListener(v -> activity.finish());
            }
        } else {
            Button loginBtn = activity.findViewById(R.id.loginButton);
            if(loginBtn != null) {
                loginBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                });
            }
        }

        Button askButton = activity.findViewById(R.id.askButton);
        if(askButton != null) {
            askButton.setOnClickListener(v -> {
                Intent intent = new Intent(activity, AskActivity.class);
                activity.startActivity(intent);
            });
        }

        Button listButton = activity.findViewById(R.id.listButton);
        if(listButton != null) {
            listButton.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ListActivity.class);
                activity.startActivity(intent);
            });
        }

        Button recipieButton = activity.findViewById(R.id.recipieButton);
        if(recipieButton != null) {
            recipieButton.setOnClickListener(v -> {
                Intent intent = new Intent(activity, RecipiesActivity.class);
                activity.startActivity(intent);
            });
        }
    }
    public static void listButton(Activity activity){

    }

}

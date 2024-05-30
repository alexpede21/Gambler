package com.example.slotmachine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private Button slotButton;
    private Button blackjackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);

        slotButton = findViewById(R.id.slotButton);
        blackjackButton = findViewById(R.id.blackjackButton);

        slotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToSlot();
            }
        });
    }

    private void changeToSlot(){
        setContentView(R.layout.activity_main);
        //Intent intent = new Intent(getApplicationContext(), );
        //startActivity(intent);
    }
}

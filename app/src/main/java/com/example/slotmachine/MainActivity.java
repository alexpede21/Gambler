package com.example.slotmachine;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private double currentBet = 0;
    private TextView currentBetTextView;

    private ImageView[] slots;
    private Button spinButton;
    private Button plusButton;
    private Button minusButton;
    private int[] images = {R.drawable.bar, R.drawable.cherry, R.drawable.diamond, R.drawable.lemon,
            R.drawable.orange, R.drawable.seven};
    private Random random = new Random();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slots = new ImageView[] {
                findViewById(R.id.slot1_top), findViewById(R.id.slot2_top), findViewById(R.id.slot3_top),
                findViewById(R.id.slot1_middle), findViewById(R.id.slot2_middle), findViewById(R.id.slot3_middle),
                findViewById(R.id.slot1_bottom), findViewById(R.id.slot2_bottom), findViewById(R.id.slot3_bottom)
        };

        spinButton = findViewById(R.id.spinButton);
        plusButton = findViewById(R.id.plusButton);
        minusButton = findViewById(R.id.minusButton);

        currentBetTextView = findViewById(R.id.current_bet);

        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinSlots();
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBet();
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtractBet();
            }
        });
    }

    private void addBet(){
        currentBet += 0.25;
        updateBetText();
    }

    private void subtractBet(){
        currentBet -= 0.25;
        updateBetText();
    }

    private void updateBetText(){
        currentBetTextView.setText("Current Bet: " + currentBet + "$");
    }

    private void spinSlots() {
        spinButton.setEnabled(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (ImageView slot : slots) {
                    int imageIndex = random.nextInt(images.length);
                    slot.setImageResource(images[imageIndex]);
                }
                spinButton.setEnabled(true);
            }
        }, 1000); // 1 second delay to simulate spinning
    }
}

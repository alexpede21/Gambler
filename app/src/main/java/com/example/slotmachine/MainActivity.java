package com.example.slotmachine;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        slots = new ImageView[]{
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

    private void addBet() {
        currentBet += 0.25;
        updateBetText();
    }

    private void subtractBet() {
        if (currentBet >= 0.25) {
            currentBet -= 0.25;
            updateBetText();
        }
    }

    private void updateBetText() {
        currentBetTextView.setText("Current Bet: " + currentBet + "$");
    }

    private void spinSlots() {
        spinButton.setEnabled(false);

        for (final ImageView slot : slots) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(slot, "rotationY", 0f, 360f);
            animator.setDuration(1000); // 1 second for each spin
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.start();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] middleRowImages = new int[3];
                for (int i = 0; i < slots.length; i++) {
                    int imageIndex = random.nextInt(images.length);
                    slots[i].setImageResource(images[imageIndex]);
                    // Save the middle row images
                    if (i >= 3 && i < 6) {
                        middleRowImages[i - 3] = imageIndex;
                    }
                }
                spinButton.setEnabled(true);
                checkWin(middleRowImages);
            }
        }, 1000); // 1 second delay to simulate spinning
    }

    private void checkWin(int[] middleRowImages) {
        if (middleRowImages[0] == middleRowImages[1] && middleRowImages[1] == middleRowImages[2]) {
            Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show();
            // Implement the winning logic here, e.g., updating the balance
        }
    }
}

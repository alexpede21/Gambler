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
    private double moneyWon = 0;
    private TextView currentBetTextView;
    private TextView moneyWonTextView;

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
        moneyWonTextView = findViewById(R.id.money_won);


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

    private void applyGlowEffect(final ImageView imageView) {
        final int glowCount = 10;
        final long duration = 800;

        imageView.animate().alpha(0.5f).setDuration(duration).withEndAction(new Runnable() {
            @Override
            public void run() {
                imageView.animate().alpha(1f).setDuration(duration).start();
            }
        }).start();

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
                int[] slotImages = new int[slots.length];
                for (int i = 0; i < slots.length; i++) {
                    int imageIndex = random.nextInt(images.length);
                    slots[i].setImageResource(images[imageIndex]);
                    slotImages[i] = imageIndex;
                }
                spinButton.setEnabled(true);
                checkWin(slotImages);
            }
        }, 1000); // 1 second delay to simulate spinning
    }

    private void checkWin(int[] slotImages) {
        boolean isWin = false;
        double winMultiplier = 0;

        int[][] lines = {
                // Rows
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                // Columns
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                // Diagonals
                {0, 4, 8}, {2, 4, 6}
        };

        // Check horizontal, vertical, and diagonal lines
        for (int[] line : lines) {
            if (slotImages[line[0]] == slotImages[line[1]] && slotImages[line[1]] == slotImages[line[2]]) {
                isWin = true;
                winMultiplier = (line.length == 3) ? 3 : 6; // 3 for rows/columns, 6 for diagonals
                for (int index : line) {
                    applyGlowEffect(slots[index]);
                }
                break; // If any line is a win, we can break out of the loop
            }
        }

        // Check 2x2 blocks
        int[][] blocks = {
                {0, 1, 3, 4}, {1, 2, 4, 5}, {3, 4, 6, 7}, {4, 5, 7, 8}
        };

        for (int[] block : blocks) {
            if (slotImages[block[0]] == slotImages[block[1]] && slotImages[block[1]] == slotImages[block[2]] && slotImages[block[2]] == slotImages[block[3]]) {
                isWin = true;
                winMultiplier = 4;
                for (int index : block) {
                    applyGlowEffect(slots[index]);
                }
                break; // If any block is a win, we can break out of the loop
            }
        }

        // Check special condition
        if ((slotImages[1] == slotImages[3] && slotImages[3] == slotImages[5]) ||
                (slotImages[7] == slotImages[3] && slotImages[3] == slotImages[5])) {
            isWin = true;
            winMultiplier = 5;
            int[] specialSlots = (slotImages[1] == slotImages[3]) ? new int[]{1, 3, 5} : new int[]{7, 3, 5};
            for (int index : specialSlots) {
                applyGlowEffect(slots[index]);
            }
        }

        // Check 3x3 block
        boolean is3x3Win = true;
        for (int i = 1; i < 9; i++) {
            if (slotImages[i] != slotImages[0]) {
                is3x3Win = false;
                break;
            }
        }

        if (is3x3Win) {
            isWin = true;
            winMultiplier = 10;
            for (ImageView slot : slots) {
                applyGlowEffect(slot);
            }
        }

        // Display win/loss message
        if (isWin) {
            moneyWon = currentBet * winMultiplier;
            moneyWonTextView.setText("Money Won: " + moneyWon + "$");
        } else {
            moneyWonTextView.setText("NO Money WON");
        }
    }

}

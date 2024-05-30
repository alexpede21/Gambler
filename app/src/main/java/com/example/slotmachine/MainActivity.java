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
        // Extract rows and columns
        int[] topRow = {slotImages[0], slotImages[1], slotImages[2]};
        int[] middleRow = {slotImages[3], slotImages[4], slotImages[5]};
        int[] bottomRow = {slotImages[6], slotImages[7], slotImages[8]};
        int[] leftColumn = {slotImages[0], slotImages[3], slotImages[6]};
        int[] middleColumn = {slotImages[1], slotImages[4], slotImages[7]};
        int[] rightColumn = {slotImages[2], slotImages[5], slotImages[8]};

        boolean isWin = false;
        double winMultiplier = 0;

        // Check horizontal lines
        if ((topRow[0] == topRow[1] && topRow[1] == topRow[2]) ||
                (middleRow[0] == middleRow[1] && middleRow[1] == middleRow[2]) ||
                (bottomRow[0] == bottomRow[1] && bottomRow[1] == bottomRow[2])) {
            isWin = true;
            winMultiplier = 3;
            if (topRow[0] == topRow[1] && topRow[1] == topRow[2]) {
                applyGlowEffect(slots[0]);
                applyGlowEffect(slots[1]);
                applyGlowEffect(slots[2]);
            }
            if (middleRow[0] == middleRow[1] && middleRow[1] == middleRow[2]) {
                applyGlowEffect(slots[3]);
                applyGlowEffect(slots[4]);
                applyGlowEffect(slots[5]);
            }
            if (bottomRow[0] == bottomRow[1] && bottomRow[1] == bottomRow[2]) {
                applyGlowEffect(slots[6]);
                applyGlowEffect(slots[7]);
                applyGlowEffect(slots[8]);
            }
        }

        // Check vertical lines
        if ((leftColumn[0] == leftColumn[1] && leftColumn[1] == leftColumn[2]) ||
                (middleColumn[0] == middleColumn[1] && middleColumn[1] == middleColumn[2]) ||
                (rightColumn[0] == rightColumn[1] && rightColumn[1] == rightColumn[2])) {
            isWin = true;
            winMultiplier = 3;
            if (leftColumn[0] == leftColumn[1] && leftColumn[1] == leftColumn[2]) {
                applyGlowEffect(slots[0]);
                applyGlowEffect(slots[3]);
                applyGlowEffect(slots[6]);
            }
            if (middleColumn[0] == middleColumn[1] && middleColumn[1] == middleColumn[2]) {
                applyGlowEffect(slots[1]);
                applyGlowEffect(slots[4]);
                applyGlowEffect(slots[7]);
            }
            if (rightColumn[0] == rightColumn[1] && rightColumn[1] == rightColumn[2]) {
                applyGlowEffect(slots[2]);
                applyGlowEffect(slots[5]);
                applyGlowEffect(slots[8]);
            }
        }

        // Check 2x2 blocks
        if ((slotImages[0] == slotImages[1] && slotImages[1] == slotImages[3] && slotImages[3] == slotImages[4]) ||
                (slotImages[1] == slotImages[2] && slotImages[2] == slotImages[4] && slotImages[4] == slotImages[5]) ||
                (slotImages[3] == slotImages[4] && slotImages[4] == slotImages[6] && slotImages[6] == slotImages[7]) ||
                (slotImages[4] == slotImages[5] && slotImages[5] == slotImages[7] && slotImages[7] == slotImages[8])) {
            isWin = true;
            winMultiplier = 4;
            if (slotImages[0] == slotImages[1] && slotImages[1] == slotImages[3] && slotImages[3] == slotImages[4]) {
                applyGlowEffect(slots[0]);
                applyGlowEffect(slots[1]);
                applyGlowEffect(slots[3]);
                applyGlowEffect(slots[4]);
            }
            if (slotImages[1] == slotImages[2] && slotImages[2] == slotImages[4] && slotImages[4] == slotImages[5]) {
                applyGlowEffect(slots[1]);
                applyGlowEffect(slots[2]);
                applyGlowEffect(slots[4]);
                applyGlowEffect(slots[5]);
            }
            if (slotImages[3] == slotImages[4] && slotImages[4] == slotImages[6] && slotImages[6] == slotImages[7]) {
                applyGlowEffect(slots[3]);
                applyGlowEffect(slots[4]);
                applyGlowEffect(slots[6]);
                applyGlowEffect(slots[7]);
            }
            if (slotImages[4] == slotImages[5] && slotImages[5] == slotImages[7] && slotImages[7] == slotImages[8]) {
                applyGlowEffect(slots[4]);
                applyGlowEffect(slots[5]);
                applyGlowEffect(slots[7]);
                applyGlowEffect(slots[8]);
            }
        }
        // Check special condition: middle of top or bottom row same as left and right of middle row
        if ((topRow[1] == middleRow[0] && middleRow[0] == middleRow[2]) ||
                (bottomRow[1] == middleRow[0] && middleRow[0] == middleRow[2])) {
            isWin = true;
            winMultiplier = 5;
            if (topRow[1] == middleRow[0] && middleRow[0] == middleRow[2]) {
                applyGlowEffect(slots[1]);
                applyGlowEffect(slots[3]);
                applyGlowEffect(slots[5]);
            }
            if (bottomRow[1] == middleRow[0] && middleRow[0] == middleRow[2]) {
                applyGlowEffect(slots[7]);
                applyGlowEffect(slots[3]);
                applyGlowEffect(slots[5]);
            }

        }
        // Check diagonal lines
        if ((slotImages[0] == slotImages[4] && slotImages[4] == slotImages[8]) ||  // top-left to bottom-right
                (slotImages[2] == slotImages[4] && slotImages[4] == slotImages[6])) {  // top-right to bottom-left
            isWin = true;
            winMultiplier = 6;
            if (slotImages[0] == slotImages[4] && slotImages[4] == slotImages[8]) {  // top-left to bottom-right
                applyGlowEffect(slots[0]);
                applyGlowEffect(slots[4]);
                applyGlowEffect(slots[8]);
            }
            if (slotImages[2] == slotImages[4] && slotImages[4] == slotImages[6]) {  // top-right to bottom-left
                applyGlowEffect(slots[2]);
                applyGlowEffect(slots[4]);
                applyGlowEffect(slots[6]);
            }
        }

        // Check 3x3 block
        if (slotImages[0] == slotImages[1] && slotImages[1] == slotImages[2] &&
                slotImages[2] == slotImages[3] && slotImages[3] == slotImages[4] &&
                slotImages[4] == slotImages[5] && slotImages[5] == slotImages[6] &&
                slotImages[6] == slotImages[7] && slotImages[7] == slotImages[8]) {
            isWin = true;
            winMultiplier = 10;
            for (ImageView slot : slots) {
                applyGlowEffect(slot);
            }
        }


        if (isWin) {
            moneyWon = currentBet * winMultiplier;
            moneyWonTextView.setText("Money Won: " + moneyWon + "$");
        } else {
            moneyWonTextView.setText("NO Money WON");
        }
    }
}

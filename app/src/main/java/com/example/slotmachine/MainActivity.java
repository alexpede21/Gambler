package com.example.slotmachine;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView slot1, slot2, slot3;
    private Button spinButton;
    private int[] images = {R.drawable.bar, R.drawable.cherry, R.drawable.diamond, R.drawable.lemon,
            R.drawable.orange, R.drawable.seven};
    private Random random = new Random();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slot1 = findViewById(R.id.slot1);
        slot2 = findViewById(R.id.slot2);
        slot3 = findViewById(R.id.slot3);
        spinButton = findViewById(R.id.spinButton);

        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinSlots();
            }
        });
    }

    private void spinSlots() {
        spinButton.setEnabled(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int imageIndex1 = random.nextInt(images.length);
                int imageIndex2 = random.nextInt(images.length);
                int imageIndex3 = random.nextInt(images.length);

                slot1.setImageResource(images[imageIndex1]);
                slot2.setImageResource(images[imageIndex2]);
                slot3.setImageResource(images[imageIndex3]);

                spinButton.setEnabled(true);
            }
        }, 1000); // 1 second delay to simulate spinning
    }
}

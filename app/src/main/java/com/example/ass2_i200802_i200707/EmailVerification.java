package com.example.ass2_i200802_i200707;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class EmailVerification extends AppCompatActivity {

    //as soon button in dial pad is selected -> shift focus to next edit box
    EditText confirmationText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        ImageView login = findViewById(R.id.backArrow);
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailVerification.this, Registration.class);
                startActivity(intent);
            }
        });

        // Keypad Buttons
        Button[] keypadButtons = {
                findViewById(R.id.zero),
                findViewById(R.id.one),
                findViewById(R.id.two),
                findViewById(R.id.three),
                findViewById(R.id.four),
                findViewById(R.id.five),
                findViewById(R.id.six),
                findViewById(R.id.seven),
                findViewById(R.id.eight),
                findViewById(R.id.nine)
        };
        final int[] count = {0}; // Initialize the count variable



        for (int i = 0; i < keypadButtons.length; i++)
        {
            final int index = i;
            keypadButtons[i].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    switch (count[0])
                    {
                        case 0:
                            confirmationText = findViewById(R.id.num1);
                            break;
                        case 1:
                            confirmationText = findViewById(R.id.num2);
                            break;
                        case 2:
                            confirmationText = findViewById(R.id.num3);
                            break;
                        case 3:
                            confirmationText = findViewById(R.id.num4);
                            break;
                        case 4:
                            confirmationText = findViewById(R.id.num5);
                            break;
                        default:
                            confirmationText = null;
                            break;
                    }

                    count[0]++;

                    if (confirmationText != null) {
                        String currentText = String.valueOf(index);
                        confirmationText.setText(currentText);
                        confirmationText.requestFocus();
                    }
                }
            });
        }

        // Backspace button
        Button backspaceButton = findViewById(R.id.backspace);
        backspaceButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DiscouragedApi")
            @Override
            public void onClick(View v) {
                EditText confText2 = confirmationText;
                if (confText2 != null) {
                    String currentText = confText2.getText().toString();
                    if (!currentText.isEmpty()) {
                        // Remove the last character from the text
                        currentText = currentText.substring(0, currentText.length() - 1);
                        confirmationText.setText(currentText);
                        int previousIndex = count[0]- 1;
                        count[0] = previousIndex;
                    } else {
                        // If the current EditText is empty, move focus to the previous one
                        int previousIndex = count[0]- 1;
                        if (previousIndex >= 0 && previousIndex < keypadButtons.length) {
                            count[0] = previousIndex;
                            confirmationText = findViewById(getResources().getIdentifier("id" + (previousIndex + 1), "id", getPackageName()));
                            confirmationText.requestFocus();
                        }
                    }
                }
            }
        });

    }
}
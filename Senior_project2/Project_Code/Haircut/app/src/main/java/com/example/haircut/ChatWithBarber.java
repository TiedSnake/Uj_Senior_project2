package com.example.haircut;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ChatWithBarber extends AppCompatActivity {

    private LinearLayout chatContainer;
    private EditText messageInput;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_with_barber);  // Set to the chat_with_barber layout

        // Initialize views
        chatContainer = findViewById(R.id.chat_container);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        ImageButton goBackButton = findViewById(R.id.go_back_button);

        // Set a click listener on the go back button
        goBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChatWithBarber.this, customer.class);
            startActivity(intent);  // Navigate back to the customer page
            finish();  // Close the chat activity
        });

        // Set a click listener on the send button
        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                addMessageToChat("You: " + message);  // Add customer's message to chat
                messageInput.setText("");  // Clear the input field
                receiveMessageFromBarber();  // Simulate receiving a message from the barber
            }
        });
    }

    private void addMessageToChat(String message) {
        TextView textView = new TextView(this);
        textView.setText(message);
        chatContainer.addView(textView);
    }

    private void receiveMessageFromBarber() {
        addMessageToChat("Sheraton: Thank you for reaching out! How can we assist you?");
    }
}

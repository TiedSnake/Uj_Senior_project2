package com.haircut.frontend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.haircut.R;

public class fragment_chat extends Fragment {

    private LinearLayout chatContainer;
    private EditText messageInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatContainer = view.findViewById(R.id.chat_container);
        messageInput = view.findViewById(R.id.message_input);
        Button sendButton = view.findViewById(R.id.send_button);

        // Set up send button to add messages to the chat
        sendButton.setOnClickListener(v -> sendMessage());

        return view;
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // Create a new TextView for the message
            TextView messageView = new TextView(getContext());
            messageView.setText(messageText);
            messageView.setTextSize(16);
            chatContainer.addView(messageView);

            // Clear the input
            messageInput.setText("");
        }
    }
}

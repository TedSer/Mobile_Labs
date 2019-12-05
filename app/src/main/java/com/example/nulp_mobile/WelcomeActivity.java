package com.example.nulp_mobile;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class WelcomeActivity extends FragmentActivity {

    private TextView welcomeTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();
        welcomeTextView = findViewById(R.id.welcome_text_view);

        String currentUserName = Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName();
        String welcomeMessage = String.format("Welcome, %s", currentUserName);

        welcomeTextView.setText(welcomeMessage);
    }
}

package com.example.nulp_mobile;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends FragmentActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                    Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PHONE_NUMBER =
            Pattern.compile("(\\+[0-9]{12})|([0-9]{12})|([0-9]{10})");
    private FragmentTransaction mFragmentTransaction;
    private Fragment mSignInFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        //load sign in fragment by default
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        mSignInFragment = new SignInFragment();
        mFragmentTransaction.replace(R.id.auth_container, mSignInFragment);
        mFragmentTransaction.commit();

    }

    public ValidationResult validateEmailInput(String inputText) {
        if(inputText.isEmpty()) {
            return ValidationResult.THIS_FIELD_CANNOT_BE_EMPTY;
        } else if(isEmailFormatCorrect(inputText.trim())) {
            return ValidationResult.FINE;
        } else {
            return ValidationResult.EMAIL_IS_NOT_VALID;
        }
    }

    public ValidationResult validatePasswordInput(String inputText) {
        if(inputText.trim().isEmpty()) {
            return ValidationResult.THIS_FIELD_CANNOT_BE_EMPTY;
        } else if(inputText.trim().length() < 8) {
            return ValidationResult.PASSWORD_SHOUD_CONTAINT_AT_LEAST_8_CHARACTERS;
        } else {
            return ValidationResult.FINE;
        }
    }

    public ValidationResult validatePhoneInput(String inputText) {
        if(inputText.isEmpty()) {
            return ValidationResult.THIS_FIELD_CANNOT_BE_EMPTY;
        } else if(isPhoneFormatCorrect(inputText.trim())) {
            return ValidationResult.FINE;
        } else {
            return ValidationResult.EMAIL_IS_NOT_VALID;
        }
    }

    public ValidationResult validateNameInput(String inputText) {
        if (inputText.trim().isEmpty()) {
            return ValidationResult.THIS_FIELD_CANNOT_BE_EMPTY;
        } else {
            return ValidationResult.FINE;
        }
    }


    public boolean isPhoneFormatCorrect(String input) {
        Matcher matcher = VALID_PHONE_NUMBER.matcher(input);
        return matcher.matches();
    }

    public boolean isEmailFormatCorrect(String input) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(input);
        return matcher.matches();
    }


    public void onSignInSucces() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void onSignInFail() {
        String errorMessage = getResources().getString(R.string.firebase_error_message);
        Toast.makeText(this,errorMessage,
                Toast.LENGTH_SHORT).show();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}


package com.example.nulp_mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import java.util.Objects;


public class SignUpFragment extends Fragment {

    private TextInputLayout mEmailInput;
    private TextInputLayout mPasswordInput;
    private TextInputLayout mPhoneInput;
    private TextInputLayout mNameInput;
    private TextInputEditText mEmailEditText;
    private TextInputEditText mPasswordEditText;
    private TextInputEditText mPhoneEditText;
    private TextInputEditText mNameEditText;
    private Button mSignUpButton;
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeView();
        initializeOnFocusChangeListeners();
        initializeOnClickListeners();
    }

    private void createUser(final String name, final String email, final String phone, final String password) {
        boolean isNetworkAvailable = ((MainActivity) Objects.requireNonNull(getActivity())).isNetworkAvailable();
        if (isNetworkAvailable) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();

                        Objects.requireNonNull(user).updateProfile(profileChangeRequest)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            ((MainActivity) Objects.requireNonNull(getActivity())).onSignInSucces();
                                        } else {
                                            ((MainActivity) Objects.requireNonNull(getActivity())).onSignInFail();
                                        }
                                    }
                                });
                    } else {
                        String errorString = getResources().getString(R.string.no_network_message);
                        Toast.makeText(getActivity(), errorString, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void initializeView() {
        if (getActivity() != null) {
            mEmailInput = getActivity().findViewById(R.id.email_input);
            mPasswordInput = getActivity().findViewById(R.id.password_input);
            mPhoneInput = getActivity().findViewById(R.id.phone_input);
            mNameInput = getActivity().findViewById(R.id.name_input);
            mEmailEditText = getActivity().findViewById(R.id.email_edit_text);
            mPasswordEditText = getActivity().findViewById(R.id.password_edit_text);
            mPhoneEditText = getActivity().findViewById(R.id.phone_edit_text);
            mNameEditText = getActivity().findViewById(R.id.name_edit_text);
            mSignUpButton = getActivity().findViewById(R.id.sign_up_button);
        }
    }

    private void initializeOnFocusChangeListeners() {
        mEmailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String inputText = ((EditText) view).getText().toString();
                    ValidationResult displayText = ((MainActivity) Objects.requireNonNull(getActivity()))
                            .validateEmailInput(inputText);

                    if (displayText == ValidationResult.FINE) {
                        mEmailInput.setErrorEnabled(false);
                    } else {
                        mEmailInput.setError(displayText.getMessage());
                    }
                }
            }
        });

        mPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String inputText = ((EditText) view).getText().toString();
                    ValidationResult displayText = ((MainActivity) Objects.requireNonNull(getActivity()))
                            .validatePasswordInput(inputText);

                    if (displayText == ValidationResult.FINE) {
                        mPasswordInput.setErrorEnabled(false);
                    } else {
                        mPasswordInput.setError(displayText.getMessage());
                    }
                }
            }
        });

        mPhoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String inputText = ((EditText) v).getText().toString();
                    ValidationResult phoneValid = ((MainActivity) Objects.requireNonNull(getActivity()))
                            .validatePhoneInput(inputText);

                    if (phoneValid == ValidationResult.FINE) {
                        mPhoneInput.setErrorEnabled(false);
                    } else {
                        mPhoneInput.setError(ValidationResult.PHONE_NUMBER_IS_NOT_VALID.getMessage());
                    }
                }
            }
        });

        mNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String inputText = ((EditText) v).getText().toString();
                    ValidationResult nameValid = ((MainActivity) Objects.requireNonNull(getActivity()))
                            .validateNameInput(inputText);

                    if (nameValid == ValidationResult.FINE) {
                        mNameInput.setErrorEnabled(false);
                    } else {
                        mNameInput.setError(ValidationResult.THIS_FIELD_CANNOT_BE_EMPTY.getMessage());
                    }
                }
            }
        });
    }

    private void initializeOnClickListeners() {
        mAuth = FirebaseAuth.getInstance();

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = Objects.requireNonNull(mEmailEditText.getText()).toString();
                ValidationResult emailValidation = ((MainActivity) Objects.requireNonNull(getActivity()))
                        .validateEmailInput(emailText);

                String passwordText = Objects.requireNonNull(mPasswordEditText.getText()).toString();
                ValidationResult passwordValidation = ((MainActivity) Objects.requireNonNull(getActivity()))
                        .validatePasswordInput(passwordText);

                String phoneText = Objects.requireNonNull(mPhoneEditText.getText()).toString();
                ValidationResult isPhoneValid = ((MainActivity) Objects.requireNonNull(getActivity()))
                        .validatePhoneInput(phoneText);

                String nameText = Objects.requireNonNull(mNameEditText.getText()).toString();
                ValidationResult isPasswordValid = ((MainActivity)Objects.requireNonNull(getActivity()))
                        .validateNameInput(nameText);

                if (
                        emailValidation == ValidationResult.FINE
                                && passwordValidation == ValidationResult.FINE
                                && isPhoneValid == ValidationResult.FINE
                                && isPasswordValid == ValidationResult.FINE
                ) {
                    createUser(nameText, emailText, phoneText, passwordText);
                }
            }
        });
    }
}

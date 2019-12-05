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
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class SignInFragment extends Fragment {


    private TextInputLayout mEmailInput;
    private TextInputLayout mPasswordInput;
    private TextInputEditText mEmailEditText;
    private TextInputEditText mPasswordEditText;
    private Button mSignInButton;
    private Fragment mSignUpFragment;
    private Button mSignUpSwitchButton;
    private FragmentTransaction mFragmentTransaction;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_sign_in, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews();

        initializeOnFocusChangeListeners();

        initializeOnClickListeners();

    }

    private void initializeViews() {
        if (getActivity() != null) {
            mEmailInput = getActivity().findViewById(R.id.email_input);
            mPasswordInput = getActivity().findViewById(R.id.password_input);
            mEmailEditText = getActivity().findViewById(R.id.email_edit_text);
            mPasswordEditText = getActivity().findViewById(R.id.password_edit_text);
            mSignUpSwitchButton = getActivity().findViewById(R.id.sign_up_switch);
            mSignInButton = getActivity().findViewById(R.id.sign_in_button);
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
    }

    private void initializeOnClickListeners() {
        mAuth = FirebaseAuth.getInstance();
        mFragmentTransaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();

        mSignUpSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignUpFragment = new SignUpFragment();

                mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                mFragmentTransaction.replace(R.id.auth_container, mSignUpFragment);
                mFragmentTransaction.addToBackStack(null);

                mFragmentTransaction.commit();
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmailEditText.getText() != null && getActivity() != null && mPasswordEditText.getText() != null) {
                    String emailText = mEmailEditText.getText().toString();


                    ValidationResult emailValidation = ((MainActivity) getActivity())
                            .validateEmailInput(emailText);


                    String passwordText = mPasswordEditText.getText().toString();
                    ValidationResult passwordValidation = ((MainActivity) Objects.requireNonNull(getActivity()))
                            .validatePasswordInput(passwordText);

                    if (emailValidation == ValidationResult.FINE &&
                            passwordValidation == ValidationResult.FINE) {
                        boolean isNetworkAvailable = ((MainActivity) Objects.requireNonNull(getActivity())).isNetworkAvailable();
                        if (isNetworkAvailable) {
                            mAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        ((MainActivity) getActivity()).onSignInSucces();
                                    } else {
                                        ((MainActivity) getActivity()).onSignInFail();
                                    }
                                }
                            });
                        } else {
                            String errorString = getResources().getString(R.string.no_network_message);
                            Toast.makeText(getActivity(), errorString, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}

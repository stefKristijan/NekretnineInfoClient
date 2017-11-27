package com.kstefancic.nekretnineinfo.LoginAndRegister;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kstefancic.nekretnineinfo.R;
import com.kstefancic.nekretnineinfo.api.model.User;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by user on 2.11.2017..
 */

public class RegisterFragment extends Fragment {

    private static final String PASSWORD_DISMATCH = "Lozinke se ne poklapaju. Pokušajte ponovno.";
    private static final String EMAIL_NOT_VALID = "Vaš e-mail nije dobrog formata. Pokušajte ponovno.";
    private static final String FIRST_NAME_EMPTY = "Unesite ime";
    private static final String LAST_NAME_EMPTY = "Unesite prezime";
    private static final String USERNAME_EMPTY = "Unesite korisničko ime";
    private static final String PASSWORD_EMPTY = "Unesite lozinku";
    private static final String EMAIL_EMPTY = "Unesite e-mail";
    private static final String USERNAME_LENGTH_NOT_VALID = "Korisničko ime se mora sastojati od 5 do 20 znakova";
    private static final String PASSWORD_LENGTH_NOT_VALID = "Lozinka mora sadržavati više od 5 znakova";
    private static final String USERNAME_FORMAT_NOT_VALID = "Neispravan format korisničkog imena";

    private EditText etFirstName, etLastName, etUsername, etPassword, etConfirmPassword, etEmail;
    private TextInputLayout tilFirstName, tilLastName, tilUsername, tilPassword, tilConfirmPassword, tilEmail;
    private Button btnRegister;
    private TextView tvLogIn;
    private UserDataInsertedListener mUserDataInsertedListener;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_register,null);
        setUI(layout);
        return layout;
    }

    private void setUI(View layout) {
        this.tilConfirmPassword = layout.findViewById(R.id.registerFr_tilConfirmPass);
        this.tilEmail = layout.findViewById(R.id.registerFr_tilEmail);
        this.tilFirstName = layout.findViewById(R.id.registerFr_tilFirstName);
        this.tilLastName = layout.findViewById(R.id.registerFr_tilLastName);
        this.tilPassword = layout.findViewById(R.id.registerFr_tilPassword);
        this.tilUsername = layout.findViewById(R.id.registerFr_tilUsername);
        this.etFirstName = layout.findViewById(R.id.registerFr_etFirstName);
        this.etLastName = layout.findViewById(R.id.registerFr_etLastName);
        this.etUsername = layout.findViewById(R.id.registerFr_etUsername);
        this.etPassword = layout.findViewById(R.id.registerFr_etPassword);
        this.etConfirmPassword = layout.findViewById(R.id.registerFr_etConfirmPass);
        this.etEmail = layout.findViewById(R.id.registerFr_etEmail);
        this.tvLogIn = layout.findViewById(R.id.registerFr_tvLogin);
        this.tvLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.activityLogin_fl, new LogInFragment());
                fragmentTransaction.commit();
            }
        });
        this.btnRegister = layout.findViewById(R.id.registerFr_btnRegister);
        this.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPass = etConfirmPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        if (checkFields(firstName, lastName, username, password, confirmPass, email)) {
            User user = new User(UUID.randomUUID().toString(),firstName,lastName,username,password,email);
            mUserDataInsertedListener.onUserDataInsertedListener(user);
        }
    }

    private boolean checkFields(String firstName, String lastName, String username, String password, String confirmPass, String email) {

        boolean isValid=true;
        refreshErrors();

        //CHECK first name field
        if(firstName.isEmpty()){
            this.tilFirstName.setError(FIRST_NAME_EMPTY);
            isValid=false;
        }

        //CHECK last name field
        if(lastName.isEmpty()){
            this.tilLastName.setError(LAST_NAME_EMPTY);
            isValid=false;
        }

        //CHECK username field
        if(username.isEmpty()){
            this.tilUsername.setError(USERNAME_EMPTY);
            isValid=false;
        } else if(username.length()<5 && username.length()<20) {
            tilUsername.setError(USERNAME_LENGTH_NOT_VALID);
            isValid = false;
        }else if (!Pattern.matches("^(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?![_.])$", username)) {
            tilUsername.setError(USERNAME_FORMAT_NOT_VALID);
            isValid = false;
        }

        //CHECK password field
        if(password.isEmpty()){
            this.tilPassword.setError(PASSWORD_EMPTY);
            isValid=false;
        }else if(password.length()<5){
            tilPassword.setError(PASSWORD_LENGTH_NOT_VALID);
            isValid=false;
        }else if (!password.equals(confirmPass)) {
            tilConfirmPassword.setError(PASSWORD_DISMATCH);
            tilPassword.setError(PASSWORD_DISMATCH);
            isValid = false;
        }

        //CHECK email field
        if(email.isEmpty()) {
            this.tilEmail.setError(EMAIL_EMPTY);
            isValid = false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(EMAIL_NOT_VALID);
            isValid = false;
        }

        return isValid;
    }

    private void refreshErrors() {
        this.tilEmail.setErrorEnabled(false);
        this.tilPassword.setErrorEnabled(false);
        this.tilConfirmPassword.setErrorEnabled(false);
        this.tilFirstName.setErrorEnabled(false);
        this.tilLastName.setErrorEnabled(false);
        this.tilConfirmPassword.setErrorEnabled(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof UserDataInsertedListener)
        {
            this.mUserDataInsertedListener = (UserDataInsertedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mUserDataInsertedListener=null;
    }

    public interface UserDataInsertedListener{
        void onUserDataInsertedListener(User user);
    }


}

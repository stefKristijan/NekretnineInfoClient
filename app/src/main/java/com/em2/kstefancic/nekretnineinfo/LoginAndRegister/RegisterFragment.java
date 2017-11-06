package com.em2.kstefancic.nekretnineinfo.LoginAndRegister;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.em2.kstefancic.nekretnineinfo.R;
import com.em2.kstefancic.nekretnineinfo.api.model.User;

import java.util.UUID;

/**
 * Created by user on 2.11.2017..
 */

public class RegisterFragment extends Fragment {

    private static final String EMPTY_FIELDS = "Unesite podatke u sva polja.";
    private static final String PASSWORD_DISMATCH = "Lozinke se ne poklapaju. Pokušajte ponovno.";
    private static final String EMAIL_NOT_VALID = "Vaš e-mail nije dobrog formata. Pokušajte ponovno.";
    private static final String SHORT_USERNAME = "Korisničko ime se mora sastojati od najmanje 5 znakova.";
    private static final String SHORT_PASSWORD = "Lozinka se mora sastojati od najmanje 5 znakova.";
    private EditText etFirstName, etLastName, etUsername, etPassword, etConfirmPassword, etEmail;
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
        User user = null;
        if (checkFields(firstName, lastName, username, password, confirmPass, email)) {
            user = new User(UUID.randomUUID().toString(),firstName,lastName,username,password,email);
            mUserDataInsertedListener.onUserDataInsertedListener(user);
        }
    }

    private boolean checkFields(String firstName, String lastName, String username, String password, String confirmPass, String email) {

        if(firstName.isEmpty()||lastName.isEmpty()||username.isEmpty()||password.isEmpty()||confirmPass.isEmpty()||email.isEmpty()){
            Toast.makeText(getActivity(),EMPTY_FIELDS,Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(username.length()<5){
            Toast.makeText(getActivity(), SHORT_USERNAME, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password.length()<5){
            Toast.makeText(getActivity(), SHORT_PASSWORD, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!password.equals(confirmPass)){
            Toast.makeText(getActivity(),PASSWORD_DISMATCH,Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getActivity(),EMAIL_NOT_VALID,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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

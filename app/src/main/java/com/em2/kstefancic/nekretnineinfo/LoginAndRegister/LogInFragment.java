package com.em2.kstefancic.nekretnineinfo.LoginAndRegister;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.em2.kstefancic.nekretnineinfo.R;

/**
 * Created by user on 2.11.2017..
 */

public class LogInFragment extends Fragment {

    private static final String EMPTY_FIELDS = "Unesite podatke za prijavu!";
    private Button btnLogIn;
    TextView tvRegister;
    private EditText etUsername, etPassword;
    private CredentialsInserted mCredentialsCreatedListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_log_in,null);
        setUI(layout);
        return layout;
    }

    private void setUI(View layout) {
        this.etUsername =  layout.findViewById(R.id.logInFr_etUsername);
        this.etPassword =  layout.findViewById(R.id.logInFr_etPassword);

        this.btnLogIn =  layout.findViewById(R.id.logInFr_btnLogIn);
        this.btnLogIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if(username.isEmpty()||password.isEmpty()){
                    Toast.makeText(getActivity(),EMPTY_FIELDS,Toast.LENGTH_SHORT).show();
                }
                else {
                   mCredentialsCreatedListener.onCredentialInserted(username,password);
                }

            }
        });
        this.tvRegister =  layout.findViewById(R.id.logInFr_tvRegister);
        this.tvRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.activityLogin_fl, new RegisterFragment());
                fragmentTransaction.commit();
            }
        });

        this.etPassword =  layout.findViewById(R.id.logInFr_etPassword);
        this.etUsername = layout.findViewById(R.id.logInFr_etUsername);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof CredentialsInserted)
        {
            this.mCredentialsCreatedListener = (CredentialsInserted) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mCredentialsCreatedListener =null;
    }

    public interface CredentialsInserted{
        void onCredentialInserted(String username, String password);
    }


}

package com.kstefancic.nekretnineinfo.LoginAndRegister;

import android.app.Application;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.kstefancic.nekretnineinfo.MainActivity;
import com.kstefancic.nekretnineinfo.R;
import com.kstefancic.nekretnineinfo.api.exceptionutils.ErrorUtils;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.CeilingMaterial;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.ConstructionSystem;
import com.kstefancic.nekretnineinfo.api.model.ExceptionResponse;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Material;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceDataResponse;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Position;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Purpose;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Roof;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Sector;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.City;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.State;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.Street;
import com.kstefancic.nekretnineinfo.api.model.MultichoiceLocationDataResponse;
import com.kstefancic.nekretnineinfo.api.model.User;
import com.kstefancic.nekretnineinfo.api.service.MultiChoiceDataService;
import com.kstefancic.nekretnineinfo.api.service.UserClient;
import com.kstefancic.nekretnineinfo.helper.DBHelper;
import com.kstefancic.nekretnineinfo.helper.GetDataService;
import com.kstefancic.nekretnineinfo.helper.RetrofitSingleton;
import com.kstefancic.nekretnineinfo.helper.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.kstefancic.nekretnineinfo.MainActivity.BUILDING_DATA;

public class LoginActivity extends AppCompatActivity implements RegisterFragment.UserDataInsertedListener, LogInFragment.CredentialsInserted{

    private static final String LOGIN_FRAGMENT = "login";
    private static final String REGISTRATION_SUCCESS = "Uspješno ste kreirali korisnički račun. Sada se možete prijaviti.";
    private static final String DEFAULT_ERROR = "Došlo je do pogreške. Pokušajte ponovno kasnije.";
    public static final String USER = "user";
    public static final String FIRST_LOGIN = "first_login";
    private SessionManager mSessionManager;
    private User mUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionManager = new SessionManager(this);
        setContentView(R.layout.activity_login);
        checkIfLoggedIn();
        setUpFragment();
    }

    private void checkIfLoggedIn() {
        this.mSessionManager = new SessionManager(this);
        //this.mSessionManager.setLogin(false,null);
        //DBHelper.getInstance(this).deleteAllTables();
        if (this.mSessionManager.isLoggedIn()) {
            mUser = DBHelper.getInstance(this).getUser();
            startMainActivity(false);
        }
    }

    private void setUpFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.activityLogin_fl, new LogInFragment(), LOGIN_FRAGMENT);
        fragmentTransaction.commit();
    }

    @Override
    public void onUserDataInsertedListener(User user) {

        registerNetworkRequest(user);

    }

    private void registerNetworkRequest(User user) {

        Call<User> call = RetrofitSingleton.getUserClient().createAccount(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), REGISTRATION_SUCCESS, Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.activityLogin_fl, new LogInFragment());
                    fragmentTransaction.commit();
                }else {
                    showErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onCredentialInserted(String username, String password) {
        loginNetworkRequest(username, password);
    }

    private void loginNetworkRequest(String username, final String password) {
        Call<User> call = RetrofitSingleton.getUserClient().login(username,password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    mUser = response.body();
                    DBHelper.getInstance(getApplicationContext()).insertUser(mUser);
                    mSessionManager.setLogin(true,password);
                    startDownloadDataService();
                    startMainActivity(true);
                }else {
                   showErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ERROR",t.toString());
                showDefaultError();
            }
        });
    }

    private void startDownloadDataService() {
        Intent serviceIntent = new Intent(this,GetDataService.class);
        serviceIntent.putExtra(USER,mUser);
        startService(serviceIntent);
    }

    private void showDefaultError() {
        Toast.makeText(getApplicationContext(),DEFAULT_ERROR,Toast.LENGTH_SHORT).show();
    }

    private void showErrorResponse(Response<?> response) {
        ExceptionResponse exceptionResponse = ErrorUtils.parseError(response,RetrofitSingleton.getRetrofitInstance());
        Toast.makeText(getApplicationContext(),exceptionResponse.getMessage(),Toast.LENGTH_SHORT).show();
    }


    private void startMainActivity(boolean firstLogin) {
        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        mainIntent.putExtra(USER, mUser);
        mainIntent.putExtra(FIRST_LOGIN, firstLogin);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }

}

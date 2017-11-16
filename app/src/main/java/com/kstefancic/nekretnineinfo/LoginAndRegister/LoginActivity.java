package com.kstefancic.nekretnineinfo.LoginAndRegister;

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
import com.kstefancic.nekretnineinfo.api.model.User;
import com.kstefancic.nekretnineinfo.api.service.MultiChoiceDataService;
import com.kstefancic.nekretnineinfo.api.service.UserClient;
import com.kstefancic.nekretnineinfo.helper.DBHelper;
import com.kstefancic.nekretnineinfo.helper.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class LoginActivity extends AppCompatActivity implements RegisterFragment.UserDataInsertedListener, LogInFragment.CredentialsInserted{

    private static final String LOGIN_FRAGMENT = "login";
    public static final String BASE_URL = "http://10.0.2.2:8080";
    private static final String REGISTRATION_SUCCESS = "Uspješno ste kreirali korisnički račun. Sada se možete prijaviti.";
    private static final String DEFAULT_ERROR = "Došlo je do pogreške. Pokušajte ponovno kasnije.";
    public static final String USER = "user";
    public static final String FIRST_LOGIN = "first_login";
    private Retrofit mRetrofit;
    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkIfLoggedIn();
        setUpFragment();
        setRetrofit();
    }

    private void checkIfLoggedIn() {
        this.mSessionManager = new SessionManager(this);
        //this.mSessionManager.setLogin(false,null);
        //DBHelper.getInstance(this).deleteAllTables();
        if(this.mSessionManager.isLoggedIn()){
            User user = DBHelper.getInstance(this).getUser();
            startMainActivity(user, false);
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
        UserClient client = mRetrofit.create(UserClient.class);
        Call<User> call = client.createAccount(user);

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

    private void setRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create());

        mRetrofit = builder.build();

    }

    @Override
    public void onCredentialInserted(String username, String password) {
        loginNetworkRequest(username, password);
    }

    private void loginNetworkRequest(String username, final String password) {
        UserClient client = mRetrofit.create(UserClient.class);
        Call<User> call = client.login(username,password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    DBHelper.getInstance(getApplicationContext()).insertUser(user);
                    mSessionManager.setLogin(true,password);
                    getMultiChoiceDataAndSaveToLocalDatabase(user);
                    startMainActivity(user, true);
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

    private void showDefaultError() {
        Toast.makeText(getApplicationContext(),DEFAULT_ERROR,Toast.LENGTH_SHORT).show();
    }

    private void getMultiChoiceDataAndSaveToLocalDatabase(User user) {
        MultiChoiceDataService multiChoiceDataClient = mRetrofit.create(MultiChoiceDataService.class);

        String base = user.getUsername()+":"+mSessionManager.getPassword();
        String authHeader = "Basic "+ Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

        final Call<MultiChoiceDataResponse> getMultiChoiceCall = multiChoiceDataClient.getMultiChoiceData(authHeader);
        getMultiChoiceCall.enqueue(new Callback<MultiChoiceDataResponse>() {
            @Override
            public void onResponse(Call<MultiChoiceDataResponse> call, Response<MultiChoiceDataResponse> response) {
                if (response.isSuccessful()){

                    insertPositionsInLocalDatabase(response.body().getPosition());
                    insertMaterialsInLocalDatabase(response.body().getMaterial());
                    insertConstructionSystemsInLocalDatabase(response.body().getConstructionSystem());
                    insertPurposesInLocalDatabase(response.body().getPurpose());
                    insertCeilingMaterialsInLocalDatabase(response.body().getCeilingMaterial());
                }else{
                    Log.e("MULTICHOICE DATA RESP", response.body().toString());
                    showErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<MultiChoiceDataResponse> call, Throwable t) {
                Log.e("MULTICHOICE DATA RESP", t.toString());
                showDefaultError();
            }
        });
    }

    private void insertCeilingMaterialsInLocalDatabase(List<CeilingMaterial> ceilingMaterials) {

        for(CeilingMaterial ceilingMaterial : ceilingMaterials) {
            DBHelper.getInstance(this).insertCeilingMaterial(ceilingMaterial);
            Log.d("Inserting CEILING MAT", ceilingMaterial.toString());
        }
    }

    private void showErrorResponse(Response<?> response) {
        ExceptionResponse exceptionResponse = ErrorUtils.parseError(response,mRetrofit);
        Toast.makeText(getApplicationContext(),exceptionResponse.getMessage(),Toast.LENGTH_SHORT).show();
    }

    private void insertPurposesInLocalDatabase(List<Purpose> purposes) {
        for(Purpose purpose : purposes){
            DBHelper.getInstance(this).insertPurpose(purpose);
            Log.d("Inserting PURPOSE",purpose.toString());
        }
    }

    private void insertConstructionSystemsInLocalDatabase(List<ConstructionSystem> constructionSystems) {
        for(ConstructionSystem constructionSystem : constructionSystems){
            DBHelper.getInstance(this).insertConstructSys(constructionSystem);
            Log.d("Inserting CONSTRUCT_SYS",constructionSystem.toString());
        }
    }

    private void insertMaterialsInLocalDatabase(List<Material> materials) {
        for(Material material : materials){
            DBHelper.getInstance(this).insertMaterial(material);
            Log.d("Inserting MATERIALS",material.toString());
        }
    }

    private void insertPositionsInLocalDatabase(List<Position> positions) {
        for(Position position : positions){
            DBHelper.getInstance(this).insertPosition(position);
            Log.d("Inserting POSITIONS",position.toString());
        }
    }

    private void startMainActivity(User user, boolean firstLogin) {
        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        mainIntent.putExtra(USER, user);
        mainIntent.putExtra(FIRST_LOGIN, firstLogin);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }
}

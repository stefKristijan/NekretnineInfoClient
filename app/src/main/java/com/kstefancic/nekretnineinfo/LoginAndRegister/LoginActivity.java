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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionManager = new SessionManager(this);
        setContentView(R.layout.activity_login);
        setUpFragment();
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
                    User user = response.body();
                    DBHelper.getInstance(getApplicationContext()).insertUser(user);
                    mSessionManager.setLogin(true,password);
                    getMultiChoiceDataAndSaveToLocalDatabase(user);

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

    private void getMultiChoiceDataAndSaveToLocalDatabase(final User user) {

        String base = user.getUsername()+":"+mSessionManager.getPassword();
        String authHeader = "Basic "+ Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

        final Call<MultiChoiceDataResponse> getMultiChoiceCall = RetrofitSingleton.getMultiChoiceDataService().getMultiChoiceData(authHeader);
        getMultiChoiceCall.enqueue(new Callback<MultiChoiceDataResponse>() {
            @Override
            public void onResponse(Call<MultiChoiceDataResponse> call, Response<MultiChoiceDataResponse> response) {
                if (response.isSuccessful()){
                    insertPositionsInLocalDatabase(response.body().getPosition());
                    insertMaterialsInLocalDatabase(response.body().getMaterial());
                    insertConstructionSystemsInLocalDatabase(response.body().getConstructionSystem());
                    insertPurposesInLocalDatabase(response.body().getPurpose());
                    insertCeilingMaterialsInLocalDatabase(response.body().getCeilingMaterial());
                    insertSectorsInLocalDatabase(response.body().getSectors());
                    insertRoofsInLocalDatabase(response.body().getRoofs());
                    getMultichoiceAddressDataAndSaveToLocalDatabase(user);
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



    private void getMultichoiceAddressDataAndSaveToLocalDatabase(final User user) {
        String base = user.getUsername()+":"+mSessionManager.getPassword();
        String authHeader = "Basic "+ Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

        final Call<MultichoiceLocationDataResponse> getMultiChoiceLocationCall = RetrofitSingleton.getMultiChoiceDataService().getMultichoiceLocationData(authHeader);
        getMultiChoiceLocationCall.enqueue(new Callback<MultichoiceLocationDataResponse>() {
            @Override
            public void onResponse(Call<MultichoiceLocationDataResponse> call, Response<MultichoiceLocationDataResponse> response) {
                if (response.isSuccessful()){
                   insertStatesInLocalDatabase(response.body().getStates());
                   insertCitiesInLocalDatabase(response.body().getCities());
                   insertStreetsInLocalDatabase(response.body().getStreets());
                    Intent loginIntent = new Intent();
                    loginIntent.putExtra(USER, user);
                    setResult(RESULT_OK, loginIntent);
                    Log.d("RESULT_OK", user.toString());
                    finish();
                }else{
                    Log.e("LOCATION DATA RESP", response.body().toString());
                    showErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<MultichoiceLocationDataResponse> call, Throwable t) {
                Log.e("MULTICHOICE DATA RESP", t.toString());
                showDefaultError();
            }
        });
    }

    private void insertRoofsInLocalDatabase(List<Roof> roofs) {
        for(Roof roof : roofs){
            DBHelper.getInstance(this).insertRoof(roof);
        }
    }


    private void insertStatesInLocalDatabase(List<State> states) {
        for (State state : states){
            DBHelper.getInstance(this).insertState(state);
        }
    }

    private void insertCitiesInLocalDatabase(List<City> cities) {
        for (City city : cities){
            DBHelper.getInstance(this).insertCity(city);
        }
    }

    private void insertStreetsInLocalDatabase(List<Street> streets) {
        for (Street street : streets){
            DBHelper.getInstance(this).insertStreet(street);
        }
    }

    private void insertCeilingMaterialsInLocalDatabase(List<CeilingMaterial> ceilingMaterials) {

        for(CeilingMaterial ceilingMaterial : ceilingMaterials) {
            DBHelper.getInstance(this).insertCeilingMaterial(ceilingMaterial);
        }
    }

    private void showErrorResponse(Response<?> response) {
        ExceptionResponse exceptionResponse = ErrorUtils.parseError(response,RetrofitSingleton.getRetrofitInstance());
        Toast.makeText(getApplicationContext(),exceptionResponse.getMessage(),Toast.LENGTH_SHORT).show();
    }

    private void insertPurposesInLocalDatabase(List<Purpose> purposes) {
        for(Purpose purpose : purposes){
            DBHelper.getInstance(this).insertPurpose(purpose);
        }
    }

    private void insertConstructionSystemsInLocalDatabase(List<ConstructionSystem> constructionSystems) {
        for(ConstructionSystem constructionSystem : constructionSystems){
            DBHelper.getInstance(this).insertConstructSys(constructionSystem);
        }
    }

    private void insertMaterialsInLocalDatabase(List<Material> materials) {
        for(Material material : materials){
            DBHelper.getInstance(this).insertMaterial(material);
        }
    }

    private void insertPositionsInLocalDatabase(List<Position> positions) {
        for(Position position : positions){
            DBHelper.getInstance(this).insertPosition(position);
        }
    }

    private void insertSectorsInLocalDatabase(List<Sector> sectors) {
        for(Sector sector : sectors){
            DBHelper.getInstance(this).insertSector(sector);
        }
    }

}

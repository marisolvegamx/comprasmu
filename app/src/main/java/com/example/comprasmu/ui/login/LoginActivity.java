package com.example.comprasmu.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.LoggedInUser;
import com.example.comprasmu.data.modelos.LoginDataSource;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.remote.ListaCompraResponse;
import com.example.comprasmu.ui.visita.ListaVisitasViewModel;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.fasterxml.jackson.databind.ser.Serializers;


import java.nio.charset.StandardCharsets;
import java.util.Date;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
     EditText usernameEditText;
     EditText passwordEditText;
     ProgressBar loadingProgressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });



        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                  //  comprobacion();
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
               comprobacion();

            }
        });
    }

    public void comprobacion(){
        //reviso si ya tengo el dato
        LoggedInUser luser=tengoUsuario();
        if(luser==null){ //primera vez
            Log.i("LoginRepository","primera vez");
            if(ComprasUtils.isOnlineNet())
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString(),new LoginListener());
           //     new LoginListener().correcto();
            else
                {
                    loadingProgressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Sin conexión a internet, verifique", Toast.LENGTH_LONG).show();

                }
        }else
        //loginlocal
        {
        //new LoginListener().correcto();
            loginViewModel.loginLocal(luser,usernameEditText.getText().toString(),
                   passwordEditText.getText().toString(),new LoginListener());
        }
    }
    public LoggedInUser tengoUsuario()
    {
        SharedPreferences prefe = getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        String user= prefe.getString("usuario", "");
        String pass= prefe.getString("password", "");
        if(user.equals("")&&pass.equals("")){
            return null; //no los tengo guardados
        }
        LoggedInUser luser=new LoggedInUser();
        String x1=new String(Base64.decode(pass,Base64.DEFAULT), StandardCharsets.UTF_8);
        String x2=new String(Base64.decode(user,Base64.DEFAULT));
        luser.setPassword(x1);
        luser.setUserId(x2);
         return luser ;

    }
    public void guardarUsuario(){


        SharedPreferences prefe=getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();

        editor.putString("usuario", android.util.Base64.encodeToString( usernameEditText.getText().toString().getBytes(), Base64.DEFAULT));
        editor.putString("password", Base64.encodeToString(passwordEditText.getText().toString().getBytes(),Base64.DEFAULT));
        editor.commit();



    }




    private void updateUiWithUser(String model) {
        String welcome = getString(R.string.welcome) +" "+ model;
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
    public void entrar(){
        //mando a la siguiente actividad
        Intent intento=new Intent(this, NavigationDrawerActivity.class);
        startActivity(intento);
    }
    public class LoginListener{

        public void incorrecto(String response){
          //muestro error

            loadingProgressBar.setVisibility(View.GONE);

            showLoginFailed(response);



            //setResult(Activity.RESULT_OK);

            //Complete and destroy login activity once successful

        }
        public void correcto() {
            loadingProgressBar.setVisibility(View.GONE);
            guardarUsuario();
            updateUiWithUser(usernameEditText.getText().toString());
            entrar();
            finish();

        }
    }

}
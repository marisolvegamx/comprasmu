package com.example.comprasmu.ui.login;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

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

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.LoggedInUser;
import com.example.comprasmu.ui.home.HomeActivity;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;


import java.nio.charset.StandardCharsets;


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
              //  loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                //        passwordEditText.getText().toString());
                if(s.length()>0)
                loginButton.setEnabled(true);
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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

                LoginFormState loginFormState=loginViewModel.getLoginFormState();
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
                if(loginFormState.isDataValid())
                    comprobacion();

                loadingProgressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LoginActivity"," regrese");
        if(Constantes.LOGGEADO){ //ya inicié sesión no pido iniciar
            entrar();
            finish();
        }
    }

    public void comprobacion(){
        //reviso si ya tengo el dato
        LoggedInUser luser=tengoUsuario();
        //siempre checa el internet
        //luser=null;
        if(luser==null){ //primera vez
         //   Log.i("LoginActivity","primera vez");
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
    public void guardarUsuario(String cveusr){


        SharedPreferences prefe=getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();
       // editor.putString("claveusuario",cveusr);
        editor.putString("usuario", android.util.Base64.encodeToString( usernameEditText.getText().toString().getBytes(), Base64.DEFAULT));
        editor.putString("password", Base64.encodeToString(passwordEditText.getText().toString().getBytes(),Base64.DEFAULT));
        editor.commit();



    }
    public void guardarClave(String clave){
        SharedPreferences prefe=getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();

        editor.putString("claveusuario",clave);
        editor.commit();
        Constantes.CLAVEUSUARIO=clave;


    }




    private void updateUiWithUser(String model) {
        String welcome = getString(R.string.welcome) +" "+ model;

        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
    public void entrar(){
        Constantes.LOGGEADO=true;
        //mando a la siguiente actividad
        Intent intento=new Intent(this, HomeActivity.class);
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
        public void correcto(String cveusr) {
            loadingProgressBar.setVisibility(View.GONE);
            guardarUsuario( cveusr);
            //busco la clave de usuario
            String aux[]=cveusr.split("=");
            if(aux.length<=0){
                Toast.makeText(getApplicationContext(),getString(R.string.error_sesion) , Toast.LENGTH_LONG).show();
                return;
            }
            String clave=aux[1];
            guardarClave(clave);
            updateUiWithUser(usernameEditText.getText().toString());
            entrar();
            finish();

        }
        public void iniciar() {
            loadingProgressBar.setVisibility(View.GONE);
         //   guardarUsuario( cveusr);
            updateUiWithUser(usernameEditText.getText().toString());
            entrar();
            finish();

        }
    }

}
package com.example.comprasmu.ui.login;

import androidx.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.LoggedInUser;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.ui.home.PruebasActivity;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity
//        implements DescargasIniAsyncTask.ProgresoListener
{

    private static final String TAG ="LoginActivity" ;
    private LoginViewModel loginViewModel;
    EditText usernameEditText;
    EditText passwordEditText;
    ProgressBar loadingProgressBar;
    //private static final String DOWNLOAD_PATH = "https://muesmerc.mx/comprasv1/fotografias";
    private   String DESTINATION_PATH ;
    ImagenDetRepositoryImpl imagenDetRepo;
    int desclis; int descinf; int descfoto;
    private long lastClickTime = 0;
    Button loginButton;
    ComprasLog comprasLog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        comprasLog = ComprasLog.getSingleton();
        comprasLog.crearLog(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath());

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton  = findViewById(R.id.login);
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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);
                long currentClickTime= SystemClock.elapsedRealtime();
                // preventing double, using threshold of 1000 ms
                if (currentClickTime - lastClickTime < 2000){
                    loginButton.setEnabled(false);
                    return;
                }

                lastClickTime = currentClickTime;
                Log.d(TAG,"model"+Build.MODEL+"-"+Constantes.modelo);
                if (Build.PRODUCT.contains ("sdk")||Build.MODEL.contains (Constantes.modelo)) {//pruebas y el lenovo//entro rapido
                 //   new LoginListener().iniciar(); return;
                }
             //   loadingProgressBar.setVisibility(View.VISIBLE);
                //hago validaciones
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

                LoginFormState loginFormState=loginViewModel.getLoginFormState();
                if (loginFormState == null) {
                    loginButton.setEnabled(true);
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {

                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                    loginButton.setEnabled(true);
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                    loginButton.setEnabled(true);
                }

                if(loginFormState.isDataValid())
                    comprobacion();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LoginActivity"," regreso");
        if(Constantes.LOGGEADO){ //ya inicié sesión no pido iniciar
            entrar();
          //  finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // unregisterReceiver(onDownloadComplete);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LoginActivity"," alto");
    }

    public void comprobacion(){
        loadingProgressBar.setVisibility(View.VISIBLE);
        LoggedInUser luser=tengoUsuario();

        if(ComprasUtils.isOnlineNet(getApplicationContext())) {
            comprasLog.grabarError(TAG+" haciendo peticion remota");
            loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), new LoginListener());

        }else
        {
            comprasLog.grabarError(TAG,"comprobacion", "sin conexion");

            if(luser==null) { //primera vez
                loadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Sin conexión a internet, verifique", Toast.LENGTH_LONG).show();
                loginButton.setEnabled(true);
            }
            else
                //loginlocal
            {
                //new LoginListener().correcto();
                Log.i(TAG,"haciendo loginlocal");
                loginViewModel.loginLocal(luser,usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),new LoginListener());
            }

        }


    }
    public LoggedInUser tengoUsuario()
    {
        SharedPreferences prefe = getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        String user= prefe.getString("usuario", "");
        String pass= prefe.getString("password", "");
        Log.e("LoginAct",user);
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

    public String buscarClaveUsuario()
    {
        SharedPreferences prefe = getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        return prefe.getString("claveusuario", "");

    }


    private void updateUiWithUser(String model) {
        String welcome = getString(R.string.welcome) +" "+ model;
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
        loginButton.setEnabled(true);
    }
    public void entrar() {
        Constantes.LOGGEADO = true;
        Log.d("LoginActivity","entre");
        //mando a la siguiente actividad
        Intent intento=new Intent(this, PruebasActivity.class);
        startActivity(intento);
        finish();
    }


    public class LoginListener{

        public void incorrecto(String response){
            comprasLog.grabarError(TAG,"LoginListener ",response);
            //muestro error
            loadingProgressBar.setVisibility(View.GONE);
            showLoginFailed(response);
        }
        public void correcto(String cveusr) {

            loadingProgressBar.setVisibility(View.GONE);
            guardarUsuario( cveusr);
            //busco la clave de usuario
            String[] aux =cveusr.split("=");
            if(aux.length<=0){
                Toast.makeText(getApplicationContext(),getString(R.string.error_sesion) , Toast.LENGTH_LONG).show();
                return;
            }
            String clave=aux[1];
            String cveguardada= buscarClaveUsuario();
            if(cveguardada.equals("")||cveguardada.equals(clave)) {
                guardarClave(clave);
                Log.i(TAG, "correcto" + clave);
                Log.i(TAG, "correcto" + Constantes.CLAVEUSUARIO);
                updateUiWithUser(usernameEditText.getText().toString());
                entrar();
            }
            else {
                showLoginFailed("Usuario incorrecto");
            }

        }
        public void iniciar() {
            Log.d(TAG,"iniciar"+Constantes.CLAVEUSUARIO);
            loadingProgressBar.setVisibility(View.GONE);
         //   guardarUsuario( cveusr);
            updateUiWithUser(usernameEditText.getText().toString());
            //hago actualizacion y cuando termine envio a la sig actividad
           // descargasIniciales();
            entrar();

        }

    }


}
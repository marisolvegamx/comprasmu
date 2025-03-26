package com.example.comprasmu.ui.login;

import androidx.lifecycle.ViewModelProvider;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.os.SystemClock;
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

import com.example.comprasmu.DescargasIniAsyncTask;
import com.example.comprasmu.R;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.LoggedInUser;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.data.remote.RespInformesResponse;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.GeocercaRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.SustitucionRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.ui.home.HomeActivity;
import com.example.comprasmu.ui.home.PruebasActivity;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;


import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

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
                if (currentClickTime - lastClickTime < 5500){
                    //  Log.d(TAG,"doble click :("+lastClickTime);
                    return;
                }

                lastClickTime = currentClickTime;
                Log.d(TAG,"model"+Build.MODEL+"-"+Constantes.modelo);
                if (Build.PRODUCT.contains ("sdk")||Build.MODEL.contains (Constantes.modelo)) {//pruebas y el lenovo//entro rapido
                  //  new LoginListener().iniciar(); return;
                }
                loadingProgressBar.setVisibility(View.VISIBLE);
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

        LoggedInUser luser=tengoUsuario();

        if(ComprasUtils.isOnlineNet(getApplicationContext())) {
            loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), new LoginListener());

        }else
        {

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

    public void descargasIniciales(){

        SharedPreferences prefe = getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        Constantes.CIUDADTRABAJO = prefe.getString("ciudadtrabajo", "");
        Constantes.IDCIUDADTRABAJO=prefe.getInt("idciudadtrabajo",0);
        Constantes.CLAVEUSUARIO = prefe.getString("claveusuario", "");
         Log.d("LoginActivity", "***** indice " + Constantes.CLAVEUSUARIO );

        SimpleDateFormat sdfparaindice=new SimpleDateFormat("M-yyyy");
        //obtengo solo mes

        Calendar cal = Calendar.getInstance(); // Obtenga un calendario utilizando la zona horaria y la configuración regional predeterminadas
        Date hoy=new Date();
        cal.setTime(hoy);
        cal.add(Calendar.MONTH, +1);
        String mesactual = sdfparaindice.format(cal.getTime());

        String[] aux = mesactual.split("-");
        int mes = Integer.parseInt(aux[0])+1;
        int anio = Integer.parseInt(aux[1]);

        Constantes.listaindices = new String[4];
        int j = 3;
        int nuevomes = mes;
        for (int i = 1; i < 5; i++) {
            Constantes.listaindices[j] = ComprasUtils.mesaLetra(nuevomes + "") + " " + anio + "";

            nuevomes = nuevomes - 1;
            if (nuevomes == 0) //empezo en 1
            {
                nuevomes = 12;
                anio = anio - 1;
            }
            j--;
        }

        Constantes.INDICEACTUAL=ComprasUtils.indiceLetra(mesactual);
     //
        //TODO falta pais trabajo
        //  Constantes.CIUDADTRABAJO="Cd Juarez";
        //  Constantes.PAISTRABAJO = "Mexico";
        //pueda descargar
        //Inicio un servicio que se encargue de descargar

        desclis=1;
        CatalogoDetalleRepositoryImpl cdrepo=new CatalogoDetalleRepositoryImpl(getApplicationContext());
        TablaVersionesRepImpl tvRepo=new TablaVersionesRepImpl(getApplicationContext());

        AtributoRepositoryImpl atRepo=new AtributoRepositoryImpl(getApplicationContext());
        ListaCompraDao dao= ComprasDataBase.getInstance(getApplicationContext()).getListaCompraDao();
        ListaCompraDetRepositoryImpl lcdrepo=new ListaCompraDetRepositoryImpl(getApplicationContext());
        ListaCompraRepositoryImpl lcrepo=ListaCompraRepositoryImpl.getInstance(dao);
        SustitucionRepositoryImpl sustRepo=new SustitucionRepositoryImpl(getApplicationContext());
        GeocercaRepositoryImpl georep=new GeocercaRepositoryImpl(getApplicationContext());
      //  DescargasIniAsyncTask task = new DescargasIniAsyncTask(this,cdrepo,tvRepo,atRepo,lcdrepo,lcrepo,this,sustRepo,georep);


    }



    public class LoginListener{

        public void incorrecto(String response){
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
            if(!cveguardada.equals("")&&cveguardada.equals(clave)) {
                guardarClave(clave);
                Log.d(TAG, "correcto" + clave);
                Log.d(TAG, "correcto" + Constantes.CLAVEUSUARIO);
                updateUiWithUser(usernameEditText.getText().toString());
                //hago actualizacion y cuando termine envio a la sig actividad
                //   descargasIniciales();
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
           // finish();

        }

    }


}
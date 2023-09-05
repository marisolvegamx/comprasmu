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

    private LoginViewModel loginViewModel;
     EditText usernameEditText;
     EditText passwordEditText;
     ProgressBar loadingProgressBar;
    //private static final String DOWNLOAD_PATH = "https://muesmerc.mx/comprasv1/fotografias";
    private   String DESTINATION_PATH ;
    ImagenDetRepositoryImpl imagenDetRepo;
    int desclis; int descinf; int descfoto;

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
                if (Build.PRODUCT.contains ("sdk")||Build.PRODUCT.contains ("A2016b30")) {//pruebas y el lenovo//entro rapido
             //     new LoginListener().iniciar();
                }
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
          //  finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LoginActivity"," mori");
       // unregisterReceiver(onDownloadComplete);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LoginActivity"," alto");
    }

    public void comprobacion(){
        //reviso si ya tengo el dato
        LoggedInUser luser=tengoUsuario();
        //siempre checa el internet
        //luser=null;
         //   Log.i("LoginActivity","primera vez");
        if(ComprasUtils.isOnlineNet()) {
            if(luser!=null) {
                //veo que sea el mismo correo
                if(!luser.getUserId().equals(usernameEditText.getText().toString())){
                    new LoginListener().incorrecto("Usuario o contraseña incorrectos");
                }

            }
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), new LoginListener());
                //     new LoginListener().correcto();
        }else
        {

            if(luser==null) { //primera vez
                loadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Sin conexión a internet, verifique", Toast.LENGTH_LONG).show();
            }
            else
                //loginlocal
            {
                //new LoginListener().correcto();
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
      //  Log.e("LoginAct",user);
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
        //  prefe.getString("ciudadtrabajo","");
    /*    Constantes.PAISTRABAJO=     prefe.getString("paistrabajo","");
        Constantes.IDCIUDADTRABAJO=prefe.getInt("idciudadtrabajo",0);
        Constantes.IDPAISTRABAJO=     prefe.getInt("idpaistrabajo",0);
        Constantes.CLAVEUSUARIO=prefe.getString("claveusuario","");
*/
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
        // Constantes.INDICEACTUAL=mesactual.replace('-','.');
        Constantes.INDICEACTUAL = "6.2022";
        if(Constantes.CLAVEUSUARIO.equals("4")){
            Constantes.INDICEACTUAL = "6.2022";
        }

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

     //   task.execute("cat","");


      /*  AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.stat_sys_download);
        builder.setTitle("Descargando");
        builder.setMessage("Por favor mantengase en la aplicación hasta que termine la descarga");
        builder.setInverseBackgroundForced(true);

        AlertDialog alert=builder.create();
        alert.show();*/

      /*  Dialog builder = new Dialog(act);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setCancelable(false);
*/
    }

    /*@Override
    public void cerrarAlerta(boolean res) {
        Log.d("LoginAct","quiero descargar "+desclis+"--"+descinf+"--"+descfoto);
        if(desclis==0&&descinf==0&&descfoto==0)
         entrar();
    }

    @Override
    public void todoBien( RespInformesResponse infoResp) {
        if (infoResp.getImagenDetalles() != null && infoResp.getImagenDetalles().size() > 0) {

            descargarImagenes(infoResp.getImagenDetalles());

        }

    }

    @Override
    public void estatusInf(int es) {
        descinf=es;
    }

    @Override
    public void estatusLis(int es) {
        desclis=es;
    }*/

   /* private void descargarImagenes(List<ImagenDetalle> imagenes){
        for(ImagenDetalle img:imagenes){
            startDownload(DOWNLOAD_PATH+"/"+img.getIndice().replace(".","_")+"/"+img.getRuta(), DESTINATION_PATH);
            Log.d("LOginAct"," descargando "+DOWNLOAD_PATH+"/"+img.getIndice().replace(".","_")+"/"+img.getRuta());
        }
       // cerrarAlerta(true);
    }*/
    private long startDownload(String downloadPath, String destinationPath) {
        Uri uri = Uri.parse(downloadPath); // Path where you want to download file.
        // registrer receiver in order to verify when download is complete
      //  registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle("Downloading a file"); // Title for notification.
        request.setVisibleInDownloadsUi(true);

        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_PICTURES, uri.getLastPathSegment());  // Storage directory path
        long id=((DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading
        return id;

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
            String[] aux =cveusr.split("=");
            if(aux.length<=0){
                Toast.makeText(getApplicationContext(),getString(R.string.error_sesion) , Toast.LENGTH_LONG).show();
                return;
            }
            String clave=aux[1];
            guardarClave(clave);
            updateUiWithUser(usernameEditText.getText().toString());
            //hago actualizacion y cuando termine envio a la sig actividad
         //   descargasIniciales();
            entrar();
         //   finish();

        }
        public void iniciar() {
            loadingProgressBar.setVisibility(View.GONE);
         //   guardarUsuario( cveusr);
            updateUiWithUser(usernameEditText.getText().toString());
            //hago actualizacion y cuando termine envio a la sig actividad
           // descargasIniciales();
            entrar();
           // finish();

        }

    }
  /*  private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
                Toast.makeText(context,"Download completed", Toast.LENGTH_LONG).show();
                // DO SOMETHING WITH THIS FILE
                descfoto=0;
            }
        }
    };*/

}
package com.example.comprasmu;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.data.remote.RespInformesResponse;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.ui.informe.PostInformeViewModel;

import java.util.ArrayList;

public class SimpleTask extends AsyncTask<Void, Integer, Void> {


   // private final int[] numbers;
    public TextView progressLabel;
    Activity act;


    public SimpleTask( Activity act) {
        this.act=act;
        //this.numbers = numbers;
      //  this.progressLabel = progressLabel;
    }

    private final ArrayList<PruebaListener> observadores = new ArrayList<PruebaListener>();


    public void agregarObservador(PruebaListener o)
    {
        observadores.add(o);

    }
    public void notificarObservadores()
    {
        Log.d("Ejemplo "," Notificando");
        Log.d("Ejemplo","tamaño->"+observadores.size());
        // Enviar la notificación a cada observador a través de su propio método
        for (PruebaListener obj : observadores) {
            obj.onSuccess();
        }
    }

    /*
        Se hace visible el botón "Cancelar" y se desactiva
        el botón "Ordenar"
         */
    @Override
    protected void onPreExecute() {

    }



    /*
  Ejecución del ordenamiento y transmision de progreso
   */
    @Override
    protected Void doInBackground(Void... params) {
        CatalogoDetalleRepositoryImpl catrep=new CatalogoDetalleRepositoryImpl(act);
        TablaVersionesRepImpl verepo=new TablaVersionesRepImpl(act);
        AtributoRepositoryImpl atrrepo=new AtributoRepositoryImpl(act);
        PeticionesServidor ps=new PeticionesServidor(4+"");
        ps.getCatalogosPrueb(new STListener());

        return null;
    }

    /*
   Se informa en progressLabel que se canceló la tarea y
   se hace invisile el botón "Cancelar"
    */
    @Override
    protected void onCancelled() {
        super.onCancelled();
      //  progressLabel.setText("Tarea cancelada");

    }

    /*
 Impresión del progreso en tiempo real
   */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
  //      progressLabel.setText(values[0] + "%");
    }

    /*
    Se notifica que se completó el ordenamiento y se habilita
    de nuevo el botón "Ordenar"
     */
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
     //   progressLabel.setText("Completado");

    }
    public class STListener {



        public void actualizar( boolean resp){
            //a ver como la regresamos
            //  miproglis.todoBien(infoResp);
            if(resp){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            notificarObservadores();
        }
    }

}

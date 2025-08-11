package com.example.comprasmu.ui.mantenimiento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.comprasmu.R;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ConfiguracionRepositoryImpl;
import com.example.comprasmu.data.dao.CorEtiquetadoCajaDao;
import com.example.comprasmu.data.dao.CorEtiquetadoCajaDetDao;
import com.example.comprasmu.data.repositories.AcuseReciboRepositoryImpl;
import com.example.comprasmu.data.repositories.CorEtiqCajaDetRepoImpl;
import com.example.comprasmu.data.repositories.CorEtiqCajaRepoImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.ui.home.HomeActivity;
import com.example.comprasmu.ui.home.PruebasActivity;
import com.example.comprasmu.utils.ComprasUtils;
import com.example.comprasmu.utils.Constantes;
import com.example.comprasmu.utils.EliminadorIndice;

/****
 * proceso para borrar las tablas con informes de el mes actual
 */
public class BorrarActivity extends AppCompatActivity {
    public static String INDICEACT="comprasmu.borindiceact";
    TextView aviso;
    private BorrarDatosViewModel mViewModel;
    String indiceact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar);
        AcuseReciboRepositoryImpl acuseReciboRepo=new AcuseReciboRepositoryImpl(this);
        ListaCompraDetRepositoryImpl listaCompraRepo=new ListaCompraDetRepositoryImpl(this);
        ConfiguracionRepositoryImpl configuracionRepository=new ConfiguracionRepositoryImpl(this);
        CorEtiquetadoCajaDao correccionEtiquetadoDao= ComprasDataBase.getInstance(this).getCorEtiquetadoCajaDao();
        CorEtiqCajaRepoImpl correccionEtiqRepo=CorEtiqCajaRepoImpl.getInstance(correccionEtiquetadoDao);
        CorEtiquetadoCajaDetDao corEtiquetadoDao= ComprasDataBase.getInstance(this).getCorEtiquetadoCajaDetDao();
        CorEtiqCajaDetRepoImpl corEtiqCajaDetRepo=CorEtiqCajaDetRepoImpl.getInstance(corEtiquetadoDao);
        mViewModel = new ViewModelProvider(this, new BorrarViewModelFactory(acuseReciboRepo,getApplication(),listaCompraRepo, configuracionRepository, correccionEtiqRepo, corEtiqCajaDetRepo)).get(BorrarDatosViewModel.class);
        Button btnborrar=findViewById(R.id.btnboaceptar);
        Button btncancelar=findViewById(R.id.btnbocancelar);
        aviso=findViewById(R.id.txtbomensaje);
        Bundle extras = getIntent().getExtras(); // Aquí es null


        if(extras!=null) {
            indiceact = extras.getString(INDICEACT);
        }
        aviso.setText("Para iniciar el nuevo indice necesita borrar la información de "+ ComprasUtils.indiceLetra(indiceact)+". Presione borrar para eliminar o cancelar para continuar con el indice actual ");
        btnborrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //borrarxindice();
                borrarautomatico();
                irAPruebas();
            }
        });
        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAHome();
            }
        });

    }

    private void irAHome() {
        Intent intento=new Intent(this, HomeActivity.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intento);
        finish();
    }
    private void irAPruebas() {
        //inicializo prefs
        inicializarEtapaPref();
        Intent intento=new Intent(this, PruebasActivity.class);
        intento.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intento);
        finish();
    }
    public void borrarautomatico(){
        //cancelo workmanager donde descargo listas de compra, porque llega a descargar mientras borro
        //elimino todos los procesos que se hayan iniciado
        WorkManager.getInstance(this).cancelAllWorkByTag("comprassync_worker2");
        String indice_anterior=indiceact;
        EliminadorIndice ei=new EliminadorIndice(this,indice_anterior);
        ei.eliminarVisitas();
        aviso.setVisibility(View.VISIBLE);
        mViewModel.borrarListasCompra(indice_anterior);
        // borrar informes etapa
        mViewModel.borrarInformesetapa(indice_anterior);
        ei.eliminarCorrecciones();
        ei.eliminarSolicitudes();
        ei.borrarImagenes();
        ei.eliminarTablaVers();
        mViewModel.borrarEnvio();
        mViewModel.borrarGasto(indice_anterior);
        mViewModel.borrarAcuseRecibo();
        mViewModel.borrarConfiguracion();
        mViewModel.borrarCorreccionEtiq(indice_anterior);
        mViewModel.borrarHistoricoMuestras();
        //inicializo constantes
        Constantes.CIUDADTRABAJO ="" ;
        Constantes.IDCIUDADTRABAJO=0;
        borrarCiudadPref();
        ei.eliminarContenidoPictures();
    }
    public void inicializarEtapaPref(){
        SharedPreferences prefe=getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();

        editor.putInt("etapaact",0 );
        editor.putString("indiceact", "");
        editor.putInt("etapafin", 0);
        editor.commit();

    }

    public void borrarCiudadPref(){
        SharedPreferences prefe=getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();

        editor.putString("ciudadtrabajo","" );
        editor.putInt("idciudadtrabajo",0 );//no es del catalogo ciudades es de plantas


        editor.commit();

    }
}
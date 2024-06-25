package com.example.comprasmu.utils.ui;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.modelos.CorEtiquetadoCajaDet;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEnvioDet;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.InformeGastoDet;
import com.example.comprasmu.data.repositories.CorEtiqCajaDetRepoImpl;
import com.example.comprasmu.data.repositories.CorreccionRepoImpl;
import com.example.comprasmu.data.repositories.DetalleCajaRepoImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.data.repositories.InfGastoDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeEnvioRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class InformesGenViewModel extends AndroidViewModel {

    private final InfEtapaRepositoryImpl ierepository;
    private final InfEtapaDetRepoImpl idrepository;
    private final InformeEnvioRepositoryImpl infEnvioRepo;
    private final CorreccionRepoImpl correpo;
    private final ImagenDetRepositoryImpl imagenDetRepository;
    private final DetalleCajaRepoImpl detCajaRepo;
    CorEtiqCajaDetRepoImpl cocrepo;
    private InfGastoDetRepositoryImpl infGastoDetRepo;
    public InformesGenViewModel(Application application) {
        super(application);
        ierepository=new InfEtapaRepositoryImpl(application);
        idrepository=new InfEtapaDetRepoImpl(application);
        correpo=new CorreccionRepoImpl(application);
        this.imagenDetRepository=new ImagenDetRepositoryImpl(application);
        this.detCajaRepo=new DetalleCajaRepoImpl(application);
        infEnvioRepo = new InformeEnvioRepositoryImpl(application);
        infGastoDetRepo=new InfGastoDetRepositoryImpl(application);
        cocrepo=new CorEtiqCajaDetRepoImpl(application);

    }


    public LiveData<List<InformeEtapa>> cargarEtapa(int etapa, String indice, int plantaid){
        return ierepository.getAll(etapa, indice, plantaid);

    }
    public LiveData<List<InformeEtapa>> cargarEtapaAll(int etapa, String indice){
        return ierepository.getAllsp(etapa, indice);

    }
    public LiveData<InformeEtapa> getInforme(int id, String indice){
        return ierepository.find(id);

    }
    public LiveData<List<InformeEtapaDet>> getfotosPrep(int id){
        return idrepository.getAllxEtapa(id, 1);

    }
    public LiveData<List<InformeEtapaDet>> getfotosxetapa(int id, int etapa){
        return idrepository.getAllxEtapa(id, etapa);

    }
    public LiveData<List<InformeEtapaDet>> getfotosxetapaxcaj(int id, int etapa, int numcaja){
        return idrepository.getByCajaEmp(id, etapa, numcaja);

    }
    public ImagenDetalle getfotoxid(String id){

            int idim = Integer.parseInt(id);
            return imagenDetRepository.findsimple(idim);


    }
    public LiveData<List<DetalleCaja>> getDetalleCajaEmp(int infid){
        return detCajaRepo.getDetalles(infid);
    }

    public InformeEnvioDet getInformeEnvioDet(int idinf){
        return  infEnvioRepo.findsimple(idinf);
    }
    public ImagenDetalle getFoto(int idfoto){
        return imagenDetRepository.findsimple(idfoto);

    }
    public LiveData<List<InformeGastoDet>> getfotosGasto(int id){

        return infGastoDetRepo.getAll(id);

    }
    public List<ImagenDetalle> getfotosCorEtiq(int id){
     List<ImagenDetalle>fotos= new ArrayList<>();
       List<CorEtiquetadoCajaDet>detalles=cocrepo.getAllByCorId(id);
       ImagenDetalle imagen;
        for (CorEtiquetadoCajaDet detalle:detalles
             ) {
            imagen=imagenDetRepository.findsimple(detalle.getRuta_fotonva());
            fotos.add(imagen);
        }
        return fotos;

    }
}
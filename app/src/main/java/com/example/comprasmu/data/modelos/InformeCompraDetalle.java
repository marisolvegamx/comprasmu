package com.example.comprasmu.data.modelos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.comprasmu.data.Converters;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "informe_detalle")
public class InformeCompraDetalle {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int informesId;

    private int estatus;
    private int estatusSync;
    private int productoId;
    private String producto;
    private String presentacion;
    private int tamanioId;
    private String empaque;
    private int empaquesId;
    private String codigo;
    private Date caducidad;

    private String origen;
    private String costo;
    private int foto_codigo_produccion;
    private int energia;
    private int producto_exhibido; //no se usa está enel informe
    private int foto_num_tienda;
    private int marca_traslape;
    private String atributoa;
    private int foto_atributoa;
    private String atributob;
    private int foto_atributob;
    private String atributoc;
    private int foto_atributoc;
    private int azucares;
    private String qr;
    private int etiqueta_evaluacion;
    private int tipoMuestra;
    private String nombreTipoMuestra;
    private int tipoAnalisis;
    private String nombreAnalisis;
    private int numMuestra;

    private String comentarios;
    private int comprasId;
    private int comprasDetId;
    private int comprasIdbu;
    private int comprasDetIdbu; //aqui guardaré de cuál se hizo backup
    private String siglas ;//para guardar las siglas

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    @TypeConverters(Converters.class)
    private Date createdAt;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    @TypeConverters(Converters.class)
    private Date updatedAt;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInformesId() {
        return informesId;
    }

    public void setInformesId(int informesId) {
        this.informesId = informesId;
    }


    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public int getEstatusSync() {
        return estatusSync;
    }

    public void setEstatusSync(int estatusSync) {
        this.estatusSync = estatusSync;
    }



    public String getEmpaque() {
        return empaque;
    }

    public void setEmpaque(String empaque) {
        this.empaque = empaque;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Date getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(Date caducidad) {
        this.caducidad = caducidad;
    }


    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public int getFoto_codigo_produccion() {
        return foto_codigo_produccion;
    }

    public void setFoto_codigo_produccion(int foto_codigo_produccion) {
        this.foto_codigo_produccion = foto_codigo_produccion;
    }

    public int getEnergia() {
        return energia;
    }

    public void setEnergia(int energia) {
        this.energia = energia;
    }

    public int getProducto_exhibido() {
        return producto_exhibido;
    }

    public void setProducto_exhibido(int producto_exhibido) {
        this.producto_exhibido = producto_exhibido;
    }

    public int getFoto_num_tienda() {
        return foto_num_tienda;
    }

    public void setFoto_num_tienda(int foto_num_tienda) {
        this.foto_num_tienda = foto_num_tienda;
    }

    public int getMarca_traslape() {
        return marca_traslape;
    }

    public void setMarca_traslape(int marca_traslape) {
        this.marca_traslape = marca_traslape;
    }

    public String getAtributoa() {
        return atributoa;
    }

    public void setAtributoa(String atributoa) {
        this.atributoa = atributoa;
    }

    public int getFoto_atributoa() {
        return foto_atributoa;
    }

    public void setFoto_atributoa(int foto_atributoa) {
        this.foto_atributoa = foto_atributoa;
    }

    public String getAtributob() {
        return atributob;
    }

    public void setAtributob(String atributob) {
        this.atributob = atributob;
    }

    public int getFoto_atributob() {
        return foto_atributob;
    }

    public void setFoto_atributob(int foto_atributob) {
        this.foto_atributob = foto_atributob;
    }

    public String getAtributoc() {
        return atributoc;
    }

    public void setAtributoc(String atributoc) {
        this.atributoc = atributoc;
    }

    public int getFoto_atributoc() {
        return foto_atributoc;
    }

    public void setFoto_atributoc(int foto_atributoc) {
        this.foto_atributoc = foto_atributoc;
    }

    public int getEtiqueta_evaluacion() {
        return etiqueta_evaluacion;
    }

    public void setEtiqueta_evaluacion(int etiqueta_evaluacion) {
        this.etiqueta_evaluacion = etiqueta_evaluacion;
    }


    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }


    public int getProductoId() {
        return productoId;
    }

    public int getNumMuestra() {
        return numMuestra;
    }

    public void setNumMuestra(int numMuestra) {
        this.numMuestra = numMuestra;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public int getEmpaquesId() {
        return empaquesId;
    }

    public void setEmpaquesId(int empaquesId) {
        this.empaquesId = empaquesId;
    }

    public int getTipoMuestra() {
        return tipoMuestra;
    }

    public void setTipoMuestra(int tipoMuestra) {
        this.tipoMuestra = tipoMuestra;
    }

    public String getNombreTipoMuestra() {
        return nombreTipoMuestra;
    }

    public void setNombreTipoMuestra(String nombreTipoMuestra) {
        this.nombreTipoMuestra = nombreTipoMuestra;
    }

    public int getAzucares() {
        return azucares;
    }

    public void setAzucares(int azucares) {
        this.azucares = azucares;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public int getTipoAnalisis() {
        return tipoAnalisis;
    }

    public void setTipoAnalisis(int tipoAnalisis) {
        this.tipoAnalisis = tipoAnalisis;
    }

    public String getNombreAnalisis() {
        return nombreAnalisis;
    }

    public void setNombreAnalisis(String nombreAnalisis) {
        this.nombreAnalisis = nombreAnalisis;
    }

    public int getComprasId() {
        return comprasId;
    }

    public void setComprasId(int comprasId) {
        this.comprasId = comprasId;
    }

    public int getComprasDetId() {
        return comprasDetId;
    }

    public void setComprasDetId(int comprasDetId) {
        this.comprasDetId = comprasDetId;
    }

    public int getTamanioId() {
        return tamanioId;
    }

    public void setTamanioId(int tamanioId) {
        this.tamanioId = tamanioId;
    }

    public void setTamanioId(String tamanioId) {
        this.tamanioId = Integer.parseInt(tamanioId);
    }
    public void setComprasDetId(String comprasDetId) {
        this.comprasDetId = Integer.parseInt(comprasDetId);
    }
    public void setComprasId(String comprasId) {
        this.comprasId = Integer.parseInt(comprasId);
    }
    public void setTipoAnalisis(String tipoAnalisis) {
        this.tipoAnalisis = Integer.parseInt(tipoAnalisis);
    }

    public void setTipoMuestra(String tipoMuestra) {
        this.tipoMuestra = Integer.parseInt(tipoMuestra);
    }
    public void setEmpaquesId(String empaquesId) {
        this.empaquesId = Integer.parseInt(empaquesId);
    }
     public void setNumMuestra(String numMuestra) {
        this.numMuestra = Integer.parseInt(numMuestra);
    }

    public void setProductoId(String productoId) {
        this.productoId = Integer.parseInt(productoId);
    }

    public int getComprasIdbu() {
        return comprasIdbu;
    }

    public void setComprasIdbu(int comprasIdbu) {
        this.comprasIdbu = comprasIdbu;
    }
    public void setComprasIdbu(String comprasId) {
        this.comprasIdbu = Integer.parseInt(comprasId);
    }

    public int getComprasDetIdbu() {
        return comprasDetIdbu;
    }
    public void setComprasDetIdbu(String comprasId) {
        this.comprasDetIdbu = Integer.parseInt(comprasId);
    }

    public void setComprasDetIdbu(int comprasDetIdbu) {
        this.comprasDetIdbu = comprasDetIdbu;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public String getCaducidadFormat(){
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yy");
        return sdf.format(caducidad);
    }
}


package com.example.comprasmu.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.ui.informe.NuevoinformeFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE;
import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

public class CreadorFormulario<T> {

    private CampoForm infocampo;
    private List<CampoForm> listaCampos;
    private String required,disabled;
    private boolean readonly;
    private Context context;



    public CreadorFormulario(List<CampoForm> listaCampos, Context context) {
        this.listaCampos = listaCampos;
        this.context = context;
    }

    public LinearLayout crearFormulario(){
        LinearLayout formulario=new LinearLayout(context);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 20, 0, 20);
        formulario.setLayoutParams(lp);
        formulario.setOrientation(LinearLayout.VERTICAL);
        formulario.setGravity(Gravity.CENTER);

        this.listaCampos = listaCampos;


        for(CampoForm campo :listaCampos){
           this.infocampo=campo;
            this.required=(infocampo.required!=null)&&this.infocampo.required.equals("required")?"required":"";
            this.readonly=infocampo.readonly!=null?true:false;
            this.disabled=(this.infocampo.disabled!=null)?"disabled":"";
            //    echo "<br>".this.infocampo["nombre_campo"];
          //  func=this.infocampo["type"];
          //  formulario.addView();
            //crear el label
            TextView tv=new TextView(context);
           // tv.setText(Html.fromHtml("<b>"+this.infocampo.label+"</b>"));
            tv.setText(this.infocampo.label);
            formulario.addView(tv);
            try {
                Class claseCargada = this.getClass();
                Method metodo = null;
                Log.d(Constantes.TAG,"nombre campo: " +this.infocampo.type);
                metodo = claseCargada.getDeclaredMethod(this.infocampo.type);
                Object obj = metodo.invoke(this);
                if(this.infocampo.type.equals("radiobutton")){ //para radiobutton es vertical
                    LinearLayout linea=new LinearLayout(context);
                    linea.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    linea.setOrientation(LinearLayout.HORIZONTAL);
                    linea.addView((View)obj);
                    obj=linea;
                }
//Ejecucion del m?todo pasandole la clase de este y los parametros.

                formulario.addView((View)obj);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                Log.e(Constantes.TAG,"Hubo un error al crear la vista "+this.infocampo.nombre_campo+"  "+e.getMessage());
            }
        }

        return  formulario;

    }

    public LinearLayout crearTabla(){
        TableLayout formulario=new TableLayout(context);
        formulario.setLayoutParams(new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        formulario.setOrientation(LinearLayout.VERTICAL);
        formulario.setGravity(Gravity.CENTER);
        this.listaCampos = listaCampos;


        for(CampoForm campo :listaCampos){
            this.infocampo=campo;
            this.required=(infocampo.required!=null)&&this.infocampo.required.equals("required")?"required":"";
            this.readonly=infocampo.readonly!=null?true:false;
            this.disabled=(this.infocampo.disabled!=null)?"disabled":"";
            //    echo "<br>".this.infocampo["nombre_campo"];
            //  func=this.infocampo["type"];
            //  formulario.addView();
            //crear el label
            TextView tv=new TextView(context);
            tv.setText(Html.fromHtml("<b>"+this.infocampo.label+"</b>:   "));
            tv.setGravity(Gravity.END);
            formulario.setLayoutParams(new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT,1f));

            TableRow row=new TableRow(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 40, 0, 20);
            row.addView(tv);
            try {
                Class claseCargada = this.getClass();
                Method metodo = null;
                Log.d(Constantes.TAG,"nombre campo: " +this.infocampo.type);
                metodo = claseCargada.getDeclaredMethod(this.infocampo.type);
                Object obj = metodo.invoke(this);
                if(this.infocampo.type.equals("radiobutton")){ //para radiobutton es vertical
                    LinearLayout linea=new LinearLayout(context);
                    linea.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    linea.setOrientation(LinearLayout.HORIZONTAL);
                    linea.addView((View)obj);
                    obj=linea;
                }
//Ejecucion del m?todo pasandole la clase de este y los parametros.

                row.addView((View)obj);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                Log.e(Constantes.TAG,"Hubo un error al crear la vista "+this.infocampo.nombre_campo+"  "+e.getMessage());
            }
            formulario.addView(row);
        }


        return  formulario;

    }
    public TextView label(){
        TextView label=new TextView(context);
        label.setText(infocampo.value);

        return label;

    }
    public TextInputEditText inputtext(){
        //   return '<div class="form-group">'.
        //readonly=isset(this.infocampo["readonly"])?;
        //,,this.infocampo["disabled"]
        String valor="";
        if(this.infocampo.value!=null&&!this.infocampo.value.equals(""))
            valor=this.infocampo.value;
      //  else
      //      valor=this.instance[this.infocampo["nombre_campo"]];
        required=(this.infocampo.required!=null)&&this.infocampo.required.equals("required")?"required":"";
        TextInputEditText input=new TextInputEditText(context);
       input.setId(this.infocampo.id);
       input.setText(valor);
       input.setEnabled(!this.readonly);
     //  ".this.required." "..
     return input;

    }
    public LinearLayout inputConBuscador(){
        //   return '<div class="form-group">'.
        //readonly=isset(this.infocampo["readonly"])?;
        //,,this.infocampo["disabled"]
        LinearLayout layout=new LinearLayout(context);
        layout.setLayoutParams(new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        String valor="";
        if(this.infocampo.value!=null&&!this.infocampo.value.equals(""))
            valor=this.infocampo.value;
        //  else
        //      valor=this.instance[this.infocampo["nombre_campo"]];
        required=(this.infocampo.required!=null)&&this.infocampo.required.equals("required")?"required":"";
        TextInputEditText input=new TextInputEditText(context);
        input.setId(this.infocampo.id);
        input.setText(valor);
        input.setEnabled(!this.readonly);
        //  ".this.required." "..
        layout.addView(input);
        //Pongo el boton
        Button button=new Button(context);
        button.setText("Buscar");
        button.setOnClickListener(this.infocampo.funcionOnClick);
        button.setBackgroundColor(context.getColor(R.color.blue_principal));
        layout.addView(button);
        return layout;

    }

    public LinearLayout agregarImagen(){
        //   return '<div class="form-group">'.
        //readonly=isset(this.infocampo["readonly"])?;
        //,,this.infocampo["disabled"]
        LinearLayout layout=new LinearLayout(context);
        layout.setLayoutParams(new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
       // layout.setGravity(Gravity.CENTER);
        String valor="";
        if(this.infocampo.value!=null&&!this.infocampo.value.equals(""))
            valor=this.infocampo.value;
        //  else
        //      valor=this.instance[this.infocampo["nombre_campo"]];
        required=(this.infocampo.required!=null)&&this.infocampo.required.equals("required")?"required":"";
        EditText input=new EditText(context);
        input.setId(this.infocampo.id);
        input.setText(valor);
        input.setEnabled(!this.readonly);
        input.setLayoutParams(new LinearLayout.LayoutParams(298, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

        //  ".this.required." "..
        layout.addView(input);
       // setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //Pongo el boton
        ImageButton button=new ImageButton(context);
        Drawable replacer = context.getResources().getDrawable(R.drawable.ic_menu_camera);

        button.setImageDrawable(replacer);

         button.setOnClickListener(infocampo.funcionOnClick);
         button.setBackgroundColor(context.getColor(R.color.blue_principal));
        button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

        layout.addView(button);

        return layout;

    }
    public ImageButton botonMicro(){
        ImageButton button=new ImageButton(context);
        Drawable replacer = context.getResources().getDrawable(R.drawable.ic_baseline_mic_24);


        button.setImageDrawable(replacer);
        button.setBackgroundColor(context.getColor(R.color.blue_principal));

       return button;
    }

    public void botonGaleria(){
        ImageButton button=new ImageButton(context);
        Drawable replacer = context.getResources().getDrawable(R.drawable.ic_menu_camera);

        replacer = context.getResources().getDrawable(R.drawable.ic_menu_gallery);

        button.setImageDrawable(replacer);
        button.setBackgroundColor(context.getColor(R.color.blue_principal));
        button.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                //pongo el nombre en el textview
                //    abrirGaleria(v);

            }});
        //layout.addView(button);
    }
    public EditText password(){
        //   return '<div class="form-group">'.
        //readonly=isset(this.infocampo["readonly"])?;
        //,,this.infocampo["disabled"]
        EditText input=new EditText(context);
        required=(this.infocampo.required!=null)&&this.infocampo.required.equals("required")?"required":"";

        input.setInputType(TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        input.setId(this.infocampo.id);
        input.setText(this.infocampo.value);
                //,this.instance[this.infocampo["nombre_campo"]]).'"'.
        //this.required." ".
        input.setEnabled(!this.readonly);
        return input;

    }

    public Button boton(){


        //Pongo el boton
        Button button=new Button(context);
        button.setText(this.infocampo.value);
        button.setOnClickListener(this.infocampo.funcionOnClick);
        button.setBackgroundColor(context.getColor(R.color.blue_principal));

        return button;

    }
    public Spinner select(){
        HashMap<Integer,String> lista = this.infocampo.select;
        Log.d("lista",this.infocampo.select+"");
        Spinner mySpinner=new Spinner(context);
        Collection<String> vals = lista.values();

        String[] spinnerArray =  vals.toArray(new String[vals.size()]);
       /* for (int i = 0; i < lista.size(); i++)
        {
            spinnerArray[i] = lista.get(i);
        }*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item,spinnerArray);

        mySpinner.setAdapter(adapter);


     /*   foreach(coleccion as key=>col){
            //    echo "***".col;
            if(old(this.infocampo["nombre_campo"],this.instance[this.infocampo["nombre_campo"]])==key)
                opciones.="<option value='".key."' selected>".col."</option>";
            else
            opciones.="<option value='".key."'>".col."</option>";
        }*/
        //  die();
       mySpinner.setId(this.infocampo.id);
       //this.required.">
            return mySpinner;

    }
    public Spinner selectCat() {

        Spinner mySpinner = new Spinner(context);
        Log.d("viendo si llego bien", this.infocampo.selectcat.get(0).getCad_descripcionesp());

        ArrayAdapter catAdapter = new ArrayAdapter<CatalogoDetalle>(context, android.R.layout.simple_spinner_dropdown_item, this.infocampo.selectcat) {


            // And the "magic" goes here
            // This is for the "passive" state of the spinner
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
                TextView label = (TextView) super.getView(position, convertView, parent);
                label.setTextColor(Color.BLACK);
                // Then you can get the current item using the values array (Users array) and the current position
                // You can NOW reference each method you has created in your bean object (User class)
                CatalogoDetalle item = getItem(position);
                label.setText(item.getCad_descripcionesp());
                //TODO elegir idioma

                // And finally return your dynamic (or custom) view for each spinner item
                return label;
            }

            // And here is when the "chooser" is popped up
            // Normally is the same view, but you can customize it if you want
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                TextView label = (TextView) super.getDropDownView(position, convertView, parent);
                label.setTextColor(Color.BLACK);
                CatalogoDetalle item = getItem(position);
                label.setText(item.getCad_descripcionesp());

                return label;
            }
        };


        mySpinner.setAdapter(catAdapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the value selected by the user
                // e.g. to store it as a field or immediately call a method
                CatalogoDetalle opcion = (CatalogoDetalle) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mySpinner.setId(this.infocampo.id);
        //this.required.">
        return mySpinner;

    }
    public EditText hidden(){
        EditText input=new EditText(context);
         input.setId(this.infocampo.id);
        input.setText(this.infocampo.value);
        //,this.instance[this.infocampo["nombre_campo"]]).'"'.
        //this.required." ".
       // input.setEnabled(false);
        input.setVisibility(View.GONE);
        return input;
      }
    public RadioGroup radiobutton(){
        RadioGroup radiogrupo=new RadioGroup(context);
        for (HashMap.Entry<Integer, String> registro :this.infocampo.select.entrySet()){
            RadioButton rb = new RadioButton(context);
            rb.setId(registro.getKey()) ;
            rb.setText(registro.getValue());
            if(this.infocampo.value!=null&&this.infocampo.value.equals(registro.getValue())) {
                rb.setChecked(true);

            }
            rb.setOnClickListener(infocampo.funcionOnClick);
            radiogrupo.addView(rb);

        }
        radiogrupo.setId((Integer.parseInt(infocampo.nombre_campo)));
        return radiogrupo;


    }
    public EditText textarea(){
        EditText input=new EditText(context);
        required=(this.infocampo.required!=null)&&this.infocampo.required.equals("required")?"required":"";

        input.setInputType(TYPE_TEXT_FLAG_MULTI_LINE);
        input.setId(this.infocampo.id);
        input.setText(this.infocampo.value);
        //,this.instance[this.infocampo["nombre_campo"]]).'"'.
        //this.required." ".
        input.setEnabled(!this.readonly);
        return input;
        }
 /*   public function file(){
        return  FormFacade::label(this.infocampo["nombre_campo"],this.infocampo["label"])." ".
        '<input type="file" name="'.this.infocampo["nombre_campo"].'" value="" class="form-control-file" id="'.this.infocampo["nombre_campo"].'" '.  this.required.">" ;
    }*/
    public LinearLayout checkbox(){
        LinearLayout grupo=new LinearLayout(context);

        for (HashMap.Entry<Integer, String> registro :this.infocampo.select.entrySet()){
            CheckBox rb = new CheckBox(context);
            rb.setId(this.infocampo.id) ;
            rb.setText(registro.getValue());
            ;
            if(this.infocampo.value!=null&&this.infocampo.value.equals(registro.getKey())) {
                rb.setChecked(true);

            }
            grupo.addView(rb);

        }
        return grupo;

    }

    public ImageView imagenView(){
        ImageView imagen=new ImageView(context);
        if(infocampo.value!=null&&!infocampo.value.equals("")) {
            Bitmap bitmap1 = BitmapFactory.decodeFile(infocampo.value);
            imagen.setImageBitmap(bitmap1);
        }
        imagen.setId(infocampo.id);
        imagen.setLayoutParams(new ViewGroup.LayoutParams(350,150));
        return  imagen;
    }

    public RecyclerView recyclerView(){
        RecyclerView lista=new RecyclerView(context);
        lista.setId(infocampo.id);
        lista.setAdapter(infocampo.adapter);
        return lista;
    }

    public static void cargarSpinnerDescr(Context context,Spinner mySpinner,List<DescripcionGenerica> selectcat){
        ArrayAdapter catAdapter = new ArrayAdapter<DescripcionGenerica>(context, android.R.layout.simple_spinner_dropdown_item, selectcat) {


            // And the "magic" goes here
            // This is for the "passive" state of the spinner
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
                TextView label = (TextView) super.getView(position, convertView, parent);
                label.setTextColor(Color.BLACK);
                // Then you can get the current item using the values array (Users array) and the current position
                // You can NOW reference each method you has created in your bean object (User class)
                DescripcionGenerica item = getItem(position);
                label.setText(item.getNombre());
                //TODO elegir idioma

                // And finally return your dynamic (or custom) view for each spinner item
                return label;
            }

            // And here is when the "chooser" is popped up
            // Normally is the same view, but you can customize it if you want
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                TextView label = (TextView) super.getDropDownView(position, convertView, parent);
                label.setTextColor(Color.BLACK);
                DescripcionGenerica item = getItem(position);
                label.setText(item.getNombre());

                return label;
            }
        };


        mySpinner.setAdapter(catAdapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the value selected by the user
                // e.g. to store it as a field or immediately call a method
                DescripcionGenerica opcion = (DescripcionGenerica) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




    }
    public static void cargarSpinnerCat(Context context,Spinner mySpinner,List<CatalogoDetalle> selectcat){
        ArrayAdapter catAdapter = new ArrayAdapter<CatalogoDetalle>(context, android.R.layout.simple_spinner_dropdown_item, selectcat) {


            // And the "magic" goes here
            // This is for the "passive" state of the spinner
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
                TextView label = (TextView) super.getView(position, convertView, parent);
                label.setTextColor(Color.BLACK);
                // Then you can get the current item using the values array (Users array) and the current position
                // You can NOW reference each method you has created in your bean object (User class)
                CatalogoDetalle item = getItem(position);
                label.setText(item.getCad_descripcionesp());
                //TODO elegir idioma

                // And finally return your dynamic (or custom) view for each spinner item
                return label;
            }

            // And here is when the "chooser" is popped up
            // Normally is the same view, but you can customize it if you want
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                TextView label = (TextView) super.getDropDownView(position, convertView, parent);
                label.setTextColor(Color.BLACK);
                CatalogoDetalle item = getItem(position);
                label.setText(item.getCad_descripcionesp());

                return label;
            }
        };


        mySpinner.setAdapter(catAdapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the value selected by the user
                // e.g. to store it as a field or immediately call a method
                CatalogoDetalle opcion = (CatalogoDetalle) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




    }
}

package com.example.comprasmu.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.text.Editable;

import android.text.InputFilter;
import android.text.TextWatcher;
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

import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprasmu.R;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.DescripcionGenerica;

import com.google.android.material.textfield.TextInputEditText;

import com.santalu.maskara.Mask;
import com.santalu.maskara.MaskChangedListener;
import com.santalu.maskara.MaskStyle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.text.NumberFormat;

import java.util.Collection;

import java.util.HashMap;
import java.util.List;

import static android.text.InputType.TYPE_CLASS_NUMBER;

import static android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE;
import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

public class CreadorFormulario {

    private CampoForm infocampo;
    private List<CampoForm> listaCampos;
    private String required,disabled;
    private boolean readonly;
    private final Context context;
    public static String AGREGARIMAGEN="agregarImagen";
    public static String BOTON="boton";
    public static String BOTONMICRO="botonMicro";
    public static String BOTONQR="botonqr";
    public static String CHECKBOX="checkbox";
    public static String HIDDEN="hidden";

    public static String IMAGENVIEW="imagenView";
    public static String IMAGENVIEWR="imagenViewr";
    public static String INPUTTEXT="inputtext";
    public static String LABEL="label";
    public static String PASSWORD="password";
    public static String PREGUNTASINO="preguntasino";
    public static String RADIOBUTTON="radiobutton";
    public static String PSELECT="select";
    public static String SELECTCAT="selectCat";
    public static String SELECTDES="selectDes";
    public static String TEXTAREA="textarea";
    public static String FECHAMASK="fechaMask";
    public static String DECIMALMASK="decimalMask";


    public CreadorFormulario(List<CampoForm> listaCampos, Context context) {
        this.listaCampos = listaCampos;
        this.context = context;
    }

    public LinearLayout crearFormulario(){
        LinearLayout formulario=new LinearLayout(context);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
       // lp.setMargins(0, 20, 0, 50);
        formulario.setLayoutParams(lp);
        formulario.setOrientation(LinearLayout.VERTICAL);
        formulario.setGravity(Gravity.CENTER);

        this.listaCampos = listaCampos;


        for(CampoForm campo :listaCampos){
            this.infocampo=campo;
           // Log.d(Constantes.TAG,"nombre campo: " +this.infocampo.type);
            try {

            if(this.infocampo.type.equals("lista")) {
                //es otro layout
                CreadorFormulario layout=new CreadorFormulario(this.infocampo.listadatos,context);
                LinearLayout ll=layout.crearFormulario();
                ll.setId(this.infocampo.id);
                formulario.addView(ll);
                continue;
            }

            this.required=(infocampo.required!=null)&&this.infocampo.required.equals("required")?"required":"";
            this.readonly= infocampo.readonly != null;
            this.disabled=(this.infocampo.disabled!=null)?"disabled":"";

            //    echo "<br>".this.infocampo["nombre_campo"];
          //  func=this.infocampo["type"];
          //  formulario.addView();
            //crear el label
            TextView tv=new TextView(context);
           // tv.setText(Html.fromHtml("<b>"+this.infocampo.label+"</b>"));
            tv.setText(this.infocampo.label);
            tv.setTextAppearance(context, R.style.formlabel);
            if(infocampo.style== R.style.formlabel)
                    tv.setTextAppearance(context, R.style.formlabel);
                else
            if(infocampo.style>0)
                tv.setTextAppearance(context,infocampo.style);

                if(infocampo.style== R.style.formlabel)
                    formulario.addView(this.espacio());
                    if(!this.infocampo.type.equals("preguntasino")&&!this.infocampo.type.equals("listDivider")) {
                formulario.addView(tv);
            }

                Class claseCargada = this.getClass();
                Method metodo = null;

                metodo = claseCargada.getDeclaredMethod(this.infocampo.type);
                View obj = (View)metodo.invoke(this);
                if(this.infocampo.type.equals("radiobutton")){ //para radiobutton es vertical
                    LinearLayout linea=new LinearLayout(context);
                    linea.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    linea.setOrientation(LinearLayout.HORIZONTAL);
                    linea.addView(obj);
                    obj=linea;
                }
//Ejecucion del m?todo pasandole la clase de este y los parametros.

                formulario.addView(obj);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                Log.e(Constantes.TAG,"Hubo un error al crear la vista "+this.infocampo.nombre_campo+"  "+e.getMessage());
            }
        }

        return  formulario;

    }

    public LinearLayout crearTabla(){
        LinearLayout formulario=new LinearLayout(context);
      //  formulario.setLayoutParams(new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        formulario.setOrientation(LinearLayout.VERTICAL);
        formulario.setGravity(Gravity.CENTER);
        this.listaCampos = listaCampos;


        for(CampoForm campo :listaCampos){
            this.infocampo=campo;
            this.required=(infocampo.required!=null)&&this.infocampo.required.equals("required")?"required":"";
            this.readonly= infocampo.readonly != null;
            this.disabled=(this.infocampo.disabled!=null)?"disabled":"";
            //    echo "<br>".this.infocampo["nombre_campo"];
            //  func=this.infocampo["type"];
            //  formulario.addView();
            //crear el label
            TextView tv=new TextView(context);
            tv.setText(this.infocampo.label);
          //  tv.setGravity(Gravity.END);
            //formulario.setLayoutParams(new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
         /*  if(this.infocampo.style>0)
             tv.setTextAppearance(context, this.infocampo.style);
*/
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 20, 0, 20);
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
           // formulario.addView(formulario);
        }


        return  formulario;

    }
    public TextView label(){
        TextView label=new TextView(context);
        label.setText(infocampo.value);
        if(infocampo.style>0)
        label.setTextAppearance(context, infocampo.style);
        return label;

    }

    public View listDivider(){
        View linea=new View(context);
        int dividerHeight = (int) (context.getResources().getDisplayMetrics().density * 2); // 1dp to pixels
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight);
        lp.setMargins(0, 60, 0, 80);
        linea.setLayoutParams(lp);
        linea.setBackgroundColor(Color.parseColor("#FFBFBFBF"));

       //linea.setBackgroundColor(context.getResources().getDrawable());

        return linea;

    }

    public View espacio(){
        View linea=new View(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10);
        //lp.setMargins(0, 60, 0, 80);
        linea.setLayoutParams(lp);
        //linea.setBackgroundColor(Color.parseColor("#FFBFBFBF"));

        //linea.setBackgroundColor(context.getResources().getDrawable());

        return linea;

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
        input.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
      //  input.addTextChangedListener(new MayusTextWatcher());
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
        input.setVisibility(View.GONE);

        //  ".this.required." "..
        layout.addView(input);
       // setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //Pongo el boton
        ImageButton button=new AppCompatImageButton(context);
        Drawable replacer = context.getResources().getDrawable(R.drawable.ic_menu_camera);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        lp.setMargins(0, 30, 0, 30);
        button.setImageDrawable(replacer);

         button.setOnClickListener(infocampo.funcionOnClick);
         button.setBackgroundColor(context.getColor(R.color.blue_secondary));
        // button.set
        button.setLayoutParams(lp);

        layout.addView(button);

        return layout;

    }

    public LinearLayout botonqr(){
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
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);

        input.setLayoutParams(lp1);
        input.setId(this.infocampo.id);
        input.setText(valor);
        input.setEnabled(!this.readonly);
        //input.setVisibility(View.GONE);

        //  ".this.required." "..
        layout.addView(input);
        // setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //Pongo el boton
        ImageButton button=new ImageButton(context);
        Drawable replacer = context.getDrawable(R.drawable.ic_baseline_qr_code_scanner_24);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        lp.setMargins(0, 30, 0, 30);
        button.setImageDrawable(replacer);

        button.setOnClickListener(infocampo.funcionOnClick);
        button.setBackgroundColor(context.getColor(R.color.blue_principal));
        button.setLayoutParams(lp);

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
    public TextInputEditText fechaMask(){
        Mask mask = new Mask(
                "__-__-__",
                 '_',
                MaskStyle.PERSISTENT
        );
        MaskChangedListener listener =new  MaskChangedListener(mask);
        TextInputEditText textField=new TextInputEditText(context);
       textField.addTextChangedListener(listener);
        String valor="";
        if(this.infocampo.value!=null&&!this.infocampo.value.equals(""))
            valor=this.infocampo.value;
        //  else
        //      valor=this.instance[this.infocampo["nombre_campo"]];
        required=(this.infocampo.required!=null)&&this.infocampo.required.equals("required")?"required":"";

        textField.setId(this.infocampo.id);
        textField.setText(valor);
        textField.setEnabled(!this.readonly);
        textField.setInputType(TYPE_CLASS_NUMBER);

       return  textField;

    }

    public TextInputEditText decimalMask(){
/*        Mask mask = new Mask(
                "___.__",
                '_',
                MaskStyle.PERSISTENT
        );
        MaskChangedListener listener2 =new  MaskChangedListener(mask);*/
        TextInputEditText textField=new TextInputEditText(context);
       // textField.addTextChangedListener(listener2);
        String valor="";
        if(this.infocampo.value!=null&&!this.infocampo.value.equals(""))
            valor=this.infocampo.value;
        //  else
        //      valor=this.instance[this.infocampo["nombre_campo"]];
        required=(this.infocampo.required!=null)&&this.infocampo.required.equals("required")?"required":"";

        textField.setId(this.infocampo.id);
        textField.setText(valor);
        textField.setEnabled(!this.readonly);
        textField.setRawInputType(Configuration.KEYBOARD_12KEY);
        textField.addTextChangedListener(new CurrencyTextWatcher());

      /*  textField.addTextChangedListener(new TextWatcher(){
            DecimalFormat dec = new DecimalFormat("0.00");
            @Override
            public void afterTextChanged(Editable arg0) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    textField.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,\\.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    NumberFormat format=NumberFormat.getCurrencyInstance();
                    format.setCurrency(Currency.getInstance("MXN"));

                    String formatted =format.format((parsed/100));

                    current = formatted;
                    textField.setText(formatted);
                    textField.setSelection(formatted.length());

                    textField.addTextChangedListener(this);
                }
            }
        });*/
        //   textField.setInputType(TYPE_NUMBER_FLAG_DECIMAL);
        return  textField;

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

        if(infocampo.value!=null&&infocampo.value.length()>0){
            mySpinner.setSelection(Integer.parseInt(infocampo.value)-1);
        }
       // mySpinner.setSelection(0);
        mySpinner.setId(this.infocampo.id);
       //this.required.">
            return mySpinner;

    }
    public Spinner selectCat() {

        Spinner mySpinner = new Spinner(context);
      //  Log.d("viendo si llego bien", this.infocampo.selectcat.get(0).getCad_descripcionesp());

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
        if(infocampo.value!=null&&infocampo.value.length()>0){
            //busco el valor en la lista
            for(CatalogoDetalle cat:this.infocampo.selectcat){
                Log.d("CreadorForm","val"+infocampo.value+" cat"+cat.getCad_idopcion());
                if(infocampo.value.equals(cat.getCad_descripcionesp())){
                    mySpinner.setSelection(catAdapter.getPosition(cat),true);
                    break;
                }
                if(infocampo.value.equals(cat.getCad_idopcion()+"")){
                    Log.d("CreadorForm",catAdapter.getPosition(cat)+"");

                    mySpinner.setSelection(catAdapter.getPosition(cat),true);
                    break;
                }
            }
        }

        mySpinner.setId(this.infocampo.id);
       // mySpinner.setSelection(3,true);
        //this.required.">
        return mySpinner;

    }

    public Spinner selectDes() {

        Spinner mySpinner = new Spinner(context);
        if( this.infocampo.selectdes!=null) {
         //   Log.d("CREADORFORM","hola"+ this.infocampo.selectdes.get(0).getNombre());

            ArrayAdapter catAdapter = new ArrayAdapter<DescripcionGenerica>(context, android.R.layout.simple_spinner_dropdown_item, infocampo.selectdes) {


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
            if(infocampo.value!=null&&infocampo.value.length()>0){
                //busco el valor en la lista
                for(DescripcionGenerica cat:this.infocampo.selectdes){
                      Log.d("CreadorForm","val"+infocampo.value+" cat"+cat.getId());
                    if(infocampo.value.equals(cat.getId()+"")){
                       // Log.d("CreadorForm","val"+infocampo.value+" cat"+cat.getId());
                        mySpinner.setSelection(catAdapter.getPosition(cat),true);
                        break;
                    }
                    if(infocampo.value.equals(cat.getNombre())){
                        Log.d("CreadorForm",catAdapter.getPosition(cat)+"");

                        mySpinner.setSelection(catAdapter.getPosition(cat),true);
                        break;
                    }
                }
            }
        }
        else
            Log.e(Constantes.TAG,"Error creando el select falta definir selectdes");

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
        radiogrupo.setId(infocampo.id);
        return radiogrupo;
    }

    public Preguntasino preguntasino(){
        Preguntasino radiogrupo=new Preguntasino(context);
        radiogrupo.setmLabel(infocampo.label);
        radiogrupo.setStyleLabel(R.style.formlabel);
        if(infocampo.style>0){
            radiogrupo.setStyleLabel(infocampo.style);
        }
        if(this.infocampo.value!=null&&this.infocampo.value.equals("true")) {
            radiogrupo.setSi(true);

        }
        radiogrupo.onclicksi(infocampo.funcionOnClick);
        radiogrupo.onclickno(infocampo.funcionOnClick2);


       // }
        radiogrupo.setId((infocampo.id));
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
            Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(infocampo.value,80,80);
            imagen.setImageBitmap(bitmap1);
        }
        imagen.setId(infocampo.id);
        imagen.setLayoutParams(new ViewGroup.LayoutParams(350,150));
        if(infocampo.funcionOnClick!=null)
        imagen.setOnClickListener(infocampo.funcionOnClick);
        return  imagen;
    }
    public ImageView imagenViewd(){
        ImageView imagen=new ImageView(context);
        if(infocampo.value!=null&&!infocampo.value.equals("")) {
            imagen.setImageResource(Integer.parseInt(infocampo.value));
        }
        imagen.setId(infocampo.id);
        imagen.setLayoutParams(new ViewGroup.LayoutParams(350,150));
        if(infocampo.funcionOnClick!=null)
            imagen.setOnClickListener(infocampo.funcionOnClick);
        return  imagen;
    }
    public LinearLayout imagenViewr(){
        LinearLayout layout=new LinearLayout(context);
        layout.setLayoutParams(new LinearLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        ImageView imagen=new ImageView(context);
        if(infocampo.value!=null&&!infocampo.value.equals("")) {
            Bitmap bitmap1 = ComprasUtils.decodeSampledBitmapFromResource(infocampo.value,80,80);
            imagen.setImageBitmap(bitmap1);
        }
        imagen.setVisibility(!infocampo.visible?View.GONE:View.VISIBLE);
        imagen.setId(infocampo.id);
        imagen.setLayoutParams(new ViewGroup.LayoutParams(900,500));
        layout.addView(imagen);
        //Pongo el boton
        ImageButton button=new ImageButton(context);
        Drawable replacer = context.getDrawable(R.drawable.ic_baseline_loop_24);

        button.setImageDrawable(replacer);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
       // ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) button.getLayoutParams();
        //params.rightMargin = 100; params.topMargin = 100;
        button.setVisibility(View.GONE);
       // LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 97, 80, 0);
        button.setLayoutParams(lp);
        button.setOnClickListener(infocampo.funcionOnClick);
        button.setId(infocampo.id+500);
       // button.setBackgroundColor(context.getColor(R.color.blue_principal));
        button.setLayoutParams(lp);
        layout.addView(button);
        return  layout;
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

    class CurrencyTextWatcher implements TextWatcher {

        boolean mEditing;

        public CurrencyTextWatcher() {
            mEditing = false;
        }

        public synchronized void afterTextChanged(Editable s) {
            if(!mEditing) {
                mEditing = true;

                String digits = s.toString().replaceAll("\\D", "");
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                try{
                    String formatted = nf.format(Double.parseDouble(digits)/100);
                    s.replace(0, s.length(), formatted);
                } catch (NumberFormatException nfe) {
                    s.clear();
                }

                mEditing = false;
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        public void onTextChanged(CharSequence s, int start, int before, int count) { }

    }
    class MayusTextWatcher implements TextWatcher {

        boolean mEditing;

        public MayusTextWatcher() {
            mEditing = false;
        }

        public synchronized void afterTextChanged(Editable s) {
            if(!mEditing) {
                mEditing = true;


                try{
                    if(s.length()>0) {
                        String s2=s.toString();
                        String nueva="";
                        if(!s2.equals(s2.toUpperCase()))
                        {
                            nueva=s2.toUpperCase();

                        }
                        s.replace(0, nueva.length(), s.toString().toUpperCase());
                    }
                } catch (NumberFormatException nfe) {
                    s.clear();
                }

                mEditing = false;
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        public void onTextChanged(CharSequence s, int start, int before, int count) { }

    }
}

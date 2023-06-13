package com.example.comprasmu.utils.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.comprasmu.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DescargarDocsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescargarDocsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button btndesc;
    public DescargarDocsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DescargarDocsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DescargarDocsFragment newInstance(String param1, String param2) {
        DescargarDocsFragment fragment = new DescargarDocsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root= inflater.inflate(R.layout.fragment_descargar_docs, container, false);
        btndesc = (Button) root.findViewById(R.id.btndddescargar);
        btndesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descargarPDF();
            }
        });
        return root;
    }


    public void descargarPDF(){
        String MY_URL = "http://192.168.1.84/comprasv1/api/public/etenv?cvereco=4&indice=3.2023";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MY_URL)));
    }
}
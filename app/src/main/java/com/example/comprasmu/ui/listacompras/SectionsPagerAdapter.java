package com.example.comprasmu.ui.listacompras;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.comprasmu.ui.listadetalle.ListaCompraFragment;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.utils.Constantes;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
  //  private final List<String> mFragmentTitles = new ArrayList<>();


    private ListaDetalleViewModel mViewModel;
    //private  final String[][] TAB_TITLES;
    private  final String[] TAB_TITLES;
    private int plantaSel;
   // private final Context mContext;
    private String[][][] plantas;
    int tabCount;

    public SectionsPagerAdapter(FragmentManager fm, String[] titulos, ListaDetalleViewModel vm) {
        super(fm);
       // mContext = context;
        this.tabCount=titulos.length;
        TAB_TITLES=titulos;
        mViewModel=vm;
      //  this.plantas=clientesplan;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
     //   Fragment fragment = ListaCompraFragment.newInstance(Integer.parseInt(TAB_TITLES[position][0]));
        Log.d("SECTIONSPAGERADAPTER","creando el fragment "+position);
     //   Fragment fragment = ListaCompraFragment.newInstance(Integer.parseInt(plantas[position][0]),plantas[position][1]);
       // Fragment fragment = new ListaCompraFragment(Integer.parseInt(plantas[position][0]),plantas[position][1]);

        return  mFragments.get(position);

    }
    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
      //  mFragmentTitles.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {

        return tabCount;
    }

}
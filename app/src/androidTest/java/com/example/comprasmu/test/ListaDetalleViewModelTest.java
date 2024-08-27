package com.example.comprasmu.test;

import static org.junit.Assert.assertNotNull;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.comprasmu.NavigationDrawerActivity;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.ui.listadetalle.ListaDetalleViewModel;
import com.example.comprasmu.ui.solcorreccion.ListaSolsViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import retrofit2.Response;

/***no puedo hacer pruebas unitarias de los view model porque los hice mal tendría que crear el repositorio o dao y pasarselos :(**/
@RunWith(MockitoJUnitRunner.class)
public class ListaDetalleViewModelTest {
        private ListaDetalleViewModel lcviewModel;
    @Mock
    NavigationDrawerActivity ndact;

   @Rule
   public ActivityScenarioRule<NavigationDrawerActivity> mActivityTestRule = new ActivityScenarioRule <>(NavigationDrawerActivity.class);
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        lcviewModel=new ListaDetalleViewModel(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void buscarPlantas() {
      //  Mockito.when().thenReturn();
       // lcviewModel.cargarPestañasEta("CIUDAD DE MEXICO", 0);


      //  assertNotNull(viewModel.getTotCancel());
    }
}
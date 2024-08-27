package com.example.comprasmu;

import static org.junit.Assert.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.ui.login.LoginActivity;
import com.example.comprasmu.ui.solcorreccion.ListaSolsViewModel;
import com.example.comprasmu.utils.Constantes;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ListaSolsViewModelTest {
    private ListaSolsViewModel viewModel;
    @Mock
    NavigationDrawerActivity ndact;
    /* @Rule
     public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();*/
    @Rule
    public ActivityScenarioRule<NavigationDrawerActivity> mActivityTestRule = new ActivityScenarioRule <>(NavigationDrawerActivity.class);
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        viewModel=new ListaSolsViewModel(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void contarCanceladas() {
        //  Mockito.when().thenReturn();
        Integer result= viewModel.getTotalCancell("8.2024").getValue();

        assertTrue(result>0);
        List<ListaCompra> listacomp = viewModel.cargarClientesSimplxet("Ciudad de Mexico", 3);

        // when(viewModel.contarCanceladas()).the
        assertNotNull(viewModel.getTotCancel());
    }
}
package com.example.contactos.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.example.contactos.AddContacto;
import com.example.contactos.DatosContacto;
import com.example.contactos.R;
import com.example.contactos.adaptadores.AdapterContactos;
import com.example.contactos.conexion.ConexionSQLiteHelper;
import com.example.contactos.clases.Contacto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class ContactosFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private List<Contacto> listaContactos;
    private AdapterContactos adapterContactos;
    private ConexionSQLiteHelper conexion;
    private ActionBar actionBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contactos, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        fab = v.findViewById(R.id.fab);
        listaContactos = new ArrayList<>();
        fab.setOnClickListener(this);
        conexion = new ConexionSQLiteHelper(getActivity());
        cargarContactos();
        return v;
    }


    private void searchRecords(String query) {
        AdapterContactos adapterContactos = new AdapterContactos(getActivity(), conexion.searchRecords(query));
        recyclerView.setAdapter(adapterContactos);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //searchView
        inflater.inflate(R.menu.menu_contact, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Buscar contacto");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // buscar cuando se hace clic en el botón de búsqueda en el teclado
                searchRecords(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // busca mientras escribes
                searchRecords(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarContactos();
    }

    public void cargarContactos() {
        listaContactos = conexion.getAllRecords();
        adapterContactos = new AdapterContactos(getActivity(), listaContactos);
        recyclerView.setAdapter(adapterContactos);
    }
    public void onClick(View v) {
        Intent addContactoIntent = new Intent(getActivity(), AddContacto.class);
        startActivity(addContactoIntent);
    }
}
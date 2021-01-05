package com.example.contactos.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.contactos.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;



public class MarcadorFragment extends Fragment implements View.OnClickListener {
    private Button buttonCall;
    private EditText etTelefono;
    private ListView listView;
    ArrayList<String> listallamadas;
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_marcador, container, false);
        buttonCall = v.findViewById(R.id.buttonCall);
        etTelefono = v.findViewById(R.id.editTextPhone);
        listView = v.findViewById(R.id.registrollamadas);

        cargarRegistro();
        buildListView();
        buttonCall.setOnClickListener(this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarRegistro();
        buildListView();
    }

    @Override
    public void onClick(View v) {
        if(!etTelefono.getText().toString().isEmpty()){
            String numero = etTelefono.getText().toString();
            insertNumero(numero);
            guardarRegistro();
            etTelefono.setText(null);
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+34 " + numero));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            startActivity(intent);
            guardarRegistro();
        }
        else{
            Toast.makeText(getActivity(), "No se ha marcado ningun número", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarRegistro() {
        SharedPreferences preferences = getActivity().getSharedPreferences("registro", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listallamadas);
        editor.putString("numero", json);
        editor.apply();
    }

    public void cargarRegistro() {
        SharedPreferences preferences = getActivity().getSharedPreferences("registro", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("numero", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        listallamadas = gson.fromJson(json, type);
        if(listallamadas == null){
            listallamadas = new ArrayList<>();
        }
    }

    private void buildListView() {
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listallamadas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String telefono = (String) parent.getItemAtPosition(position);
                etTelefono.setText(telefono);
            }
        });
    }
    private void insertNumero(String numero){
        listallamadas.add(numero);
        adapter.notifyDataSetChanged();
    }
}
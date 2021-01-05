package com.example.contactos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.contactos.clases.Contacto;
import com.example.contactos.conexion.ConexionSQLiteHelper;
import com.example.contactos.conexion.Utilidades;

import de.hdodenhof.circleimageview.CircleImageView;

public class DatosContacto extends AppCompatActivity implements View.OnClickListener {

    private TextView tvnombre;
    private TextView tvapellidos;
    private TextView tvtelefono;
    private TextView tvemail;
    private Contacto contacto;
    private Button btnLlamar;
    private CircleImageView imagencv;
    private ActionBar actionBar;
    private int contactoID;
    private ConexionSQLiteHelper conexion;
    private int id;
    private String nombre, apellido, telefono, email, imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_contacto);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Datos del contacto");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //obtener la identificación de registro del adaptador mediante la intención
        Intent intent = getIntent();
        contactoID = intent.getIntExtra("ID_CONTACTO", 0);

        conexion = new ConexionSQLiteHelper(this);

        tvnombre = findViewById(R.id.tvNombre);
        tvapellidos = findViewById(R.id.tvApellidos);
        tvtelefono = findViewById(R.id.tvTelefono);
        tvemail = findViewById(R.id.tvEmail);
        btnLlamar = findViewById(R.id.buttonCall);
        imagencv = findViewById(R.id.imagenContact);

        mostrarDatosContacto();
        btnLlamar.setOnClickListener(this);
    }

    public void mostrarDatosContacto() {
        String selectQuery = " SELECT * FROM " + Utilidades.TABLA_CONTACTO + " WHERE " + Utilidades.CAMPO_ID + " = "  + contactoID;
        SQLiteDatabase db = conexion.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                 id = cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_ID));
                 nombre = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOMBRE));
                 apellido = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_APELLIDO));
                 telefono = String.valueOf(cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_TELEFONO)));
                 email = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_EMAIL));
                 imagen = cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_IMAGEN));

                tvnombre.setText(nombre);
                tvapellidos.setText(apellido);
                tvtelefono.setText(String.valueOf(telefono));
                tvemail.setText(email);

                if (imagen.equals("null")) {
                    // no hay imagen en el registro, establecer predeterminado
                    imagencv.setImageResource(R.drawable.contacto);
                } else {
                    // tener imagen en el registro
                    imagencv.setImageURI(Uri.parse(imagen));
                }
            } while (cursor.moveToNext());
        }
        db.close();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+34 " + telefono));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//ir a la actividad anterior
        return super.onSupportNavigateUp();
    }

}
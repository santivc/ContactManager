package com.example.contactos;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contactos.clases.Contacto;
import com.example.contactos.conexion.ConexionSQLiteHelper;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContacto extends AppCompatActivity implements View.OnClickListener {
    private EditText etNombre;
    private EditText etApellidos;
    private EditText etTelefono;
    private EditText etEmail;
    private Toolbar toolbar;
    private ConexionSQLiteHelper conexion;
    private CircleImageView imagencv;
    private Uri imageUri;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private boolean isEditMode = false;
    private Contacto contacto;
    private String nombre, apellido, telefono, email;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacto);

        etNombre = findViewById(R.id.editTextTextPersonName);
        etApellidos = findViewById(R.id.editTextTextPersonName2);
        etTelefono = findViewById(R.id.editTextPhone);
        etEmail = findViewById(R.id.editTextTextEmailAddress);
        imagencv = findViewById(R.id.imagenContact);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.adduser));

        modificarContacto();
        conexion = new ConexionSQLiteHelper(this);
        imagencv.setOnClickListener(this);
    }

    public void modificarContacto() {
        //obtener los datos de la intencion
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);
        //establer la vista de los datos
        if (isEditMode) {
            //set View data
            contacto = (Contacto) intent.getSerializableExtra("CONTACTO");
            id = contacto.getId();
            nombre = contacto.getNombre();
            apellido = contacto.getApellido();
            telefono = String.valueOf(contacto.getTelefono());
            email = contacto.getEmail();
            imageUri = Uri.parse(contacto.getImagen());

            etNombre.setText(nombre);
            etApellidos.setText(apellido);
            etTelefono.setText(telefono);
            etEmail.setText(email);
            //sino se selecciona una imagen al agregr datos; el valor de la imagen ser "NULL"
            if (imageUri.toString().equals("null")) {
                //sino ahi imagen , set default
                imagencv.setImageResource(R.drawable.contacto);
            } else {
                imagencv.setImageURI(imageUri);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_cerrar:
                finish();
                break;
            case R.id.item_añadir:
                añadirContacto();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        cargarImagen();
    }


    private void añadirContacto() {
        Intent intent = new Intent(this, MainActivity.class);
        nombre = etNombre.getText().toString().trim();
        apellido = etApellidos.getText().toString().trim();
        telefono = etTelefono.getText().toString().trim();
        email = etEmail.getText().toString().trim();

        boolean validar = nombre.isEmpty() || apellido.isEmpty()
                || telefono.isEmpty();

        if (!validar) {
            if (isEditMode) {
                conexion.updateRecord(id, nombre, apellido, telefono, email, String.valueOf(imageUri));
                Toast.makeText(this, "El contacto se ha modificado", Toast.LENGTH_SHORT).show();
            } else {
                conexion.insertRecord(nombre, apellido, telefono, email, String.valueOf(imageUri));
                Toast.makeText(this, "El contacto se ha añadido", Toast.LENGTH_SHORT).show();
            }
            startActivity(intent);
        } else {
            Toast.makeText(this, "El contacto está vacío", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarImagen() {
        // opciones para mostrar en el diálogo
        String[] options = {"Camara", "Galeria"};
        //dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Titulo
        builder.setTitle("Seleccionar imagen");
        // establecer elementos / opciones
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // manejar clicks
                switch (which) {
                    case 0:
                        PickFromCamera();
                        break;
                    case 1:
                        PickFromGallery();
                        break;
                }
            }
        });
        // Crear / mostrar diálogo
        builder.create().show();
    }

    private void PickFromGallery() {
        // intento de elegir la imagen de la galería, la imagen se devolverá en el método onActivityResult
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void PickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Titulo de la Imagen");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripción de la imagen");
        //put image Uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // Intento de abrir la cámara para la imagen
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //Image is picked
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //Picked from gallery
                //crop image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //Picked from camera
                //crop Image
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                //Croped image received
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    //set Image
                    imagencv.setImageURI(resultUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    //ERROR
                    Exception error = result.getError();
                    Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
                }

            }

        }
    }
}
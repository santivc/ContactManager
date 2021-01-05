package com.example.contactos.adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.contactos.AddContacto;
import com.example.contactos.DatosContacto;

import com.example.contactos.MainActivity;
import com.example.contactos.R;
import com.example.contactos.clases.Contacto;
import com.example.contactos.conexion.ConexionSQLiteHelper;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterContactos extends RecyclerView.Adapter<AdapterContactos.ViewHolder> {

    private List<Contacto> recordsList;
    private Context context;
    private ConexionSQLiteHelper conexion;

    public AdapterContactos(Context context, List<Contacto> recordsList) {
        this.context = context;
        this.recordsList = recordsList;
        this.conexion = new ConexionSQLiteHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lista_contactos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contacto model = recordsList.get(position);
        int id = model.getId();
        String nombres = model.getNombre();
        String apellido = model.getApellido();
        String telefono = String.valueOf(model.getTelefono());
        String email = model.getEmail();
        String imageContact = model.getImagen();

        holder.nombres.setText(nombres + " " + apellido);
        holder.telefonos.setText(telefono);
        if (imageContact.equals("null")) {
            // no hay imagen en el registro, establecer predeterminado
            holder.imagen.setImageResource(R.drawable.contacto);
        } else {
            // tener imagen en el registro
            holder.imagen.setImageURI(Uri.parse(imageContact));
        }

        // manejar clicks de elementos (ir a la actividad de registro de detalles)

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pass record id to next activity to show details of thet record
                Intent intent = new Intent(context, DatosContacto.class);
                intent.putExtra("ID_CONTACTO", id);
                context.startActivity(intent);
            }
        });

        //manejar clicks de botones (mostrar opciones como editar, eliminar)

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreDialog(position, id, nombres, apellido, Integer.parseInt(telefono), email, imageContact);
            }
        });
    }

    public void showMoreDialog(int position, final int id, final String nombre, final String apellido, final int telefono, final String email, final String imagen) {
        //opciones para mostrar en el dialogo
        String[] options = {"Editar", "Eliminar"};
        //Dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //Agregar elementos al dialogo
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //se hace click en editar
                    //inicie la actividad para actualizar los registros existentes
                    Intent intent = new Intent(context, AddContacto.class);
                    Contacto contacto = new Contacto(id, nombre, apellido, telefono, email, imagen);
                    intent.putExtra("CONTACTO", contacto);
                    intent.putExtra("isEditMode", true);//nesecita para establecer datos existente
                    context.startActivity(intent);
                } else if (which == 1) {
                    //Hace click en la opcion eliminar
                    conexion.deleteData(id);
                    ((MainActivity) context).refreshFragment();
                    Toast.makeText(context, "El contacto se ha eliminado", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Mostrar el dialogo
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return recordsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombres, telefonos;
        CircleImageView imagen;
        ImageButton moreBtn;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            nombres = itemView.findViewById(R.id.tvNombres);
            telefonos = itemView.findViewById(R.id.tvTelefonos);
            imagen = itemView.findViewById(R.id.imagenContact);
            moreBtn = itemView.findViewById(R.id.moreBtn);
        }

    }

}


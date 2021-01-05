package com.example.contactos.conexion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.contactos.clases.Contacto;

import java.util.ArrayList;


public class ConexionSQLiteHelper extends SQLiteOpenHelper {


    public ConexionSQLiteHelper(@Nullable Context context) {
        super(context, Utilidades.DB_NAME, null, Utilidades.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_CONTACTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_CONTACTO);
        onCreate(db);
    }

    //Inserta datos a la base de datos
    public void insertRecord(String nombre, String apellido, String telefono, String email, String imagen) {
        //get databse grabable porque queremos escribir datos
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues registro = new ContentValues();
        // inserta datos
        registro.put(Utilidades.CAMPO_NOMBRE, nombre);
        registro.put(Utilidades.CAMPO_APELLIDO, apellido);
        registro.put(Utilidades.CAMPO_TELEFONO, Integer.parseInt(telefono));
        registro.put(Utilidades.CAMPO_EMAIL, email);
        registro.put(Utilidades.CAMPO_IMAGEN, imagen);
        // insertar fila, devolverá la identificación del registro guardado
        db.insert(Utilidades.TABLA_CONTACTO, null, registro);
        // cerrar db Connection
        db.close();
    }

    public ArrayList<Contacto> getAllRecords() {
        // la orden de consulta permitirá ordenar los datos más nuevo / más antiguo primero, nombre ascendente / descendente
        // devolverá la lista o registros ya que hemos utilizado return tipo ArrayList <ModelRecord>
        ArrayList<Contacto> recordsList = new ArrayList<>();
        // consulta para seleccionar registros
        String selectQuery = " SELECT * FROM " + Utilidades.TABLA_CONTACTO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // recorrer todos los registros y agregarlos a la lista
        if (cursor.moveToFirst()) {
            do {
                Contacto contacto = new Contacto(
                        cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_ID)),
                        cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_APELLIDO)),
                        cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_TELEFONO)),
                        cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_IMAGEN)));
                // Añadir registro a la list
                recordsList.add(contacto);
            } while (cursor.moveToNext());
        }
        //cierre de conexión db
        //retorna la lista
        return recordsList;
    }
    public ArrayList<Contacto> searchRecords(String query) {
        // la orden de consulta permitirá ordenar los datos más nuevo / más antiguo primero, nombre ascendente / descendente
        // devolverá la lista o registros ya que hemos utilizado return tipo ArrayList <ModelRecord>
        ArrayList<Contacto> recordsList = new ArrayList<>();
        // consulta para seleccionar registros
        String selectQuery = " SELECT * FROM " + Utilidades.TABLA_CONTACTO + " WHERE " + Utilidades.CAMPO_NOMBRE + " LIKE '%" + query + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // recorrer todos los registros y agregarlos a la lista
        if (cursor.moveToFirst()) {
            do {
                Contacto contacto = new Contacto(
                        cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_ID)),
                        cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_NOMBRE)),
                        cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_APELLIDO)),
                        cursor.getInt(cursor.getColumnIndex(Utilidades.CAMPO_TELEFONO)),
                        cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(Utilidades.CAMPO_IMAGEN)));
                // Añadir registro a la list
                recordsList.add(contacto);
            } while (cursor.moveToNext());
        }
        //cierre de conexión db
        db.close();
        //retorna la lista
        return recordsList;
    }

    //Obtener el numero de registros
    public int getRecordsCount() {
        String countQuery = " SELECT * FROM " + Utilidades.TABLA_CONTACTO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public void updateRecord(int id, String nombre, String apellido, String telefono, String email, String imagen){
        //get databse grabable porque queremos escribir datos
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues registro = new ContentValues();
        // inserta datos
        registro.put(Utilidades.CAMPO_NOMBRE, nombre);
        registro.put(Utilidades.CAMPO_APELLIDO, apellido);
        registro.put(Utilidades.CAMPO_TELEFONO, Integer.parseInt(telefono));
        registro.put(Utilidades.CAMPO_EMAIL, email);
        registro.put(Utilidades.CAMPO_IMAGEN, imagen);
        // update datos
        String[] args = new String []{String.valueOf(id)};
        db.update(Utilidades.TABLA_CONTACTO, registro, Utilidades.CAMPO_ID + " = ?", args);
        // cerrar db Connection
        db.close();

    }
    public void deleteData(int id){
        SQLiteDatabase db =this.getWritableDatabase();
        String[] args = new String []{String.valueOf(id)};
        db.delete(Utilidades.TABLA_CONTACTO, Utilidades.CAMPO_ID + " = ?", args);
        db.close();
    }

}
package com.example.contactos.conexion;

public class Utilidades {
    // nombre base de datos
    public static final String DB_NAME = "dbcontactos";
    //version de base de datos
    public static final int DB_VERSION = 1;
    //nombre de la tabla
    public static final String TABLA_CONTACTO = "contactos";
    //Constantes campos tabla usuario
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_APELLIDO = "apellido";
    public static final String CAMPO_TELEFONO = "telefono";
    public static final String CAMPO_EMAIL = "email";
    public static final String CAMPO_IMAGEN = "imagen";

    public static final String CREAR_TABLA_CONTACTOS = "CREATE TABLE " + TABLA_CONTACTO
            + " (" + CAMPO_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT, "
            + CAMPO_NOMBRE + " TEXT, "
            + CAMPO_APELLIDO + " TEXT, "
            + CAMPO_TELEFONO + " INTEGER, "
            + CAMPO_EMAIL + " TEXT, "
            + CAMPO_IMAGEN + " TEXT)";
}

package com.sistemac.MDI;

public class SesionUsuario {
    private static SesionUsuario instancia;
    private int userID;
    private String userName;
    private String rol;

    private SesionUsuario() {
        // Constructor privado para evitar instanciación
    }

    public static SesionUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SesionUsuario();
        }
        return instancia;
    }

    public void setDatosUsuario(int userID, String userName, String rol) {
        this.userID = userID;
        this.userName = userName;
        this.rol = rol;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getRol() {
        return rol;
    }
}

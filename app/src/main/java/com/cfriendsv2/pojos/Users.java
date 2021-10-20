package com.cfriendsv2.pojos;

public class Users {
    private String id;
    private String nombre;
    private String password;
    private String email;
    private String imagen;
    private String estado;
    private String fecha;
    private String hora;
    private int solicitudes;
    private int nuevos_mensajes;

    public Users() {
    }

    public Users(String id, String nombre, String password, String email, String imagen, String estado, String fecha, String hora, int solicitudes, int nuevos_mensajes) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
        this.email = email;
        this.imagen = imagen;
        this.estado = estado;
        this.fecha = fecha;
        this.hora = hora;
        this.solicitudes = solicitudes;
        this.nuevos_mensajes = nuevos_mensajes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(int solicitudes) {
        this.solicitudes = solicitudes;
    }

    public int getNuevos_mensajes() {
        return nuevos_mensajes;
    }

    public void setNuevos_mensajes(int nuevos_mensajes) {
        this.nuevos_mensajes = nuevos_mensajes;
    }
}

package com.cfriendsv2.pojos;

public class Estado {
    String estado;
    String fecha;
    String hora;
    String chatCon;

    public Estado() {
    }

    public Estado(String estado, String fecha, String hora, String chatCon) {
        this.estado = estado;
        this.fecha = fecha;
        this.hora = hora;
        this.chatCon = chatCon;
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

    public String getChatCon() {
        return chatCon;
    }

    public void setChatCon(String chatCon) {
        this.chatCon = chatCon;
    }
}

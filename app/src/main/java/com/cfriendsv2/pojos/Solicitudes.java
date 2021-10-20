package com.cfriendsv2.pojos;

public class Solicitudes {
    String estado;
    String idChat;

    public Solicitudes() {
    }

    public Solicitudes(String estado, String idChat) {
        this.estado = estado;
        this.idChat = idChat;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }
}

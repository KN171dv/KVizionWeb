package com.kvizion.model;

public class Usuario {

    public static final String PERFIL_ADMIN = "ADMIN";
    public static final String PERFIL_VENDEDOR = "VENDEDOR";

    private int id;
    private String nome;
    private String login;
    private String perfil;

    public Usuario(int id, String nome, String login, String perfil) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.perfil = perfil;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public String getPerfil() {
        return perfil;
    }

    public boolean isAdmin() {
        return PERFIL_ADMIN.equalsIgnoreCase(perfil);
    }
}

package com.leonardoserra.watchlist.ViewModels;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private ArrayList<MovieViewModel> myMovies;
    private String nome;
    private String token;
    private String hash;

    public User() {
        this.myMovies = new ArrayList();
    }

    public void adicionaFilme(MovieViewModel pMovieViewModel) {
        pMovieViewModel.setIsInMyList(true);
        this.myMovies.add(pMovieViewModel);
    }

    public void removeFilme(MovieViewModel pMovieViewModel) {
        pMovieViewModel.setIsInMyList(false);
        this.myMovies.remove(pMovieViewModel);
    }

    public String obterNome() {
        return this.nome;
    }

    public void definirNome(String pNome) {
        this.nome = pNome;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}

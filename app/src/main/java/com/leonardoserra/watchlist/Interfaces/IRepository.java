package com.leonardoserra.watchlist.Interfaces;

import com.leonardoserra.watchlist.Domain.Filme;

import java.util.ArrayList;

public interface IRepository extends IObservador {
    String criarOuObterUsuario(String usuario);
    ArrayList<Filme> obterMyListt();
    ArrayList<Filme> buscar(String termo);
    boolean removerFilme(Filme filme);
    boolean adicionarFilme(Filme filme);
}

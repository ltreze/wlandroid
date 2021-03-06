package com.leonardoserra.watchlist.Repository;

import com.leonardoserra.watchlist.Interfaces.IITerador;
import com.leonardoserra.watchlist.Interfaces.IRepository;

public class RepositoryIterator implements IITerador {

    IRepository[] itens;

    int posicao = 0;

    public RepositoryIterator(IRepository[] repos){
        itens = repos;
    }

    public void resetPosition(){
        posicao = 0;
    }

    public Object next(){
        IRepository iRepository = itens[posicao];
        posicao++;
        return iRepository;
    }

    public boolean hasNext() {
        if (posicao >= itens.length || itens[posicao] == null) {
            return false;
        } else {
            return true;
        }
    }
}

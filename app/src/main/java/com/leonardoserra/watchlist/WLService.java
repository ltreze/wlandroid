package com.leonardoserra.watchlist;

import android.content.Context;

import com.leonardoserra.watchlist.Bean.Filme;
import com.leonardoserra.watchlist.Interfaces.IRepository;
import com.leonardoserra.watchlist.Repository.CloudRepository;
import com.leonardoserra.watchlist.Repository.DeviceRepository;
import com.leonardoserra.watchlist.Repository.RepositoryIterator;

import java.util.ArrayList;
import java.util.Random;

public class WLService {

    private IRepository[] repositories;
    private RepositoryIterator repositoryIterator;
    private CloudRepository cloudRepository;

    public WLService(Context context){
        DeviceRepository deviceRepository = new DeviceRepository(context);
        cloudRepository = new CloudRepository();

        repositories = new IRepository[2];
        repositories[0] = deviceRepository;
        repositories[1] = cloudRepository;

        deviceRepository.registrarObservador(cloudRepository);
        cloudRepository.registrarObservador(deviceRepository);

        repositoryIterator = new RepositoryIterator(repositories);
    }

    public String criarOuObterUsuario(String usuario){
        boolean encontrou = false;

        while(!encontrou && repositoryIterator.hasNext()){
            IRepository iRepository = (IRepository)repositoryIterator.next();
            usuario = iRepository.criarOuObterUsuario(usuario);
            if (usuario != null){
                encontrou = true;
            }
        }
        repositoryIterator.resetPosition();
        return usuario;
    }

    public String[] obterItensCarrossel(){
        String[] urls = null;

        boolean encontrou = false;

        while(!encontrou && repositoryIterator.hasNext()){
            IRepository iRepository = (IRepository)repositoryIterator.next();
            urls = iRepository.obterItensCarrossel();
            if (urls != null){
                encontrou = true;
            }
        }
        repositoryIterator.resetPosition();
        return urls;
    }

    public ArrayList<Filme> obterMyListt(){
        ArrayList<Filme> filmes = null;
        boolean encontrou = false;

        while(!encontrou && repositoryIterator.hasNext()){
            IRepository iRepository = (IRepository)repositoryIterator.next();
            int aleatorio = Aleatorio.gerar();
            if ((iRepository instanceof DeviceRepository) && (aleatorio % 2 == 1)){
                iRepository = (IRepository)repositoryIterator.next();
            }
            filmes = iRepository.obterMyListt();
            if (filmes != null){
                encontrou = true;
            }
        }
        repositoryIterator.resetPosition();
        return filmes;
    }

    public ArrayList<Filme> buscar(String termo){
        ArrayList<Filme> filmes = null;

        boolean encontrou = false;

        while(!encontrou && repositoryIterator.hasNext()){
            IRepository iRepository = (IRepository)repositoryIterator.next();
            filmes = iRepository.buscar(termo);
            if (filmes != null){
                encontrou = true;
            }
        }
        repositoryIterator.resetPosition();
        return filmes;
    }

    public Boolean adicionarFilme(Filme filme){
        boolean sucesso = false;

        boolean encontrou = false;

        while(!encontrou && repositoryIterator.hasNext()){
            IRepository iRepository = (IRepository)repositoryIterator.next();
            sucesso = iRepository.adicionarFilme(filme);
            if (sucesso){
                encontrou = true;
            }
        }
        repositoryIterator.resetPosition();
        return sucesso;
    }

    public boolean removerFilme(Filme filme){
        boolean sucesso = false;

        boolean encontrou = false;

        while(!encontrou && repositoryIterator.hasNext()){
            IRepository iRepository = (IRepository)repositoryIterator.next();
            sucesso = iRepository.removerFilme(filme);
            if (sucesso){
                encontrou = true;
            }
        }
        repositoryIterator.resetPosition();
        return sucesso;
    }

    private static final class Aleatorio {

        static int gerar() {
            Random randomGenerator = new Random();

            return randomGenerator.nextInt(100);
        }
    }
}

package com.leonardoserra.watchlist.Repository;

import com.google.gson.Gson;
import com.leonardoserra.watchlist.ApiHelper;
import com.leonardoserra.watchlist.Bean.Filme;
import com.leonardoserra.watchlist.Interfaces.IObservador;
import com.leonardoserra.watchlist.Interfaces.IRepository;
import com.leonardoserra.watchlist.Interfaces.ISujeito;
import com.leonardoserra.watchlist.ViewModels.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CloudRepository implements IRepository, IObservador, ISujeito {

    private ApiHelper apiHelper;
    private String hash;
    private ArrayList<IObservador> observadores = new ArrayList<>();

    public CloudRepository(){
        apiHelper = new ApiHelper();
    }

    public String criarOuObterUsuario(String usuario){
        Message msgCreateUser = apiHelper.createuser(usuario);

        try {
            if (msgCreateUser.getSucess()) {
                String usuarioApi = msgCreateUser.getObject("hash");
                if ((usuario == null && usuarioApi != null) || (usuario != null && !usuario.equals(usuarioApi))) {
                    notificarObservadores("usuario", usuarioApi);
                    notificarObservadores("mylistt_zerada", new ArrayList<Filme>());
                    hash = usuarioApi;
                }
            } else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return hash;
    }

    public ArrayList<Filme> obterMyListt(){

        ArrayList<Filme> myListt = null;

        Message msg = apiHelper.obterMyListt(hash);

        if (msg != null && msg.getObject() != null) {
            JSONObject mylisttJson = msg.getObject();
            JSONArray jsonArray;

            try {
                jsonArray = mylisttJson.getJSONArray("mylistt");

                if (jsonArray != null) {
                    myListt = new ArrayList<>();

                    int qtdMyListt = jsonArray.length();

                    if (qtdMyListt > 0) {

                        for (int i = 0; i < qtdMyListt; i++) {
                            String str = jsonArray.get(i).toString();
                            Filme f = new Gson().fromJson(str, Filme.class);
                            myListt.add(f);
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return myListt;
    }

    public ArrayList<Filme> buscar(String termo) {

        ArrayList<Filme> resultadoDaBusca = null;
        Message msg = apiHelper.search(hash, termo);

        if (msg != null && msg.getObject() != null) {
            try {

                JSONObject object = msg.getObject();
                JSONArray jsonArray = object.getJSONArray("movies");

                if (jsonArray != null) {
                    resultadoDaBusca = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String str = jsonArray.get(i).toString();
                        Filme f = new Gson().fromJson(str, Filme.class);

                        resultadoDaBusca.add(f);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoDaBusca;
    }

    public String[] obterItensCarrossel() {

        //String[] resultadoArray = null;
        List<String> resultado = new ArrayList<String>();
        Message msg = apiHelper.obterItensCarrossel();

        if (msg != null && msg.getObject() != null) {
            try {

                JSONObject object = msg.getObject();
                JSONArray jsonArray = object.getJSONArray("urls");

                if (jsonArray != null) {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String str = jsonArray.get(i).toString();
                        resultado.add(str);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String[] simpleArray = new String[ resultado.size() ];
        resultado.toArray( simpleArray );

        return simpleArray;
    }

    public boolean removerFilme(Filme filme){
        boolean sucesso = false;
        try {
            apiHelper.removeMovie(hash, filme);
            sucesso = true;
            notificarObservadores("filme_removido", filme);
        } catch (Exception ex){
            sucesso = false;
            ex.printStackTrace();
        }
        return sucesso;
    }

    /*
        Padrao Observer
     */

    public boolean adicionarFilme(Filme filme){
        boolean sucesso = false;
        try {
            apiHelper.adicionaFilme(hash, filme);
            sucesso = true;
            notificarObservadores("filme_adicionado", filme);
        } catch (Exception ex){
            sucesso = false;
            ex.printStackTrace();
        }
        return sucesso;
    }

    public void registrarObservador(IObservador o) {
        observadores.add(o);
    }

    public void removerObservador(IObservador o) {
        observadores.remove(o);
    }

    public void notificarObservadores(String param, Object valor) {
        for(IObservador o : observadores) {
            o.atualizar(this, param, valor);
        }
    }

    public void atualizar(ISujeito s, String p, Object v) {
        if (s != this) {

            if (p.equals("usuario")){

                if (v != null && !v.toString().equals("")) {

                    //ApiHelper api = new ApiHelper();
                    //try {
                    //Message msg = api.createuser("");
                    hash = v.toString();

                    //if (msg.getSucess()) {

                    //hash = msg.getObject("hash");
                    //notificarObservadores("usuario", hash);
                    //}
                    //} catch (Exception ex){
                    //    ex.printStackTrace();
                    //}
                }
            }
        }
    }
}
package com.leonardoserra.watchlist.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.leonardoserra.watchlist.Bean.Filme;
import com.leonardoserra.watchlist.MovieAdapter;
import com.leonardoserra.watchlist.R;
import com.leonardoserra.watchlist.Singleton;
import com.leonardoserra.watchlist.ViewModels.MovieViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ResultadoBuscaActivity extends AppCompatActivity {

    private Bundle bundle;
    private ListView listView;
    private String termo;
    private MovieAdapter movieAdapter;
    private TextView txtFraseBusca;
    private ArrayList<MovieViewModel> lista;
    private ArrayList<Filme> filmes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_busca);

        bundle = getIntent().getExtras();

        listView = (ListView) findViewById(R.id.listViewResultadoBusca);

        termo = bundle.getString("resultadodabusca_termo");
        movieAdapter = new MovieAdapter(this, R.layout.simple_row, termo);

        Resources r = getResources();

        listView.setAdapter(movieAdapter);

        filmes = Singleton.getInstance().getWLService().buscar(termo);

        Collections.sort(filmes, new CustomComparator());

        lista = ToViewModel(filmes);

        for(final MovieViewModel entry : lista) {
            movieAdapter.add(entry);
        }

        txtFraseBusca = (TextView) findViewById(R.id.txtFraseBusca);
        String suaBuscaPara = r.getString(R.string.sua_busca_para);
        String retornou = r.getString(R.string.retornou);
        Integer qtd = lista.size();
        String resultados = qtd == 1 ? r.getString(R.string.resultado) : r.getString(R.string.resultados);
        txtFraseBusca.setText(suaBuscaPara + " \"" + termo + "\" " + retornou + " " + qtd + " " + resultados);

        configuraActionbar();
    }

    private ArrayList<MovieViewModel> ToViewModel(ArrayList<Filme> filmes){

        ArrayList<MovieViewModel> models = new ArrayList<>();
        for (Filme f : filmes){
            MovieViewModel m = new MovieViewModel();
            m.set_id(f.get_id());
            m.setTitulo(f.getTitulo() + f.getPopularidade());
            m.setTituloOriginal(f.getTituloOriginal());
            m.setIsInMyList(f.getIsInMyList());
            m.setPoster(f.getPoster());
            m.setUrlPoster(f.getUrlPoster());
            m.setDataLancamento(f.getDataLancamento());

            models.add(m);
        }
        return models;
    }

    private void configuraActionbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //titulo
        String titulo = getResources().getString(R.string.app_name) == null ? "WatchListt" : getResources().getString(R.string.app_name);
        getSupportActionBar().setTitle(titulo);
    }

    public void vaiParaMyWatchListt(View view){
        startActivity(new Intent(this, MyWatchListt.class));
    }

    public void vaiParaBusca(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle b = data.getExtras();

            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).get_id().equals(b.getString("filme_filmeId"))) {
                    Boolean esta = b.getBoolean("filme_estaNaMyListt");
                    lista.get(i).setIsInMyList(esta);
                }
            }
        }
    }

    public class CustomComparator implements Comparator<Filme> {
        @Override
        public int compare(Filme f1, Filme f2) {
            return f2.getPopularidade().compareTo(f1.getPopularidade());
        }
    }
}

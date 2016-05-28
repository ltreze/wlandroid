package com.leonardoserra.watchlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ResultadoBuscaActivity extends AppCompatActivity {

    private TextView txtFraseBusca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_busca);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String nomeApp = getResources().getString(R.string.resultado_busca);
        getSupportActionBar().setTitle(nomeApp);

        //mostra listagem da busca
        final ListView newsEntryListView = (ListView) findViewById(R.id.listView);
        final FilmeAdapter fruitEntryAdapter = new FilmeAdapter(this, R.layout.simple_row);
        newsEntryListView.setAdapter(fruitEntryAdapter);

        // Populate the list, through the adapter
        for(final Filme entry : getNewsEntries()) {
            fruitEntryAdapter.add(entry);
        }

        txtFraseBusca = (TextView)findViewById(R.id.txtFraseBusca);

        String suaBuscaPara = getResources().getString(R.string.sua_busca_para);
        String termo = getIntent().getStringExtra("termo");
        String retornou = getResources().getString(R.string.retornou);
        Integer qtd = getIntent().getIntExtra("qtd", 0);
        String resultados = getResources().getString(R.string.resultados);

        txtFraseBusca.setText(suaBuscaPara + " \"" + termo + "\" " + retornou + " " + qtd + " " + resultados);
    }

    //obtem resultados da busca
    private List<Filme> getNewsEntries() {
        ArrayList<Filme> myListItems = null;
        myListItems = (ArrayList<Filme>)getIntent().getSerializableExtra("bundle_searchResult");

        return myListItems;
    }
}
package com.dam2.m08.movies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener
{
    //region VARIABLES
    private static boolean isFiltered = false;
    private RecylcerAdapter adapter;
    private static String tipoConsulta = "normal";
    private static String JSON_URL = "https://api.themoviedb.org/3/movie/popular?api_key=e4eef30c60f4dd1f5c89f9c4e9dcde59";
    private static String JSON_URL_NOW_PLAYING = "https://api.themoviedb.org/3/movie/now_playing?api_key=e4eef30c60f4dd1f5c89f9c4e9dcde59&language=en-US&page=1";
    private static String JSON_URL_TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?api_key=e4eef30c60f4dd1f5c89f9c4e9dcde59&language=en-US&page=1";
    private static String JSON_URL_POPULAR = "https://api.themoviedb.org/3/movie/popular?api_key=e4eef30c60f4dd1f5c89f9c4e9dcde59&language=en-US&page=1";

    public static Movie pelicula = new Movie();
    List<Movie> movieList;
    RecyclerView recyclerView;
    ImageView header;
    Button topRated, popular, nowPlaying;
    SearchView txtBuscar;
    //endregion

    //region ONCREATE
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtBuscar = findViewById(R.id.search);
        movieList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        header = findViewById(R.id.imageFilm);

        GetData getData = new GetData();
        getData.execute();

        topRated = findViewById(R.id.btnTopRated);
        popular = findViewById(R.id.btnPopular);
        nowPlaying = findViewById(R.id.btnNowPlaying);

        //region ACCIONS dels Botons
        topRated.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!tipoConsulta.equals("topRated") || isFiltered)
                {
                    tipoConsulta = "topRated";
                    changeActivity(view, tipoConsulta);
                    isFiltered = false;
                }
            }
        });

        popular.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!tipoConsulta.equals("popular") || isFiltered)
                {
                    tipoConsulta = "popular";
                    changeActivity(view, tipoConsulta);
                    isFiltered = false;
                }
            }
        });

        nowPlaying.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(!tipoConsulta.equals("nowPlaying") || isFiltered)
                {
                    tipoConsulta = "nowPlaying";
                    changeActivity(view, tipoConsulta);
                    isFiltered = false;
                }
            }
        });

        //Dispara funcio implementada SearchView
        txtBuscar.setOnQueryTextListener(this);
        //endregion
    }
    //endregion

    //region Mètodes propis
    public void changeActivity(View v, String tipoDato)
    {
        GetData data = new GetData();
        data.giveUrl(tipoDato);

        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    private void PutDataIntoRecyclerView(List<Movie> movieList)
    {
        RecylcerAdapter adaptery = new RecylcerAdapter(this, movieList);

        //EN CAS DE QUE VULGUI FER VISTA Llista
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //EN CAS DE QUE VULGUI FER VISTA AMB NOMES IMATGE TITOL I VOT DE 3 EN 3
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        adaptery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pelicula = movieList.get(recyclerView.getChildAdapterPosition(view));
                //Miro quin id estic guardant
                //Toast.makeText(getApplicationContext(), movieList.get(recyclerView.getChildAdapterPosition(view)).getTitle() + ", ID: " + movieList.get(recyclerView.getChildAdapterPosition(view)).getId(), Toast.LENGTH_SHORT).show();
                changeActivity(view);
            }
        });
        recyclerView.setAdapter(adaptery);
    }

    public void changeActivity(View v)
    {
        Intent myIntent = new Intent(this, MovieDetail.class);
        this.startActivity(myIntent);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
    //endregion

    //region Implementacions SEARCHVIEW
    @Override
    public boolean onQueryTextSubmit(String query) {return false;}
    @Override
    public boolean onQueryTextChange(String newText)
    {
        adapter = new RecylcerAdapter(getApplicationContext(), movieList);
        adapter.filtrado(newText);
        isFiltered = true;
        PutDataIntoRecyclerView(movieList);
        return false;
    }
    //endregion

    //region Peticions GET de l'API
    public class GetData extends AsyncTask<String, String, String>
    {
        public String giveUrl(String tipoBusqueda)
        {
            String urlString = null;
            if(tipoBusqueda.equals("normal")){urlString = JSON_URL;}
            if(tipoBusqueda.equals("nowPlaying")){urlString = JSON_URL_NOW_PLAYING;}
            if(tipoBusqueda.equals("popular")){urlString = JSON_URL_POPULAR;}
            if(tipoBusqueda.equals("topRated")){urlString = JSON_URL_TOP_RATED;}
            return urlString;
        }

        @Override
        protected String doInBackground(String... strings)
        {
            String current = "";
            try
            {
                URL url;
                HttpURLConnection urlConnection = null;
                try
                {
                    url = new URL(giveUrl(tipoConsulta));
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = isr.read();
                    while(data != -1)
                    {
                        current += (char) data;
                        data = isr.read();
                    }
                    return current;
                }
                catch (MalformedURLException e) {e.printStackTrace();}
                catch (IOException e) {e.printStackTrace();}
                finally
                {
                    if(urlConnection != null)
                    {
                        urlConnection.disconnect();
                    }
                }
            }
            catch (Exception e){e.printStackTrace();}
            return current;
        }

        @Override
        protected void onPostExecute(String s)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    Movie model = new Movie();
                    model.setVote(jsonObject1.getString("vote_average"));
                    model.setTitle(jsonObject1.getString("title"));
                    model.setImg(jsonObject1.getString("poster_path"));
                    model.setOverview(jsonObject1.getString("overview"));
                    model.setId(jsonObject1.getInt("id"));
                    model.setVoteCount(jsonObject1.getInt("vote_count"));
                    model.setPopularity(jsonObject1.getDouble("popularity"));
                    model.setOriginalLanguage(jsonObject1.getString("original_language"));
                    model.setOriginalTitle(jsonObject1.getString("original_title"));

                    movieList.add(model);
                }
            }
            catch (JSONException e) {e.printStackTrace();}
            PutDataIntoRecyclerView(movieList);
        }
    }
    //endregion
}
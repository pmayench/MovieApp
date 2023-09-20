package com.dam2.m08.movies;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieDetail extends AppCompatActivity
{
    //region Variables
    public static String stringCurrent;
    public static Movie model = new Movie();
    private static String JSON_URL = "https://api.themoviedb.org/3/movie/" + MainActivity.pelicula.getId() + "?api_key=e4eef30c60f4dd1f5c89f9c4e9dcde59&language=en-US";
    ImageView image;
    TextView title, overview, vote, originalTitle, originalLanguage, voteCount, popularity;
    //endregion

    //region OnCreate
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        getData();
        actualitzaView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //endregion

    //region Mètodes propis
    public void actualitzaView()
    {
        Movie movie = MainActivity.pelicula;

        image = findViewById(R.id.imageFilm);
        title = findViewById(R.id.txtTitle);
        overview = findViewById(R.id.txtOverview);
        vote = findViewById(R.id.txtVoteAverage);
        originalTitle = findViewById(R.id.txtOriginalTitle);
        originalLanguage = findViewById(R.id.txtOriginalLanguage);
        voteCount = findViewById(R.id.txtVoteCount);
        popularity = findViewById(R.id.txtPopularity);

        Glide.with(RecylcerAdapter.mContext).load("https://image.tmdb.org/t/p/w500" + movie.getImg()).into(image);
        title.setText(movie.getTitle() + " ");
        overview.setText(movie.getOverview() + " ");
        vote.setText(movie.getVote() + " ");
        originalTitle.setText(movie.getOriginalTitle() + " ");
        originalLanguage.setText(movie.getOriginalLanguage() + " ");
        voteCount.setText(movie.getVoteCount() + " ");
        popularity.setText(movie.getPopularity() + " ");
    }

    public String getData()
    {
        String current = "";
        try
        {
            URL url;
            HttpURLConnection urlConnection = null;
            try
            {
                url = new URL(JSON_URL);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream is = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int data = isr.read();
                while(data != -1)
                {
                    current += (char) data;
                    data = isr.read();
                }
                stringCurrent = current;
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
        stringCurrent = current;
        return current;
    }
    //endregion
}
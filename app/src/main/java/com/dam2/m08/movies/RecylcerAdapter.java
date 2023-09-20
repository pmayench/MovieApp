package com.dam2.m08.movies;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecylcerAdapter extends RecyclerView.Adapter<RecylcerAdapter.ViewHolder> implements View.OnClickListener
{
    //region Variables
    public static Context mContext;
    private List<Movie> mData;
    private List<Movie> listaOriginal;
    private View.OnClickListener listener;
    //endregion

    //region Constructor
    public RecylcerAdapter(Context mContext, List<Movie> mData)
    {
        this.mContext = mContext;
        this.mData = mData;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(mData);
    }
    //endregion

    //region ViewHolder
    @NonNull
    @Override
    public RecylcerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.movie_item, parent, false);

        //Escoltar el event de selecció a la llista
        v.setOnClickListener(this);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylcerAdapter.ViewHolder holder, int position)
    {
        Movie movie = mData.get(position);
        holder.txtTitle.setText(movie.getTitle());
        holder.txtVote.setText(movie.getVote());

        Glide.with(mContext).load("https://image.tmdb.org/t/p/w500" + mData.get(position).getImg()).into(holder.image);
    }

    @Override
    public int getItemCount(){return mData.size();}

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView txtVote;
        TextView txtTitle;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.imageFilm);
            txtVote = itemView.findViewById(R.id.txtVoteAverage);
            txtTitle = itemView.findViewById(R.id.txtTitle);
        }
    }
    //endregion

    //region Mètodes propis
    public void setOnClickListener(View.OnClickListener listener)
    {
        this.listener = listener;
    }

    //Metode per filtrar els camps del buscador al MainActivity
    public void filtrado(String txtBuscar)
    {
        int size = txtBuscar.length();
        if(size == 0)
        {
            mData.clear();
            mData.addAll(listaOriginal);
        }
        else
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                List<Movie> collecion = mData.stream()
                        .filter(i -> i.getTitle().toLowerCase().contains(txtBuscar.toLowerCase()))
                        .collect(Collectors.toList());
                mData.clear();
                mData.addAll(collecion);
            }
            else
            {
                for(Movie m : listaOriginal)
                {
                    if(m.getTitle().toLowerCase().contains(txtBuscar.toLowerCase()))
                    {
                        mData.add(m);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
    //endregion

    //region OnClick
    @Override
    public void onClick(View view)
    {
        if(listener != null)
            listener.onClick(view);
    }
    //endregion
}

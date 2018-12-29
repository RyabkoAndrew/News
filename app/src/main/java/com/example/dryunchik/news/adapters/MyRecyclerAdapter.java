package com.example.dryunchik.news.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dryunchik.news.RecyclerViewClickListener;
import com.example.dryunchik.news.model.NewsData;
import com.example.dryunchik.news.R;
import com.example.dryunchik.news.webservice.WebActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dryunchik on 04.04.2018.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
   private Context mContext;
    private ArrayList<NewsData> mList;

    private static RecyclerViewClickListener mRecyclerClickListener;


    public MyRecyclerAdapter(ArrayList<NewsData> list, Context context,RecyclerViewClickListener recyclerClickListener) {
       mList = list;
       mContext = context;
       mRecyclerClickListener = recyclerClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
       View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);

       return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       NewsData data = mList.get(position);

       holder.tvTitle.setText(data.getTitle());
//       if (data.getDescription() != null && data.getDescription().length() > 35){
//           holder.tvDescription.setText(data.getDescription().substring(0,35).concat("..."));
//       }
        holder.tvDescription.setText(data.getDescription());

       try {
           if (data.getUrlToImage().length() == 0){
               holder.ivImage.setImageResource(R.drawable.error);
           }else {
               Picasso.get()
                       .load(data.getUrlToImage())
                       .error(R.drawable.error)
                       .resize(160,120)
                       .into(holder.ivImage);
           }
       }catch (NullPointerException e){
           e.printStackTrace();
       }

    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        TextView tvTitle;
        TextView tvDescription;
        ImageView ivImage;
        TextView tvSave;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title);
            tvDescription = (TextView)itemView.findViewById(R.id.tv_description);
            ivImage = (ImageView)itemView.findViewById(R.id.iv_image);
            tvSave = (TextView) itemView.findViewById(R.id.tv_save);

            tvTitle.setOnClickListener(this);
            tvDescription.setOnClickListener(this);
            ivImage.setOnClickListener(this);
            tvSave.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    NewsData  data = mList.get(position);
                    mRecyclerClickListener.recyclerViewClickListener(data);
                }
            });
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext,WebActivity.class);
           int position = getAdapterPosition();
           NewsData data = mList.get(position);
            intent.putExtra(Intent.EXTRA_REFERRER,data.getUrl());
            mContext.startActivity(intent);

        }
    }
}

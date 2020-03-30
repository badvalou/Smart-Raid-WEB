package fr.cv.projetiut.smart_raid.ui.timeline;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fr.cv.projetiut.smart_raid.R;

/**
 * Created by valentincroset on November,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewAdapter.HorizontalRecyclerViewHolder> {

    private ArrayList<TimelineObject> list;

    public HorizontalRecyclerViewAdapter(ArrayList<TimelineObject> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HorizontalRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_timeline, parent, false);

        return new HorizontalRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalRecyclerViewHolder holder, final int position) {
        holder.textView.setText("" + list.get(position).getTitle());

        onLoadImage(holder.imageView, list.get(position).getImageUrl());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public void onViewRecycled(@NonNull HorizontalRecyclerViewHolder holder) {
        holder.imageView.setImageDrawable(null);
        super.onViewRecycled(holder);
    }

    public void onLoadImage(ImageView imageView, String uri) {

        Picasso.with(imageView.getContext())
                .load(uri)
                .resize(250, 250)
                .centerCrop()
                .into(imageView);

    }

    public class HorizontalRecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageView imageView;
        public CardView cardView;

        //public MaterialRatingBar ratingBar;

        public HorizontalRecyclerViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.tv_timeline_horizontal_card_name);
            cardView = view.findViewById(R.id.timeline_obj_cardview);
            imageView = view.findViewById(R.id.iv_horizontal_card_image);

            textView.setTextSize(15);
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundColor(Color.GRAY);
        }
    }


}

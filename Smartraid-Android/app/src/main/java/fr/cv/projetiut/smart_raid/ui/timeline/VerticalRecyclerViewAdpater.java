package fr.cv.projetiut.smart_raid.ui.timeline;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.cv.projetiut.smart_raid.R;

/**
 * Created by valentincroset on November,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class VerticalRecyclerViewAdpater extends RecyclerView.Adapter<VerticalRecyclerViewAdpater.VerticalRecyclerViewHolder> {

    private Context context;
    private List<String> list;
    private HorizontalRecyclerViewAdapter horizontalRecyclerViewAdapter;

    public VerticalRecyclerViewAdpater(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VerticalRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_timeline, parent, false);
        return new VerticalRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalRecyclerViewHolder holder, int position) {
        holder.time.setText(TimeLineConfig.headerList.get(position));

        horizontalRecyclerViewAdapter = new HorizontalRecyclerViewAdapter(TimeLineConfig.timelineObjMap.get(TimeLineConfig.headerList.get(position)));
        holder.recyclerView.setAdapter(horizontalRecyclerViewAdapter);
    }

    @Override
    public int getItemCount() {
        return TimeLineConfig.headerList.size();
    }

    public class VerticalRecyclerViewHolder extends RecyclerView.ViewHolder {


        public TextView time;
        public RecyclerView recyclerView;

        RelativeLayout timelineindicator_container;
        TextView timelineindicator_line;

        public VerticalRecyclerViewHolder(View view) {
            super(view);

            time = (TextView) view.findViewById(R.id.tv_timeline_time);

            timelineindicator_container = (RelativeLayout) view.findViewById(R.id.container_timeline_indicator);
            timelineindicator_line = (TextView) view.findViewById(R.id.tv_timeline_indicator_line);

            time.setTextColor(Color.parseColor(TimeLineConfig.getTimelineHeaderTextColour()));
            time.setTextSize(TimeLineConfig.getTimelineHeaderSize());
            timelineindicator_container.setBackgroundColor(Color.parseColor(TimeLineConfig.getTimelineIndicatorBackgroundColour()));

            recyclerView = (RecyclerView) view.findViewById(R.id.rv_horizontal_timeline);

            LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(recyclerViewLayoutManager);
            LinearLayoutManager horizontalLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLinearLayoutManager);
        }
    }

    public void notifyDataSetChangedToHorizontalView() {
        if (horizontalRecyclerViewAdapter == null){
            return;
        }
        horizontalRecyclerViewAdapter.notifyDataSetChanged();
    }


}

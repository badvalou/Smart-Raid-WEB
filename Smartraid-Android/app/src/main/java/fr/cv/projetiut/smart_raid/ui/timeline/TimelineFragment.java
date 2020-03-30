package fr.cv.projetiut.smart_raid.ui.timeline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.cv.projetiut.smart_raid.R;

/**
 * Created by valentincroset on November,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public class TimelineFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager recyclerViewLayoutManager;
    private LinearLayoutManager verticalLinearLayoutManager;
    private VerticalRecyclerViewAdpater verticalRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timeline, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_vertical_timeline);
        setup();
    }

    private void setup() {
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        verticalRecyclerViewAdapter = new VerticalRecyclerViewAdpater(getContext(), null);
        verticalLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLinearLayoutManager);
        recyclerView.setAdapter(verticalRecyclerViewAdapter);
    }

}

/*
package kr.nogcha.sejongbus.drawer;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import kr.nogcha.sejongbus.R;
public class DrawerFragment extends Fragment {

    private String menuname[];
    private tabAdapter tAdapter;
    private LinearLayoutManager llmanager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_main_drawer, container, false);

        RecyclerView tabView = (RecyclerView)rootView.findViewById(R.id.recycler_tab_main);
        tabView.setHasFixedSize(true);
        llmanager = new LinearLayoutManager(getActivity());
        llmanager.scrollToPosition(0);
        tAdapter = new tabAdapter();
        tabView.setLayoutManager(llmanager);
        tabView.setAdapter(tAdapter);
        tabView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }
}*/
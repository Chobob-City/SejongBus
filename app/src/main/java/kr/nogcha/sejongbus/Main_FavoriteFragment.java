package kr.nogcha.sejongbus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Main_FavoriteFragment extends Fragment {
    public static Main_FavoriteFragment newInstance(int a) {
        Main_FavoriteFragment fragment = new Main_FavoriteFragment();
        Bundle args = new Bundle();
        args.putInt("num", a);
        fragment.setArguments(args);
        return fragment;
    }

    public Main_FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

}

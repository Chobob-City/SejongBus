package kr.nogcha.sejongbus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by FullOfOrange on 2015. 7. 25..
 */
public class FavoriteFramgent extends Fragment {
    public static FavoriteFramgent newInstance(int num) {
        FavoriteFramgent f = new FavoriteFramgent();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_explore, container, false);

        return rootView;
    }
}

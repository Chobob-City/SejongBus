package kr.nogcha.sejongbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by FullOfOrange on 2015. 10. 29..
 */
public class Main_FavoriteFragment extends Fragment {
    public static Main_FavoriteFragment newInstance(int num) {
        Main_FavoriteFragment f = new Main_FavoriteFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return null;
    }
}

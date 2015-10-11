package kr.nogcha.sejongbus;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by FullOfOrange on 2015. 10. 7..
 */
public class BusRouteFragment extends Fragment{
    public static BusRouteFragment newInstance(int num) {
        BusRouteFragment f = new BusRouteFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }
}

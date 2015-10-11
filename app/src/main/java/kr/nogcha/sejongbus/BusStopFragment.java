package kr.nogcha.sejongbus;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by FullOfOrange on 2015. 10. 7..
 */
public class BusStopFragment extends Fragment{
        public static BusStopFragment newInstance(int num) {
            BusStopFragment f = new BusStopFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);
            return f;
        }
}

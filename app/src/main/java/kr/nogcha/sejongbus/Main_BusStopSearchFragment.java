package kr.nogcha.sejongbus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by FullOfOrange on 2015. 10. 29..
 */
public class Main_BusStopSearchFragment extends Fragment {
    private List<CommonListItem> mList = new ArrayList<>();
    private SejongBisClient mBisClient;
    private CommonAdapter mAdapter;
    private ListView mListView;
    private JSONArray mJSONArray;

    public static Main_BusStopSearchFragment newInstance(int num) {
        Main_BusStopSearchFragment f = new Main_BusStopSearchFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        mBisClient = new SejongBisClient(activity);
        mAdapter = new CommonAdapter(activity, R.layout.common_list_item, mList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_search, container, false);

        Bundle busString = getArguments();
        if(busString.getBoolean("edittextBoolean")==true) {
            search(busString.getString("busStop"));
        }
        mListView = (ListView) rootView.findViewById(R.id.list_view);
        mListView.setEmptyView(rootView.findViewById(R.id.text_view));
        mListView.setAdapter(mAdapter);

        return rootView;
    }

    private void search(String busStop) {
        String query = busStop;
        if (!query.equals("")) {
            MainActivity.hideSoftInput();
            if (mBisClient.isNetworkConnected()) {
                searchBusStop(query);
            }
        }
    }
    private void searchBusStop(String busStop) {
        try {
            mList.clear();
            mJSONArray = mBisClient.searchBusStop(busStop, true).getJSONArray("busStopList");
            for (int i = 0; i < mJSONArray.length(); i++) {
                CommonListItem item = new CommonListItem();
                JSONObject json = mJSONArray.getJSONObject(i);
                item.resId = R.drawable.busstopicon;
                item.text1 = json.getString("stop_name");
                item.text2 = json.getString("service_id");
                mList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BusStopRouteActivity.class);
                Bundle extras = new Bundle();
                try {
                    extras.putInt("stop_id", mJSONArray.getJSONObject(position).getInt("stop_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }
}

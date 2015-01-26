package kr.nogcha.sejongbus;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MainFragment extends Fragment {
    private EditText editText;
    private TextView textView;
    private ListView listView;
    private ArrayList<Spanned> list;
    private ArrayAdapter<Spanned> adapter;
    private JSONArray busRouteList;
    private JSONArray busStopList;

    private Animation transUp;

    RelativeLayout MainSearchBar;


    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_main, container, false);

        //MainSearchBar = (RelativeLayout)rootView.findViewById(R.id.mainsearchbar);

        editText = (EditText) rootView.findViewById(R.id.editText);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    editText.setText("");
                    return true;
                }
                return false;
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //transUp = AnimationUtils.loadAnimation(getActivity(),R.anim.view_transup);
                    //MainSearchBar.startAnimation(transUp);
                    String query = editText.getText().toString();
                    if (!query.equals("")) onSearch(query);
                    return true;
                }
                return false;
            }
        });

        textView = (TextView) rootView.findViewById(R.id.textView);
        textView.setVisibility(View.INVISIBLE);
        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setEmptyView(textView);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editText.getText().toString();
                if (!query.equals("")) onSearch(query);
            }
        });

        return rootView;
    }

    private void onSearch(String query) {
        MainActivity.toggleSoftInput();

        if (Pattern.matches("^\\d{5}$", query)) {
            Fragment busStopFragment = null;
            try {
                busStopList = SejongBis.searchBusStop(query).getJSONArray("busStopList");
                if (busStopList.length() == 0) return;

                busStopFragment = new BusStopFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("busStopId", busStopList.getJSONObject(0).getInt("stop_id"));
                busStopFragment.setArguments(bundle);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, busStopFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (Pattern.matches("^[0-9-]+$", query)) {
            searchBusRoute(query);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Fragment busRouteFragment = new BusRouteFragment();
                    Bundle bundle = new Bundle();
                    try {
                        bundle.putInt("busRouteId", busRouteList.getJSONObject(position).getInt("route_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    busRouteFragment.setArguments(bundle);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, busRouteFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        } else {
            searchBusStop(query);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Fragment busStopFragment = new BusStopFragment();
                    Bundle bundle = new Bundle();
                    try {
                        bundle.putInt("busStopId", busStopList.getJSONObject(position)
                                .getInt("stop_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    busStopFragment.setArguments(bundle);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, busStopFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
    }

    private void searchBusRoute(String busRoute) {
        try {
            busRouteList = SejongBis.searchBusRoute(busRoute).getJSONArray("busRouteList");

            list.clear();
            for (int i = 0; i < busRouteList.length(); i++) {
                JSONObject object = busRouteList.getJSONObject(i);
                Spanned route = (Spanned) TextUtils.concat(
                        SejongBis.getRouteType(object.getInt("route_type")),
                        new SpannableString(" " + object.getString("route_name") + "\n" +
                                object.getString("st_stop_name") + "~" +
                                object.getString("ed_stop_name")));
                list.add(route);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    private void searchBusStop(String busStop) {
        try {
            busStopList = SejongBis.searchBusStop(busStop).getJSONArray("busStopList");

            list.clear();
            for (int i = 0; i < busStopList.length(); i++) {
                JSONObject object = busStopList.getJSONObject(i);
                list.add(new SpannableString(object.getString("stop_name") + "\n[" +
                        object.getString("service_id") + "]"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
}

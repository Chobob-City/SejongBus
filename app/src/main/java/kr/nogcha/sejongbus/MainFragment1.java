package kr.nogcha.sejongbus;

import android.app.Fragment;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MainFragment1 extends Fragment {
    private ArrayList<Spanned> list;
    private ArrayAdapter<Spanned> adapter;
    private EditText editText;
    private ListView listView;
    private JSONArray jsonArray;
//    private Animation transUp;
//    private RelativeLayout MainSearchBar;

    public MainFragment1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_main_1, container, false);

//        MainSearchBar = (RelativeLayout)rootView.findViewById(R.id.mainsearchbar);

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
//                    transUp = AnimationUtils.loadAnimation(getActivity(),R.anim.view_transup);
//                    MainSearchBar.startAnimation(transUp);

                    String query = editText.getText().toString();
                    if (!query.equals("")) {
                        MainActivity.toggleSoftInput();

                        onSearch(query);
                    }

                    return true;
                }
                return false;
            }
        });

        listView = (ListView) rootView.findViewById(R.id.listView);
        TextView textView = (TextView) rootView.findViewById(R.id.textView);
        listView.setEmptyView(textView);
        listView.setAdapter(adapter);

        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editText.getText().toString();
                if (!query.equals("")) {
                    MainActivity.toggleSoftInput();

                    onSearch(query);
                }
            }
        });

        return rootView;
    }

    private void onSearch(String query) {
        if (Pattern.matches("^\\d{5}$", query)) {
            try {
                jsonArray = SejongBis.searchBusStop(query).getJSONArray("busStopList");
                if (jsonArray.length() == 0) return;

                MainActivity.startHostActivity(HostActivity.BUS_STOP,
                        jsonArray.getJSONObject(0).getInt("stop_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (Pattern.matches("^[0-9-]+$", query)) {
            searchBusRoute(query);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        MainActivity.startHostActivity(HostActivity.BUS_ROUTE,
                                jsonArray.getJSONObject(position).getInt("route_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            searchBusStop(query);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        MainActivity.startHostActivity(HostActivity.BUS_STOP,
                                jsonArray.getJSONObject(position).getInt("stop_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void searchBusRoute(String busRoute) {
        try {
            jsonArray = SejongBis.searchBusRoute(busRoute).getJSONArray("busRouteList");
            list.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Spanned route = (Spanned) TextUtils.concat(
                        HostActivity.getRouteType(json.getInt("route_type")),
                        new SpannableString(" " + json.getString("route_name") + "\n" +
                                json.getString("st_stop_name") + "~" +
                                json.getString("ed_stop_name")));
                list.add(route);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    private void searchBusStop(String busStop) {
        try {
            jsonArray = SejongBis.searchBusStop(busStop).getJSONArray("busStopList");
            list.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                list.add(new SpannableString(json.getString("stop_name") + "\n(" +
                        json.getString("service_id") + ")"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
}

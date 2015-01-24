package kr.nogcha.sejongbus;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Html;
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

public class MainFragment extends Fragment {
    private EditText editText;
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private JSONArray busRouteList;
    private JSONArray busStopList;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        editText = (EditText) rootView.findViewById(R.id.editText);
        editText.setText(Html.fromHtml("<span style=\"color: gray\">TODO</span>"));
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
                    onSearch();
                    return true;
                }
                return false;
            }
        });

        listView = (ListView) rootView.findViewById(R.id.listView);
        TextView textView = (TextView) rootView.findViewById(R.id.textView);
        listView.setEmptyView(textView);
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(MainActivity.context, android.R.layout.simple_list_item_1,
                arrayList);
        listView.setAdapter(arrayAdapter);

        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearch();
            }
        });

        return rootView;
    }

    private void onSearch() {
        String query = editText.getText().toString();
        if (query.equals("")) {
            return;
        }

        MainActivity.toggleSoftInput();

        try {
            if (Pattern.matches("^\\d{5}$", query)) {
                searchBusStop(query);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
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
            } else if (Pattern.matches("^[0-9-]+$", query)) {
                searchBusRoute(query);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Fragment busRouteFragment = new BusRouteFragment();
                        Bundle bundle = new Bundle();
                        try {
                            bundle.putInt("busRouteId", busRouteList.getJSONObject(position)
                                    .getInt("route_id"));
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
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void searchBusRoute(String busRoute) throws JSONException {
        busRouteList = SejongBis.searchBusRoute(busRoute).getJSONArray("busRouteList");

        arrayList.clear();
        for (int i = 0; i < busRouteList.length(); i++) {
            JSONObject jsonObject = busRouteList.getJSONObject(i);
            String route;

            int route_type = jsonObject.getInt("route_type");
            switch (route_type) {
                case 43:
                    route = "[세종광역]";
                    break;
                case 50:
                    route = "[대전광역]";
                    break;
                case 51:
                    route = "[청주광역]";
                    break;
                case 30:
                    route = "[마을]";
                    break;
                default:
                    route = "[일반]";
            }

            route += " " + jsonObject.getString("route_name") + "(" +
                    jsonObject.getString("st_stop_name") + "~" +
                    jsonObject.getString("ed_stop_name") + ")";
            arrayList.add(route);
        }
        arrayAdapter.notifyDataSetChanged();
    }

    private void searchBusStop(String busStop) throws JSONException {
        busStopList = SejongBis.searchBusStop(busStop).getJSONArray("busStopList");

        arrayList.clear();
        for (int i = 0; i < busStopList.length(); i++) {
            JSONObject object = busStopList.getJSONObject(i);
            arrayList.add(object.getString("stop_name") + " [" +
                    object.getString("service_id") + "]");
        }
        arrayAdapter.notifyDataSetChanged();
    }
}

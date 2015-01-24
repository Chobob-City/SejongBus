package kr.nogcha.sejongbus;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusStopFragment extends Fragment {
    JSONArray busStopRouteList;

    public BusStopFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bus_stop, container, false);

        if (MainActivity.isNetworkConnected()) {
            ArrayList<String> list = new ArrayList<>();
            try {
                busStopRouteList = SejongBis
                        .searchBusStopRoute(getArguments().getInt("busStopId"))
                        .getJSONArray("busStopRouteList");

                TextView textView = (TextView) rootView.findViewById(R.id.textView);
                JSONObject jsonObject = busStopRouteList.getJSONObject(0);
                textView.setText(jsonObject.getString("stop_name") + "\n[" +
                        jsonObject.getString("service_id") + "]");

                for (int i = 0; i < busStopRouteList.length(); i++) {
                    jsonObject = busStopRouteList.getJSONObject(i);
                    String route = SejongBis.getRouteTypeString(jsonObject.getInt("route_type")) +
                            jsonObject.getString("route_name") + "\n";

                    int provide_code = jsonObject.getInt("provide_code");
                    switch (provide_code) {
                        case 1:
                            route += "예정: " + jsonObject.getString("provide_type") +
                                    "\n현재 위치: 기점";
                            break;
                        case 2:
                            route += "회차지 대기 중";
                            break;
                        default:
                            route += "예정: " + jsonObject.getString("provide_type") +
                                    "\n현재 위치: " + jsonObject.getString("rstop");
                    }
                    list.add(route);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ListView listView = (ListView) rootView.findViewById(R.id.listView);
            listView.setAdapter(new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, list));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Fragment busRouteFragment = new BusRouteFragment();
                    Bundle bundle = new Bundle();
                    try {
                        bundle.putInt("busRouteId", busStopRouteList.getJSONObject(position)
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
        }

        return rootView;
    }
}

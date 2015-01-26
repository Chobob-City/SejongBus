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

public class BusRouteFragment extends Fragment {

    private JSONArray busRouteDetailList;

    public BusRouteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_bus_route, container, false);

        if (MainActivity.isNetworkConnected()) {
            ArrayList<String> list = new ArrayList<>();
            try {
                busRouteDetailList = SejongBis
                        .searchBusRouteDetail(getArguments().getInt("busRouteId"))
                        .getJSONArray("busRouteDetailList");

                TextView textView = (TextView) rootView.findViewById(R.id.textView);
                JSONObject object = busRouteDetailList.getJSONObject(
                        busRouteDetailList.length() - 1);
                textView.setText(object.getString("route_name") + "\n" +
                        object.getString("st_stop_name") + "~" +
                        object.getString("ed_stop_name") + "\n" +
                        object.getString("alloc_time"));

                for (int i = 0; i < busRouteDetailList.length() - 1; i++) {
                    object = busRouteDetailList.getJSONObject(i);
                    list.add(object.getString("stop_name") + "\n[" +
                            object.getString("service_id") + "]");
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
                    Fragment busStopFragment = new BusStopFragment();
                    Bundle bundle = new Bundle();
                    try {
                        bundle.putInt("busStopId", busRouteDetailList.getJSONObject(position)
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

        return rootView;
    }
}

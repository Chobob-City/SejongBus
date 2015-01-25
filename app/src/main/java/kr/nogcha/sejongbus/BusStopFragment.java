package kr.nogcha.sejongbus;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
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
            ArrayList<Spanned> list = new ArrayList<>();
            try {
                busStopRouteList = SejongBis
                        .searchBusStopRoute(getArguments().getInt("busStopId"))
                        .getJSONArray("busStopRouteList");

                TextView textView = (TextView) rootView.findViewById(R.id.textView);
                JSONObject object = busStopRouteList.getJSONObject(0);
                textView.setText(object.getString("stop_name") + "\n[" +
                        object.getString("service_id") + "]");

                for (int i = 0; i < busStopRouteList.length(); i++) {
                    object = busStopRouteList.getJSONObject(i);
                    Spanned route = (Spanned) TextUtils.concat(
                            SejongBis.getRouteType(object.getInt("route_type")),
                            new SpannableString(" " + object.getString("route_name") + "\n"));

                    int provide_code = object.getInt("provide_code");
                    switch (provide_code) {
                        case 1:
                            route = (Spanned) TextUtils.concat(route, new SpannableString("예정: " +
                                    object.getString("provide_type") +
                                    "\n현재 위치: 기점"));
                            break;
                        case 2:
                            route = (Spanned) TextUtils.concat(route,
                                    new SpannableString("회차지 대기 중"));
                            break;
                        default:
                            route = (Spanned) TextUtils.concat(route, new SpannableString("예정: " +
                                    object.getString("provide_type") +
                                    "\n현재 위치: " + object.getString("rstop")));
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

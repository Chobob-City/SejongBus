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
    private SejongBisClient bisClient;
    private JSONArray jsonArray;

    public BusStopFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bisClient = new SejongBisClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_bus_stop, container, false);

        ArrayList<Spanned> list = new ArrayList<>();
        try {
            jsonArray = bisClient.searchBusStopRoute(getArguments().getInt("busStopId"), true)
                    .getJSONArray("busStopRouteList");

            TextView textView1 = (TextView) rootView.findViewById(R.id.textView1);
            JSONObject json = jsonArray.getJSONObject(0);
            textView1.setText(json.getString("stop_name") + "(" + json.getString("service_id") +
                    ")");

            for (int i = 0; i < jsonArray.length(); i++) {
                json = jsonArray.getJSONObject(i);

                Spanned route = (Spanned) TextUtils.concat(
                        HostActivity.getRouteType(json.getInt("route_type")),
                        new SpannableString(" " + json.getString("route_name") + "\n"));

                int provide_code = json.getInt("provide_code");
                switch (provide_code) {
                    case 1:
                        route = (Spanned) TextUtils.concat(route, new SpannableString("도착: " +
                                json.getString("provide_type") + "\n현위치: 기점"));
                        break;
                    case 2:
                        route = (Spanned) TextUtils.concat(route,
                                new SpannableString("회차지 대기 중"));
                        break;
                    default:
                        route = (Spanned) TextUtils.concat(route, new SpannableString("도착: " +
                                json.getString("provide_type") + "\n현위치: " +
                                json.getString("rstop")));
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
                    bundle.putInt("busRouteId", jsonArray.getJSONObject(position)
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

        return rootView;
    }
}

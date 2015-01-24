package kr.nogcha.sejongbus;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends ActionBarActivity {
    private static Context context;

    public static boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            Fragment busRouteFragment = new BusRouteFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("busRouteId", 293000001);
            busRouteFragment.setArguments(bundle);

            getFragmentManager().beginTransaction().add(R.id.container, busRouteFragment)
                    .commit();
        }

        context = getBaseContext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public static class BusRouteFragment extends Fragment {
        private JSONArray busRouteDetailList;

        public BusRouteFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_bus_route, container, false);

            if (isNetworkConnected()) {
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

    public static class BusStopFragment extends Fragment {
        JSONArray busStopRouteList;

        public BusStopFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_bus_stop, container, false);

            if (isNetworkConnected()) {
                ArrayList<String> list = new ArrayList<>();
                try {
                    busStopRouteList = SejongBis
                            .searchBusStopRoute(getArguments().getInt("busStopId"))
                            .getJSONArray("busStopRouteList");

                    TextView textView = (TextView) rootView.findViewById(R.id.textView);
                    JSONObject object = busStopRouteList.getJSONObject(0);
                    textView.setText(object.getString("stop_name") + " [" +
                            object.getString("service_id") + "]");

                    for (int i = 0; i < busStopRouteList.length(); i++) {
                        object = busStopRouteList.getJSONObject(i);
                        String route;

                        int route_type = object.getInt("route_type");
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

                        route += " " + object.getString("route_name") + "\n";
                        int provide_code = object.getInt("provide_code");
                        switch (provide_code) {
                            case 1:
                                route += object.getString("provide_type") + "\n현재 위치: 기점";
                                break;
                            case 2:
                                route += "회차지 대기 중";
                                break;
                            default:
                                route += object.getString("provide_type") + "\n현재 위치: " +
                                        object.getString("rstop");
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
}

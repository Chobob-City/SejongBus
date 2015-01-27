package kr.nogcha.sejongbus;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainFragment2 extends Fragment
        implements View.OnTouchListener, TextView.OnEditorActionListener {
    private int stBusStop = 0;
    private int edBusStop = 0;
    private ArrayList<Spanned> list;
    private ArrayAdapter<Spanned> adapter;
    private EditText editText1;
    private EditText editText2;
    private ListView listView;
    private JSONArray busStopList;

    public MainFragment2() {
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
        View rootView = inflater.inflate(R.layout.f_main_2, container, false);

        editText1 = (EditText) rootView.findViewById(R.id.editText1);
        editText1.setOnTouchListener(this);
        editText1.setOnEditorActionListener(this);

        editText2 = (EditText) rootView.findViewById(R.id.editText2);
        editText2.setOnTouchListener(this);
        editText2.setOnEditorActionListener(this);

        listView = (ListView) rootView.findViewById(R.id.listView);
        TextView textView = (TextView) rootView.findViewById(R.id.textView);
        textView.setVisibility(View.INVISIBLE);
        listView.setEmptyView(textView);
        listView.setAdapter(adapter);

        ImageButton imageButton1 = (ImageButton) rootView.findViewById(R.id.imageButton1);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editText1.getText().toString();
                if (!query.equals("")) onSearch1(query);
            }
        });

        ImageButton imageButton2 = (ImageButton) rootView.findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editText2.getText().toString();
                if (!query.equals("")) onSearch2(query);
            }
        });

        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stBusStop == 0) {
                    Toast.makeText(getActivity(), "출발할 정류장을 검색하세요.", Toast.LENGTH_SHORT)
                            .show();
                } else if (edBusStop == 0) {
                    Toast.makeText(getActivity(), "도착할 정류장을 검색하세요.", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Intent intent = new Intent(getActivity(), BisHostActivity.class);
                    intent.putExtra("arg0", 2);
                    intent.putExtra("arg1", stBusStop);
                    intent.putExtra("arg2", edBusStop);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int id = v.getId();
            if (id == R.id.editText1) {
                list.clear();
                adapter.notifyDataSetChanged();

                stBusStop = 0;
                editText1.setHint("");
                editText1.setText("");

                return true;
            } else if (id == R.id.editText2) {
                list.clear();
                adapter.notifyDataSetChanged();

                edBusStop = 0;
                editText2.setHint("");
                editText2.setText("");

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            int id = v.getId();
            if (id == R.id.editText1) {
                String query = editText1.getText().toString();
                if (!query.equals("")) onSearch1(query);
                return true;
            } else if (id == R.id.editText2) {
                String query = editText2.getText().toString();
                if (!query.equals("")) onSearch2(query);
                return true;
            }
        }
        return false;
    }

    private void onSearch1(String query) {
        MainActivity.toggleSoftInput();

        searchBusStop(query);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject json = busStopList.getJSONObject(position);
                    stBusStop = json.getInt("stop_id");
                    editText1.setHint(json.getString("stop_name") + " [" +
                            json.getString("service_id") + "]");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editText1.setText("");
            }
        });
    }

    private void onSearch2(String query) {
        MainActivity.toggleSoftInput();

        searchBusStop(query);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject json = busStopList.getJSONObject(position);
                    stBusStop = json.getInt("stop_id");
                    editText2.setHint(json.getString("stop_name") + " [" +
                            json.getString("service_id") + "]");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editText2.setText("");
            }
        });
    }

    private void searchBusStop(String busStop) {
        try {
            busStopList = SejongBis.searchBusStop(busStop).getJSONArray("busStopList");

            list.clear();
            for (int i = 0; i < busStopList.length(); i++) {
                JSONObject json = busStopList.getJSONObject(i);
                list.add(new SpannableString(json.getString("stop_name") + "\n[" +
                        json.getString("service_id") + "]"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
}

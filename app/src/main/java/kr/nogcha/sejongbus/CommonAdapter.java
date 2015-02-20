/*
 * Copyright (C) 2015 Chobob City
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.nogcha.sejongbus;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class CommonAdapter extends ArrayAdapter<CommonListItem> {
    private LayoutInflater mInflater;
    private int mResource;

    public CommonAdapter(Context context, int resource, List<CommonListItem> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(mResource, null);
            viewHolder = new CommonViewHolder();
            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.text_view_1);
            viewHolder.textView2 = (TextView) convertView.findViewById(R.id.text_view_2);
            viewHolder.textView3 = (TextView) convertView.findViewById(R.id.text_view_3);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            //viewHolder.button1 = (Button) convertView.findViewById(R.id.add_favorite);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommonViewHolder) convertView.getTag();
        }



        /*viewHolder.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        CommonListItem item = getItem(position);
        viewHolder.textView1.setText(item.text1);
        viewHolder.textView2.setText(item.text2);
        viewHolder.textView3.setText(item.text3);
        int resId;
        switch (item.busType) {
            case 1:
            case 2:
                resId = R.drawable.ic_action_search;
                break;
            default:
                resId = 0;
        }
        viewHolder.imageView.setImageResource(resId);

        return convertView;
    }



    public class CommonViewHolder {
        public ImageView view;
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        //public Button button1;
    }
}

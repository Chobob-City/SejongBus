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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CommonArrayAdapter extends ArrayAdapter<CommonListItem> {
    private LayoutInflater mInflater;
    private int mResource;

    public CommonArrayAdapter(Context context, int resource, List<CommonListItem> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        CommonViewHolder viewHolder;
        if (convertView == null) {
            view = mInflater.inflate(mResource, null);
            viewHolder = new CommonViewHolder();
            viewHolder.textView1 = (TextView) view.findViewById(R.id.text_view_1);
            viewHolder.textView2 = (TextView) view.findViewById(R.id.text_view_2);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (CommonViewHolder) view.getTag();
        }

        CommonListItem item = getItem(position);
        viewHolder.textView1.setText(item.text1);
        viewHolder.textView2.setText(item.text2);

        return view;
    }
}

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
import android.widget.ImageView;
import android.widget.TextView;

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
            viewHolder.imageView1 = (ImageView) convertView.findViewById(R.id.image_view_1);
            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.text_view_1);
            viewHolder.textView2 = (TextView) convertView.findViewById(R.id.text_view_2);
            viewHolder.imageView2 = (ImageView) convertView.findViewById(R.id.image_view_2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommonViewHolder) convertView.getTag();
        }

        CommonListItem item = getItem(position);
        viewHolder.imageView1.setImageResource(item.resId);
        viewHolder.textView1.setText(item.text1);
        viewHolder.textView2.setText(item.text2);
        if (item.isBusHere) {
            viewHolder.imageView2.setImageResource(R.drawable.bus_beside);
        } else {
            viewHolder.imageView2.setImageDrawable(null);
        }

        return convertView;
    }
}

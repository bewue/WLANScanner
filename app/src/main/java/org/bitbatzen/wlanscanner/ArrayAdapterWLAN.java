/*
 *  Copyright (C) 2014 Benjamin W. (bitbatzen@gmail.com)
 *
 *  This file is part of WLANScanner.
 *
 *  WLANScanner is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  WLANScanner is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with WLANScanner.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.bitbatzen.wlanscanner;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


class ArrayAdapterWLAN extends BaseAdapter {

    Context context;
    
    ArrayList<ScanResult> data;
    
    private static LayoutInflater inflater = null;

    
    public ArrayAdapterWLAN(Context context, ArrayList<ScanResult> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.row_item_wlan, null);
        }
        
        ScanResult itemData = data.get(position);

        // ssid
        TextView ssidItem = (TextView) view.findViewById(R.id.rowItemSSID);
        ssidItem.setText(itemData.SSID);

        // frequency band
        TextView bandItem = (TextView) view.findViewById(R.id.rowItemFrequencyBand);
        Util.FrequencyBand fBand = Util.getFrequencyBand(itemData.frequency);
        String text = "";
        if (fBand == Util.FrequencyBand.TWO_FOUR_GHZ) {
            text = "2.4 GHz";
        }
        else if (fBand == Util.FrequencyBand.FIVE_GHZ) {
            text = "5 GHz";
        }
        bandItem.setText(text);

        // wlan standard
        TextView wlanStandard = (TextView) view.findViewById(R.id.rowItemWLANStandard);
        String sWlanStandard = Util.getWLANStandard(itemData);
        if (sWlanStandard == "") {
            wlanStandard.setVisibility(View.INVISIBLE);
        }
        else {
            wlanStandard.setText(sWlanStandard);
        }

        // capabilities
        TextView capabilitiesItem = (TextView) view.findViewById(R.id.rowItemCapabilities);
        capabilitiesItem.setText(Util.getCapabilitiesString(itemData.capabilities));

        // bssid (mac)
        TextView bssidItem = (TextView) view.findViewById(R.id.rowItemBSSID);
        bssidItem.setText(itemData.BSSID);

        // level
        TextView levelItem = (TextView) view.findViewById(R.id.rowItemLevel);
        levelItem.setText(Integer.toString(itemData.level) + " dBm");
        if (itemData.level >= -65) {
            levelItem.setBackgroundColor(Color.rgb(41, 163, 41));
        }
        else if (itemData.level >= -85) {
            levelItem.setBackgroundColor(Color.rgb(204, 204, 0));
        }
        else {
            levelItem.setBackgroundColor(Color.rgb(230, 46, 0));
        }

        // channel
        TextView channelItem = (TextView) view.findViewById(R.id.rowItemChannel);
        int channel = Util.getChannelFromFrequency(itemData.frequency);
        channelItem.setText(Integer.toString(channel));
        float cOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, parent.getResources().getDisplayMetrics());
        channelItem.setX(parent.getWidth() - cOffset);

        // channel width
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            TextView channelWidthItem = (TextView) view.findViewById(R.id.rowItemChannelWidth);
            channelWidthItem.setText(Util.getChannelWidth(itemData) + " MHz");
            float cwOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 135, parent.getResources().getDisplayMetrics());
            channelWidthItem.setX(parent.getWidth() - cwOffset);
        }

        return view;
    }
}
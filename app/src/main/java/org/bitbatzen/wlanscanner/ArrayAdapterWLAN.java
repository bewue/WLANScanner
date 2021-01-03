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
import android.util.Log;
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

        // capabilities
        TextView capabilitiesItem = (TextView) view.findViewById(R.id.rowItemCapabilities);
        capabilitiesItem.setText(Util.getCapabilitiesString(itemData.capabilities));

        // level
        TextView levelItem = (TextView) view.findViewById(R.id.rowItemLevel);
        levelItem.setText(Integer.toString(itemData.level) + " dBm");
        if (itemData.level >= -65) {
            levelItem.setBackgroundResource(R.drawable.list_item_level_bg_green);
        }
        else if (itemData.level >= -85) {
            levelItem.setBackgroundResource(R.drawable.list_item_level_bg_yellow);
        }
        else {
            levelItem.setBackgroundResource(R.drawable.list_item_level_bg_red);
        }

        // frequency band
        TextView bandItem = (TextView) view.findViewById(R.id.rowItemFrequencyBand);
        Util.FrequencyBand fBand = Util.getFrequencyBand(itemData);
        String text = "";
        if (fBand == Util.FrequencyBand.TWO_FOUR_GHZ) {
            text = "2.4 GHz";
        }
        else if (fBand == Util.FrequencyBand.FIVE_GHZ) {
            text = "5 GHz";
        }
        else if (fBand == Util.FrequencyBand.SIX_GHZ) {
            text = "6 GHz";
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
        wlanStandard.setX(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, parent.getResources().getDisplayMetrics()));

        // channel width
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            TextView channelWidthItem = (TextView) view.findViewById(R.id.rowItemChannelWidth);
            channelWidthItem.setText(Util.getChannelWidth(itemData) + " MHz");
            channelWidthItem.setX(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, parent.getResources().getDisplayMetrics()));
        }

        // channel
        String channel = "";
        int[] frequencies = Util.getFrequencies(itemData);
        if (frequencies.length == 1) {
            channel = String.valueOf(Util.getChannel(frequencies[0]));
        }
        else if (frequencies.length == 2) {
            channel = Util.getChannel(frequencies[0]) + "+" + Util.getChannel(frequencies[1]);
        }
        TextView channelItem = (TextView) view.findViewById(R.id.rowItemChannel);
        channelItem.setText(channel);
        channelItem.setX(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 190, parent.getResources().getDisplayMetrics()));

        // bssid (mac)
        TextView bssidItem = (TextView) view.findViewById(R.id.rowItemBSSID);
        bssidItem.setText(itemData.BSSID);

        return view;
    }
}
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

import java.util.ArrayList;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


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
        TextView tvText;
        
        tvText = (TextView) view.findViewById(R.id.rowItemSSID);
        tvText.setText(itemData.SSID);
        tvText = (TextView) view.findViewById(R.id.rowItemCapabilities);
        tvText.setText(Util.getCapabilitiesShortString(itemData.capabilities));
        tvText = (TextView) view.findViewById(R.id.rowItemBSSID);
        tvText.setText(itemData.BSSID);
        
        tvText = (TextView) view.findViewById(R.id.rowItemLevel);
        tvText.setText(Integer.toString(itemData.level) + " dBm");
        
        tvText = (TextView) view.findViewById(R.id.rowItemChannel);
        int channel = Util.getChannelFromFrequency(itemData.frequency);
        tvText.setText("CH " + Integer.toString(channel));
        float paddingRight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, parent.getResources().getDisplayMetrics());
        float posXTVChannel = parent.getWidth() - paddingRight;
        tvText.setX(posXTVChannel);
        
        tvText = (TextView) view.findViewById(R.id.rowItemFrequencyBand);
        Util.FrequencyBand fBand = Util.getFrequencyBand(itemData.frequency);
        if (fBand == Util.FrequencyBand.TWO_FOUR_GHZ) {
        	tvText.setText("2.4GHz");
        }
        else if (fBand == Util.FrequencyBand.FIVE_GHZ) {
        	tvText.setText("5GHz");
        }
        else {
        	tvText.setText("");
        }
        paddingRight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, parent.getResources().getDisplayMetrics());
        tvText.setX(posXTVChannel - paddingRight);
        
        return view;
    }
}
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
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.os.SystemClock;
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
        this.context    = context;
        this.data       = data;
        inflater        = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        MainActivity mainActivity       = (MainActivity) context;
        SharedPreferences sharedPrefs   = mainActivity.getPreferences(Context.MODE_PRIVATE);
        
        ScanResult sr = data.get(position);

        // ssid
        TextView ssidItem = (TextView) view.findViewById(R.id.rowItemSSID);
        ssidItem.setText(sr.SSID);

        // last seen (will be updated via FragmentWLANList.startUpdateListView())
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            TextView tvLastSeen = (TextView) view.findViewById(R.id.rowItemLastSeen);
            long age            = ((SystemClock.elapsedRealtime() * 1000) - sr.timestamp) / 1000000;
            float scan_delay    = sharedPrefs.getFloat(Util.PREF_SETTING_SCAN_DELAY, Util.getDefaultScanDelay()) / 1000;
            if (age >= scan_delay + 10) {
                tvLastSeen.setText("Last seen: " + age + "s");
            } else {
                tvLastSeen.setText("");
            }
        }
        
        // capabilities
        TextView capabilitiesItem = (TextView) view.findViewById(R.id.rowItemCapabilities);
        capabilitiesItem.setText(Util.getCapabilitiesString(sr.capabilities));

        // level
        TextView levelItem = (TextView) view.findViewById(R.id.rowItemLevel);
        levelItem.setText(Integer.toString(sr.level) + " dBm");
        if (sr.level >= -65) {
            levelItem.setBackgroundResource(R.drawable.list_item_level_bg_green);
        }
        else if (sr.level >= -85) {
            levelItem.setBackgroundResource(R.drawable.list_item_level_bg_yellow);
        }
        else {
            levelItem.setBackgroundResource(R.drawable.list_item_level_bg_red);
        }

        // channel width
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            TextView channelWidthItem = (TextView) view.findViewById(R.id.rowItemChannelWidth);
            channelWidthItem.setText(Util.getChannelWidth(sr) + " MHz");

            channelWidthItem.setX(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, (int) parent.getWidth() * 0.4f, parent.getResources().getDisplayMetrics()));
        }

        // channel
        String channel              = "";
        String channelFrequencies   = "";
        int[] frequencies           = Util.getFrequencies(sr);
        int channel1                = Util.getChannel(frequencies[0]);
        if (frequencies.length == 1) {
            channel             = (channel1 == -1) ? "?" : String.valueOf(channel1);
            channelFrequencies  = String.valueOf(frequencies[0]);
        }
        else if (frequencies.length == 2) {
            channel             = (channel1 == -1) ? "?+?" : channel1 + "+" + Util.getChannel(frequencies[1]);
            channelFrequencies  = frequencies[0] + "+" + frequencies[1];
        }
        TextView channelItem = (TextView) view.findViewById(R.id.rowItemChannel);
        channelItem.setText(channel);
        channelItem.setX(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, (int) parent.getWidth() * 0.58f, parent.getResources().getDisplayMetrics()));

        // frequency band (+ channel frequency(s), + wlan standard)
        TextView bandItem           = (TextView) view.findViewById(R.id.rowItemFrequencyBand);
        Util.FrequencyBand fBand    = Util.getFrequencyBand(sr);
        String text = "";
        if (fBand == Util.FrequencyBand.TWO_FOUR_GHZ) {
            text = "2.4 GHz";
        } else if (fBand == Util.FrequencyBand.FIVE_GHZ) {
            text = "5 GHz";
        } else if (fBand == Util.FrequencyBand.SIX_GHZ) {
            text = "6 GHz";
        } else if (fBand == Util.FrequencyBand.SIXTY_GHZ) {
            text = "60 GHz";
        } else {
            text = "?";
        }

        text += " [" + channelFrequencies + "]";
        String wlanStandard = Util.getWLANStandard(sr);
        if (wlanStandard != "") {
            text += " " + wlanStandard;
        }
        bandItem.setText(text);

        // bssid (mac)
        TextView bssidItem = (TextView) view.findViewById(R.id.rowItemBSSID);
        bssidItem.setText(sr.BSSID);

        return view;
    }
}
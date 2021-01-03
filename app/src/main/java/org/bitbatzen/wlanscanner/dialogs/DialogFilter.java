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

package org.bitbatzen.wlanscanner.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.bitbatzen.wlanscanner.R;
import org.bitbatzen.wlanscanner.Util;
import org.bitbatzen.wlanscanner.events.EventManager;
import org.bitbatzen.wlanscanner.events.Events;


public class DialogFilter
		extends Dialog
		implements View.OnClickListener {

	public Activity activity;
	public Button buttonOk;

	CheckBox cbFilterSSIDEnabled;
	EditText etFilterSSID;

	CheckBox cbFilterChannelEnabled;
	EditText etFilterChannel;

	CheckBox cbFilterCapabiliEnabled;
	EditText etFilterCapabili;

	TextView etFilterInfo;


	public DialogFilter(Activity activity) {
		super(activity);
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_filter);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		buttonOk = (Button) findViewById(R.id.button_dialog_filter_ok);
		buttonOk.setOnClickListener(this);

		SharedPreferences sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE);

		// filter ssid enabled
		boolean filterSSIDEnabled 		= sharedPrefs.getBoolean(Util.PREF_FILTER_SSID_ENABLED, false);
		cbFilterSSIDEnabled 			= (CheckBox) findViewById(R.id.cb_dialog_filter_ssid);
		cbFilterSSIDEnabled.setChecked(filterSSIDEnabled);

		// filter ssid
		String filterSSID 				= sharedPrefs.getString(Util.PREF_FILTER_SSID, "");
		etFilterSSID 					= (EditText) findViewById(R.id.et_dialog_filter_ssid);
		etFilterSSID.setText(filterSSID);

		// filter channel enabled
		boolean filterChannelEnabled 	= sharedPrefs.getBoolean(Util.PREF_FILTER_CHANNEL_ENABLED, false);
		cbFilterChannelEnabled 			= (CheckBox) findViewById(R.id.cb_dialog_filter_channel);
		cbFilterChannelEnabled.setChecked(filterChannelEnabled);

		// filter channel
		String filterChannel			= sharedPrefs.getString(Util.PREF_FILTER_CHANNEL, "");
		etFilterChannel 				= (EditText) findViewById(R.id.et_dialog_filter_channel);
		etFilterChannel.setText(filterChannel);

		// filter capabili enabled
		boolean filterCapabiliEnabled	= sharedPrefs.getBoolean(Util.PREF_FILTER_CAPABILI_ENABLED, false);
		cbFilterCapabiliEnabled			= (CheckBox) findViewById(R.id.cb_dialog_filter_capabili);
		cbFilterCapabiliEnabled.setChecked(filterCapabiliEnabled);

		// filter capabili
		String filterCapabili			= sharedPrefs.getString(Util.PREF_FILTER_CAPABILI, "");
		etFilterCapabili 				= (EditText) findViewById(R.id.et_dialog_filter_capabili);
		etFilterCapabili.setText(filterCapabili);

		etFilterInfo = (TextView) findViewById(R.id.tv_dialog_filter_info);
	}

	public void onClickOk() {
		SharedPreferences.Editor editor = activity.getPreferences(Context.MODE_PRIVATE).edit();

		boolean filterSSIDEnabled		= cbFilterSSIDEnabled.isChecked();
		String filterSSID 				= etFilterSSID.getText().toString();

		// validate ssid filter
		if (filterSSIDEnabled && filterSSID.equals("")) {
			etFilterInfo.setText("Invalid SSID filter!");
			return;
		}

		// save ssid filter
		editor.putBoolean(Util.PREF_FILTER_SSID_ENABLED, filterSSIDEnabled);
		editor.putString(Util.PREF_FILTER_SSID, filterSSID);

		boolean filterChannelEnabled	= cbFilterChannelEnabled.isChecked();
		String filterChannel			= etFilterChannel.getText().toString();

		// validate channel filter
		if (filterChannelEnabled) {
			int fChannel = -1;
			try {
				fChannel = Integer.parseInt(filterChannel);
			} catch (Exception e) {
				etFilterInfo.setText("Invalid channel filter!");
				return;
			}

			if (Util.getFrequency(Util.FrequencyBand.TWO_FOUR_GHZ, fChannel) == -1
					&& Util.getFrequency(Util.FrequencyBand.FIVE_GHZ, fChannel) == -1
					&& Util.getFrequency(Util.FrequencyBand.SIX_GHZ, fChannel) == -1) {
				etFilterInfo.setText("Invalid channel filter!");
				return;
			}
		}

		// save channel filter
		editor.putBoolean(Util.PREF_FILTER_CHANNEL_ENABLED, filterChannelEnabled);
		editor.putString(Util.PREF_FILTER_CHANNEL, filterChannel);

		boolean filterCapabiliEnabled	= cbFilterCapabiliEnabled.isChecked();
		String filterCapabili			= etFilterCapabili.getText().toString();

		// validate capabilities filter
		if (filterCapabiliEnabled && filterCapabili.equals("")) {
			etFilterInfo.setText("Invalid capabilities filter!");
			return;
		}

		// save capabilities filter
		editor.putBoolean(Util.PREF_FILTER_CAPABILI_ENABLED, filterCapabiliEnabled);
		editor.putString(Util.PREF_FILTER_CAPABILI, filterCapabili);

		editor.commit();

		EventManager.sharedInstance().sendEvent(Events.EventID.FILTER_CHANGED);

		dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_dialog_filter_ok:
			onClickOk();
			break;

		default:
			dismiss();
			break;
		}	
	}
}
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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.bitbatzen.wlanscanner.R;


public class DialogWLANListItemInfo 
		extends Dialog 
		implements android.view.View.OnClickListener {

	public Activity activity;
	public Button buttonClose;

	String SSID;
	String capabilities;
	String frequency;

	
	public DialogWLANListItemInfo(Activity activity, String ssid, String capabilities, String frequency) {
		super(activity);
		this.activity = activity;
		
		this.SSID = ssid;
		this.capabilities = capabilities;
		this.frequency = frequency;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_wlanlist_item_info);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		TextView tvHeader = (TextView) findViewById(R.id.tv_dialog_wlanlist_item_info_header);
		tvHeader.setText(SSID);
		
		TextView tvCapabilites = (TextView) findViewById(R.id.tv_dialog_wlanlist_item_info_capabilities);
		String formattedCaps = "";
		for (int i = 0; i < capabilities.length(); i++) {
			formattedCaps += capabilities.charAt(i);
			if (capabilities.charAt(i) == "]".toCharArray()[0] && i < capabilities.length() - 1) {
				formattedCaps += "\n";
			}
		}
		formattedCaps = formattedCaps.replace("[", "");
		formattedCaps = formattedCaps.replace("]", "");
		tvCapabilites.setText(formattedCaps);
		
		TextView tvFreqency = (TextView) findViewById(R.id.tv_dialog_wlanlist_item_info_frequency);
		tvFreqency.setText(frequency + " MHz");
		
		buttonClose = (Button) findViewById(R.id.button_dialog_close);
		buttonClose.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		dismiss();
	}
}
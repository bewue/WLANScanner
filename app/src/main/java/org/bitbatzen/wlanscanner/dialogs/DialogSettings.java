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
import android.widget.EditText;
import android.widget.TextView;

import org.bitbatzen.wlanscanner.R;
import org.bitbatzen.wlanscanner.Util;


public class DialogSettings
		extends Dialog
		implements View.OnClickListener {

	public Activity activity;
	public Button buttonOk;

	EditText etScanDelay;


	public DialogSettings(Activity activity) {
		super(activity);
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_settings);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		buttonOk = (Button) findViewById(R.id.button_dialog_settings_ok);
		buttonOk.setOnClickListener(this);

		SharedPreferences sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE);
		float scanDelay = sharedPrefs.getFloat(Util.PREF_SETTING_SCAN_DELAY, Util.getDefaultScanDelay());

		etScanDelay = (EditText) findViewById(R.id.et_dialog_settings_scan_delay);
		etScanDelay.setText(String.valueOf(scanDelay / 1000));

		if (android.os.Build.VERSION.SDK_INT >= 28) {
			TextView tvScanDelayInfo = (TextView) findViewById(R.id.tv_dialog_settings_scan_delay_info);
			tvScanDelayInfo.setText("Since android 9 each foreground app can only scan four times in a 2-minute period by default. Thus the default scan delay should be set to " + Util.getDefaultScanDelay() / 1000f + " seconds to get continuous scan results");
		}
	}

	public void onClickOk() {
		float scanDelay = 0;
		try {
			scanDelay = Float.parseFloat(etScanDelay.getText().toString()) * 1000;
		} catch (Exception e) {
			return;
		}

		// save scan delay
		SharedPreferences.Editor editor = activity.getPreferences(Context.MODE_PRIVATE).edit();
		editor.putFloat(Util.PREF_SETTING_SCAN_DELAY, scanDelay);

		editor.commit();

		dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_dialog_settings_ok:
			onClickOk();
			break;

		default:
			dismiss();
			break;
		}	
	}
}
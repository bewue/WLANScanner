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

import org.bitbatzen.wlanscanner.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class DialogAbout 
		extends Dialog 
		implements android.view.View.OnClickListener {

	public Activity activity;
	public Button buttonClose;

	
	public DialogAbout(Activity activity) {
		super(activity);
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_about);
		
		buttonClose = (Button) findViewById(R.id.button_dialog_close);
		buttonClose.setOnClickListener(this);

		try {
			String versionName = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
			TextView tvVersion = (TextView) findViewById(R.id.tv_dialog_about_version);
			tvVersion.setText(versionName);
		}
		catch (NameNotFoundException e) {}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_dialog_close:
			dismiss();
			break;
		default:
			break;
		}	
	}
}
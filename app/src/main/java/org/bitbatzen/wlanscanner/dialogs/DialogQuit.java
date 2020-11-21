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
import org.bitbatzen.wlanscanner.events.EventManager;
import org.bitbatzen.wlanscanner.events.Events.EventID;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class DialogQuit 
		extends Dialog 
		implements android.view.View.OnClickListener {

	public Activity activity;
	public Button buttonYes; 
	public Button buttonNo;

	
	public DialogQuit(Activity activity) {
		super(activity);
		this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_quit);
		
		buttonYes = (Button) findViewById(R.id.button_dialog_yes);
		buttonYes.setOnClickListener(this);
		
		buttonNo = (Button) findViewById(R.id.button_dialog_no);
		buttonNo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_dialog_yes:
			EventManager.sharedInstance().sendEvent(EventID.USER_QUIT);
			break;
		case R.id.button_dialog_no:
			dismiss();
			break;
		default:
			break;
		}	
		
		dismiss();
	}
}
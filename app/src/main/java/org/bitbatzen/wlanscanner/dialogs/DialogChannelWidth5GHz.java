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

import java.util.HashMap;
import java.util.Map.Entry;

import org.bitbatzen.wlanscanner.MainActivity;
import org.bitbatzen.wlanscanner.R;
import org.bitbatzen.wlanscanner.Util;
import org.bitbatzen.wlanscanner.events.EventManager;
import org.bitbatzen.wlanscanner.events.Events.EventID;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class DialogChannelWidth5GHz 
		extends Dialog 
		implements android.view.View.OnClickListener {

	public MainActivity mainActivity;
	
	private HashMap<Integer, Integer> radioButtonToOption;
	
	private RadioGroup radioGroup;
	
	
	public DialogChannelWidth5GHz(MainActivity mainActivity) {
		super(mainActivity);
		this.mainActivity = mainActivity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		radioButtonToOption = new HashMap<Integer, Integer>();
		
		setContentView(R.layout.dialog_channel_width5ghz);
		
		Button buttonOk = (Button) findViewById(R.id.button_dialog_ok);
		buttonOk.setOnClickListener(this);
		
		radioGroup = (RadioGroup) findViewById(R.id.rg_dialog_channelwidth);
		
		RadioButton rb1 = (RadioButton) findViewById(R.id.rb_dialog_channelwidth_1);
		rb1.setOnClickListener(this);
		radioButtonToOption.put(rb1.getId(), Util.CHANNEL_WIDTH_OPTION_20_MHZ);
		
		RadioButton rb2 = (RadioButton) findViewById(R.id.rb_dialog_channelwidth_2);
		rb2.setOnClickListener(this);
		radioButtonToOption.put(rb2.getId(), Util.CHANNEL_WIDTH_OPTION_40_MHZ);
		
		RadioButton rb3 = (RadioButton) findViewById(R.id.rb_dialog_channelwidth_3);
		rb3.setOnClickListener(this);
		radioButtonToOption.put(rb3.getId(), Util.CHANNEL_WIDTH_OPTION_80_MHZ);
		
		RadioButton rb4 = (RadioButton) findViewById(R.id.rb_dialog_channelwidth_4);
		rb4.setOnClickListener(this);
		radioButtonToOption.put(rb4.getId(), Util.CHANNEL_WIDTH_OPTION_160_MHZ);

		setCheckedRadioButton(mainActivity.getSelectedChannelWidth5GHz());
	}
	
	private void setCheckedRadioButton(int channelWidthOption) {
		View rbView = findViewById(R.id.rb_dialog_channelwidth_1);
		
        for (Entry<Integer, Integer> entry : radioButtonToOption.entrySet()) {
            if (entry.getValue() == channelWidthOption) {
            	rbView = findViewById(entry.getKey());
            }
        }
		
		RadioButton rb = (RadioButton) rbView;
		rb.setChecked(true);
	}
	
	@Override
	public void onClick(View v) {
    	if (v.getId() == R.id.button_dialog_ok) {
    		int selectedOption = radioButtonToOption.get(radioGroup.getCheckedRadioButtonId());
        	mainActivity.setSelectedChannelWidth5GHz(selectedOption);
            
        	EventManager.sharedInstance().sendEvent(EventID.OPTION_CHANNEL_WIDTH_CHANGED);

        	dismiss();
    	}
	}
}
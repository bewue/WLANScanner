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

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.bitbatzen.wlanscanner.events.EventManager;
import org.bitbatzen.wlanscanner.events.Events.EventID;
import org.bitbatzen.wlanscanner.events.IEventListener;


public class FragmentDiagram24GHz 
		extends Fragment
		implements IEventListener {
	
	LevelDiagram24GHz levelDiagram;
	
    private MainActivity mainActivity;

    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    	mainActivity = (MainActivity) activity;
    	EventManager.sharedInstance().addListener(this, EventID.SCAN_RESULT_CHANGED);
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wlan_diagram_2_4ghz, container, false);
		
        levelDiagram = (LevelDiagram24GHz) view.findViewById(R.id.levelDiagram24GHz);
        levelDiagram.updateDiagram(mainActivity);

		mainActivity.invalidateOptionsMenu();
		mainActivity.setCurrentFragmentID(mainActivity.FRAGMENT_ID_DIAGRAM_24GHZ);
        
		return view;
	}
	
	@Override
	public void handleEvent(EventID eventID) {
		switch (eventID) {
		case SCAN_RESULT_CHANGED:
			if (levelDiagram == null) {
				return;
			}

			levelDiagram.updateDiagram(mainActivity);
			break;
		default:
			break;
		}
		
	}
}
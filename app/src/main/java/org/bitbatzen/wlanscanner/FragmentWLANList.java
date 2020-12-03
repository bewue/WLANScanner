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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.bitbatzen.wlanscanner.events.EventManager;
import org.bitbatzen.wlanscanner.events.Events.EventID;
import org.bitbatzen.wlanscanner.events.IEventListener;

import java.util.ArrayList;
import java.util.List;


@TargetApi(29)
public class FragmentWLANList 
		extends Fragment 
		implements OnItemClickListener, OnItemSelectedListener, IEventListener {
  
    private ListView lv;
    private ArrayList<ScanResult> scanResultList;
    private ArrayAdapterWLAN wlanAdapter;
    private Spinner sortingSpinner;
    private SortingHelper sortingHelper;
    private int currentSortingOption;
    
    private MainActivity mainActivity;

    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    	mainActivity = (MainActivity) activity;
    	EventManager.sharedInstance().addListener(this, EventID.SCAN_RESULT_CHANGED);
		EventManager.sharedInstance().addListener(this, EventID.USER_QUIT);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wlan_list, container, false);
		
        sortingHelper = new SortingHelper();
        
        sortingSpinner = (Spinner) view.findViewById(R.id.spinnerSorting);
    	List<String> list = new ArrayList<String>();
    	list.add(sortingHelper.getSortingOptionName(SortingHelper.SORTING_OPTION_LEVEL));
    	list.add(sortingHelper.getSortingOptionName(SortingHelper.SORTING_OPTION_CHANNEL));
		list.add(sortingHelper.getSortingOptionName(SortingHelper.SORTING_OPTION_CHANNEL_WIDTH));
    	list.add(sortingHelper.getSortingOptionName(SortingHelper.SORTING_OPTION_SSID));
		if (android.os.Build.VERSION.SDK_INT >= 30) {
			list.add(sortingHelper.getSortingOptionName(SortingHelper.SORTING_OPTION_WLAN_STANDARD));
		}

    	ArrayAdapter<String> sortingAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, list);
		sortingSpinner.setAdapter(sortingAdapter);
		sortingSpinner.setOnItemSelectedListener(this);
		
		SharedPreferences sharedPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        currentSortingOption = sharedPrefs.getInt(Util.PREF_SORTING_OPTION, SortingHelper.SORTING_OPTION_LEVEL);
		int spinnerPosition = sortingAdapter.getPosition(sortingHelper.getSortingOptionName(currentSortingOption));
		sortingSpinner.setSelection(spinnerPosition);
        
        lv = (ListView) view.findViewById(R.id.list);
        lv.setOnItemClickListener(this);
        
        scanResultList = new ArrayList<ScanResult>();
        
        wlanAdapter = new ArrayAdapterWLAN(getActivity(), scanResultList);
        lv.setAdapter(wlanAdapter);

        updateWLANList();
        
        getActivity().invalidateOptionsMenu();
        
		return view;
    }

    @Override
    public void onPause() {
    	super.onPause();
    	SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
        editor.putInt(Util.PREF_SORTING_OPTION, currentSortingOption);
        editor.commit();
    }
    
    private void scanResultChanged() {
    	updateWLANList();
    }
    
	private void updateWLANList() {
    	ArrayList<ScanResult> scanResults = mainActivity.getScanResults();
    	scanResultList.clear();
    	for (ScanResult sr : scanResults) {
    		scanResultList.add(sr);
    	}
    	
    	SortingHelper.sort(scanResultList, currentSortingOption);
        wlanAdapter.notifyDataSetChanged();   
    }
    
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//    	ScanResult scanResult = (ScanResult) wlanAdapter.getItem(position);

//		DialogWLANListItemInfo dialogwlii = new DialogWLANListItemInfo(getActivity(), scanResult.SSID, scanResult.capabilities, Integer.toString(scanResult.frequency));
//		dialogwlii.show();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		String optionName = (String) sortingSpinner.getItemAtPosition(position);
		currentSortingOption = sortingHelper.getSortingOption(optionName);
		SortingHelper.sort(scanResultList, currentSortingOption);
		wlanAdapter.notifyDataSetChanged();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleEvent(EventID eventID) {
		switch (eventID) {
		case SCAN_RESULT_CHANGED:
			scanResultChanged();
			break;

		case USER_QUIT:
			currentSortingOption = SortingHelper.SORTING_OPTION_LEVEL;
			break;

		default:
			break;
		}
	}  
}
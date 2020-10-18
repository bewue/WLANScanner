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
import java.util.HashMap;
import java.util.Map.Entry;

import android.net.wifi.ScanResult;


public class SortingHelper {
	
	public static final int SORTING_OPTION_LEVEL 	= 0;
	public static final int SORTING_OPTION_CHANNEL	= 1;
	public static final int SORTING_OPTION_SSID 	= 2;
	
    private HashMap<Integer, String> sortingOptions;

    
    public SortingHelper() {
    	sortingOptions = new HashMap<Integer, String>();
        sortingOptions.put(SORTING_OPTION_LEVEL, "Order by Level");
        sortingOptions.put(SORTING_OPTION_CHANNEL, "Order by Channel");
        sortingOptions.put(SORTING_OPTION_SSID, "Order by SSID");
    }
    
    public String getSortingOptionName(int sortingOption) {
    	return sortingOptions.get(sortingOption);
    }
    
    public int getSortingOption(String optionName) {
    	for (Entry<Integer, String> e : sortingOptions.entrySet()) {
    		if (e.getValue() == optionName) {
    			return e.getKey(); 
    		}
    	}
    	return -1;
    }
    
    public static void sort(ArrayList<ScanResult> scanResults, int sortingOption) {
    	if (scanResults.size() <= 1) {
    		return;
    	}
    	
		ScanResult[] tmpArray = scanResults.toArray(new ScanResult[scanResults.size()]);
    	ScanResult tempItem;

    	for (int i = 1; i < tmpArray.length; i++) {
			for (int j = 0; j < tmpArray.length - i; j++) {
				if (compare(tmpArray[j], tmpArray[j+1], sortingOption)) {
					tempItem = tmpArray[j];
					tmpArray[j] = tmpArray[j+1];
					tmpArray[j+1] = tempItem;
				}
			}
		}

		scanResults.clear();
		for (int i = 0; i < tmpArray.length; i++) {
			scanResults.add(tmpArray[i]);
		}
    }
    
    private static boolean compare(ScanResult v1, ScanResult v2, int sortingOption) {
		switch (sortingOption) {
			case SORTING_OPTION_LEVEL:
				if (v1.level < v2.level) {
					return true;
				}
				break;
			case SORTING_OPTION_CHANNEL:
				if (v1.frequency > v2.frequency) {
					return true;
				}				
				break;
			case SORTING_OPTION_SSID:
				if (v1.SSID.compareToIgnoreCase(v2.SSID) > 0) {
					return true;
				}
				break;
		}
		
		return false;
    }
}

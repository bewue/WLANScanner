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

import android.graphics.Color;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class Util {
	
	public final static int CHANNEL_WIDTH_OPTION_20_MHZ		= 0;
	public final static int CHANNEL_WIDTH_OPTION_22_MHZ 	= 1;
	public final static int CHANNEL_WIDTH_OPTION_40_MHZ 	= 2;
	public final static int CHANNEL_WIDTH_OPTION_80_MHZ 	= 3;
	public final static int CHANNEL_WIDTH_OPTION_160_MHZ	= 4;
	
	public enum FrequencyBand {
		FIVE_GHZ,
		TWO_FOUR_GHZ,
		UNKOWN
	}
	
    public static final Map<Integer, Integer> channels24GHzBand;
    public static final Map<Integer, Integer> channels5GHzBand;

    public static final int START_24GHZ_BAND = 2412;
    public static final int END_24GHZ_BAND = 2484;

    public static final int START_5GHZ_BAND = 4915;
    public static final int END_5GHZ_BAND = 5865;
    
    static {
    	Map<Integer, Integer> aMap = new HashMap<Integer, Integer>();
        aMap.put(2412, 1);
        aMap.put(2417, 2);
        aMap.put(2422, 3);
        aMap.put(2427, 4);
        aMap.put(2432, 5);
        aMap.put(2437, 6);
        aMap.put(2442, 7);
        aMap.put(2447, 8);
        aMap.put(2452, 9);
        aMap.put(2457, 10);
        aMap.put(2462, 11);
        aMap.put(2467, 12);
        aMap.put(2472, 13);
        aMap.put(2484, 14);
        channels24GHzBand = Collections.unmodifiableMap(aMap);
        
    	aMap = new HashMap<Integer, Integer>();
    	aMap.put(4915, 183);
    	aMap.put(4920, 184);
    	aMap.put(4925, 185);
    	aMap.put(4935, 187);
    	aMap.put(4940, 188);
    	aMap.put(4945, 189);
    	aMap.put(4960, 192);
    	aMap.put(4980, 196);
    	aMap.put(5035, 7);
    	aMap.put(5040, 8);
    	aMap.put(5045, 9);
    	aMap.put(5055, 11);
    	aMap.put(5060, 12);
    	aMap.put(5080, 16);
		aMap.put(5160, 32);
    	aMap.put(5170, 34);
    	aMap.put(5180, 36);
		aMap.put(5190, 38);
		aMap.put(5200, 40);
		aMap.put(5210, 42);
		aMap.put(5220, 44);
		aMap.put(5230, 46);
		aMap.put(5240, 48);
		aMap.put(5250, 50);
		aMap.put(5260, 52);
		aMap.put(5270, 54);
		aMap.put(5280, 56);
		aMap.put(5290, 58);
		aMap.put(5300, 60);
		aMap.put(5310, 62);
		aMap.put(5320, 64);
		aMap.put(5340, 68);
		aMap.put(5480, 96);
		aMap.put(5500, 100);
		aMap.put(5510, 102);
		aMap.put(5520, 104);
		aMap.put(5530, 106);
		aMap.put(5540, 108);
		aMap.put(5550, 110);
		aMap.put(5560, 112);
		aMap.put(5570, 114);
		aMap.put(5580, 116);
		aMap.put(5590, 118);
		aMap.put(5600, 120);
		aMap.put(5610, 122);
		aMap.put(5620, 124);
		aMap.put(5630, 126);
		aMap.put(5640, 128);
		aMap.put(5660, 132);
		aMap.put(5670, 134);
		aMap.put(5680, 136);
		aMap.put(5690, 138);
		aMap.put(5700, 140);
		aMap.put(5710, 142);
		aMap.put(5720, 144);
		aMap.put(5745, 149);
		aMap.put(5755, 151);
		aMap.put(5765, 153);
		aMap.put(5775, 155);
		aMap.put(5785, 157);
		aMap.put(5795, 159);
		aMap.put(5805, 161);
		aMap.put(5825, 165);
		aMap.put(5845, 169);
		aMap.put(5865, 173);
        channels5GHzBand = Collections.unmodifiableMap(aMap);
    }

	public static FrequencyBand getFrequencyBand(int frequency) {
		if (channels24GHzBand.containsKey(frequency)) {
			return FrequencyBand.TWO_FOUR_GHZ;
		}
		else if (channels5GHzBand.containsKey(frequency)) {
			return FrequencyBand.FIVE_GHZ;
		}
		else {
			return FrequencyBand.UNKOWN;
		}
	}
	
	public static int getFrequencyFromChannel(int channel) {
        for (Entry<Integer, Integer> entry : channels24GHzBand.entrySet()) {
            if (entry.getValue() == channel) {
            	return entry.getKey();
            }
        }
        for (Entry<Integer, Integer> entry : channels5GHzBand.entrySet()) {
            if (entry.getValue() == channel) {
            	return entry.getKey();
            }
        }
        
		return -1;
	}

	public static int getChannelFromFrequency(int frequency) {
		if (channels24GHzBand.containsKey(frequency)) {
			return channels24GHzBand.get(frequency);
		}
		else if (channels5GHzBand.containsKey(frequency)) {
			return channels5GHzBand.get(frequency);
		}
		else {
			return -1;
		}
	}
	
	public static String getCapabilitiesShortString(String capabilities) {
		String shortString = "";

		if (capabilities.contains("WEP")) {
			shortString += "[WEP]";
		}
		if (capabilities.contains("WPA-")) {
			shortString += "[WPA]";
		}
		if (capabilities.contains("WPA2")) {
			shortString += "[WPA2]";
		}
		if (capabilities.contains("WPA3")) {
			shortString += "[WPA3]";
		}
		if (capabilities.contains("WPS")) {
			shortString += "[WPS]";
		}
//		if (capabilities.contains("ESS")) {
//			shortString += "[ESS]";
//		}
		
		return shortString;
	}
	
    public static int getRandomColor(int min, int max) {
    	int r = (int) (Math.random() * (max - min) + min);
    	int g = (int) (Math.random() * (max - min) + min);
    	int b = (int) (Math.random() * (max - min) + min);
    	
    	return Color.rgb(r, g, b);
    }
    
    public static int getChannelWidth(int channelWidthOption) {
    	switch (channelWidthOption) {
		case CHANNEL_WIDTH_OPTION_20_MHZ:
			return 20;
		case CHANNEL_WIDTH_OPTION_22_MHZ:
			return 22;
		case CHANNEL_WIDTH_OPTION_40_MHZ:
			return 40;
		case CHANNEL_WIDTH_OPTION_80_MHZ:
			return 80;
		case CHANNEL_WIDTH_OPTION_160_MHZ:
			return 160;
		default:
			return -1;
		}
    }
}

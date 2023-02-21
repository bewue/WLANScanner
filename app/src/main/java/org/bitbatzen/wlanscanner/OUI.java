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

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;


public class OUI {

	public static final String OUI_FILE = "oui"; // without filename extension

	private HashMap<String, String> ouis = new HashMap<>();

	private MainActivity mainActivity;


    public OUI(MainActivity mainActivity) {
		this.mainActivity = mainActivity;

		try {
			parseOUIFile();
		} catch (Exception e) {
			Log.e(Util.LOG_TAG, "OUI() -- failed to parse oui file: " + e);
		}
    }

	/**
	 * @param mac address
	 * @param maxLength shorten the vendor name to max length
	 * @return the vendor name
	 */
	public String getVendor(String mac, int maxLength) {
		String oui 		= mac.substring(0, Math.min(mac.length(), 8)); // get first three octets
		String vendor 	= ouis.get(oui.toLowerCase());

		if (vendor == null) {
			return "";
		} else if (maxLength > 0) {
			String[] parts = vendor.split(" ");
			return parts[0].substring(0, Math.min(parts[0].length(), maxLength));
		} else {
			return vendor;
		}
	}

	private void parseOUIFile() throws Exception {
		ouis.clear();

		InputStream is = mainActivity.getResources().openRawResource(mainActivity.getResources().getIdentifier(OUI.OUI_FILE,"raw", mainActivity.getPackageName()));

		if (is != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			int lineNr = 0;

			while ((line = reader.readLine()) != null) {
				lineNr++;
				String[] parts = line.split("\\|", 2);
				if (parts.length != 2 || parts[0].length() != 8 || parts[1].length() == 0 || parts[1].length() > 128) {
					ouis.clear();
					throw new Exception("invalid data in line " + lineNr + ": " + line.substring(0, Math.min(line.length(), 128)));
				}

				ouis.put(parts[0], parts[1]);
			}

			is.close();
		}

		if (ouis.size() == 0) {
			Log.e(Util.LOG_TAG, "OUI.parseOUIFile() -- missing oui entries");
		} else {
			Log.i(Util.LOG_TAG, "OUI.parseOUIFile() -- parsed " + ouis.size() + " entries");
		}
	}
}

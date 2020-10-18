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


public class WLANDiagramItem {
	
	String SSID;
	String BSSID;
	int frequency;
	int dBm;
	
	int color;
	
	
	public WLANDiagramItem() {
	}
	
	public WLANDiagramItem(String SSID, String BSSID, int frequency, int dBm) {
		this.SSID 		= SSID;
		this.BSSID 		= BSSID;
		this.frequency 	= frequency;
		this.dBm 		= dBm;
	}
	
	public WLANDiagramItem(WLANDiagramItem wdi) {
		this.SSID 		= wdi.SSID;
		this.BSSID 		= wdi.BSSID;
		this.frequency	= wdi.frequency;
		this.dBm		= wdi.dBm;
		this.color		= wdi.color;
	}
}

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
import java.util.Map.Entry;

import org.bitbatzen.wlanscanner.Util.FrequencyBand;

import android.content.Context;
import android.graphics.Canvas;
import android.net.wifi.ScanResult;
import android.util.AttributeSet;


public class LevelDiagram24GHz extends LevelDiagram {
  
	private static ArrayList<WLANDiagramItem> wlansOld = new ArrayList<WLANDiagramItem>();

	
	public LevelDiagram24GHz(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	@Override
    public void updateDiagram(ArrayList<ScanResult> scanResults) {
    	wlans.clear();
    	for (ScanResult sr : scanResults) {
    		if (Util.getFrequencyBand(sr.frequency) != FrequencyBand.TWO_FOUR_GHZ) {
    			continue;
    		}
    		
    		WLANDiagramItem wdi = new WLANDiagramItem(sr.SSID, sr.BSSID, sr.frequency, sr.level);
    		WLANDiagramItem wlanOld = checkWLANSOld(wdi);
    		
    		if (wlanOld != null) {
    			wdi.color = wlanOld.color; 
    		}
    		else {
    			wdi.color = Util.getRandomColor(80, 180);
    			wlansOld.add(new WLANDiagramItem(wdi));
    		}
    		
    		wlans.add(wdi);
    	}	
    	
    	invalidate();
    }
    
    private WLANDiagramItem checkWLANSOld(WLANDiagramItem wdi) {
    	for (WLANDiagramItem w : wlansOld) {
    		if (w.SSID.equals(wdi.SSID) && w.BSSID.equals(wdi.BSSID)) {
    			return w;
    		}
    	}
    	
    	return null;
    }
    
    @Override
    protected void updateMeasures() {
    	super.updateMeasures();
    	rowsMarginLeft = getWidth() * 0.08f;
    	rowsMarginRight = getWidth() * 0.03f;
    }
    
    @Override
    public float getXAxisPos(int frequency) {
    	float margin = rowsMarginLeft + rowsMarginRight;
    	int relFreq = frequency - Util.START_24GHZ_BAND;
    	int bandWidth = Util.END_24GHZ_BAND - Util.START_24GHZ_BAND;
    	float mghzWidth = (innerRect.right - innerRect.left - margin) / bandWidth;
    	
    	return innerRect.left + rowsMarginLeft + mghzWidth * relFreq;
    }
    
    @Override
    protected void drawXAxisLabelsAndLines(Canvas canvas) {
    	for (Entry<Integer, Integer> entry : Util.channels24GHzBand.entrySet()) {
    		float posX = getXAxisPos(entry.getKey());
    		canvas.drawLine(posX, innerRect.bottom, posX, innerRect.top, linesPaint);
    		canvas.drawText(Integer.toString(entry.getValue()), posX, getHeight(), xLabelsPaint);
        }
    }
    
    @Override 
    protected void drawSSIDLabels(Canvas canvas) {
		for (WLANDiagramItem wdi : wlans) {
			float levelHeight = getLevelHeight(wdi.dBm);
			float levelY = innerRect.bottom - levelHeight;
			float posX = getXAxisPos(wdi.frequency);
			
			ssidPaint.setColor(wdi.color);
			canvas.drawText(wdi.SSID, posX, levelY - 8, ssidPaint);			
		}
    }
 }

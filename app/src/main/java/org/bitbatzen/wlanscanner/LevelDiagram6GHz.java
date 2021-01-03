/*
 *  Copyright (C) 2020 Benjamin W. (bitbatzen@gmail.com)
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

import android.content.Context;
import android.graphics.Canvas;
import android.net.wifi.ScanResult;
import android.util.AttributeSet;

import org.bitbatzen.wlanscanner.Util.FrequencyBand;

import java.util.ArrayList;
import java.util.Map.Entry;


public class LevelDiagram6GHz extends LevelDiagram {

	public LevelDiagram6GHz(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	@Override
    public void updateDiagram(ArrayList<ScanResult> scanResults) {
    	wlans.clear();
    	for (ScanResult sr : scanResults) {
    		if (Util.getFrequencyBand(sr) == FrequencyBand.SIX_GHZ) {
				handleWLANDiagramItem(sr);
    		}
    	}
    	
    	invalidate();
    }
    
    @Override
    protected void updateMeasures() {
    	super.updateMeasures();
    	rowsMarginLeft = getWidth() * 0.03f;
    	rowsMarginRight = getWidth() * 0.03f;
    }
    
    @Override
    public float getXAxisPos(int frequency) {
		int start 		= Util.START_6GHZ_BAND - 10;
		int end 		= Util.END_6GHZ_BAND + 10;
    	float margin 	= rowsMarginLeft + rowsMarginRight;
    	int relFreq 	= frequency - start;
    	int bandWidth 	= end - start;
    	float mghzWidth = (innerRect.right - innerRect.left - margin) / bandWidth;
    	
    	return innerRect.left + rowsMarginLeft + mghzWidth * relFreq;
    }
    
    @Override
    protected void drawXAxisLabelsAndLines(Canvas canvas) {
		// x-axis label
    	String s = Integer.toString(Util.START_6GHZ_BAND) + " - " + Integer.toString(Util.END_6GHZ_BAND) + " MHz";
		canvas.drawText(s, innerRect.left + innerRect.width() / 2, getHeight(), xLabelsPaint);
		
		// x-axis lines
        for (Entry<Integer, Integer> entry : Util.CHANNELS_6GHZ_BAND.entrySet()) {
    		float posX = getXAxisPos(entry.getKey());
    		canvas.drawLine(posX, innerRect.bottom, posX, innerRect.top, linesPaint);
        }
    }
    
    @Override 
    protected void drawSSIDLabels(Canvas canvas) {
		for (WLANDiagramItem wdi : wlans) {
			drawSSIDLabel(canvas, wdi, wdi.SSID + " (CH " + Util.getChannel(wdi.frequency) + ")");
		}
    }
 }

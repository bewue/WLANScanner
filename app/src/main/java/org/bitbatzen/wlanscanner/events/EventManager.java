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

package org.bitbatzen.wlanscanner.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EventManager {
	
	private static EventManager instance;
	
	private HashMap<Events.EventID, ArrayList<IEventListener>> eventListeners;
	
	
	private EventManager() {
		eventListeners = new HashMap<Events.EventID, ArrayList<IEventListener>>();
	}
	
	public static EventManager sharedInstance() {
		if (instance == null) {
			instance = new EventManager();
		}
		
		return instance;
	}
	
	public void addListener(IEventListener listener, Events.EventID eventID) {
		ArrayList<IEventListener> listenerList = eventListeners.get(eventID);
		if (listenerList == null) {
			listenerList = new ArrayList<IEventListener>();
			listenerList.add(listener);		
			eventListeners.put(eventID, listenerList);
		}
		else if (listenerList.contains(listener) == false) {
			listenerList.add(listener);		
		}
	}
	
	public void sendEvent(Events.EventID eventID) {
		for (Map.Entry<Events.EventID, ArrayList<IEventListener>> entry : eventListeners.entrySet()) {
		    if (entry.getKey() ==  eventID) {
		    	ArrayList<IEventListener> listenerList = entry.getValue();
		    	for (IEventListener listener : listenerList) {
//		    		Log.d("++++++++++", "EventManager::sendEvent() : " + eventID + " : " + listener.getClass().toString());
		    		listener.handleEvent(eventID);
		    	}
		    	return;
		    }
		}
	}
 }

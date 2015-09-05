package de.biba.informationrequestservice;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.biba.informationrequestservice.datatypes.InformationServices;

public class Main {

	/**[InformationRequestService. This is a mediation based data integration approach which aggregate data sources via Wrapper]
	Copyright (C) [2015  [Marco Franke (BIBA-Bremer Institut für Produktion und Logistik GmbH)]

	This program is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.

	    This program is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.

	    You should have received a copy of the GNU General Public License
	    along with this program.  If not, see <http://www.gnu.org/licenses/>.
	    **/
	
	public static void main (String[] argv){
		InformationServiceManager manager;
		
		
		try {
			manager = InformationServiceManager.getInstance();
			for (int i =0; i < manager.getAllServices().size();i++){
			manager.startOneService(i,true);
			}
			while (!manager.canBeClosed()){
				
			}
			Logger.getAnonymousLogger().log(Level.INFO, "Close the Programm");
			System.exit(0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

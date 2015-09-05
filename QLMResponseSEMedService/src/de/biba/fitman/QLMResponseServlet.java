package de.biba.fitman;

import java.io.IOException;
import java.io.PrintWriter;


import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class HTMLResonseServlet
 */

/**[QLMResponseSEMedService. This is a wrapper to tranform a xml based data base schema to an ontology]
Copyright (C) [2014  [Marco Franke (BIBA-Bremer Institut für Produktion und Logistik GmbH)]

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
@WebServlet("/QLMResponseServlet")
public class QLMResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static long counter = 0;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QLMResponseServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println (request.toString());
		counter++;
		if (counter > 50000000){
			counter=0;
		}
		String alternatreRequestID= "REQ000000" +counter;
		for (String str: request.getParameterMap().keySet()){
			System.out.println(str);
		}
		String jobject = request.getParameter("jObject");
		Logger.getAnonymousLogger().log(Level.INFO, "jObject: " + jobject);		
		String requestID = request.getParameter("requestID");
		String oneConcept = request.getParameter("OneConcept");
		boolean bOneConcept = true;
		if (oneConcept != null){
			try{
				bOneConcept = Boolean.valueOf(oneConcept);
			}
			catch(Exception e){
				Logger.getAnonymousLogger().log(Level.WARNING, "Wrong parameter for: OneConcept (true/false)" );
			}
		}
		Logger.getAnonymousLogger().log(Level.INFO, "requestID: " + requestID);
		if ((requestID == null) || (requestID.length() < 1)){
			requestID = alternatreRequestID;
		}
		QLMWebservice webservice = new QLMWebservice();
		String result = webservice.queryAsQML(jobject,requestID, bOneConcept);		
		PrintWriter writer = response.getWriter();	
		
		writer.println(result);		
		writer.close();			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				System.out.println (request.toString());
				counter++;
				if (counter > 50000000){
					counter=0;
				}
				String alternatreRequestID= "REQ000000" +counter;
				for (String str: request.getParameterMap().keySet()){
					System.out.println(str);
				}
				String jobject = request.getParameter("jObject");
				Logger.getAnonymousLogger().log(Level.INFO, "jObject: " + jobject);		
				String requestID = request.getParameter("requestID");
				String oneConcept = request.getParameter("OneConcept");
				boolean bOneConcept = true;
				if (oneConcept != null){
					try{
						bOneConcept = Boolean.valueOf(oneConcept);
					}
					catch(Exception e){
						Logger.getAnonymousLogger().log(Level.WARNING, "Wrong parameter for: OneConcept (true/false)" );
					}
				}
				Logger.getAnonymousLogger().log(Level.INFO, "requestID: " + requestID);
				if ((requestID == null) || (requestID.length() < 1)){
					requestID = alternatreRequestID;
				}
				QLMWebservice webservice = new QLMWebservice();
				String result = webservice.queryAsQML(jobject,requestID, bOneConcept);
				PrintWriter writer = response.getWriter();		
				writer.println(result);		
				writer.close();			
	}

}

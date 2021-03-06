package de.biba.informationrequestservice.datatypes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import de.biba.ontology.datatypes.Datatype;

/**[InformationRequestService. This is a mediation based data integration approach which aggregate data sources via Wrapper]
Copyright (C) [2015  [Marco Franke (BIBA-Bremer Institut f�r Produktion und Logistik GmbH)]

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

public interface IReader {

	public abstract void open();

	public abstract boolean next() throws IOException;

	public abstract String getString(int pPos) throws IOException;

	
	public void setText(String text, ArrayList<String> tagPathes);

	public abstract void reset();

	public abstract double getDouble(int pPos) throws IOException,
			ParseException;

	public abstract Date getDate(int pPos) throws ParseException, IOException;

	public abstract int getInt(int pPos) throws NumberFormatException,
			IOException;

	public abstract Datatype getValue(String tag) throws ParseException;

	public abstract boolean getBoolean(int pPos) throws IOException;

	public abstract void loadDatatypeDefinition(String pFileName);

	public abstract void setDateFormat(DateFormat pDateFormat);

	Datatype getValue(String datatypeName, String pos) throws ParseException;

	

}
package de.biba.wrapper;

/**[Reasoning Mediator. This is a mediation based data integration approach which aggregate data sources via Wrapper]
Copyright (C) [2014  [Marco Franke (BIBA-Bremer Institut für Produktion und Logistik GmbH)]

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
**/

public abstract  class DataSourceV2 extends DataSource {

	/**
	 * Diese Funktion überprüft, ob die KOnfiguration des Wrappers korrekt ist. Dies ist Funktion ist, weil in zukünftigen Versionen Datenquellen dynamisch hinzugelinkt werden sollen.
	 * Damit der Mediator sich nicht an einer falschen Konfiguration aufhängen kann, muss diese Methode implementiert werden.!!!!
	 * @return
	 */
	public abstract ValidationReport validateConfiguration();
	
	
}

package de.biba.wrapper;

/**[Reasoning Mediator. This is a mediation based data integration approach which aggregate data sources via Wrapper]
Copyright (C) [2014  [Marco Franke (BIBA-Bremer Institut für Produktion und Logistik GmbH)]

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
**/
/**
 * Der Validation Report liefert die Information, ob dem Wrapper eine geprüfte und konsistente Konfigurationsdatei übergeben wurde
 * @author Marco Franke
 *
 */
public class ValidationReport {

	private boolean isConsistent = false;
	private boolean isChecked = false;
	private String errorMessage = "";
	public boolean isConsistent() {
		return isConsistent;
	}
	
	/**
	 * Diese Methode dient dazu zu definieren, ob die Konfiguration konsistent ist. Der Boolean kann erst gesetzt werden, sobald die Variable isChecked auf true gesetzt wurde
	 * @param isConsistent Gibt an, ob die Konfiguration korrekt ist
	 */
	public void setConsistent(boolean isConsistent) {
		if (isChecked){
		this.isConsistent = isConsistent;
		}
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	/**
	 * Die Methode liefert erst eine Fehlermeldung zurück, sobald der angegeben wurde, dass die Validation durchgeührt wurde (isChecked)
	 * @return
	 */
	public String getErrorMessage() {
		if (isChecked()){
		return errorMessage;
		}
		return "Consistency haven't been checked";
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	@Override
	public String toString() {
		return "ValidationReport [isConsistent=" + isConsistent
				+ ", isChecked=" + isChecked + ", errorMessage=" + errorMessage
				+ "]";
	}

	public ValidationReport(boolean isConsistent, boolean isChecked) {
		super();
		this.isConsistent = isConsistent;
		this.isChecked = isChecked;
	}
	
	
	
}

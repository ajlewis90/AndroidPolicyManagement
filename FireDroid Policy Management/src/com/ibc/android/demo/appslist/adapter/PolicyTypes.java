package com.ibc.android.demo.appslist.adapter;

public class PolicyTypes {

	String polType = "";
	String dOutcome = "";

	public PolicyTypes(String polTypes, String defOutcome) {
		// TODO Auto-generated constructor stub
		polType = polTypes;
		dOutcome = defOutcome;
	}

	public String getPolType() {
		return polType;
	}

	public void setPolType(String polType) {
		this.polType = polType;
	}
	public String getDOutcome() {
		return dOutcome;
	}

	public void setDOutcome(String dOutcome) {
		this.dOutcome = dOutcome;
	}

}
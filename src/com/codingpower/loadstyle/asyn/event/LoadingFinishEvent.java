package com.codingpower.loadstyle.asyn.event;

import java.util.List;

public class LoadingFinishEvent {

	private List result;

	public LoadingFinishEvent(List result) {
		super();
		this.result = result;
	}

	public List getResult() {
		return result;
	}

	public void setResult(List result) {
		this.result = result;
	}
	
	
}

package com.app.wizard;

import java.util.HashMap;

import org.springframework.ui.Model;
import org.springframework.web.context.request.async.DeferredResult;

public class CustomDeferedResult extends DeferredResult<String>{
	private String view;
	private Model model;
	
	public CustomDeferedResult(Model model,String view) {
		super();
		this.model = model;
		
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
}

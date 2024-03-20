package com.cognicx.AppointmentRemainder.response;

import java.util.List;

public class ModuleListResponse {
	
	private String moduleUId;
	private String moduleName;
	private List<ScreenListResponse> screens;
	
	public ModuleListResponse() {}
	
	public ModuleListResponse(String moduleUId, String moduleName, List<ScreenListResponse> screens) {
		this.moduleName = moduleName;
		this.moduleUId = moduleUId;
		this.screens = screens;
	}
	
	public String getModuleUId() {
		return moduleUId;
	}
	public void setModuleUId(String moduleUId) {
		this.moduleUId = moduleUId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public List<ScreenListResponse> getScreens() {
		return screens;
	}
	public void setScreens(List<ScreenListResponse> screens) {
		this.screens = screens;
	}
}

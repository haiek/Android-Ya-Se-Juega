package com.example.yasejuega;

public class Custom {
	private String customRumor;
	private String customDate;
	private String customSource;
	
	public Custom(String rumor, String date, String source) {
		this.customRumor = rumor;
		this.customDate = date;
		this.customSource = source;
	}
	public String getcustomRumor() { return customRumor; }
	public void setcustomRumor(String customRumor) { this.customRumor = customRumor; }
	
	public String getcustomDate() { return customDate; }
	public void setcustomDate(String customDate) { this.customDate = customDate; }
	
	public String getcustomSource() { return customSource; }
	public void setcustomSource(String customSource) { this.customSource = customSource; }
}

package com.yasejuega.yasejuega;

public class Custom {
	private int customId;
	private String customRumor;
	private String customDate;
	private String customSource;
	private String customNumLikes;
	
	public Custom(int id, String rumor, String date, String source, String numLikes) {
		this.customId = id;
		this.customRumor = rumor;
		this.customDate = date;
		this.customSource = source;
		this.customNumLikes = numLikes;
	}
	public int getcustomId() { return customId; }
	public void setcustomId(int customId) { this.customId = customId; }
	
	public String getcustomRumor() { return customRumor; }
	public void setcustomRumor(String customRumor) { this.customRumor = customRumor; }
	
	public String getcustomDate() { return customDate; }
	public void setcustomDate(String customDate) { this.customDate = customDate; }
	
	public String getcustomSource() { return customSource; }
	public void setcustomSource(String customSource) { this.customSource = customSource; }
	
	public String getcustomNumLikes() { return customNumLikes; }
	public void setcustomNumLikes(String customNumLikes) { this.customNumLikes = customNumLikes; }
}

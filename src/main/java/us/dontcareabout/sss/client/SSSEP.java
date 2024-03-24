package us.dontcareabout.sss.client;

import com.google.gwt.user.client.Window;

import us.dontcareabout.gwt.client.GFEP;

public class SSSEP extends GFEP {
	public SSSEP() {}

	@Override
	protected String version() { return "0.0.1"; }

	@Override
	protected String defaultLocale() { return "zh_TW"; }

	@Override
	protected void featureFail() {
		Window.alert("這個瀏覽器我不尬意，不給用..... \\囧/");
	}

	@Override
	protected void start() {
	}
}

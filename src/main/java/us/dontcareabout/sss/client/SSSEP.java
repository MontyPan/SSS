package us.dontcareabout.sss.client;

import com.google.gwt.user.client.Window;

import us.dontcareabout.gst.client.GSTEP;
import us.dontcareabout.gst.client.data.SheetIdDao;
import us.dontcareabout.gxt.client.component.RwdRootPanel;
import us.dontcareabout.sss.client.data.DataCenter;
import us.dontcareabout.sss.client.ui.MainView;

public class SSSEP extends GSTEP {
	public SSSEP() {
		super("SSS-Key", "1zaEouhkIQYorcPkVBtdTZod2mKqhH_Au5-8d42CerIo");
	}

	@Override
	protected String version() { return "POC"; }

	@Override
	protected String defaultLocale() { return "zh_TW"; }

	@Override
	protected void featureFail() {
		Window.alert("這個瀏覽器我不尬意，不給用..... \\囧/");
	}

	@Override
	protected void start() {
		RwdRootPanel.setComponent(new MainView());
		DataCenter.wantSchedule(SheetIdDao.priorityValue(), 112, false); //Refactory magic number
	}
}

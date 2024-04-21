package us.dontcareabout.sss.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ImageRS extends ClientBundle {
	static ImageRS I = GWT.create(ImageRS.class);

	ImageResource calendarCheck();
	ImageResource pencilSquare();
	ImageResource personCheck();
	ImageResource search();
}

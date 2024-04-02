package us.dontcareabout.sss.client.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.sss.client.ui.event.ChangeNameEvent;
import us.dontcareabout.sss.client.ui.event.ChangeNameEvent.ChangeNameHandler;

public class UiCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	public static void changeName(String name) {
		eventBus.fireEvent(new ChangeNameEvent(name));
	}

	public static HandlerRegistration addChangeName(ChangeNameHandler handler) {
		return eventBus.addHandler(ChangeNameEvent.TYPE, handler);
	}
}

package us.dontcareabout.sss.client.ui.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.sss.client.ui.event.ChangeNameEvent.ChangeNameHandler;

public class ChangeNameEvent extends GwtEvent<ChangeNameHandler> {
	public static final Type<ChangeNameHandler> TYPE = new Type<ChangeNameHandler>();

	public final String data;

	public ChangeNameEvent(String name) {
		this.data = name;
	}

	@Override
	public Type<ChangeNameHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ChangeNameHandler handler) {
		handler.onChangeName(this);
	}

	public interface ChangeNameHandler extends EventHandler{
		public void onChangeName(ChangeNameEvent event);
	}
}

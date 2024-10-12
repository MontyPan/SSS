package us.dontcareabout.sss.client.data.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.sss.client.data.event.InitFinishEvent.InitFinishHandler;

public class InitFinishEvent extends GwtEvent<InitFinishHandler> {
	public static final Type<InitFinishHandler> TYPE = new Type<InitFinishHandler>();

	@Override
	public Type<InitFinishHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(InitFinishHandler handler) {
		handler.onInit(this);
	}

	public interface InitFinishHandler extends EventHandler{
		public void onInit(InitFinishEvent event);
	}
}

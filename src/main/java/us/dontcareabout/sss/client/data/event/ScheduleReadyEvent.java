package us.dontcareabout.sss.client.data.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.sss.client.data.event.ScheduleReadyEvent.ScheduleReadyHandler;

public class ScheduleReadyEvent extends GwtEvent<ScheduleReadyHandler> {
	public static final Type<ScheduleReadyHandler> TYPE = new Type<ScheduleReadyHandler>();

	@Override
	public Type<ScheduleReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ScheduleReadyHandler handler) {
		handler.onScheduleReady(this);
	}

	public interface ScheduleReadyHandler extends EventHandler{
		public void onScheduleReady(ScheduleReadyEvent event);
	}
}

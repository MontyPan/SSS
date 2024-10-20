package us.dontcareabout.sss.client.data.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.sss.client.data.event.RecordReadyEvent.RecordReadyHandler;

public class RecordReadyEvent extends GwtEvent<RecordReadyHandler> {
	public static final Type<RecordReadyHandler> TYPE = new Type<RecordReadyHandler>();

	@Override
	public Type<RecordReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RecordReadyHandler handler) {
		handler.onRecordReady(this);
	}

	public interface RecordReadyHandler extends EventHandler{
		public void onRecordReady(RecordReadyEvent event);
	}
}

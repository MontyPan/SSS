package us.dontcareabout.sss.client.data;

import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.gst.client.data.ApiKey;
import us.dontcareabout.gwt.client.google.sheet.Sheet;
import us.dontcareabout.gwt.client.google.sheet.SheetDto;
import us.dontcareabout.gwt.client.google.sheet.SheetDto.Callback;
import us.dontcareabout.sss.client.Util;
import us.dontcareabout.sss.client.data.event.ScheduleReadyEvent;
import us.dontcareabout.sss.client.data.event.ScheduleReadyEvent.ScheduleReadyHandler;
import us.dontcareabout.sss.client.vo.WeekSchedule;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	private static void loadError() {
		//TODO
	}

	////////////////

	public static List<WeekSchedule> weekScheduleList;

	public static void wantSchedule(String sheetId, int year, boolean isUp) {
		new SheetDto<WeekSchedule>().key(ApiKey.jsValue())
				.sheetId(sheetId).tabName(Util.semester(year, isUp))
				.fetch(
			new Callback<WeekSchedule>() {
				@Override
				public void onSuccess(Sheet<WeekSchedule> gs) {
					weekScheduleList = gs.getRows();
					eventBus.fireEvent(new ScheduleReadyEvent());
				}

				@Override
				public void onError(Throwable exception) {
					loadError();
				}
		});
	}

	public static HandlerRegistration addScheduleReady(ScheduleReadyHandler handler) {
		return eventBus.addHandler(ScheduleReadyEvent.TYPE, handler);
	}
}

package us.dontcareabout.sss.client.data;

import java.util.HashMap;
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

	// ==== 原始資料區 ==== //
	public static List<WeekSchedule> weekScheduleList;
	// ======== //

	// ==== 二級資料區 ==== //
	/** 志工狀態（目前只有進班次數） */
	public static HashMap<String, Integer> volunteerMap;	//TODO int 換成 VolunteerDetail
	// ======== //

	public static void wantSchedule(String sheetId, int year, boolean isUp) {
		new SheetDto<WeekSchedule>().key(ApiKey.jsValue())
				.sheetId(sheetId).tabName(Util.semester(year, isUp))
				.fetch(
			new Callback<WeekSchedule>() {
				@Override
				public void onSuccess(Sheet<WeekSchedule> gs) {
					weekScheduleList = gs.getRows();
					buildVolunteerMap();
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

	////////////////

	private static void buildVolunteerMap() {
		volunteerMap = new HashMap<>();

		for (WeekSchedule ws : weekScheduleList) {
			for (int g = 1; g <= Util.MAX_GRADE; g++) {
				for (int s = 1; s <= Util.MAX_SERIAL; s++) {
					String name = ws.getHost(g, s);

					if(name.isEmpty()) { continue; }

					Integer count = volunteerMap.get(name);
					if (count == null) {
						volunteerMap.put(name, 1);
					} else {
						volunteerMap.put(name, count + 1);
					}
				}
			}
		}
	}
}

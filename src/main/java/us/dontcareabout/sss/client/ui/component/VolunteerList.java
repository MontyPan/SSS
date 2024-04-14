package us.dontcareabout.sss.client.ui.component;

import java.util.stream.Stream;

import com.google.common.base.Strings;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.sencha.gxt.chart.client.draw.RGB;

import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.gxt.client.draw.layout.VerticalLayoutLayer;
import us.dontcareabout.sss.client.data.DataCenter;

public class VolunteerList extends LayerContainer implements HasSelectionHandlers<String> {
	private SimpleEventBus eventBus = new SimpleEventBus();

	private VerticalLayoutLayer root = new VerticalLayoutLayer();

	private String filter;

	public VolunteerList() {
		root.setGap(4);
		root.setMargins(4);
		addLayer(root);
		DataCenter.addScheduleReady(e-> refresh());
	}

	public void filter(String value) {
		if (filter == null && Strings.isNullOrEmpty(value)) { return; }
		if (value != null && value.equals(filter)) { return; }

		filter = Strings.isNullOrEmpty(value) ? null : value;
		refresh();
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}

	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<String> handler) {
		return eventBus.addHandler(SelectionEvent.getType(), handler);
	}

	@Override
	protected void adjustMember(int width, int height) {
		root.resize(width, root.getViewSize());
	}

	private void refresh() {
		root.clear();

		Stream<String> stream;

		if (filter != null) {
			stream = DataCenter.volunteerMap.keySet().stream()
				.filter(name -> name.indexOf(filter) != -1);
		} else {
			stream = DataCenter.volunteerMap.keySet().stream();
		}

		stream.sorted().forEach(name -> {
			TextButton btn = new TextButton(name);
			btn.setTextColor(RGB.WHITE);
			btn.setBgColor(RGB.BLACK);
			btn.setBgRadius(5);
			root.addChild(btn, 40);
			btn.addSpriteSelectionHandler(e -> SelectionEvent.fire(this, name));
		});

		root.redeploy();
		root.resize(Integer.MAX_VALUE, 1);
		setPixelSize(getOffsetWidth(), (int)root.getViewSize());
	}
}

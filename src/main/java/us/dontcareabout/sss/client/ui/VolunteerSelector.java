package us.dontcareabout.sss.client.ui;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextField;

import us.dontcareabout.sss.client.ui.component.VolunteerList;

public class VolunteerSelector extends VerticalLayoutContainer {
	private TextField input = build();
	private VolunteerList list = new VolunteerList();

	public VolunteerSelector() {
		setScrollMode(ScrollMode.AUTOY);
		setAdjustForScroll(true);

		input.setEmptyText("輸入單字可過濾");
		input.addKeyUpHandler(e -> {
			//電腦打注音會觸發兩次，其中一次的 key code 就是 229
			if (e.getNativeKeyCode() == 229) { return; }

			list.filter(input.getCurrentValue());
		});
		add(input, new VerticalLayoutData(1, -1));
		add(list, new VerticalLayoutData(1, -1));
	}

	public HandlerRegistration addSelectionHandler(SelectionHandler<String> handler) {
		return list.addSelectionHandler(handler);
	}

	private static TextField build() {
		TextField result = new TextField();
		InputElement ie = result.getCell().getInputElement(result.getElement());
		ie.getStyle().setHeight(40, Unit.PX);
		ie.getStyle().setFontSize(20, Unit.PX);
		return result;
	}
}

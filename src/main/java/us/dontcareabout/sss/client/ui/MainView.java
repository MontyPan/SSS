package us.dontcareabout.sss.client.ui;

import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import us.dontcareabout.gxt.client.component.RwdRootPanel;

public class MainView extends VerticalLayoutContainer {
	public MainView() {
		FlowLayoutContainer sflc = new FlowLayoutContainer();
		sflc.setScrollMode(ScrollMode.AUTO);
		sflc.add(new SemesterSchedule());

		add(sflc, new VerticalLayoutData(1, 1));

		if (RwdRootPanel.isMobileDevice()) {
			add(new UserPanel(), new VerticalLayoutData(1, UserPanel.M_HEIGHT));
		} else {
			add(new UserPanel(), new VerticalLayoutData(1, 60));
		}
	}
}

package us.dontcareabout.sss.client.ui;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import us.dontcareabout.gxt.client.util.PopUtil;
import us.dontcareabout.gxt.client.util.PopUtil.IsDialogWidget;
import us.dontcareabout.sss.client.data.DataCenter;
import us.dontcareabout.sss.client.vo.Assignment;
import us.dontcareabout.sss.client.vo.WeekSchedule;

public class VolunteerBoard extends VerticalLayoutContainer implements IsDialogWidget {
	private static final MyTemplate template = GWT.create(MyTemplate.class);
	private static final MyResource resource = GWT.create(MyResource.class);
	private static final int btnHeight = 36;

	private HTML html = new HTML();

	public VolunteerBoard() {
		resource.css().ensureInjected();

		html.getElement().getStyle().setOverflow(Overflow.AUTO);

		TextButton closeBtn = new TextButton("關閉");
		closeBtn.addSelectHandler(e -> {
			PopUtil.closeDialog();
		});
		closeBtn.setPixelSize(60, btnHeight);

		CenterLayoutContainer center = new CenterLayoutContainer();
		center.add(closeBtn);

		this.add(html, new VerticalLayoutData(1, 1));
		this.add(center, new VerticalLayoutData(1, btnHeight + 10));
	}

	public void refresh(WeekSchedule data, int g, int s) {
		String name = data.getHost(g, s);
		html.setHTML(template.content(
			resource.css(),
			name,
			DataCenter.volunteerMap.get(name).assignmentList
		));
	}

	@Override
	public int dialogWidth() { return 300; }

	@Override
	public int dialogHeight() { return 400; }

	interface MyStyle extends CssResource {
		String title();
		String name();
		String normal();
		String table();
		String tr();
		String date();
		String clazz();
		String topic();
	}

	interface MyTemplate extends XTemplates {
		@XTemplate(source = "VolunteerBoard.html")
		SafeHtml content(MyStyle style, String name, List<Assignment> assignmentList);
	}

	interface MyResource extends ClientBundle {
		@Source("VolunteerBoard.gss")
		MyStyle css();
	}
}

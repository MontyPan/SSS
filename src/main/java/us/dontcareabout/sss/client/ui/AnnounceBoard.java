package us.dontcareabout.sss.client.ui;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import us.dontcareabout.gxt.client.util.PopUtil;
import us.dontcareabout.gxt.client.util.PopUtil.IsDialogWidget;
import us.dontcareabout.sss.client.Util;

public class AnnounceBoard extends VerticalLayoutContainer implements IsDialogWidget {
	private static final MyTemplate template = GWT.create(MyTemplate.class);
	private static final MyResource resource = GWT.create(MyResource.class);
	private static final int btnHeight = 36;

	private HTML html = new HTML();

	public AnnounceBoard() {
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

	public void refresh(String name, Date date, String className) {
		html.setHTML(template.announce(
			resource.css(),
			name, Util.
			MMdd.format(date),
			CalendarUtil.getDaysBetween(new Date(), date),
			className)
		);
	}

	@Override
	public int dialogWidth() { return 300; }

	@Override
	public int dialogHeight() { return 300; }

	interface MyStyle extends CssResource {
		String title();
		String normal();
		String name();
	}

	interface MyTemplate extends XTemplates {
		@XTemplate(source = "AnnounceBoard.html")
		SafeHtml announce(MyStyle style, String name, String nextDate, int duration, String className);
	}

	interface MyResource extends ClientBundle {
		@Source("AnnounceBoard.gss")
		MyStyle css();
	}
}

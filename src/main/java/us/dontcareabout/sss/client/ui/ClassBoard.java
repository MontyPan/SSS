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
import us.dontcareabout.sss.client.vo.Record;

public class ClassBoard extends VerticalLayoutContainer implements IsDialogWidget {
	private static final MyTemplate template = GWT.create(MyTemplate.class);
	private static final MyResource resource = GWT.create(MyResource.class);
	private static final int btnHeight = 36;

	private HTML html = new HTML();

	public ClassBoard() {
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

	public void refresh(int grade, int serial) {
		List<Record> list1 = DataCenter.getClassTopic(DataCenter.nowYS.year, grade, serial);
		List<Record> list2 =
			//今年的二年級跟去年的一年級是同一個班級
			grade == 2 ? DataCenter.getClassTopic(DataCenter.nowYS.year - 1, grade - 1, serial) : null;
		html.setHTML(template.content(
			resource.css(),
			grade, serial,
			list1, list2
		));
	}

	@Override
	public int dialogWidth() { return 300; }

	@Override
	public int dialogHeight() { return 400; }

	interface MyStyle extends CssResource {
		String title();
		String normal();
		String table();
		String tr();
		String date();
		String topic();
	}

	interface MyTemplate extends XTemplates {
		/** list2 允許 null，如果是 null 則完全不會顯示對應區塊 */
		@XTemplate(source = "ClassBoard.html")
		SafeHtml content(MyStyle style, int grade, int serial,
			List<Record> list1, List<Record> list2);
	}

	interface MyResource extends ClientBundle {
		@Source("ClassBoard.gss")
		MyStyle css();
	}
}

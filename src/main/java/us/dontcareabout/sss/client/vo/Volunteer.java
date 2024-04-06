package us.dontcareabout.sss.client.vo;

import java.util.ArrayList;
import java.util.List;

public class Volunteer {
	public final String name;
	public final List<Assignment> assignmentList = new ArrayList<>();

	public Volunteer(String name) {
		this.name = name;
	}
}

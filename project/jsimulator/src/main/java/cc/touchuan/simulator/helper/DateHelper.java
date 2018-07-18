package cc.touchuan.simulator.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

	public static String fYmdhmsms(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
		return sdf.format(date);
		
	}
}

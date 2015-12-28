package foo.crawler;

import java.util.Date;
import java.util.Random;

import org.joda.time.DateTime;

/**
 * �üƲ��ͤ�����ؤl
 * @author phil
 */
public class DateSeed {

	private DateTime start;
	private DateTime end;

	/**
	 * �üƺؤl���϶�
	 * @param start
	 * @param end
	 */
	public DateSeed(DateTime start, DateTime end) {
		this.start = new DateTime(start);
		this.end = new DateTime(end);
	}

	/**
	 * ���ͤ@�Ӷüƪ����
	 * @return
	 */
	public synchronized Date next() {
		Random r = new Random();
		long randomTS = (long) (r.nextDouble() * (end.getMillis() - start
				.getMillis()))
				+ start.getMillis();

		return new Date(randomTS);

	}

}
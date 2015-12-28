package foo.crawler;

import java.util.Date;
import java.util.Random;

import org.joda.time.DateTime;

/**
 * 亂數產生日期的種子
 * @author phil
 */
public class DateSeed {

	private DateTime start;
	private DateTime end;

	/**
	 * 亂數種子的區間
	 * @param start
	 * @param end
	 */
	public DateSeed(DateTime start, DateTime end) {
		this.start = new DateTime(start);
		this.end = new DateTime(end);
	}

	/**
	 * 產生一個亂數的日期
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
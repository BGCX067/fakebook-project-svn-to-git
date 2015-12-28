package foo.crawler;


/**
 * 亂數種子
 * @author phil
 */
public class Seed {

	private int max;

	/**
	 * @param max 最大亂數範圍
	 */
	public Seed(int max) {
		this.max = max;
	}

	/**
	 * 取得亂數
	 * @return
	 */
	public synchronized int next() {
		return (int) (Math.random() * max);
	}

}
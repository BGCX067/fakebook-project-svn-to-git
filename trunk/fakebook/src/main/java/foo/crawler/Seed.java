package foo.crawler;


/**
 * �üƺؤl
 * @author phil
 */
public class Seed {

	private int max;

	/**
	 * @param max �̤j�üƽd��
	 */
	public Seed(int max) {
		this.max = max;
	}

	/**
	 * ���o�ü�
	 * @return
	 */
	public synchronized int next() {
		return (int) (Math.random() * max);
	}

}
import java.time.LocalDateTime;

/**
 * WŒvŒ‹‰Ê‚ğŠi”[‚·‚éƒhƒƒCƒ“.
 * @author takahiro.araki
 *
 */
public class ResultDomain {
	
	/** */
	private Object time;
	/**500msˆÈ‰º‚ÌŒ‹‰Ê*/
	private Integer under500;
	/**2000msˆÈ‰º‚ÌŒ‹‰Ê*/
	private Integer under2000;
	/**2000msˆÈã‚ÌŒ‹‰Ê */
	private Integer over2000;
	/**•½‹Ï‰“šŠÔ */
	private Integer averageTime;
	
	public Integer getUnder500() {
		return under500;
	}
	public void setUnder500(Integer under500) {
		this.under500 = under500;
	}
	public Integer getUnder2000() {
		return under2000;
	}
	public void setUnder2000(Integer under2000) {
		this.under2000 = under2000;
	}
	public Integer getOver2000() {
		return over2000;
	}
	public void setOver2000(Integer over2000) {
		this.over2000 = over2000;
	}
	public Integer getAverageTime() {
		return averageTime;
	}
	public void setAverageTime(Integer averageTime) {
		this.averageTime = averageTime;
	}
	public Object getTime() {
		return time;
	}
	public void setTime(Object time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "ResultDomain [time=" + time + ", under500=" + under500 + ", under2000=" + under2000 + ", over2000="
				+ over2000 + ", averageTime=" + averageTime + "]";
	}
	
	
	
	
	
	
}

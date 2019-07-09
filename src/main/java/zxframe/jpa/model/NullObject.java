package zxframe.jpa.model;

import java.io.Serializable;

/**
 * 防止缓存击穿使用
 * @author zx
 *
 */
public class NullObject implements Serializable{
	private static final long serialVersionUID = 1L;
	private String zxframe="nullObject";
	public String getZxframe() {
		return zxframe;
	}
	public void setZxframe(String zxframe) {
		this.zxframe = zxframe;
	}
}

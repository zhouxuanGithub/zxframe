package zxframe.jpa.model;

import javax.sql.DataSource;

public class RDataSourceModel {
	private int id;
	private DataSource dataSource;
	private int status=0;//0状态可用 1正在确认 2状态不可用
	private long dtime=0;//开始熔断时间戳
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getDtime() {
		return dtime;
	}
	public void setDtime(long dtime) {
		this.dtime = dtime;
	}
}

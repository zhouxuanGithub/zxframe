/**
 * ZxFrame Java Library
 * https://github.com/zhouxuanGithub/zxframe
 *
 * Copyright (c) 2019 zhouxuan
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
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

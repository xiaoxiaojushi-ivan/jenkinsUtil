package com.ndpmedia.comm.vo;

import java.io.Serializable;

public class CommonResultVO<T> extends BaseResultVO implements Serializable {
	private static final long serialVersionUID = -7978635757653362784L;

	// 返回数据
	private T data;
	private int total;
	private PageDTO pageInfo;
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public PageDTO getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageDTO pageInfo) {
		this.pageInfo = pageInfo;
	}

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
	
	
}

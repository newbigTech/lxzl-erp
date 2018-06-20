package com.lxzl.erp.dataaccess.domain.k3;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import java.util.Date;


public class K3StatementDateChangeDO  extends BaseDO {

	private Integer id;
	private String orderNo;
	private Date statementDateChangeTime;
	private Integer beforeStatementDate;
	private Integer afterStatementDate;
	private Integer changeType;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public String getOrderNo(){
		return orderNo;
	}

	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}

	public Date getStatementDateChangeTime(){
		return statementDateChangeTime;
	}

	public void setStatementDateChangeTime(Date statementDateChangeTime){
		this.statementDateChangeTime = statementDateChangeTime;
	}

	public Integer getBeforeStatementDate(){
		return beforeStatementDate;
	}

	public void setBeforeStatementDate(Integer beforeStatementDate){
		this.beforeStatementDate = beforeStatementDate;
	}

	public Integer getAfterStatementDate(){
		return afterStatementDate;
	}

	public void setAfterStatementDate(Integer afterStatementDate){
		this.afterStatementDate = afterStatementDate;
	}

	public Integer getChangeType() {
		return changeType;
	}

	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}
}
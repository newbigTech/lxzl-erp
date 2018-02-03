package com.lxzl.erp.common.domain.statementOrderCorrect.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.BaseCommitParam;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.statement.pojo.StatementOrderDetail;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.CancelGroup;
import com.lxzl.erp.common.domain.validGroup.QueryGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.domain.validGroup.statementOrderCorrect.CommitGroup;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrderCorrect extends BaseCommitParam {

	private Integer statementOrderCorrectId;   //唯一标识
	@NotNull(message = ErrorCode.STATEMENT_ORDER_CORRECT_NO_NOT_NULL , groups = {CommitGroup.class, UpdateGroup.class,CancelGroup.class,QueryGroup.class})
	private String statementCorrectNo;   //冲正单号
	private Integer statementOrderId;   //结算单ID
	@Min(value = 1, message = ErrorCode.COUNT_MORE_THAN_ZERO, groups = {AddGroup.class, UpdateGroup.class})
	@NotNull(message = ErrorCode.STATEMENT_ORDER_CORRECT_AMOUNT_NOT_NULL , groups = {AddGroup.class, UpdateGroup.class})
	private BigDecimal statementCorrectAmount;   //冲正金额
	@NotNull(message = ErrorCode.STATEMENT_ORDER_DETAIL_ID_NOT_NULL , groups = {AddGroup.class, UpdateGroup.class})
	private Integer statementOrderDetailId;   //结算单项ID
	private String statementCorrectReason;   //冲正原因
	private Integer statementOrderCorrectStatus;   //结算冲正单状态，0-待提交，1-审核中，2-审核通过（待冲正），3-冲正成功，4-冲正失败，5-取消冲正
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private Date statementCorrectSuccessTime;   //冲正成功时间
	private String statementCorrectFailReason;   //冲正失败原因（建议格式为 错误代码:错误描述）

	private StatementOrderDetail statementOrderDetail;   //结算单详情

	public Integer getStatementOrderCorrectId(){
		return statementOrderCorrectId;
	}

	public void setStatementOrderCorrectId(Integer statementOrderCorrectId){
		this.statementOrderCorrectId = statementOrderCorrectId;
	}

	public String getStatementCorrectNo(){
		return statementCorrectNo;
	}

	public void setStatementCorrectNo(String statementCorrectNo){
		this.statementCorrectNo = statementCorrectNo;
	}

	public Integer getStatementOrderId(){
		return statementOrderId;
	}

	public void setStatementOrderId(Integer statementOrderId){
		this.statementOrderId = statementOrderId;
	}

	public BigDecimal getStatementCorrectAmount(){
		return statementCorrectAmount;
	}

	public void setStatementCorrectAmount(BigDecimal statementCorrectAmount){
		this.statementCorrectAmount = statementCorrectAmount;
	}

	public String getStatementCorrectReason(){
		return statementCorrectReason;
	}

	public void setStatementCorrectReason(String statementCorrectReason){
		this.statementCorrectReason = statementCorrectReason;
	}

	public Integer getStatementOrderCorrectStatus(){
		return statementOrderCorrectStatus;
	}

	public void setStatementOrderCorrectStatus(Integer statementOrderCorrectStatus){
		this.statementOrderCorrectStatus = statementOrderCorrectStatus;
	}

	public Integer getDataStatus(){
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus){
		this.dataStatus = dataStatus;
	}

	public String getRemark(){
		return remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public String getCreateUser(){
		return createUser;
	}

	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public String getUpdateUser(){
		return updateUser;
	}

	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
	}

	public Date getStatementCorrectSuccessTime(){
		return statementCorrectSuccessTime;
	}

	public void setStatementCorrectSuccessTime(Date statementCorrectSuccessTime){
		this.statementCorrectSuccessTime = statementCorrectSuccessTime;
	}

	public String getStatementCorrectFailReason(){
		return statementCorrectFailReason;
	}

	public void setStatementCorrectFailReason(String statementCorrectFailReason){
		this.statementCorrectFailReason = statementCorrectFailReason;
	}

	public Integer getStatementOrderDetailId() {
		return statementOrderDetailId;
	}

	public void setStatementOrderDetailId(Integer statementOrderDetailId) {
		this.statementOrderDetailId = statementOrderDetailId;
	}

	public StatementOrderDetail getStatementOrderDetail() {
		return statementOrderDetail;
	}

	public void setStatementOrderDetail(StatementOrderDetail statementOrderDetail) {
		this.statementOrderDetail = statementOrderDetail;
	}
}
package com.lxzl.erp.common.domain.customer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.ExtendGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerRiskManagement implements Serializable {

	@NotNull(message = ErrorCode.CUSTOMER_RISK_MANAGEMENT_ID_NOT_NULL , groups = {IdGroup.class,UpdateGroup.class})
	private Integer customerRiskManagementId;   //唯一标识

	private Integer customerId;
	@NotEmpty(message = ErrorCode.CUSTOMER_NO_NOT_NULL , groups = {AddGroup.class,ExtendGroup.class})
	private String customerNo;   //用户编号
	@NotNull(message = ErrorCode.CUSTOMER_RISK_MANAGEMENT_CREDIT_AMOUNT_NOT_NULL , groups = {AddGroup.class})
	private BigDecimal creditAmount;   //授信额度
	private BigDecimal creditAmountUsed;   //已用授信额度
	@NotNull(message = ErrorCode.CUSTOMER_RISK_MANAGEMENT_DEPOSIT_CYCLE_NOT_NULL , groups = {AddGroup.class})
	private Integer depositCycle;   //押金期数
	@NotNull(message = ErrorCode.CUSTOMER_RISK_MANAGEMENT_PAYMENT_CYCLE_NOT_NULL , groups = {AddGroup.class})
	private Integer paymentCycle;   //付款期数
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人


	private String customerName;

	public Integer getCustomerRiskManagementId(){
		return customerRiskManagementId;
	}

	public void setCustomerRiskManagementId(Integer customerRiskManagementId){
		this.customerRiskManagementId = customerRiskManagementId;
	}

	public Integer getCustomerId(){
		return customerId;
	}

	public void setCustomerId(Integer customerId){
		this.customerId = customerId;
	}

	public BigDecimal getCreditAmount(){
		return creditAmount;
	}

	public void setCreditAmount(BigDecimal creditAmount){
		this.creditAmount = creditAmount;
	}

	public BigDecimal getCreditAmountUsed(){
		return creditAmountUsed;
	}

	public void setCreditAmountUsed(BigDecimal creditAmountUsed){
		this.creditAmountUsed = creditAmountUsed;
	}

	public Integer getDepositCycle(){
		return depositCycle;
	}

	public void setDepositCycle(Integer depositCycle){
		this.depositCycle = depositCycle;
	}

	public Integer getPaymentCycle(){
		return paymentCycle;
	}

	public void setPaymentCycle(Integer paymentCycle){
		this.paymentCycle = paymentCycle;
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

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
package com.lxzl.erp.dataaccess.domain.customer;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

public class CustomerDO  extends BaseDO {

	private Integer id;
	private Integer customerType;
	private String customerNo;
	private String customerName;
	private Integer isDisabled;
	private Integer dataStatus;
	private String remark;

	private Integer owner;		//数据归属人，跟单员
	private Integer unionUser;  //联合开发人
	private Integer customerStatus;  //客户状态，0初始化，1资料提交，2审核通过，3资料驳回


	@Transient
	private CustomerCompanyDO customerCompanyDO;
	@Transient
	private CustomerPersonDO customerPersonDO;

	private CustomerRiskManagementDO customerRiskManagementDO;

	@Transient
	private String unionUserName; //联合业务员
	@Transient
	private String ownerName; //业务员

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getCustomerType(){
		return customerType;
	}

	public void setCustomerType(Integer customerType){
		this.customerType = customerType;
	}

	public String getCustomerNo(){
		return customerNo;
	}

	public void setCustomerNo(String customerNo){
		this.customerNo = customerNo;
	}

	public Integer getIsDisabled(){
		return isDisabled;
	}

	public void setIsDisabled(Integer isDisabled){
		this.isDisabled = isDisabled;
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

	public CustomerCompanyDO getCustomerCompanyDO() {
		return customerCompanyDO;
	}

	public void setCustomerCompanyDO(CustomerCompanyDO customerCompanyDO) {
		this.customerCompanyDO = customerCompanyDO;
	}

	public CustomerPersonDO getCustomerPersonDO() {
		return customerPersonDO;
	}

	public void setCustomerPersonDO(CustomerPersonDO customerPersonDO) {
		this.customerPersonDO = customerPersonDO;
	}

	public CustomerRiskManagementDO getCustomerRiskManagementDO() {
		return customerRiskManagementDO;
	}

	public void setCustomerRiskManagementDO(CustomerRiskManagementDO customerRiskManagementDO) {
		this.customerRiskManagementDO = customerRiskManagementDO;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getOwner() {
		return owner;
	}

	public void setOwner(Integer owner) {
		this.owner = owner;
	}

	public Integer getUnionUser() {
		return unionUser;
	}

	public void setUnionUser(Integer unionUser) {
		this.unionUser = unionUser;
	}

	public String getUnionUserName() {
		return unionUserName;
	}

	public void setUnionUserName(String unionUserName) {
		this.unionUserName = unionUserName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Integer getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(Integer customerStatus) {
		this.customerStatus = customerStatus;
	}
}
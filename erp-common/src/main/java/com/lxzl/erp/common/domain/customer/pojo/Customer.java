package com.lxzl.erp.common.domain.customer.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.payment.account.pojo.CustomerAccount;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.customer.AddCustomerCompanyGroup;
import com.lxzl.erp.common.domain.validGroup.customer.AddCustomerPersonGroup;
import com.lxzl.erp.common.domain.validGroup.customer.UpdateCustomerCompanyGroup;
import com.lxzl.erp.common.domain.validGroup.customer.UpdateCustomerPersonGroup;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer extends BasePO {

	private Integer customerId;   //唯一标识
	private Integer customerType;   //用户类型,1为企业用户，2为个人用户
	@NotBlank(message = ErrorCode.CUSTOMER_NO_NOT_NULL , groups = {IdGroup.class,UpdateCustomerCompanyGroup.class,UpdateCustomerPersonGroup.class})
	private String customerNo;   //客戶编号
	private String customerName; //客户名称
	private Integer isDisabled;   //是否禁用，0不可用；1可用
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //添加时间
	private String updateUser;   //修改人
	@NotNull(message = ErrorCode.CUSTOMER_OWNER_NOT_NULL , groups = {AddCustomerCompanyGroup.class,AddCustomerPersonGroup.class,UpdateCustomerCompanyGroup.class,UpdateCustomerPersonGroup.class})
	private Integer owner;		//数据归属人，跟单员
	private Integer unionUser;  //联合开发人


	private String ownerName; //业务员姓名
	private String unionUserName; //联合业务员姓名
	private String unionAreaProvinceName;// 联合省名
	private String unionAreaCityName; //联合城市名
	private String unionAreaDistrictName; //联合地区名


	@Valid
	private CustomerPerson customerPerson;
	@Valid
	private CustomerCompany customerCompany;

	private CustomerRiskManagement customerRiskManagement;

	private CustomerAccount customerAccount;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
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

	public Integer getIsDisabled() {
		return isDisabled;
	}

	public void setIsDisabled(Integer isDisabled) {
		this.isDisabled = isDisabled;
	}

	public Integer getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(Integer dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
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

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getUnionUserName() {
		return unionUserName;
	}

	public void setUnionUserName(String unionUserName) {
		this.unionUserName = unionUserName;
	}

	public String getUnionAreaProvinceName() {
		return unionAreaProvinceName;
	}

	public void setUnionAreaProvinceName(String unionAreaProvinceName) {
		this.unionAreaProvinceName = unionAreaProvinceName;
	}

	public String getUnionAreaCityName() {
		return unionAreaCityName;
	}

	public void setUnionAreaCityName(String unionAreaCityName) {
		this.unionAreaCityName = unionAreaCityName;
	}

	public String getUnionAreaDistrictName() {
		return unionAreaDistrictName;
	}

	public void setUnionAreaDistrictName(String unionAreaDistrictName) {
		this.unionAreaDistrictName = unionAreaDistrictName;
	}

	public CustomerPerson getCustomerPerson() {
		return customerPerson;
	}

	public void setCustomerPerson(CustomerPerson customerPerson) {
		this.customerPerson = customerPerson;
	}

	public CustomerCompany getCustomerCompany() {
		return customerCompany;
	}

	public void setCustomerCompany(CustomerCompany customerCompany) {
		this.customerCompany = customerCompany;
	}

	public CustomerRiskManagement getCustomerRiskManagement() {
		return customerRiskManagement;
	}

	public void setCustomerRiskManagement(CustomerRiskManagement customerRiskManagement) {
		this.customerRiskManagement = customerRiskManagement;
	}

	public CustomerAccount getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(CustomerAccount customerAccount) {
		this.customerAccount = customerAccount;
	}
}
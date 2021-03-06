package com.lxzl.erp.dataaccess.domain.customer;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;

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
    private Date localizationTime;	//属地化时间
    private BigDecimal shortLimitReceivableAmount; //短租应收上限
    private Integer statementDate; //结算时间（天），20和31两种情况，如果为空取系统设定
	private Integer deliveryMode; //发货方式，1快递，2自提,3凌雄配送'
	private Integer ownerSubCompanyId; //业务员所属分公司ID
	private String ownerSubCompanyName; //业务员所属分公司名称

	private BigDecimal firstApplyAmount;		// 首期申请额度
	private BigDecimal laterApplyAmount;		// 后期申请额度
	private String passReason; //通过原因
	private String failReason; //拒绝原因
	private Integer isRisk;//'是否授信，0-未授信；1-已授信'

	private Integer confirmStatementStatus; // 客户结算单确认状态 0否1是
	private Integer confirmStatementUser; // 客户结算单确认人
	private Date confirmStatementTime; // 客户结算单确认时间
	private  Integer customerSource;//用户的来源(1:erp,2:大学生商城)

	private Integer confirmBadAccountStatus; // 客户是否为坏账状态 0否1是
	private Integer confirmBadAccountUser; // 客户坏账确认人
	private Date confirmBadAccountTime; // 客户坏账确认时间

	@Transient
	private CustomerCompanyDO customerCompanyDO;
	@Transient
	private CustomerPersonDO customerPersonDO;

	private CustomerRiskManagementDO customerRiskManagementDO;

	@Transient
	private String unionUserName; //联合业务员
	@Transient
	private String ownerName; //业务员
	@Transient
	private String verifyRemark;

	private Integer isDefaultConsignAddress; //是否以地址作为收货地址,1是，0否


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

	public BigDecimal getFirstApplyAmount() {
		return firstApplyAmount;
	}

	public void setFirstApplyAmount(BigDecimal firstApplyAmount) {
		this.firstApplyAmount = firstApplyAmount;
	}

	public BigDecimal getLaterApplyAmount() {
		return laterApplyAmount;
	}

	public void setLaterApplyAmount(BigDecimal laterApplyAmount) {
		this.laterApplyAmount = laterApplyAmount;
	}

    public Date getLocalizationTime() {
        return localizationTime;
    }

    public void setLocalizationTime(Date localizationTime) {
        this.localizationTime = localizationTime;
    }

    public BigDecimal getShortLimitReceivableAmount() {
        return shortLimitReceivableAmount;
    }

    public void setShortLimitReceivableAmount(BigDecimal shortLimitReceivableAmount) {
        this.shortLimitReceivableAmount = shortLimitReceivableAmount;
    }

    public Integer getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(Integer statementDate) {
        this.statementDate = statementDate;
    }

	public String getVerifyRemark() {
		return verifyRemark;
	}

	public void setVerifyRemark(String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}

	public Integer getIsDefaultConsignAddress() {
		return isDefaultConsignAddress;
	}

	public void setIsDefaultConsignAddress(Integer isDefaultConsignAddress) {
		this.isDefaultConsignAddress = isDefaultConsignAddress;
	}

	public String getPassReason() {
		return passReason;
	}

	public void setPassReason(String passReason) {
		this.passReason = passReason;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public Integer getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(Integer deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	public Integer getOwnerSubCompanyId() {
		return ownerSubCompanyId;
	}

	public void setOwnerSubCompanyId(Integer ownerSubCompanyId) {
		this.ownerSubCompanyId = ownerSubCompanyId;
	}

	public String getOwnerSubCompanyName() {
		return ownerSubCompanyName;
	}

	public void setOwnerSubCompanyName(String ownerSubCompanyName) {
		this.ownerSubCompanyName = ownerSubCompanyName;
	}

	public Integer getIsRisk() {
		return isRisk;
	}

	public void setIsRisk(Integer isRisk) {
		this.isRisk = isRisk;
	}

	public Integer getConfirmStatementStatus() {
		return confirmStatementStatus;
	}

	public void setConfirmStatementStatus(Integer confirmStatementStatus) {
		this.confirmStatementStatus = confirmStatementStatus;
	}

	public Integer getConfirmStatementUser() {
		return confirmStatementUser;
	}

	public void setConfirmStatementUser(Integer confirmStatementUser) {
		this.confirmStatementUser = confirmStatementUser;
	}

	public Date getConfirmStatementTime() {
		return confirmStatementTime;
	}

	public void setConfirmStatementTime(Date confirmStatementTime) {
		this.confirmStatementTime = confirmStatementTime;
	}

	public Integer getConfirmBadAccountStatus() {
		return confirmBadAccountStatus;
	}

	public void setConfirmBadAccountStatus(Integer confirmBadAccountStatus) {
		this.confirmBadAccountStatus = confirmBadAccountStatus;
	}

	public Integer getConfirmBadAccountUser() {
		return confirmBadAccountUser;
	}

	public void setConfirmBadAccountUser(Integer confirmBadAccountUser) {
		this.confirmBadAccountUser = confirmBadAccountUser;
	}

	public Date getConfirmBadAccountTime() {
		return confirmBadAccountTime;
	}

	public void setConfirmBadAccountTime(Date confirmBadAccountTime) {
		this.confirmBadAccountTime = confirmBadAccountTime;
	}

	public Integer getCustomerSource() {
		return customerSource;
	}

	public void setCustomerSource(Integer customerSource) {
		this.customerSource = customerSource;
	}
}
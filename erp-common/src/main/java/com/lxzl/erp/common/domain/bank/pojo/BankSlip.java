package com.lxzl.erp.common.domain.bank.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.bank.AssignGroup;
import com.lxzl.erp.common.util.validate.constraints.CollectionNotNull;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlip extends BasePO {

	@NotNull(message = ErrorCode.BANK_SLIP_ID_NULL,groups = {IdGroup.class})
	private Integer bankSlipId;   //唯一标识
	@NotNull(message = ErrorCode.SUB_COMPANY_ID_NOT_NULL,groups = {AddGroup.class})
	private Integer subCompanyId;   //分公司ID
	private String subCompanyName;   //分公司名称
	private Integer bankType;   //银行类型，1-支付宝，2-中国银行，3-交通银行，4-南京银行，5-农业银行，6-工商银行，7-建设银行，8-平安银行，9-招商银行，10-浦发银行
	@NotNull(message = ErrorCode.MONTH_IS_NOT_NULL,groups = {AddGroup.class})
	private Date slipDay;   //导入日期
	private String accountNo;   //查询账号
	private Integer inCount;   //进款笔数
	private Integer needClaimCount;   //需认领笔数
	private Integer claimCount;   //已认领笔数
	private Integer confirmCount;   //已确认笔数
	private Integer slipStatus;   //单据状态：0-初始化，1-已下推，2-部分认领，3-全部认领
	@NotEmpty(message = ErrorCode.EXCEL_URL_IS_NOT_NULL,groups = {AddGroup.class})
	private String excelUrl;   //表格URL
	private Integer dataStatus;   //状态：0不可用；1可用；2删除
	private String remark;   //备注
	private Date createTime;   //添加时间
	private String createUser;   //添加人
	private Date updateTime;   //修改时间
	private String updateUser;   //修改人
	private Integer localizationCount;  //属地化数量
	@NotNull(message = ErrorCode.SUB_COMPANY_ID_NOT_NULL,groups = {AssignGroup.class})
	private Integer localizationSubCompanyId;  //属地化公司id

	@Valid
	@CollectionNotNull(message = ErrorCode.RECORD_NOT_EXISTS,groups = {AssignGroup.class})
	private List<BankSlipDetail> bankSlipDetailList;

	public Integer getLocalizationCount() {
		return localizationCount;
	}

	public void setLocalizationCount(Integer localizationCount) {
		this.localizationCount = localizationCount;
	}

	public List<BankSlipDetail> getBankSlipDetailList() {
		return bankSlipDetailList;
	}

	public void setBankSlipDetailList(List<BankSlipDetail> bankSlipDetailList) {
		this.bankSlipDetailList = bankSlipDetailList;
	}

	public Integer getLocalizationSubCompanyId() {
		return localizationSubCompanyId;
	}

	public void setLocalizationSubCompanyId(Integer localizationSubCompanyId) {
		this.localizationSubCompanyId = localizationSubCompanyId;
	}

	public Integer getBankSlipId(){
		return bankSlipId;
	}

	public void setBankSlipId(Integer bankSlipId){
		this.bankSlipId = bankSlipId;
	}

	public Integer getSubCompanyId(){
		return subCompanyId;
	}

	public void setSubCompanyId(Integer subCompanyId){
		this.subCompanyId = subCompanyId;
	}

	public String getSubCompanyName(){
		return subCompanyName;
	}

	public void setSubCompanyName(String subCompanyName){
		this.subCompanyName = subCompanyName;
	}

	public Integer getBankType(){
		return bankType;
	}

	public void setBankType(Integer bankType){
		this.bankType = bankType;
	}

	public Date getSlipDay(){
		return slipDay;
	}

	public void setSlipDay(Date slipDay){
		this.slipDay = slipDay;
	}

	public String getAccountNo(){
		return accountNo;
	}

	public void setAccountNo(String accountNo){
		this.accountNo = accountNo;
	}

	public Integer getInCount(){
		return inCount;
	}

	public void setInCount(Integer inCount){
		this.inCount = inCount;
	}

	public Integer getNeedClaimCount(){
		return needClaimCount;
	}

	public void setNeedClaimCount(Integer needClaimCount){
		this.needClaimCount = needClaimCount;
	}

	public Integer getClaimCount(){
		return claimCount;
	}

	public void setClaimCount(Integer claimCount){
		this.claimCount = claimCount;
	}

	public Integer getConfirmCount(){
		return confirmCount;
	}

	public void setConfirmCount(Integer confirmCount){
		this.confirmCount = confirmCount;
	}

	public Integer getSlipStatus(){
		return slipStatus;
	}

	public void setSlipStatus(Integer slipStatus){
		this.slipStatus = slipStatus;
	}

	public String getExcelUrl(){
		return excelUrl;
	}

	public void setExcelUrl(String excelUrl){
		this.excelUrl = excelUrl;
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

}
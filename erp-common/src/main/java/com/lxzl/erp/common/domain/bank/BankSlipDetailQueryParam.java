package com.lxzl.erp.common.domain.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePageParam;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: your name
 * @Description：
 * @Date: Created in 21:08 2018/3/20
 * @Modified By:
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlipDetailQueryParam extends BasePageParam {
    private Integer bankSlipId;   //对公流水单ID
    private String payerName;   //付款人名称
    private Integer loanSign;   //借贷标志,1-贷（收入），2-借（支出）
    private Integer detailStatus;   //明细状态，1-未认领，2-已认领，3-已确定，4-忽略
    private Integer isLocalization;   //'是否已属地化,0-否，1-是[总公司时有值]',
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date slipDayStart;   //导入开始时间日期
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date slipDayEnd;   //导入结束时间日期
    private String otherSideAccountNo;   //对方账号
    private String tradeSerialNo;   //交易流水号
    private Integer ownerSubCompanyId; //数据归属化公司
    private Integer subCompanyId; //属地化分公司
    private BigDecimal tradeAmountStart; //查询起始交易金额
    private BigDecimal tradeAmountEnd; //查询结束交易金额
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date tradeTimeStart;   //交易开始时间
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date tradeTimeEnd;   //交易结束时间

    public Date getTradeTimeStart() {
        return tradeTimeStart;
    }

    public void setTradeTimeStart(Date tradeTimeStart) {
        this.tradeTimeStart = tradeTimeStart;
    }

    public Date getTradeTimeEnd() {
        return tradeTimeEnd;
    }

    public void setTradeTimeEnd(Date tradeTimeEnd) {
        this.tradeTimeEnd = tradeTimeEnd;
    }

    public BigDecimal getTradeAmountStart() {
        return tradeAmountStart;
    }

    public void setTradeAmountStart(BigDecimal tradeAmountStart) {
        this.tradeAmountStart = tradeAmountStart;
    }

    public BigDecimal getTradeAmountEnd() {
        return tradeAmountEnd;
    }

    public void setTradeAmountEnd(BigDecimal tradeAmountEnd) {
        this.tradeAmountEnd = tradeAmountEnd;
    }

    public Integer getOwnerSubCompanyId() {
        return ownerSubCompanyId;
    }

    public void setOwnerSubCompanyId(Integer ownerSubCompanyId) {
        this.ownerSubCompanyId = ownerSubCompanyId;
    }

    public Integer getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(Integer subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public Integer getBankSlipId() {
        return bankSlipId;
    }

    public void setBankSlipId(Integer bankSlipId) {
        this.bankSlipId = bankSlipId;
    }

    public String getTradeSerialNo() {
        return tradeSerialNo;
    }

    public void setTradeSerialNo(String tradeSerialNo) {
        this.tradeSerialNo = tradeSerialNo;
    }

    public String getOtherSideAccountNo() {
        return otherSideAccountNo;
    }

    public void setOtherSideAccountNo(String otherSideAccountNo) {
        this.otherSideAccountNo = otherSideAccountNo;
    }

    public Date getSlipDayStart() {
        return slipDayStart;
    }

    public void setSlipDayStart(Date slipDayStart) {
        this.slipDayStart = slipDayStart;
    }

    public Date getSlipDayEnd() {
        return slipDayEnd;
    }

    public void setSlipDayEnd(Date slipDayEnd) {
        this.slipDayEnd = slipDayEnd;
    }

    public Integer getIsLocalization() {
        return isLocalization;
    }

    public void setIsLocalization(Integer isLocalization) {
        this.isLocalization = isLocalization;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public Integer getLoanSign() {
        return loanSign;
    }

    public void setLoanSign(Integer loanSign) {
        this.loanSign = loanSign;
    }

    public Integer getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(Integer detailStatus) {
        this.detailStatus = detailStatus;
    }
}

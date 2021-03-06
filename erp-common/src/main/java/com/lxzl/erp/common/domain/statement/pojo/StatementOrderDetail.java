package com.lxzl.erp.common.domain.statement.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.util.BigDecimalUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StatementOrderDetail extends BasePO {

    private Integer statementOrderDetailId;   //唯一标识
    private Integer statementOrderId;   //结算单ID
    private Integer customerId;   //客户ID
    private Integer orderType;    // 单子类型，详见ORDER_TYPE
    private Integer orderId;   //订单ID
    private Integer orderItemType;        // 订单项类型，1为商品，2为物料
    private Integer orderItemReferId;    // 订单项关联ID
    private Integer returnReferId;     // 退款的时候，关联的结算单detail ID
    private Integer statementDetailType;        // 结算单明细类型
    private Integer statementDetailPhase;   // 结算单期数
    private Date statementExpectPayTime;    // 结算单预计支付时间
    private BigDecimal statementDetailAmount;   //结算单金额
    private BigDecimal statementDetailOtherAmount;   //结算单其他金额
    private BigDecimal statementDetailOtherPaidAmount;
    private BigDecimal statementDetailPaidAmount;    // 已付总金额
    private BigDecimal statementDetailRentDepositAmount;    // 结算租金押金金额
    private BigDecimal statementDetailRentDepositPaidAmount;    // 已付租金押金金额
    private BigDecimal statementDetailRentDepositReturnAmount;    // 退还租金押金金额
    private Date statementDetailRentDepositReturnTime;      // 退还租金押金时间
    private BigDecimal statementDetailDepositAmount;   //结算押金金额
    private BigDecimal statementDetailDepositPaidAmount;   //已付押金金额
    private BigDecimal statementDetailDepositReturnAmount;   //退还押金金额
    private Date statementDetailDepositReturnTime;      // 退还押金时间
    private BigDecimal statementDetailRentAmount;                // 结算单租金金额
    private BigDecimal statementDetailRentPaidAmount;            // 租金已付金额
    private Date statementDetailPaidTime;            // 结算单支付时间
    private BigDecimal statementDetailOverdueAmount;    // 逾期金额
    private BigDecimal statementDetailOverduePaidAmount;
    private Integer statementDetailOverdueDays;
    private Integer statementDetailOverduePhaseCount;
    private BigDecimal statementDetailPenaltyAmount;//违约金
    private BigDecimal statementDetailPenaltyPaidAmount;//已付违约金
    private Integer statementDetailStatus;   //结算状态，0未结算，1已结算
    private Date statementStartTime;   //结算开始时间
    private Date statementEndTime;   //结算结束时间
    private BigDecimal statementDetailCorrectAmount;  //结算单冲正金额
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //添加时间
    private String updateUser;   //修改人
    private BigDecimal statementCouponAmount;   //结算单优惠券优惠总和

    private Integer reletOrderItemReferId;  //续租订单项ID  查询时结算单关联续租
    private Integer sourceId;//次结算单关联目标源id

    private String orderNo;
    private String itemName;
    private Integer itemCount;
    private BigDecimal unitAmount;
    private Integer itemRentType;
    private String statementOrderNo;  //结算单编号
    private List<Integer> mergeStatementItemIdList;//详情合并的结算详情id

    public Integer getStatementOrderDetailId() {
        return statementOrderDetailId;
    }

    public void setStatementOrderDetailId(Integer statementOrderDetailId) {
        this.statementOrderDetailId = statementOrderDetailId;
    }

    public Integer getStatementOrderId() {
        return statementOrderId;
    }

    public void setStatementOrderId(Integer statementOrderId) {
        this.statementOrderId = statementOrderId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getStatementDetailAmount() {
        return statementDetailAmount;
    }

    public void setStatementDetailAmount(BigDecimal statementDetailAmount) {
        this.statementDetailAmount = statementDetailAmount;
    }

    public Integer getStatementDetailStatus() {
        return statementDetailStatus;
    }

    public void setStatementDetailStatus(Integer statementDetailStatus) {
        this.statementDetailStatus = statementDetailStatus;
    }

    public Date getStatementStartTime() {
        return statementStartTime;
    }

    public void setStatementStartTime(Date statementStartTime) {
        this.statementStartTime = statementStartTime;
    }

    public Date getStatementEndTime() {
        return statementEndTime;
    }

    public void setStatementEndTime(Date statementEndTime) {
        this.statementEndTime = statementEndTime;
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

    public Date getStatementExpectPayTime() {
        return statementExpectPayTime;
    }

    public void setStatementExpectPayTime(Date statementExpectPayTime) {
        this.statementExpectPayTime = statementExpectPayTime;
    }

    public BigDecimal getStatementDetailRentDepositAmount() {
        return statementDetailRentDepositAmount;
    }

    public void setStatementDetailRentDepositAmount(BigDecimal statementDetailRentDepositAmount) {
        this.statementDetailRentDepositAmount = statementDetailRentDepositAmount;
    }

    public BigDecimal getStatementDetailRentDepositPaidAmount() {
        return statementDetailRentDepositPaidAmount;
    }

    public void setStatementDetailRentDepositPaidAmount(BigDecimal statementDetailRentDepositPaidAmount) {
        this.statementDetailRentDepositPaidAmount = statementDetailRentDepositPaidAmount;
    }

    public BigDecimal getStatementDetailRentDepositReturnAmount() {
        return statementDetailRentDepositReturnAmount;
    }

    public void setStatementDetailRentDepositReturnAmount(BigDecimal statementDetailRentDepositReturnAmount) {
        this.statementDetailRentDepositReturnAmount = statementDetailRentDepositReturnAmount;
    }

    public BigDecimal getStatementDetailRentAmount() {
        return statementDetailRentAmount;
    }

    public void setStatementDetailRentAmount(BigDecimal statementDetailRentAmount) {
        this.statementDetailRentAmount = statementDetailRentAmount;
    }

    public BigDecimal getStatementDetailRentPaidAmount() {
        return statementDetailRentPaidAmount;
    }

    public void setStatementDetailRentPaidAmount(BigDecimal statementDetailRentPaidAmount) {
        this.statementDetailRentPaidAmount = statementDetailRentPaidAmount;
    }

    public BigDecimal getStatementDetailOverdueAmount() {
        return statementDetailOverdueAmount;
    }

    public void setStatementDetailOverdueAmount(BigDecimal statementDetailOverdueAmount) {
        this.statementDetailOverdueAmount = statementDetailOverdueAmount;
    }

    public Integer getOrderItemType() {
        return orderItemType;
    }

    public void setOrderItemType(Integer orderItemType) {
        this.orderItemType = orderItemType;
    }

    public Integer getOrderItemReferId() {
        return orderItemReferId;
    }

    public void setOrderItemReferId(Integer orderItemReferId) {
        this.orderItemReferId = orderItemReferId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Date getStatementDetailPaidTime() {
        return statementDetailPaidTime;
    }

    public void setStatementDetailPaidTime(Date statementDetailPaidTime) {
        this.statementDetailPaidTime = statementDetailPaidTime;
    }

    public BigDecimal getStatementDetailDepositAmount() {
        return statementDetailDepositAmount;
    }

    public void setStatementDetailDepositAmount(BigDecimal statementDetailDepositAmount) {
        this.statementDetailDepositAmount = statementDetailDepositAmount;
    }

    public BigDecimal getStatementDetailDepositPaidAmount() {
        return statementDetailDepositPaidAmount;
    }

    public void setStatementDetailDepositPaidAmount(BigDecimal statementDetailDepositPaidAmount) {
        this.statementDetailDepositPaidAmount = statementDetailDepositPaidAmount;
    }

    public BigDecimal getStatementDetailDepositReturnAmount() {
        return statementDetailDepositReturnAmount;
    }

    public void setStatementDetailDepositReturnAmount(BigDecimal statementDetailDepositReturnAmount) {
        this.statementDetailDepositReturnAmount = statementDetailDepositReturnAmount;
    }

    public BigDecimal getStatementDetailPaidAmount() {
        return statementDetailPaidAmount == null || BigDecimal.ZERO.equals(statementDetailPaidAmount) ? BigDecimalUtil.add(BigDecimalUtil.add(BigDecimalUtil.add(statementDetailRentPaidAmount, statementDetailDepositPaidAmount), statementDetailRentDepositPaidAmount), statementDetailOtherPaidAmount) : statementDetailPaidAmount;
    }

    public void setStatementDetailPaidAmount(BigDecimal statementDetailPaidAmount) {
        this.statementDetailPaidAmount = statementDetailPaidAmount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public BigDecimal getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(BigDecimal unitAmount) {
        this.unitAmount = unitAmount;
    }

    public Integer getStatementDetailPhase() {
        return statementDetailPhase;
    }

    public void setStatementDetailPhase(Integer statementDetailPhase) {
        this.statementDetailPhase = statementDetailPhase;
    }

    public Integer getItemRentType() {
        return itemRentType;
    }

    public void setItemRentType(Integer itemRentType) {
        this.itemRentType = itemRentType;
    }

    public Date getStatementDetailRentDepositReturnTime() {
        return statementDetailRentDepositReturnTime;
    }

    public void setStatementDetailRentDepositReturnTime(Date statementDetailRentDepositReturnTime) {
        this.statementDetailRentDepositReturnTime = statementDetailRentDepositReturnTime;
    }

    public Date getStatementDetailDepositReturnTime() {
        return statementDetailDepositReturnTime;
    }

    public void setStatementDetailDepositReturnTime(Date statementDetailDepositReturnTime) {
        this.statementDetailDepositReturnTime = statementDetailDepositReturnTime;
    }

    public BigDecimal getStatementDetailOtherAmount() {
        return statementDetailOtherAmount;
    }

    public void setStatementDetailOtherAmount(BigDecimal statementDetailOtherAmount) {
        this.statementDetailOtherAmount = statementDetailOtherAmount;
    }

    public Integer getStatementDetailType() {
        return statementDetailType;
    }

    public void setStatementDetailType(Integer statementDetailType) {
        this.statementDetailType = statementDetailType;
    }

    public BigDecimal getStatementDetailOtherPaidAmount() {
        return statementDetailOtherPaidAmount;
    }

    public void setStatementDetailOtherPaidAmount(BigDecimal statementDetailOtherPaidAmount) {
        this.statementDetailOtherPaidAmount = statementDetailOtherPaidAmount;
    }

    public Integer getStatementDetailOverdueDays() {
        return statementDetailOverdueDays;
    }

    public void setStatementDetailOverdueDays(Integer statementDetailOverdueDays) {
        this.statementDetailOverdueDays = statementDetailOverdueDays;
    }

    public Integer getStatementDetailOverduePhaseCount() {
        return statementDetailOverduePhaseCount;
    }

    public void setStatementDetailOverduePhaseCount(Integer statementDetailOverduePhaseCount) {
        this.statementDetailOverduePhaseCount = statementDetailOverduePhaseCount;
    }

    public Integer getReturnReferId() {
        return returnReferId;
    }

    public void setReturnReferId(Integer returnReferId) {
        this.returnReferId = returnReferId;
    }

    public BigDecimal getStatementDetailCorrectAmount() {
        return statementDetailCorrectAmount;
    }

    public void setStatementDetailCorrectAmount(BigDecimal statementDetailCorrectAmount) {
        this.statementDetailCorrectAmount = statementDetailCorrectAmount;
    }

    public BigDecimal getStatementDetailOverduePaidAmount() {
        return statementDetailOverduePaidAmount;
    }

    public void setStatementDetailOverduePaidAmount(BigDecimal statementDetailOverduePaidAmount) {
        this.statementDetailOverduePaidAmount = statementDetailOverduePaidAmount;
    }

    public BigDecimal getStatementCouponAmount() {
        return statementCouponAmount;
    }

    public void setStatementCouponAmount(BigDecimal statementCouponAmount) {
        this.statementCouponAmount = statementCouponAmount;
    }

    public BigDecimal getStatementDetailPenaltyAmount() {
        return statementDetailPenaltyAmount;
    }

    public void setStatementDetailPenaltyAmount(BigDecimal statementDetailPenaltyAmount) {
        this.statementDetailPenaltyAmount = statementDetailPenaltyAmount;
    }

    public BigDecimal getStatementDetailPenaltyPaidAmount() {
        return statementDetailPenaltyPaidAmount;
    }

    public void setStatementDetailPenaltyPaidAmount(BigDecimal statementDetailPenaltyPaidAmount) {
        this.statementDetailPenaltyPaidAmount = statementDetailPenaltyPaidAmount;
    }

    public Integer getReletOrderItemReferId() {
        return reletOrderItemReferId;
    }

    public void setReletOrderItemReferId(Integer reletOrderItemReferId) {
        this.reletOrderItemReferId = reletOrderItemReferId;
    }

    public String getStatementOrderNo() {
        return statementOrderNo;
    }

    public void setStatementOrderNo(String statementOrderNo) {
        this.statementOrderNo = statementOrderNo;
    }

    public List<Integer> getMergeStatementItemIdList() {
        return mergeStatementItemIdList;
    }

    public void setMergeStatementItemIdList(List<Integer> mergeStatementItemIdList) {
        this.mergeStatementItemIdList = mergeStatementItemIdList;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }
}
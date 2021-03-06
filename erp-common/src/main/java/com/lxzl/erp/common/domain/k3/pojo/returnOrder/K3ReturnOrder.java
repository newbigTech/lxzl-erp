package com.lxzl.erp.common.domain.k3.pojo.returnOrder;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.k3.group.K3ReturnOrderCallback;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.util.validate.constraints.CollectionNotNull;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class K3ReturnOrder extends BasePO {

    private Integer k3ReturnOrderId;   //唯一标识
    @NotBlank(message = ErrorCode.RETURN_ORDER_NO_NOT_NULL, groups = {IdGroup.class, K3ReturnOrderCallback.class, UpdateGroup.class})
    private String returnOrderNo;   //退还编号
    @NotBlank(message = ErrorCode.K3_CUSTOMER_NO_IS_NULL, groups = {AddGroup.class})
    private String k3CustomerNo;   //K3客户编码
    private String k3CustomerName;   //K3客户名称
    @NotNull(message = ErrorCode.RETURN_TIME_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
    private Date returnTime;   //退货时间
    @NotBlank(message = ErrorCode.K3_RETURN_ADDRESS_IS_NULL, groups = {AddGroup.class, UpdateGroup.class})
    private String returnAddress;   //退货地址
    @NotBlank(message = ErrorCode.K3_RETURN_CONTACTS_IS_NULL, groups = {AddGroup.class, UpdateGroup.class})
    private String returnContacts;   //联系人
    @NotBlank(message = ErrorCode.K3_RETURN_PHONE_IS_NULL, groups = {AddGroup.class, UpdateGroup.class})
    private String returnPhone;   //联系电话
    @NotNull(message = ErrorCode.K3_RETURN_MODE_IS_NULL, groups = {AddGroup.class, UpdateGroup.class})
    private Integer returnMode;   //退还方式，1-上门取件，2邮寄
    private Integer returnOrderStatus;   // 归还订单状态，0-待提交，4-审核中，16-已取消，20-已完成，24已驳回
    private BigDecimal logisticsAmount;    // 运费
    private BigDecimal serviceAmount;    // 服务费
    @Min(value = 1, message = ErrorCode.K3_RETURN_REASON_TYPE_ERROR, groups = {AddGroup.class, UpdateGroup.class})
    @Max(value = 20, message = ErrorCode.K3_RETURN_REASON_TYPE_ERROR, groups = {AddGroup.class, UpdateGroup.class})
    private Integer returnReasonType;    // 退货原因
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //修改时间
    private String updateUser;   //修改人

    private BigDecimal eqAmount;   //需恢复的信用额度
    @NotNull(message = ErrorCode.DELIVERY_COMPANY_ID_NULL, groups = {AddGroup.class, UpdateGroup.class})
    private Integer deliverySubCompanyId;//发货分公司id
    private String deliverySubCompanyName;//发货分公司
    private Integer successStatus;//处理成功状态---1处理成功 0 未处理成功
    @Valid
    @CollectionNotNull(message = ErrorCode.RETURN_DETAIL_LIST_NOT_NULL, groups = {K3ReturnOrderCallback.class, AddGroup.class})
    private List<K3ReturnOrderDetail> k3ReturnOrderDetailList;

    public Integer getReturnReasonType() {
        return returnReasonType;
    }

    public void setReturnReasonType(Integer returnReasonType) {
        this.returnReasonType = returnReasonType;
    }

    public Integer getK3ReturnOrderId() {
        return k3ReturnOrderId;
    }

    public void setK3ReturnOrderId(Integer k3ReturnOrderId) {
        this.k3ReturnOrderId = k3ReturnOrderId;
    }

    public String getReturnOrderNo() {
        return returnOrderNo;
    }

    public void setReturnOrderNo(String returnOrderNo) {
        this.returnOrderNo = returnOrderNo;
    }

    public String getK3CustomerNo() {
        return k3CustomerNo;
    }

    public void setK3CustomerNo(String k3CustomerNo) {
        this.k3CustomerNo = k3CustomerNo;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

    public String getReturnContacts() {
        return returnContacts;
    }

    public void setReturnContacts(String returnContacts) {
        this.returnContacts = returnContacts;
    }

    public String getReturnPhone() {
        return returnPhone;
    }

    public void setReturnPhone(String returnPhone) {
        this.returnPhone = returnPhone;
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

    public List<K3ReturnOrderDetail> getK3ReturnOrderDetailList() {
        return k3ReturnOrderDetailList;
    }

    public void setK3ReturnOrderDetailList(List<K3ReturnOrderDetail> k3ReturnOrderDetailList) {
        this.k3ReturnOrderDetailList = k3ReturnOrderDetailList;
    }

    public String getK3CustomerName() {
        return k3CustomerName;
    }

    public void setK3CustomerName(String k3CustomerName) {
        this.k3CustomerName = k3CustomerName;
    }

    public Integer getReturnOrderStatus() {
        return returnOrderStatus;
    }

    public void setReturnOrderStatus(Integer returnOrderStatus) {
        this.returnOrderStatus = returnOrderStatus;
    }

    public Integer getReturnMode() {
        return returnMode;
    }

    public void setReturnMode(Integer returnMode) {
        this.returnMode = returnMode;
    }

    public BigDecimal getLogisticsAmount() {
        return logisticsAmount;
    }

    public void setLogisticsAmount(BigDecimal logisticsAmount) {
        this.logisticsAmount = logisticsAmount;
    }

    public BigDecimal getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(BigDecimal serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public BigDecimal getEqAmount() {
        return eqAmount;
    }

    public void setEqAmount(BigDecimal eqAmount) {
        this.eqAmount = eqAmount;
    }

    public Integer getDeliverySubCompanyId() {
        return deliverySubCompanyId;
    }

    public void setDeliverySubCompanyId(Integer deliverySubCompanyId) {
        this.deliverySubCompanyId = deliverySubCompanyId;
    }

    public String getDeliverySubCompanyName() {
        return deliverySubCompanyName;
    }

    public void setDeliverySubCompanyName(String deliverySubCompanyName) {
        this.deliverySubCompanyName = deliverySubCompanyName;
    }

    public Integer getSuccessStatus() {
        return successStatus;
    }

    public void setSuccessStatus(Integer successStatus) {
        this.successStatus = successStatus;
    }

    @JSONField(name = "Note")
    public void setNote(String note) {
        this.remark = StringUtils.trimToNull(note);
    }

    @JSONField(name = "returnReason")
    public void setReturnReason(Integer returnReason) {
        this.returnReasonType = returnReason;
    }

    @JSONField(name = "consignmentCompany")
    public void setConsignmentCompany(String consignmentCompany) {
        this.deliverySubCompanyId = Integer.parseInt(consignmentCompany);
    }

}
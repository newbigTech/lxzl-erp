package com.lxzl.erp.common.domain.purchase.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.constant.PurchaseType;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.IdGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.common.domain.workflow.pojo.WorkflowLink;
import com.lxzl.erp.common.util.validate.constraints.In;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseOrder extends BasePO {

    private Integer purchaseOrderId;   //唯一标识
    @NotEmpty(message = ErrorCode.PURCHASE_ORDER_NO_NOT_NULL, groups = {UpdateGroup.class, IdGroup.class})
    private String purchaseNo;   //采购单编号
    @NotNull(message = ErrorCode.PRODUCT_SUPPLIER_ID_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
    private Integer productSupplierId;   //商品供应商ID

    private Integer warehouseId;   //仓库ID
    @NotEmpty(message = ErrorCode.WAREHOUSE_NO_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
    private String warehouseNo;   //仓库编号
    private String warehouseSnapshot;   //仓库快照
    @NotNull(message = ErrorCode.IS_INVOICE_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
    @In(value = {CommonConstant.YES, CommonConstant.NO}, message = ErrorCode.IS_NEW_VALUE_ERROR, groups = {AddGroup.class, UpdateGroup.class})
    private Integer isInvoice;   //是否有发票，0否1是
    @NotNull(message = ErrorCode.IS_NEW_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
    @In(value = {CommonConstant.YES, CommonConstant.NO}, message = ErrorCode.IS_INVOICE_VALUE_ERROR, groups = {AddGroup.class, UpdateGroup.class})
    private Integer isNew;   //是否全新机
//    @NotNull(message = ErrorCode.PURCHASE_TAX_RATE_ERROR, groups = {AddGroup.class, UpdateGroup.class})
    @Min(value = 0 ,message = ErrorCode.PURCHASE_TAX_RATE_ERROR,groups = {AddGroup.class, UpdateGroup.class})
    @Max(value = 1 ,message = ErrorCode.PURCHASE_TAX_RATE_ERROR,groups = {AddGroup.class, UpdateGroup.class})
    private Double taxRate;   //税率
    private BigDecimal purchaseOrderAmountTotal;   //采购单总价
    private BigDecimal purchaseOrderAmountReal;   //采购单实收
    private BigDecimal purchaseOrderAmountStatement;   //采购单结算金额
    private Integer purchaseOrderStatus;   //采购单状态，0-待提交，3-审核中，6-采购中，9-部分采购，12-全部采购，15-结束采购
    private Date deliveryTime;   //发货时间
    @NotNull(message = ErrorCode.PURCHASE_TYPE_NOT_NULL, groups = {AddGroup.class, UpdateGroup.class})
    @In(value = {PurchaseType.PURCHASE_TYPE_ALL_OR_MAIN, PurchaseType.PURCHASE_TYPE_GADGET}, message = ErrorCode.PURCHASE_TYPE_ERROR, groups = {AddGroup.class, UpdateGroup.class})
    private Integer purchaseType;//采购类型：1-整机及四大件，2-小配件
    private Integer dataStatus;   //状态：0不可用；1可用；2删除
    private Integer owner;   //数据归属人
    private String remark;   //备注
    private Date createTime;   //添加时间
    private String createUser;   //添加人
    private Date updateTime;   //添加时间
    private String updateUser;   //修改人

    private String productSupplierName;//商品供应商名称
    private String ownerName;//采购员名称

    private List<PurchaseOrderProduct> purchaseOrderProductList;
    private List<PurchaseOrderMaterial> purchaseOrderMaterialList;

    private List<PurchaseDeliveryOrder> purchaseDeliveryOrderList;
    private List<PurchaseReceiveOrder> purchaseReceiveOrderList;

    private WorkflowLink workflowLink;

    public Integer getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Integer purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public Integer getProductSupplierId() {
        return productSupplierId;
    }

    public void setProductSupplierId(Integer productSupplierId) {
        this.productSupplierId = productSupplierId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getWarehouseSnapshot() {
        return warehouseSnapshot;
    }

    public void setWarehouseSnapshot(String warehouseSnapshot) {
        this.warehouseSnapshot = warehouseSnapshot;
    }

    public Integer getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(Integer isInvoice) {
        this.isInvoice = isInvoice;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getPurchaseOrderAmountTotal() {
        return purchaseOrderAmountTotal;
    }

    public void setPurchaseOrderAmountTotal(BigDecimal purchaseOrderAmountTotal) {
        this.purchaseOrderAmountTotal = purchaseOrderAmountTotal;
    }

    public BigDecimal getPurchaseOrderAmountReal() {
        return purchaseOrderAmountReal;
    }

    public void setPurchaseOrderAmountReal(BigDecimal purchaseOrderAmountReal) {
        this.purchaseOrderAmountReal = purchaseOrderAmountReal;
    }

    public BigDecimal getPurchaseOrderAmountStatement() {
        return purchaseOrderAmountStatement;
    }

    public void setPurchaseOrderAmountStatement(BigDecimal purchaseOrderAmountStatement) {
        this.purchaseOrderAmountStatement = purchaseOrderAmountStatement;
    }

    public Integer getPurchaseOrderStatus() {
        return purchaseOrderStatus;
    }

    public void setPurchaseOrderStatus(Integer purchaseOrderStatus) {
        this.purchaseOrderStatus = purchaseOrderStatus;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(Integer purchaseType) {
        this.purchaseType = purchaseType;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
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

    public List<PurchaseOrderProduct> getPurchaseOrderProductList() {
        return purchaseOrderProductList;
    }

    public void setPurchaseOrderProductList(List<PurchaseOrderProduct> purchaseOrderProductList) {
        this.purchaseOrderProductList = purchaseOrderProductList;
    }

    public String getProductSupplierName() {
        return productSupplierName;
    }

    public void setProductSupplierName(String productSupplierName) {
        this.productSupplierName = productSupplierName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<PurchaseDeliveryOrder> getPurchaseDeliveryOrderList() {
        return purchaseDeliveryOrderList;
    }

    public void setPurchaseDeliveryOrderList(List<PurchaseDeliveryOrder> purchaseDeliveryOrderList) {
        this.purchaseDeliveryOrderList = purchaseDeliveryOrderList;
    }

    public List<PurchaseReceiveOrder> getPurchaseReceiveOrderList() {
        return purchaseReceiveOrderList;
    }

    public void setPurchaseReceiveOrderList(List<PurchaseReceiveOrder> purchaseReceiveOrderList) {
        this.purchaseReceiveOrderList = purchaseReceiveOrderList;
    }

    public List<PurchaseOrderMaterial> getPurchaseOrderMaterialList() {
        return purchaseOrderMaterialList;
    }

    public void setPurchaseOrderMaterialList(List<PurchaseOrderMaterial> purchaseOrderMaterialList) {
        this.purchaseOrderMaterialList = purchaseOrderMaterialList;
    }

    public WorkflowLink getWorkflowLink() {
        return workflowLink;
    }

    public void setWorkflowLink(WorkflowLink workflowLink) {
        this.workflowLink = workflowLink;
    }
}
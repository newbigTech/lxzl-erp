package com.lxzl.erp.common.domain.order.pojo;

import com.lxzl.erp.common.domain.product.pojo.ProductSkuProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderProduct implements Serializable {
    private Integer orderProductId;
    private Integer orderId;
    private Integer productId;
    private String productName;
    private Integer productSkuId;
    private String productSkuName;
    private Integer productCount;
    private BigDecimal productUnitAmount;
    private BigDecimal productAmount;
    private String productSnapshot;
    private List<String> equipmentNoList;
    private Integer dataStatus;
    private String remark;
    private List<ProductSkuProperty> productSkuPropertyList;

    public Integer getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(Integer orderProductId) {
        this.orderProductId = orderProductId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(Integer productSkuId) {
        this.productSkuId = productSkuId;
    }

    public String getProductSkuName() {
        return productSkuName;
    }

    public void setProductSkuName(String productSkuName) {
        this.productSkuName = productSkuName;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public BigDecimal getProductUnitAmount() {
        return productUnitAmount;
    }

    public void setProductUnitAmount(BigDecimal productUnitAmount) {
        this.productUnitAmount = productUnitAmount;
    }

    public BigDecimal getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(BigDecimal productAmount) {
        this.productAmount = productAmount;
    }

    public String getProductSnapshot() {
        return productSnapshot;
    }

    public void setProductSnapshot(String productSnapshot) {
        this.productSnapshot = productSnapshot;
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

    public List<String> getEquipmentNoList() {
        return equipmentNoList;
    }

    public void setEquipmentNoList(List<String> equipmentNoList) {
        this.equipmentNoList = equipmentNoList;
    }

    public List<ProductSkuProperty> getProductSkuPropertyList() {
        return productSkuPropertyList;
    }

    public void setProductSkuPropertyList(List<ProductSkuProperty> productSkuPropertyList) {
        this.productSkuPropertyList = productSkuPropertyList;
    }
}
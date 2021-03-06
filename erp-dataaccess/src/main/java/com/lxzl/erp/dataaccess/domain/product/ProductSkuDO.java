package com.lxzl.erp.dataaccess.domain.product;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.List;

public class ProductSkuDO extends BaseDO {
    private Integer id;
    private String skuName;
    private Integer productId;
    private Integer stock;
    private BigDecimal skuPrice;
    private BigDecimal dayRentPrice;
    private BigDecimal monthRentPrice;
    private BigDecimal newSkuPrice;
    private BigDecimal newDayRentPrice;
    private BigDecimal newMonthRentPrice;
    private String customCode;
    private String barCode;
    private String properties;
    private String remark;
    private Integer dataStatus;
    private List<ProductSkuPropertyDO> productSkuPropertyDOList;
    private List<ProductMaterialDO> productMaterialDOList;

    @Transient
    private String productName;
    @Transient
    private Integer rentCount;
    @Transient
    private Integer canProcessCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getDayRentPrice() {
        return dayRentPrice;
    }

    public void setDayRentPrice(BigDecimal dayRentPrice) {
        this.dayRentPrice = dayRentPrice;
    }

    public BigDecimal getMonthRentPrice() {
        return monthRentPrice;
    }

    public void setMonthRentPrice(BigDecimal monthRentPrice) {
        this.monthRentPrice = monthRentPrice;
    }

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public List<ProductSkuPropertyDO> getProductSkuPropertyDOList() {
        return productSkuPropertyDOList;
    }

    public void setProductSkuPropertyDOList(List<ProductSkuPropertyDO> productSkuPropertyDOList) {
        this.productSkuPropertyDOList = productSkuPropertyDOList;
    }

    public BigDecimal getSkuPrice() {
        return skuPrice;
    }

    public void setSkuPrice(BigDecimal skuPrice) {
        this.skuPrice = skuPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<ProductMaterialDO> getProductMaterialDOList() {
        return productMaterialDOList;
    }

    public void setProductMaterialDOList(List<ProductMaterialDO> productMaterialDOList) {
        this.productMaterialDOList = productMaterialDOList;
    }

    public Integer getRentCount() {
        return rentCount;
    }

    public void setRentCount(Integer rentCount) {
        this.rentCount = rentCount;
    }

    public Integer getCanProcessCount() {
        return canProcessCount;
    }

    public void setCanProcessCount(Integer canProcessCount) {
        this.canProcessCount = canProcessCount;
    }

    public BigDecimal getNewSkuPrice() {
        return newSkuPrice;
    }

    public void setNewSkuPrice(BigDecimal newSkuPrice) {
        this.newSkuPrice = newSkuPrice;
    }

    public BigDecimal getNewDayRentPrice() {
        return newDayRentPrice;
    }

    public void setNewDayRentPrice(BigDecimal newDayRentPrice) {
        this.newDayRentPrice = newDayRentPrice;
    }

    public BigDecimal getNewMonthRentPrice() {
        return newMonthRentPrice;
    }

    public void setNewMonthRentPrice(BigDecimal newMonthRentPrice) {
        this.newMonthRentPrice = newMonthRentPrice;
    }
}

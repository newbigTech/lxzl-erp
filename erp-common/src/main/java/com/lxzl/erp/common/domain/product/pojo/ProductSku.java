package com.lxzl.erp.common.domain.product.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.base.BasePO;
import com.lxzl.erp.common.domain.validGroup.AddGroup;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSku extends BasePO {
    private Integer skuId;
    private String skuName;
    private Integer productId;
    private Integer stock;
    private BigDecimal skuPrice;
    private BigDecimal dayRentPrice;
    private BigDecimal monthRentPrice;
    @NotNull(message = ErrorCode.MATERIAL_NO_NOT_NULL,groups = {AddGroup.class})
    private BigDecimal newSkuPrice;
    @NotNull(message = ErrorCode.MATERIAL_NO_NOT_NULL,groups = {AddGroup.class})
    private BigDecimal newDayRentPrice;
    @NotNull(message = ErrorCode.MATERIAL_NO_NOT_NULL,groups = {AddGroup.class})
    private BigDecimal newMonthRentPrice;
    private String customCode;
    private String barCode;
    private String properties;
    private String remark;
    private Integer dataStatus;
    private List<ProductSkuProperty> productSkuPropertyList;
    private List<ProductMaterial> productMaterialList;
    /**
     * 应该有哪些属性值
     */
    private List<ProductCategoryPropertyValue> shouldProductCategoryPropertyValueList;

    private String productName;

    //在租数量
    private Integer rentCount;
    //可退换数量
    private Integer canProcessCount;

    private Integer newProductSkuCount;     // 全新SKU设备数量
    private Integer oldProductSkuCount;     // 次新SKU设备数量
    protected Date createTime;
    protected Date updateTime;

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
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

    public BigDecimal getSkuPrice() {
        return skuPrice;
    }

    public void setSkuPrice(BigDecimal skuPrice) {
        this.skuPrice = skuPrice;
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

    public List<ProductSkuProperty> getProductSkuPropertyList() {
        return productSkuPropertyList;
    }

    public void setProductSkuPropertyList(List<ProductSkuProperty> productSkuPropertyList) {
        this.productSkuPropertyList = productSkuPropertyList;
    }

    public List<ProductMaterial> getProductMaterialList() {
        return productMaterialList;
    }

    public void setProductMaterialList(List<ProductMaterial> productMaterialList) {
        this.productMaterialList = productMaterialList;
    }

    public List<ProductCategoryPropertyValue> getShouldProductCategoryPropertyValueList() {
        return shouldProductCategoryPropertyValueList;
    }

    public void setShouldProductCategoryPropertyValueList(List<ProductCategoryPropertyValue> shouldProductCategoryPropertyValueList) {
        this.shouldProductCategoryPropertyValueList = shouldProductCategoryPropertyValueList;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public Integer getNewProductSkuCount() {
        return newProductSkuCount;
    }

    public void setNewProductSkuCount(Integer newProductSkuCount) {
        this.newProductSkuCount = newProductSkuCount;
    }

    public Integer getOldProductSkuCount() {
        return oldProductSkuCount;
    }

    public void setOldProductSkuCount(Integer oldProductSkuCount) {
        this.oldProductSkuCount = oldProductSkuCount;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}

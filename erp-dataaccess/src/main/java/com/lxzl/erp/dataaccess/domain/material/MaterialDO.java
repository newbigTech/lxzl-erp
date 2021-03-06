package com.lxzl.erp.dataaccess.domain.material;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-10-30 19:57
 */
public class MaterialDO extends BaseDO {
    private Integer id;
    private String materialNo;
    private String k3MaterialNo;
    private String materialName;
    private String materialModel;
    private Integer materialType;
    private Integer isMainMaterial;
    private Double materialCapacityValue;
    private Integer materialModelId;
    private Integer brandId;
    private String brandName;
    private Integer isRent;
    private BigDecimal materialPrice;
    private Integer stock;
    private BigDecimal dayRentPrice;
    private BigDecimal monthRentPrice;
    private Integer isConsumable;
    private String materialDesc;
    private Integer dataStatus;
    private String remark;
    private List<MaterialImgDO> materialImgDOList;
    @Transient
    private Integer rentCount;
    @Transient
    private Integer returnCount;
    @Transient
    private Integer canProcessCount;
    @Transient
    private String materialModelName;
    @Transient
    private String materialTypeName;
    private BigDecimal newMaterialPrice;  //全新配件本身的价值(单价)
    private BigDecimal newDayRentPrice;  //全新天租赁价格
    private BigDecimal newMonthRentPrice;  //全新月租赁价格

    private Integer newMaterialCount;
    private Integer oldMaterialCount;

    private Integer isReturnAnyTime;    //是否允许随时归还，0否1是

    public BigDecimal getNewMaterialPrice() {
        return newMaterialPrice;
    }

    public void setNewMaterialPrice(BigDecimal newMaterialPrice) {
        this.newMaterialPrice = newMaterialPrice;
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
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Integer getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Integer materialType) {
        this.materialType = materialType;
    }

    public Integer getIsMainMaterial() {
        return isMainMaterial;
    }

    public void setIsMainMaterial(Integer isMainMaterial) {
        this.isMainMaterial = isMainMaterial;
    }

    public Double getMaterialCapacityValue() {
        return materialCapacityValue;
    }

    public void setMaterialCapacityValue(Double materialCapacityValue) {
        this.materialCapacityValue = materialCapacityValue;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public BigDecimal getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(BigDecimal materialPrice) {
        this.materialPrice = materialPrice;
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

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
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

    public List<MaterialImgDO> getMaterialImgDOList() {
        return materialImgDOList;
    }

    public void setMaterialImgDOList(List<MaterialImgDO> materialImgDOList) {
        this.materialImgDOList = materialImgDOList;
    }

    public Integer getMaterialModelId() {
        return materialModelId;
    }

    public void setMaterialModelId(Integer materialModelId) {
        this.materialModelId = materialModelId;
    }

    public Integer getIsRent() {
        return isRent;
    }

    public void setIsRent(Integer isRent) {
        this.isRent = isRent;
    }

    public Integer getRentCount() {
        return rentCount;
    }

    public void setRentCount(Integer rentCount) {
        this.rentCount = rentCount;
    }

    public Integer getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(Integer returnCount) {
        this.returnCount = returnCount;
    }

    public Integer getCanProcessCount() {
        return canProcessCount;
    }

    public void setCanProcessCount(Integer canProcessCount) {
        this.canProcessCount = canProcessCount;
    }

    public String getMaterialModelName() {
        return materialModelName;
    }

    public void setMaterialModelName(String materialModelName) {
        this.materialModelName = materialModelName;
    }

    public Integer getIsConsumable() {
        return isConsumable;
    }

    public void setIsConsumable(Integer isConsumable) {
        this.isConsumable = isConsumable;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getMaterialModel() {
        return materialModel;
    }

    public void setMaterialModel(String materialModel) {
        this.materialModel = materialModel;
    }

    public String getMaterialTypeName() {
        return materialTypeName;
    }

    public void setMaterialTypeName(String materialTypeName) {
        this.materialTypeName = materialTypeName;
    }

    public Integer getNewMaterialCount() {
        return newMaterialCount;
    }

    public void setNewMaterialCount(Integer newMaterialCount) {
        this.newMaterialCount = newMaterialCount;
    }

    public Integer getOldMaterialCount() {
        return oldMaterialCount;
    }

    public void setOldMaterialCount(Integer oldMaterialCount) {
        this.oldMaterialCount = oldMaterialCount;
    }

    public Integer getIsReturnAnyTime() { return isReturnAnyTime; }

    public void setIsReturnAnyTime(Integer isReturnAnyTime) { this.isReturnAnyTime = isReturnAnyTime; }

    public String getK3MaterialNo() { return k3MaterialNo; }

    public void setK3MaterialNo(String k3MaterialNo) { this.k3MaterialNo = k3MaterialNo; }
}

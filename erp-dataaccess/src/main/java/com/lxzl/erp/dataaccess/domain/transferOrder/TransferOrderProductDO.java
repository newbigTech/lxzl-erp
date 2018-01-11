package com.lxzl.erp.dataaccess.domain.transferOrder;

import com.lxzl.se.dataaccess.mysql.domain.BaseDO;
import org.springframework.data.annotation.Transient;

import java.util.List;


public class TransferOrderProductDO  extends BaseDO {

	private Integer id;
	private Integer transferOrderId;
	private Integer productId;
	private Integer productSkuId;
	private Integer productCount;
	private Integer isNew;
	private Integer dataStatus;
	private String remark;

	@Transient
	private List<TransferOrderProductEquipmentDO> transferOrderProductEquipmentDOList;

	public Integer getId(){
		return id;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getTransferOrderId(){
		return transferOrderId;
	}

	public void setTransferOrderId(Integer transferOrderId){
		this.transferOrderId = transferOrderId;
	}

	public Integer getProductId(){
		return productId;
	}

	public void setProductId(Integer productId){
		this.productId = productId;
	}

	public Integer getProductSkuId(){
		return productSkuId;
	}

	public void setProductSkuId(Integer productSkuId){
		this.productSkuId = productSkuId;
	}

	public Integer getProductCount(){
		return productCount;
	}

	public void setProductCount(Integer productCount){
		this.productCount = productCount;
	}

	public Integer getIsNew(){
		return isNew;
	}

	public void setIsNew(Integer isNew){
		this.isNew = isNew;
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

	public List<TransferOrderProductEquipmentDO> getTransferOrderProductEquipmentDOList() {
		return transferOrderProductEquipmentDOList;
	}

	public void setTransferOrderProductEquipmentDOList(List<TransferOrderProductEquipmentDO> transferOrderProductEquipmentDOList) {
		this.transferOrderProductEquipmentDOList = transferOrderProductEquipmentDOList;
	}
}
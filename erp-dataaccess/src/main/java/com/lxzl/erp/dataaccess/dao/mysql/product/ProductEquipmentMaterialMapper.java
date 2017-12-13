package com.lxzl.erp.dataaccess.dao.mysql.product;

import com.lxzl.erp.dataaccess.domain.product.ProductEquipmentMaterialDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ProductEquipmentMaterialMapper extends BaseMysqlDAO<ProductEquipmentMaterialDO> {

	Integer saveList(@Param("productEquipmentMaterialDOList")List<ProductEquipmentMaterialDO> productEquipmentMaterialDOList);

	List<ProductEquipmentMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	ProductEquipmentMaterialDO findByEquipmentIdAndMaterialId(@Param("equipmentId") Integer equipmentId,
															  @Param("materialId")Integer materialId);

}
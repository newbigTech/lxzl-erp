package com.lxzl.erp.dataaccess.dao.mysql.returnOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderProductDO;import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface ReturnOrderProductMapper extends BaseMysqlDAO<ReturnOrderProductDO> {

	List<ReturnOrderProductDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	void batchSave(@Param("returnOrderId") Integer returnOrderId, @Param("returnOrderNo") String returnOrderNo, @Param("returnOrderProductDOList")List<ReturnOrderProductDO> returnOrderProductDOList);

	ReturnOrderProductDO findBySkuIdAndReturnOrderId(@Param("skuId") Integer skuId,@Param("returnOrderId") Integer returnOrderId);

	List<ReturnOrderProductDO> findByReturnOrderId(@Param("returnOrderId") Integer returnOrderId);
}
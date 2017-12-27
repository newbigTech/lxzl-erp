package com.lxzl.erp.dataaccess.dao.mysql.warehouse;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderBulkMaterialDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface StockOrderBulkMaterialMapper extends BaseMysqlDAO<StockOrderBulkMaterialDO> {

	List<StockOrderBulkMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer saveList(@Param("stockOrderBulkMaterialDOList")List<StockOrderBulkMaterialDO> stockOrderBulkMaterialDOList);

    List<StockOrderBulkMaterialDO> findByStockOrderNo(String stockOrderNo);
}
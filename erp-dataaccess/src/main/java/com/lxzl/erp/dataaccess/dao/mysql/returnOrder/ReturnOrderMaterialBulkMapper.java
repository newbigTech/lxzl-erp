package com.lxzl.erp.dataaccess.dao.mysql.returnOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.returnOrder.ReturnOrderMaterialBulkDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface ReturnOrderMaterialBulkMapper extends BaseMysqlDAO<ReturnOrderMaterialBulkDO> {

	List<ReturnOrderMaterialBulkDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	void saveList(@Param("returnOrderMaterialBulkDOList") List<ReturnOrderMaterialBulkDO> returnOrderMaterialBulkDOList);

	List<ReturnOrderMaterialBulkDO> findByReturnOrderNo(@Param("returnOrderNo") String returnOrderNo);

	List<ReturnOrderMaterialBulkDO> findByReturnOrderMaterialId(@Param("returnOrderMaterialId") Integer returnOrderMaterialId);
}
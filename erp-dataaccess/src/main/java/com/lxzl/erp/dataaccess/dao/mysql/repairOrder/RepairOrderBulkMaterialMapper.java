package com.lxzl.erp.dataaccess.dao.mysql.repairOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderBulkMaterialDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface RepairOrderBulkMaterialMapper extends BaseMysqlDAO<RepairOrderBulkMaterialDO> {

	List<RepairOrderBulkMaterialDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

    List<RepairOrderBulkMaterialDO> findByRepairOrderNo(@Param("repairOrderNo")String repairOrderNo);

    Integer findRepairOrderBulkMaterialCountByParams(@Param("maps")Map<String, Object> maps);

    List<RepairOrderBulkMaterialDO> findRepairOrderBulkMaterialByParams(@Param("maps")Map<String, Object> maps);

    void clearDateStatus(@Param("repairOrderNo")String repairOrderNo);
}
package com.lxzl.erp.dataaccess.dao.mysql.repairOrder;

import com.lxzl.erp.dataaccess.domain.repairOrder.RepairOrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface RepairOrderMapper extends BaseMysqlDAO<RepairOrderDO> {

    List<RepairOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    RepairOrderDO findByRepairOrderNo(@Param("repairOrderNo") String repairOrderNo);

    Integer findRepairOrderCountByParams(@Param("maps") Map<String, Object> maps);

    List<RepairOrderDO> findRepairOrderByParams(@Param("maps") Map<String, Object> maps);
}
package com.lxzl.erp.dataaccess.dao.mysql.deploymentOrder;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.deploymentOrder.DeploymentOrderProductEquipmentDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface DeploymentOrderProductEquipmentMapper extends BaseMysqlDAO<DeploymentOrderProductEquipmentDO> {

    List<DeploymentOrderProductEquipmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    DeploymentOrderProductEquipmentDO findDeploymentOrderByEquipmentNo(@Param("deploymentOrderId") Integer deploymentOrderId,
                                                                       @Param("equipmentNo") String equipmentNo);

    List<DeploymentOrderProductEquipmentDO> findByDeploymentOrderProductId(@Param("deploymentOrderProductId") Integer deploymentOrderProductId);
}
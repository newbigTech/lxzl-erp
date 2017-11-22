package com.lxzl.erp.dataaccess.dao.mysql.order;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.order.OrderProductEquipmentDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface OrderProductEquipmentMapper extends BaseMysqlDAO<OrderProductEquipmentDO> {

	List<OrderProductEquipmentDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	Integer saveList(@Param("orderProductEquipmentDOList") List<OrderProductEquipmentDO> orderProductEquipmentDOList);
}
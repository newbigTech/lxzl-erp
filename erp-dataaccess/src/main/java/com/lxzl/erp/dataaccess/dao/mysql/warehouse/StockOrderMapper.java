package com.lxzl.erp.dataaccess.dao.mysql.warehouse;

import com.lxzl.erp.dataaccess.domain.warehouse.StockOrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface StockOrderMapper extends BaseMysqlDAO<StockOrderDO> {
    List<StockOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    StockOrderDO findOrderByTypeAndRefer(@Param("causeType") Integer causeType,
                                         @Param("referNo") String referNo);

    String findNoByTypeAndRefer(@Param("causeType") Integer causeType,
                                         @Param("referNo") String referNo);

}
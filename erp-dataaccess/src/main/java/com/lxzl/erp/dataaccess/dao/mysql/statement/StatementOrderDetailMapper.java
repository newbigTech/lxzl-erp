package com.lxzl.erp.dataaccess.dao.mysql.statement;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDetailDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StatementOrderDetailMapper extends BaseMysqlDAO<StatementOrderDetailDO> {

    List<StatementOrderDetailDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    Integer saveList(@Param("statementOrderDetailDOList") List<StatementOrderDetailDO> statementOrderDetailDOList);
}
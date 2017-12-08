package com.lxzl.erp.dataaccess.dao.mysql.statement;

import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import com.lxzl.erp.dataaccess.domain.statement.StatementOrderDO;import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface StatementOrderMapper extends BaseMysqlDAO<StatementOrderDO> {

	List<StatementOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
}
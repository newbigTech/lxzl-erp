package com.lxzl.erp.dataaccess.dao.mysql.k3;

import com.lxzl.erp.dataaccess.domain.k3.K3MappingCustomerDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface K3MappingCustomerMapper extends BaseMysqlDAO<K3MappingCustomerDO> {

    List<K3MappingCustomerDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    K3MappingCustomerDO findByErpCode(@Param("erpCode") String erpCode);

    K3MappingCustomerDO findByK3Code(@Param("k3Code") String k3Code);
}
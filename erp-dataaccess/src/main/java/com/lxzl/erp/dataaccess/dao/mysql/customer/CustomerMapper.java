package com.lxzl.erp.dataaccess.dao.mysql.customer;

import com.lxzl.erp.common.domain.customer.pojo.CustomerRentCount;
import com.lxzl.erp.dataaccess.domain.customer.CustomerDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface CustomerMapper extends BaseMysqlDAO<CustomerDO> {

    List<CustomerDO> listPage(@Param("maps") Map<String, Object> paramMap);

    Integer listCount(@Param("maps") Map<String, Object> paramMap);

    CustomerDO findByNo(@Param("customerNo") String customerNo);

    List<CustomerDO> findByCustomerParam(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    CustomerDO findByName(@Param("customerName") String customerName);

    CustomerDO findByRealNameAndPersonNo(@Param("realName") String realName, @Param("personNo") String personNo);

    List<CustomerDO> findCustomerCompanyByParams(@Param("maps") Map<String, Object> paramMap);

    Integer findCustomerCompanyCountByParams(@Param("maps") Map<String, Object> paramMap);

    List<CustomerDO> findCustomerPersonByParams(@Param("maps") Map<String, Object> paramMap);

    Integer findCustomerPersonCountByParams(@Param("maps") Map<String, Object> paramMap);

    CustomerDO findCustomerCompanyByNo(@Param("customerNo") String customerNo);

    CustomerDO findCustomerPersonByNo(@Param("customerNo") String customerNo);

    void setIsRisk();

    List<CustomerDO> findCustomer();

    CustomerRentCount queryRentCountByCustomerNo(@Param("customerNo") String customerNo);

    List<CustomerDO> findByCustomerNoList(@Param("customerNoList") List<String> customerNoList);

    List<CustomerDO> findByCustomerNameList(@Param("customerNameList") List<String> customerNameList);

    List<CustomerDO> findCustomerByOwner(@Param("ownerId") Integer ownerId);

    void updateListForUser(@Param("updateCustomerDOList") List<CustomerDO> updateCustomerDOList);

    CustomerDO findByNoForCharge(@Param("customerNo") String customerNo);

	Integer findSubsidiaryCustomerCompanyCountByParams(@Param("maps") Map<String, Object> maps);

	List<CustomerDO> findSubsidiaryCustomerCompanyByParams(@Param("maps") Map<String, Object> maps);
}
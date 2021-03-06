package com.lxzl.erp.dataaccess.dao.mysql.area;

import com.lxzl.erp.dataaccess.domain.area.AreaCityDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface AreaCityMapper extends BaseMysqlDAO<AreaCityDO> {

	List<AreaCityDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);
	//添加邮政编号
	Integer savePostCode(@Param("postCode")String postCode,@Param("cityName")String cityName);
	//查找city_name
	List<String> selectCityNames();
}
package com.lxzl.erp.dataaccess.dao.mysql.purchase;

import com.lxzl.erp.dataaccess.domain.purchase.PurchaseReceiveOrderDO;
import com.lxzl.se.dataaccess.mysql.BaseMysqlDAO;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface PurchaseReceiveOrderMapper extends BaseMysqlDAO<PurchaseReceiveOrderDO> {

	List<PurchaseReceiveOrderDO> listPage(@Param("maps") Map<String, Object> paramMap);

	Integer listCount(@Param("maps") Map<String, Object> paramMap);

	PurchaseReceiveOrderDO findByNo(@Param("purchaseReceiveNo") String purchaseReceiveNo);

	/**
	 * 为了后面修改方便，此接口查询的商品项数据为包含所有data_status的数据
	 * @param purchaseReceiveNo
	 * @return
	 */
	PurchaseReceiveOrderDO findAllByNo(@Param("purchaseReceiveNo") String purchaseReceiveNo);

	List<PurchaseReceiveOrderDO> findPurchaseReceiveOrderByParams(@Param("maps") Map<String, Object> paramMap);

	Integer findPurchaseReceiveOrderCountByParams(@Param("maps") Map<String, Object> paramMap);
}
package com.lxzl.erp.core.service.warehouse;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.product.pojo.ProductInStorage;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.warehouse.ProductInStockParam;
import com.lxzl.erp.common.domain.warehouse.WarehouseQueryParam;
import com.lxzl.erp.common.domain.warehouse.pojo.Warehouse;
import com.lxzl.se.core.service.BaseService;

import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-06 17:43
 */
public interface WarehouseService extends BaseService {
    /**
     * 添加仓库
     *
     * @param warehouse 仓库基本信息
     * @return 仓库编号
     */
    ServiceResult<String, String> addWarehouse(Warehouse warehouse);

    /**
     * 修改仓库
     *
     * @param warehouse 仓库基本信息
     * @return 仓库编号
     */
    ServiceResult<String, String> updateWarehouse(Warehouse warehouse);

    /**
     * 获取仓库列表信息
     *
     * @param param 查询仓库参数
     * @return 仓库列表
     */
    ServiceResult<String, Page<Warehouse>> getWarehousePage(WarehouseQueryParam param);

    ServiceResult<String, List<Warehouse>> getWarehouseByCompany(Integer subCompanyId);

    ServiceResult<String, List<Warehouse>> getWarehouseByCurrentCompany();

    ServiceResult<String, Warehouse> getWarehouseById(Integer warehouseId);

    /**
     * @param productInStockParam 商品入库基本信息
     * @return
     * @description 商品入库，只支持采购首次入库
     */
    ServiceResult<String, Integer> productInStock(ProductInStockParam productInStockParam);

    ServiceResult<String, Integer> productOutStock(List<ProductInStorage> productInStorageList);
}

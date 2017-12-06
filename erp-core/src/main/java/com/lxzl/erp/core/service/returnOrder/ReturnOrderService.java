package com.lxzl.erp.core.service.returnOrder;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.Product;
import com.lxzl.erp.common.domain.product.pojo.ProductEquipment;
import com.lxzl.erp.common.domain.returnOrder.*;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrder;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderMaterialBulk;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderProductEquipment;

public interface ReturnOrderService {
    ServiceResult<String,String> create(AddReturnOrderParam addReturnOrderParam);
    ServiceResult<String, ProductEquipment> doReturnEquipment(DoReturnEquipmentParam doReturnEquipmentParam);
    ServiceResult<String, Material> doReturnMaterial(DoReturnMaterialParam doReturnMaterialParam);
    ServiceResult<String, ReturnOrder> detail(ReturnOrder returnOrder);
    ServiceResult<String, Page<ReturnOrder>> page(ReturnOrderPageParam returnOrderPageParam);
    ServiceResult<String, String> end(ReturnOrder returnOrder);
    ServiceResult<String, String> cancel(ReturnOrder returnOrder);
    ServiceResult<String,Page<ReturnOrderProductEquipment>> pageReturnEquipment(ReturnEquipmentPageParam returnEquipmentPageParam);
    ServiceResult<String,Page<ReturnOrderMaterialBulk>> pageReturnBulk(ReturnBulkPageParam returnBulkPageParam);
    ServiceResult<String,Page<Product>> pageRentProductSku(RentProductSkuPageParam rentProductSkuPageParam);
    ServiceResult<String,Page<Material>> pageRentMaterial(RentMaterialPageParam rentMaterialPageParam);
}

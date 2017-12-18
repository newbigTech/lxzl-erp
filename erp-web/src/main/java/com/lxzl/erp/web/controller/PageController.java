package com.lxzl.erp.web.controller;

import com.lxzl.se.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by gaochao on 2016/11/2.
 */
@Controller("pageController")
public class PageController extends BaseController {

    @RequestMapping("/index")
    public String index() {
        return "/home";
    }

    @RequestMapping("/test")
    public String test() {
        return "/test";
    }

    @RequestMapping("/home")
    public String productList() {
        return "/home";
    }

    @RequestMapping("/user-manage/password")
    public String userManagePassword() {
        return "/component/user/password";
    }

    @RequestMapping("/user-manage/list")
    public String userManageList() {
        return "/userManage/userList";
    }

    @RequestMapping("/user-manage/view")
    public String userManageViewUser() {
        return "/component/user/view";
    }

    @RequestMapping("/user-manage/addUser")
    public String userManageAddUser() {
        return "/component/user/add";
    }

    @RequestMapping("/user-manage/editUser")
    public String userManageEditUser() {
        return "/component/user/edit";
    }

    @RequestMapping("/user-manage/userAccessData")
    public String userManageUserAccessData() {
        return "/component/user/userAccessData";
    }

    @RequestMapping("/user-manage/role")
    public String userManageRole() {
        return "/userManage/roleList";
    }

    @RequestMapping("/user-manage/addRole")
    public String userManageAddRole() {
        return "/component/user/addRole";
    }

    @RequestMapping("/user-manage/editRole")
    public String userManageEditRole() {
        return "/component/user/editRole";
    }

    @RequestMapping("/user-manage/roleAccessFunction")
    public String roleAccessFunction() {
        return "/component/user/roleAccessFunction";
    }

    @RequestMapping("/user-manage/roleAccessData")
    public String roleAccessData() {
        return "/component/user/roleAccessData";
    }

    @RequestMapping("/user-manage/department")
    public String userManageDepartment() {
        return "/departmentManage/departmentList";
    }

    @RequestMapping("/user-manage/departmentAdd")
    public String userManageDepartmentAdd() {
        return "/departmentManage/departmentAdd";
    }

    @RequestMapping("/user-manage/departmentEdit")
    public String userManageDepartmentEdit() {
        return "/departmentManage/departmentEdit";
    }

    //商品管理
    @RequestMapping("/product-manage/list")
    public String productManageList() {
        return "/productManage/productList";
    }

    @RequestMapping("/product-manage/detail")
    public String productManageDetail() {
        return "/productManage/productDetail";
    }

    @RequestMapping("/product-manage/add")
    public String productManageAdd() {
        return "/productManage/productAdd";
    }

    @RequestMapping("/product-manage/edit")
    public String productManageEdit() {
        return "/productManage/productEdit";
    }

    @RequestMapping("/product-manage/equipmentList")
    public String productManageEquipmentList() {
        return "/productManage/equipmentList";
    }

    @RequestMapping("/product-manage/skuList")
    public String productManageSkuList() {
        return "/productManage/skuList";
    }

    //物料型号管理
    @RequestMapping("/material-modal-manage/list")
    public String materialMaodalManageList() {
        return "/materialModalManage/materialModal";
    }

    @RequestMapping("/material-modal-manage/add")
    public String materialMaodalManageAdd() {
        return "/materialModalManage/materialModalAdd";
    }

    @RequestMapping("/material-modal-manage/edit")
    public String materialMaodalManageEdit() {
        return "/materialModalManage/materialModalEdit";
    }

    //物料管理
    @RequestMapping("/material-manage/list")
    public String materialManageMaterialList() {
        return "/materialManage/materialList";
    }

    @RequestMapping("/material-manage/detail")
    public String materialManageMaterialDetail() {
        return "/materialManage/materialDetail";
    }

    @RequestMapping("/material-manage/add")
    public String materialManageMaterialAdd() {
        return "/materialManage/materialAdd";
    }

    @RequestMapping("/material-manage/edit")
    public String materialManageMaterialEdit() {
        return "/materialManage/materialEdit";
    }

    //散料列表
    @RequestMapping("/material-manage/bulk-list")
    public String bulkMaterialManageMaterialList() {
        return "/materialManage/bulkMaterialList";
    }

    //订单管理
    @RequestMapping("/order-manage/list")
    public String orderManageList() {
        return "/orderManage/orderList";
    }

    @RequestMapping("/order-manage/detail")
    public String orderManageDetail() {
        return "/orderManage/orderDetail";
    }

    @RequestMapping("/order-manage/add")
    public String orderManageAdd() {
        return "/orderManage/orderAdd";
    }

    @RequestMapping("/order-manage/edit")
    public String orderManageEdit() {
        return "/orderManage/orderEdit";
    }

    @RequestMapping("/order-manage/orderItem")
    public String orderManageOrderItem() {
        return "/orderManage/orderItem";
    }

    //退货单
    @RequestMapping("/order-return-manage/list")
    public String returnOrderManageList() {
        return "/returnOrderManage/returnOrderList";
    }
    @RequestMapping("/order-return-manage/detail")
    public String returnOrderManageDtail() {
        return "/returnOrderManage/returnOrderDetail";
    }
    @RequestMapping("/order-return-manage/add")
    public String returnOrderManageAdd() {
        return "/returnOrderManage/returnOrderAdd";
    }
    @RequestMapping("/order-return-manage/edit")
    public String returnOrderManageEdit() {
        return "/returnOrderManage/returnOrderEdit";
    }

    //配货
    @RequestMapping("/order-manage/picking")
    public String orderManageDelivery() {
        return "/component/order/picking";
    }

    //个人客户管理
    @RequestMapping("/customer-manage/list")
    public String customerManageList() {
        return "/customerManage/customerList";
    }

    @RequestMapping("/customer-manage/detail")
    public String customerManageDetail() {
        return "/customerManage/customerDetail";
    }

    @RequestMapping("/customer-manage/add")
    public String customerManageAdd() {
        return "/customerManage/customerAdd";
    }

    @RequestMapping("/customer-manage/edit")
    public String customerManageEdit() {
        return "/customerManage/customerEdit";
    }

    @RequestMapping("/customer-manage/riskModal")
    public String customerManageInfo() {
        return "/component/customer/riskModal";
    }

    //企业客户
    @RequestMapping("/customer-business-manage/list")
    public String businessCustomerManageList() {
        return "/businessCustomerManage/businessCustomerList";
    }

    @RequestMapping("/customer-business-manage/detail")
    public String businessCustomerManageDetail() {
        return "/businessCustomerManage/businessCustomerDetail";
    }

    @RequestMapping("/customer-business-manage/add")
    public String businessCustomerManageAdd() {
        return "/businessCustomerManage/businessCustomerAdd";
    }

    @RequestMapping("/customer-business-manage/edit")
    public String businessCustomerManageEdit() {
        return "/businessCustomerManage/businessCustomerEdit";
    }

    @RequestMapping("/customer-consign-info/add")
    public String customerConsignInfoAdd() {
        return "/component/customerConsignInfo/add";
    }

    @RequestMapping("/customer-consign-info/edit")
    public String customerConsignInfoEdit() {
        return "/component/customerConsignInfo/edit";
    }


    //采购管理
    @RequestMapping("/purchase-manage/list")
    public String purchaseManageList() {
        return "/purchaseManage/purchaseList";
    }

    @RequestMapping("/purchase-manage/add")
    public String purchaseManageAdd() {
        return "/purchaseManage/purchaseAdd";
    }

    @RequestMapping("/purchase-manage/edit")
    public String purchaseManageEdit() {
        return "/purchaseManage/purchaseEdit";
    }

    @RequestMapping("/purchase-manage/detail")
    public String purchaseManageDetail() {
        return "/purchaseManage/purchaseDetail";
    }

    //采购收料通知
    @RequestMapping("/purchase-receive-manage/list")
    public String purchaseReceiveManageList() {
        return "/purchaseReceiveManage/purchaseReceiveList";
    }

    @RequestMapping("/purchase-receive-manage/detail")
    public String purchaseReceiveManageDetail() {
        return "/purchaseReceiveManage/purchaseReceiveDetail";
    }

    @RequestMapping("/purchase-receive-manage/edit")
    public String purchaseReceiveManageEdit() {
        return "/purchaseReceiveManage/purchaseReceiveEdit";
    }

    //采购发货单
    @RequestMapping("/purchase-delivery-manage/list")
    public String purchaseDeliveryManageList() {
        return "/purchaseDeliveryManage/purchaseDeliveryList";
    }

    @RequestMapping("/purchase-delivery-manage/detail")
    public String purchaseDeliveryManageDetail() {
        return "/purchaseDeliveryManage/purchaseDeliveryDetail";
    }


    //供应商管理
    @RequestMapping("/supplier-manage/list")
    public String supplierManageList() {
        return "/supplierManage/supplierList";
    }

    @RequestMapping("/supplier-manage/detail")
    public String supplierManageDetail() {
        return "/supplierManage/supplierDetail";
    }

    @RequestMapping("/supplier-manage/add")
    public String supplierManageAdd() {
        return "/supplierManage/supplierAdd";
    }

    @RequestMapping("/supplier-manage/edit")
    public String supplierManageEdit() {
        return "/supplierManage/supplierEdit";
    }

    //仓库商管理
    @RequestMapping("/warehouse-manage/list")
    public String warehouseManageList() {
        return "/warehouseManage/warehouseList";
    }

    @RequestMapping("/warehouse-manage/detail")
    public String warehouseManageDetail() {
        return "/warehouseManage/warehouseList";
    }

    @RequestMapping("/warehouse-manage/add")
    public String warehouseManageAdd() {
        return "/warehouseManage/warehouseList";
    }

    @RequestMapping("/warehouse-manage/edit")
    public String warehouseManageEdit() {
        return "/warehouseManage/warehouseList";
    }

    //审核管理
    @RequestMapping("/audit-manage/list")
    public String auditManageList() {
        return "/auditManage/auditList";
    }

    @RequestMapping("/audit-manage/detail")
    public String auditManageDetail() {
        return "/auditManage/auditDetail";
    }

    @RequestMapping("/audit-manage/modal/pass")
    public String auditManagePassModal() {
        return "/component/audit/passModal";
    }

    @RequestMapping("/audit-manage/modal/reject")
    public String auditManageRejectModal() {
        return "/component/audit/rejectModal";
    }


    //选择仓库Modal
    @RequestMapping("/warehouse/choose")
    public String warehouseChoose() {
        return "/component/warehouse/chooseModal";
    }

    //选择供应商Modal
    @RequestMapping("/supplier/choose")
    public String supplierChoose() {
        return "/component/supplier/chooseModal";
    }

    //选择商品Modal
    @RequestMapping("/product/choose")
    public String productChoose() {
        return "/component/product/chooseModal";
    }

    //提交审核选择审核人及填写审核备注
    @RequestMapping("/submit-audit/modal")
    public String submitAuditModal() {
        return "/component/audit/submitModal";
    }

    //采购单选择审核人Modal
    @RequestMapping("/audit-user/choose")
    public String auditUserChoose() {
        return "/component/audit/chooseAuditUserModal";
    }

    //采购单选择审核人Modal
    @RequestMapping("/material/choose")
    public String materialChoose() {
        return "/component/material/chooseModal";
    }

    //选择物料型号
    @RequestMapping("/material-modal/choose")
    public String materialModalChoose() {
        return "/component/materialModal/chooseModal";
    }

    //选择个人客户
    @RequestMapping("/customer-modal/choose")
    public String customerModalChoose() {
        return "/component/customer/chooseCustomerModal";
    }

    //选择企业客户
    @RequestMapping("/business-customer-modal/choose")
    public String businessCustomerModalChoose() {
        return "/component/customer/chooseBusinessCustomerModal";
    }

    //选择用户
    @RequestMapping("/user-modal/choose")
    public String chooseUserModal() {
        return "/component/user/chooseUserModal";
    }

    //选择公司
    @RequestMapping("/company-modal/chooseCompany")
    public String chooseCompanyModal() {
        return "/component/company/chooseCompanyModal";
    }

    //选择客户可退换商品
    @RequestMapping("/customer-rent-product-modal/choose")
    public String chooseRentProductModal() {
        return "/component/customer/chooseRentProductModal";
    }

    //选择客户可退物料
    @RequestMapping("/customer-can-return-material-modal/choose")
    public String chooseCustomerCanReturnMaterialModal() {
        return "/component/customer/chooseCanReturnMaterial";
    }

    //选择客户地址
    @RequestMapping("/customer-address-modal/choose")
    public String chooseCustomerAddressModal() {
        return "/component/customer/chooseCustomerAddressModal";
    }

}

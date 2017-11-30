package com.lxzl.erp.common.constant;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode {
    private static Map<String, String> MAP = new HashMap<String, String>();
    /**
     * 返回码
     */
    public static final String SUCCESS = "J000000";

    public static final String SYSTEM_DEVELOPING = "J000994";
    public static final String USER_NOT_LOGIN = "J000995";
    public static final String CUSTOM_ERROR = "J000996";
    public static final String BUSINESS_EXCEPTION = "J000997";
    public static final String SYSTEM_EXCEPTION = "J000998";
    public static final String SYSTEM_ERROR = "J000999";
    public static final String RECORD_NOT_EXISTS = "J009999";
    public static final String RECORD_ALREADY_EXISTS = "J099999";
    public static final String EMAIL_ERROR = "J000993";
    public static final String COMMIT_ONLY_SELF = "J000992";
    public static final String RECORD_USED_CAN_NOT_DELETE = "J000993";

    public static final String USER_NAME_NOT_NULL = "J100000";
    public static final String USER_PASSWORD_NOT_NULL = "J100001";
    public static final String USER_DISABLE = "J100002";
    public static final String USER_NOT_ACTIVATED = "J100003";
    public static final String USER_NAME_NOT_FOUND = "J100004";
    public static final String USER_PASSWORD_ERROR = "J100005";
    public static final String USER_EXISTS = "J100006";
    public static final String USER_NOT_EXISTS = "J100007";
    public static final String USER_ROLE_NOT_NULL = "J100008";
    public static final String ROLE_NOT_NULL = "J100009";
    public static final String ROLE_HAVE_USER = "J100010";
    public static final String ROLE_NAME_NOT_NULL = "J100011";
    public static final String ROLE_ID_NOT_NULL = "J100013";
    public static final String DEPARTMENT_ID_NOT_NULL = "J100014";
    public static final String DEPARTMENT_NOT_NULL = "J100015";
    public static final String USER_NOT_NULL = "J100016";
    public static final String ACTIVE_USER_ID_NOT_NULL = "J100017";
    public static final String SUB_COMPANY_NAME_NOT_NULL = "J100018";
    public static final String DEPARTMENT_NOT_EXISTS = "J100019";

    public static final String PRODUCT_ID_NOT_NULL = "J200000";
    public static final String PRODUCT_NAME_NOT_NULL = "J200001";
    public static final String PRODUCT_PROPERTY_NOT_NULL = "J200002";
    public static final String PRODUCT_SKU_NOT_NULL = "J200003";
    public static final String PRODUCT_SKU_PROPERTY_NOT_NULL = "J200004";
    public static final String PRODUCT_PROPERTY_VALUE_NOT_NULL = "J200005";
    public static final String PRODUCT_SKU_PROPERTY_VALUE_NOT_NULL = "J200006";
    public static final String PRODUCT_IMAGE_UPLOAD_ERROR = "J200007";
    public static final String PRODUCT_IS_NULL_OR_NOT_EXISTS = "J200008";
    public static final String PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS = "J200009";
    public static final String PRODUCT_SKU_PRICE_ERROR = "J200010";
    public static final String PRODUCT_SKU_COUNT_ERROR = "J200011";
    public static final String PRODUCT_CATEGORY_PROPERTY_NOT_EXISTS = "J200012";
    public static final String PRODUCT_CATEGORY_PROPERTY_VALUE_NOT_EXISTS = "J200013";
    public static final String PRODUCT_CATEGORY_PROPERTY_VALUE_ALREADY_EXISTS = "J200014";
    public static final String PRODUCT_EQUIPMENT_NOT_EXISTS = "J200015";
    public static final String MATERIAL_NOT_EXISTS = "J200016";
    public static final String PRODUCT_EQUIPMENT_HAVE_NO_BULK_MATERIAL = "J200017";
    public static final String PRODUCT_EQUIPMENT_IS_NOT_IDLE = "J200018";
    public static final String BULK_MATERIAL_IS_IN_PRODUCT_EQUIPMENT = "J200019";
    public static final String BULK_MATERIAL_NOT_EXISTS = "J200020";
    public static final String PRODUCT_NOT_EXISTS = "J200021";
    public static final String PRODUCT_EQUIPMENT_NOT_IN_THIS_WAREHOUSE = "J200022";
    public static final String PRODUCT_IS_NOT_RENT = "J200023";
    public static final String MATERIAL_IN_USED = "J200024";
    public static final String PRODUCT_SKU_CAN_NOT_DELETE = "J200025";
    public static final String BULK_MATERIAL_IS_NOT_IDLE = "J200026";
    public static final String BULK_MATERIAL_STATUS_ERROR = "J200027";
    public static final String PRODUCT_EQUIPMENT_STATUS_ERROR = "J200028";
    public static final String BULK_MATERIAL_NOT_IN_THIS_WAREHOUSE = "J200029";


    public static final String WORKFLOW_TYPE_NOT_EXISTS = "J800001";
    public static final String WORKFLOW_TEMPLATE_HAVE_NO_NODE = "J800002";
    public static final String WORKFLOW_LINK_EXISTS = "J800003";
    public static final String WORKFLOW_LINK_NOT_EXISTS = "J800004";
    public static final String WORKFLOW_LINK_HAVE_NO_DETAIL = "J800005";
    public static final String WORKFLOW_LINK_VERIFY_ALREADY_OVER = "J800006";
    public static final String WORKFLOW_NOT_BELONG_TO_YOU = "J800007";
    public static final String WORKFLOW_NOT_EXISTS_CLOSED = "J800008";
    public static final String WORKFLOW_NODE_NOT_EXISTS = "J800009";
    public static final String WORKFLOW_VERIFY_USER_ERROR = "J800010";
    public static final String WORKFLOW_LINK_STATUS_ERROR = "J800011";
    public static final String WORKFLOW_TEMPLATE_NOT_EXISTS = "J800012";

    public static final String REMARK_PATTERN = "J900001";
    public static final String ID_NOT_NULL = "J900002";
    public static final String MONEY_MORE_THAN_ZERO = "J900003";
    public static final String IMAGE_NOT_EXISTS = "J900004";
    public static final String IMAGE_TYPE_ERROR = "J900005";
    public static final String IMAGE_USED = "J900006";
    public static final String DATA_DICTIONARY_NAME_NOT_NULL = "J900007";
    public static final String PARAM_IS_NOT_NULL = "J900008";
    public static final String HAVE_NO_CHANGE_STATUS = "J900009";
    public static final String STATUS_PATTERN = "J900010";
    public static final String COUNT_MORE_THAN_ZERO = "J900011";
    public static final String DAYS_MORE_THAN_ZERO = "J900012";
    public static final String DATA_STATUS_ERROR = "J900013";
    public static final String VERIFY_STATUS_ERROR = "J900014";
    public static final String OPERATOR_IS_NOT_YOURSELF = "J900015";
    public static final String PARAM_IS_NOT_ENOUGH = "J900016";
    public static final String NO_DUPLICATE_COMMIT = "J900017";
    public static final String PARAM_IS_ERROR = "J900018";

    public static final String PURCHASE_ORDER_ID_NOT_NULL = "J300000";
    public static final String PRODUCT_SUPPLIER_ID_NOT_NULL = "J300001";
    public static final String WAREHOUSE_ID_NOT_NULL = "J300002";
    public static final String IS_INVOICE_NOT_NULL = "J300003";
    public static final String IS_NEW_NOT_NULL = "J300004";
    public static final String PURCHASE_ORDER_PRODUCT_LIST_NOT_NULL = "J300005";
    public static final String IS_INVOICE_VALUE_ERROR = "J300006";
    public static final String IS_NEW_VALUE_ERROR = "J300007";
    public static final String PURCHASE_ORDER_NO_NOT_NULL = "J300008";
    public static final String PURCHASE_ORDER_NOT_EXISTS = "J300009";
    public static final String PURCHASE_ORDER_COMMITTED_CAN_NOT_UPDATE = "J300010";
    public static final String PURCHASE_ORDER_CANNOT_CREATE_BY_NEW_AND_AMOUNT = "J300011";
    public static final String WAREHOUSE_NOT_EXISTS = "J300012";
    public static final String PURCHASE_ORDER_PRODUCT_CAN_NOT_REPEAT = "J300013";
    public static final String PURCHASE_ORDER_COMMITTED_CAN_NOT_COMMIT_AGAIN = "J300014";
    public static final String WAREHOUSE_IN_STORAGE_LIST_NOT_NULL = "J300015";
    public static final String USER_CAN_NOT_OP_WAREHOUSE = "J300016";
    public static final String VERIFY_USER_NOT_NULL = "J300017";
    public static final String PURCHASE_ORDER_COMMITTED_CAN_NOT_DELETE = "J300018";
    public static final String PURCHASE_DELIVERY_ORDER_NO_NOT_NULL = "J300019";
    public static final String PURCHASE_DELIVERY_ORDER_NOT_EXISTS = "J300020";
    public static final String WAREHOUSE_NO_NOT_NULL = "J300021";
    public static final String SUPPLIER_NOT_EXISTS = "J300022";
    public static final String PURCHASE_RECEIVE_ORDER_NO_NOT_NULL = "J300023";
    public static final String PURCHASE_RECEIVE_ORDER_NOT_EXISTS = "J300024";
    public static final String PURCHASE_RECEIVE_ORDER_AUTO_ALLOT_YES_CAN_NOT_UPDATE = "J300025";
    public static final String PURCHASE_RECEIVE_ORDER_STATUS_YET_CAN_NOT_UPDATE = "J300026";
    public static final String PURCHASE_RECEIVE_ORDER_PRODUCT_LIST_NOT_NULL = "J300027";
    public static final String PURCHASE_RECEIVE_ORDER_PRODUCT_REAL_COUNT_NOT_NULL = "J300028";
    public static final String PURCHASE_RECEIVE_ORDER_PRODUCT_SKU_ID_NOT_NULL = "J300029";
    public static final String STOCK_ORDER_ALREADY_EXISTS = "J300030";
    public static final String PURCHASE_ORDER_STATUS_CAN_NOT_END = "J300031";
    public static final String PURCHASE_ORDER_STATUS_CAN_NOT_CONTINUE = "J300032";
    public static final String PURCHASE_RECEIVE_ORDER_STATUS_YET_CAN_NOT_IN_AGAIN = "J300033";
    public static final String PURCHASE_TYPE_ERROR = "J300034";
    public static final String PURCHASE_TYPE_NOT_NULL = "J300035";
    public static final String PURCHASE_PRODUCT_MATERIAL_CAN_NOT_ALL_NULL = "J300036";
    public static final String MATERIAL_ID_NOT_NULL = "J300037";
    public static final String MATERIAL_PRICE_ERROR = "J300038";
    public static final String MATERIAL_COUNT_ERROR = "J300039";
    public static final String PURCHASE_ORDER_SKU_MATERIAL_ERROR = "J300040";
    public static final String PURCHASE_ORDER_MATERIAL_CAN_NOT_REPEAT = "J300041";
    public static final String PURCHASE_ORDER_MATERIAL_NOT_MAIN = "J300042";
    public static final String PURCHASE_ORDER_MATERIAL_NOT_GADGET = "J300043";
    public static final String PURCHASE_ORDER_MATERIAL_LIST_NOT_NULL = "J300044";
    public static final String PURCHASE_ORDER_MATERIAL_CAN_NOT_CREATE = "J300045";
    public static final String MATERIAL_NO_NOT_NULL = "J300046";
    public static final String STOCK_ALLOCATION_WAREHOUSE_IS_NOT_SAME = "J300047";
    public static final String WAREHOUSE_OUT_STORAGE_LIST_NOT_NULL = "J300048";
    public static final String PURCHASE_RECEIVE_ORDER_MATERIAL_ID_NOT_NULL = "J300049";
    public static final String PURCHASE_RECEIVE_ORDER_MATERIAL_REAL_COUNT_NOT_NULL = "J300050";
    public static final String MATERIAL_CAPACITY_VALUE_NOT_NULL = "J300051";
    public static final String MATERIAL_MODEL_NOT_NULL = "J300052";
    public static final String MATERIAL_TYPE_HAVE_NO_MODEL = "J300053";
    public static final String PURCHASE_RECEIVE_ORDER_MATERIAL_NO_NOT_NULL = "J300054";


    public static final String ORDER_PRODUCT_LIST_NOT_NULL = "J400000";
    public static final String ORDER_CUSTOMER_CONSIGN_NOT_NULL = "J400001";
    public static final String ORDER_PRODUCT_STOCK_INSUFFICIENT = "J400002";
    public static final String ORDER_NOT_EXISTS = "J400004";
    public static final String ORDER_PRODUCT_EQUIPMENT_NOT_NULL = "J400005";
    public static final String ORDER_PRODUCT_EQUIPMENT_STATUS_ERROR = "J400006";
    public static final String ORDER_PRODUCT_EQUIPMENT_SKU_NOT_SAME = "J400007";
    public static final String ORDER_PRODUCT_EQUIPMENT_IS_NULL = "J400008";
    public static final String ORDER_STATUS_ERROR = "J400009";
    public static final String ORDER_PRODUCT_EQUIPMENT_COUNT_ERROR = "J400010";
    public static final String ORDER_RENT_TYPE_OR_LENGTH_ERROR = "J400011";
    public static final String ORDER_PAY_MODE_NOT_NULL = "J400012";
    public static final String ORDER_PAY_STATUS_ERROR = "J400013";
    public static final String ORDER_REFUND_STATUS_ERROR = "J400014";
    public static final String ORDER_PAY_RECORD_NOT_EXISTS = "J400015";
    public static final String ORDER_PRODUCT_BULK_MATERIAL_COUNT_ERROR = "J400016";
    public static final String ORDER_HAVE_NO_RENT_START_TIME = "J400017";
    public static final String ORDER_STATUS_NOT_PROCESSED = "J400018";
    public static final String ORDER_PRODUCT_AMOUNT_ERROR = "J400019";
    public static final String ORDER_MATERIAL_AMOUNT_ERROR = "J400020";
    public static final String ORDER_HAVE_NO_THIS_ITEM = "J400021";
    public static final String ORDER_PRODUCT_EQUIPMENT_COUNT_MAX = "J400022";
    public static final String ORDER_MATERIAL_BULK_COUNT_MAX = "J400023";
    public static final String DEPLOYMENT_ORDER_PRODUCT_EQUIPMENT_STOCK_NOT_ENOUGH = "J400024";
    public static final String DEPLOYMENT_ORDER_BULK_MATERIAL_STOCK_NOT_ENOUGH = "J400025";
    public static final String DEPLOYMENT_ORDER_STATUS_ERROR = "J400026";
    public static final String DEPLOYMENT_ORDER_HAVE_NO_THIS_ITEM = "J400027";
    public static final String RETURN_ORDER_NO_NOT_NULL = "J400028";
    public static final String EQUIPMENT_NO_NOT_NULL = "J400029";
    public static final String RETURN_ORDER_NO_EXISTS = "J400030";
    public static final String EQUIPMENT_NOT_EXISTS = "J400031";
    public static final String EQUIPMENT_NOT_RENT = "J400032";

    public static final String CUSTOMER_COMPANY_NOT_NULL = "J500001";
    public static final String CUSTOMER_COMPANY_NAME_NOT_NULL = "J500002";
    public static final String CUSTOMER_PERSON_NOT_NULL = "J500003";
    public static final String CUSTOMER_PERSON_NAME_NOT_NULL = "J500004";
    public static final String CUSTOMER_TYPE_NOT_NULL = "J500005";
    public static final String CUSTOMER_COMPANY_CONNECT_NAME_NOT_NULL = "J500006";
    public static final String CUSTOMER_NOT_NULL = "J500007";
    public static final String CUSTOMER_NO_NOT_NULL = "J500008";
    public static final String CUSTOMER_RISK_MANAGEMENT_ID_NOT_NULL = "J500009";
    public static final String CUSTOMER_RISK_MANAGEMENT_CREDIT_AMOUNT_NOT_NULL = "J500010";
    public static final String CUSTOMER_RISK_MANAGEMENT_DEPOSIT_CYCLE_NOT_NULL = "J500011";
    public static final String CUSTOMER_RISK_MANAGEMENT_PAYMENT_CYCLE_NOT_NULL = "J500012";
    public static final String CUSTOMER_NOT_EXISTS = "J500013";
    public static final String CUSTOMER_RISK_MANAGEMENT_CREDIT_AMOUNT_ERROR = "J500014";
    public static final String CUSTOMER_RISK_MANAGEMENT_DEPOSIT_CYCLE_ERROR = "J500015";
    public static final String CUSTOMER_RISK_MANAGEMENT_PAYMENT_CYCLE_ERROR = "J500016";
    public static final String CUSTOMER_RISK_MANAGEMENT_NOT_EXISTS = "J500017";
    public static final String CUSTOMER_HAVE_NO_RETURN = "J500018";
    public static final String CUSTOMER_NOT_RENT_THIS = "J500019";
    public static final String CUSTOMER_RETURN_TOO_MORE = "J500020";
    public static final String CONSIGNEE_NAME_NOT_NULL = "J500021";
    public static final String CONSIGNEE_PHONE_NOT_NULL = "J500022";
    public static final String PROVINCE_ID_NOT_NULL = "J500023";
    public static final String CITY_ID_NOT_NULL = "J500024";
    public static final String DISTRICT_ID_NOT_NULL = "J500025";
    public static final String ADDRESS_NOT_NULL = "J500026";
    public static final String RETURN_COUNT_ERROR = "J500027";
    public static final String RETURN_ORDER_IS_CHARGING_IS_NOT_NULL = "J500028";
    public static final String PRODUCT_SKU_CAN_NOT_REPEAT = "J500029";
    public static final String MATERIAL_CAN_NOT_REPEAT = "J500030";
    public static final String CUSTOMER_CONSIGN_NOT_EXISTS = "J500031";

    public static final String MESSAGE_TITLE_NOT_NULL = "J600001";
    public static final String MESSAGE_CONTENT_NOT_NULL = "J600002";
    public static final String MESSAGE_RECEIVER_NOT_NULL = "J600003";
    public static final String MESSAGE_ID_NOT_NULL = "J600004";
    public static final String MESSAGE_NOT_EXISTS = "J600005";
    public static final String MESSAGE_CAN_NOT_SEND_SELF = "J600006";

    static {
        MAP.put(SUCCESS, "成功");
        MAP.put(ID_NOT_NULL, "ID不能为空");
        MAP.put(BUSINESS_EXCEPTION, "业务异常");
        MAP.put(SYSTEM_EXCEPTION, "系统异常");
        MAP.put(SYSTEM_ERROR, "系统错误,请联系管理员");
        MAP.put(RECORD_NOT_EXISTS, "记录不存在");
        MAP.put(RECORD_ALREADY_EXISTS, "记录已经存在");
        MAP.put(SYSTEM_DEVELOPING, "开发中……");
        MAP.put(EMAIL_ERROR, "邮箱格式有误");
        MAP.put(COMMIT_ONLY_SELF, "只能提交自己的数据");
        MAP.put(RECORD_USED_CAN_NOT_DELETE, "数据被使用，无法删除");

        MAP.put(USER_NOT_LOGIN, "用户未登录");
        MAP.put(USER_DISABLE, "用户已禁用，请联系管理员");
        MAP.put(USER_NAME_NOT_FOUND, "用户名不存在");
        MAP.put(USER_NOT_ACTIVATED, "用户未激活，请联系管理员");
        MAP.put(USER_PASSWORD_ERROR, "密码错误");
        MAP.put(USER_EXISTS, "用户已存在");
        MAP.put(USER_NAME_NOT_NULL, "用户名不能为空");
        MAP.put(USER_PASSWORD_NOT_NULL, "密码不能为空");
        MAP.put(USER_ROLE_NOT_NULL, "用户角色不能为空");
        MAP.put(ROLE_NOT_NULL, "角色不存在");
        MAP.put(USER_NOT_EXISTS, "用户不存在");
        MAP.put(ROLE_ID_NOT_NULL, "角色ID不能为空");
        MAP.put(DEPARTMENT_ID_NOT_NULL, "部门ID不能为空");
        MAP.put(DEPARTMENT_NOT_NULL, "部门不能为空");
        MAP.put(USER_NOT_NULL, "用户不能为空");
        MAP.put(ACTIVE_USER_ID_NOT_NULL, "观察者用户ID不能为空");
        MAP.put(OPERATOR_IS_NOT_YOURSELF, "操作者不是本人，不能操作");
        MAP.put(ROLE_NAME_NOT_NULL, "角色名称不能为空");
        MAP.put(ROLE_HAVE_USER, "角色包含用户");
        MAP.put(SUB_COMPANY_NAME_NOT_NULL, "子公司名称不能为空");
        MAP.put(DEPARTMENT_NOT_EXISTS, "部门不存在");

        MAP.put(PRODUCT_ID_NOT_NULL, "商品ID不能为空");
        MAP.put(PRODUCT_NAME_NOT_NULL, "商品名称不能为空");
        MAP.put(PRODUCT_PROPERTY_NOT_NULL, "商品属性不能为空");
        MAP.put(PRODUCT_SKU_NOT_NULL, "商品SKU不能为空");
        MAP.put(PRODUCT_SKU_PROPERTY_NOT_NULL, "商品SKU属性不能为空");
        MAP.put(PRODUCT_PROPERTY_VALUE_NOT_NULL, "商品属性值不能为空");
        MAP.put(PRODUCT_SKU_PROPERTY_VALUE_NOT_NULL, "商品SKU属性值不能为空");
        MAP.put(PRODUCT_IMAGE_UPLOAD_ERROR, "商品图片上传失败");
        MAP.put(PRODUCT_IS_NULL_OR_NOT_EXISTS, "商品不存在或发生变更");
        MAP.put(PRODUCT_SKU_IS_NULL_OR_NOT_EXISTS, "商品SKU不存在或发生变更");
        MAP.put(PRODUCT_SKU_PRICE_ERROR, "商品SKU价格填写不正确");
        MAP.put(PRODUCT_SKU_COUNT_ERROR, "商品SKU数量填写不正确");
        MAP.put(PRODUCT_CATEGORY_PROPERTY_NOT_EXISTS, "商品属性不存在");
        MAP.put(PRODUCT_CATEGORY_PROPERTY_VALUE_NOT_EXISTS, "商品属性值不存在");
        MAP.put(PRODUCT_CATEGORY_PROPERTY_VALUE_ALREADY_EXISTS, "商品属性值不存在");
        MAP.put(PRODUCT_EQUIPMENT_NOT_EXISTS, "设备不存在");
        MAP.put(MATERIAL_NOT_EXISTS, "物料不存在");
        MAP.put(PRODUCT_EQUIPMENT_HAVE_NO_BULK_MATERIAL, "该设备没有散料，数据有误");
        MAP.put(PRODUCT_EQUIPMENT_IS_NOT_IDLE, "该设备没有在空闲状态");
        MAP.put(BULK_MATERIAL_IS_IN_PRODUCT_EQUIPMENT, "散料在某设备上");
        MAP.put(BULK_MATERIAL_NOT_EXISTS, "散料不存在");
        MAP.put(PRODUCT_NOT_EXISTS, "商品不存在");
        MAP.put(PRODUCT_EQUIPMENT_NOT_IN_THIS_WAREHOUSE, "商品不在该库房");
        MAP.put(PRODUCT_IS_NOT_RENT, "该商品已下架");
        MAP.put(MATERIAL_IN_USED, "物料正在被使用");
        MAP.put(PRODUCT_SKU_CAN_NOT_DELETE, "检测到您要删除商品SKU，目前SKU不支持删除。");
        MAP.put(BULK_MATERIAL_IS_NOT_IDLE, "物料没有在闲置状态。");
        MAP.put(PRODUCT_EQUIPMENT_STATUS_ERROR, "商品设备状态有误。");
        MAP.put(BULK_MATERIAL_STATUS_ERROR, "散料状态有误。");
        MAP.put(BULK_MATERIAL_NOT_IN_THIS_WAREHOUSE, "散料不在该库房。");

        MAP.put(WORKFLOW_TYPE_NOT_EXISTS, "工作流类型不存在");
        MAP.put(WORKFLOW_TEMPLATE_HAVE_NO_NODE, "此工作流模板没有节点");
        MAP.put(WORKFLOW_LINK_EXISTS, "此单工作流已经存在");
        MAP.put(WORKFLOW_LINK_NOT_EXISTS, "此单工作流不存在");
        MAP.put(WORKFLOW_LINK_HAVE_NO_DETAIL, "此单工作流没有明细");
        MAP.put(WORKFLOW_LINK_VERIFY_ALREADY_OVER, "此单工作流已经结束");
        MAP.put(WORKFLOW_NOT_BELONG_TO_YOU, "此单工作流还不该您审核");
        MAP.put(WORKFLOW_NOT_EXISTS_CLOSED, "此工作流不存在或已经关闭");
        MAP.put(WORKFLOW_NODE_NOT_EXISTS, "此工作流节点不存在");
        MAP.put(WORKFLOW_VERIFY_USER_ERROR, "此工作流审核人员有误");
        MAP.put(WORKFLOW_LINK_STATUS_ERROR, "此工作流状态有误");
        MAP.put(WORKFLOW_TEMPLATE_NOT_EXISTS, "工作流模板不存在或者已经禁用");

        MAP.put(REMARK_PATTERN, "备注信息超过限制，最多输入200个字符");
        MAP.put(ID_NOT_NULL, "ID不能为空");
        MAP.put(MONEY_MORE_THAN_ZERO, "金额不能小于0");
        MAP.put(IMAGE_NOT_EXISTS, "图片不存在");
        MAP.put(IMAGE_TYPE_ERROR, "图片类型错误");
        MAP.put(IMAGE_USED, "图片已使用");
        MAP.put(DATA_DICTIONARY_NAME_NOT_NULL, "系统字典名称不能为空");
        MAP.put(PARAM_IS_NOT_NULL, "参数不能为空");
        MAP.put(HAVE_NO_CHANGE_STATUS, "无需改变状态");
        MAP.put(STATUS_PATTERN, "未知状态");
        MAP.put(COUNT_MORE_THAN_ZERO, "数量必须大于0");
        MAP.put(DAYS_MORE_THAN_ZERO, "天数不能小于0");
        MAP.put(DATA_STATUS_ERROR, "数据状态异常");
        MAP.put(VERIFY_STATUS_ERROR, "审核状态异常");
        MAP.put(OPERATOR_IS_NOT_YOURSELF, "操作者不是本人，不能操作");
        MAP.put(PARAM_IS_NOT_ENOUGH, "信息不全，请仔细检查");
        MAP.put(NO_DUPLICATE_COMMIT, "禁止重复提交");
        MAP.put(PARAM_IS_ERROR, "参数有误，请仔细检查");

        MAP.put(PURCHASE_ORDER_ID_NOT_NULL, "采购订单ID不能为空");
        MAP.put(PRODUCT_SUPPLIER_ID_NOT_NULL, "商品供应商ID不能为空");
        MAP.put(WAREHOUSE_ID_NOT_NULL, "仓库ID不能为空");
        MAP.put(IS_INVOICE_NOT_NULL, "是否有发票字段必填");
        MAP.put(IS_NEW_NOT_NULL, "是否是全新机字段必填");
        MAP.put(PURCHASE_ORDER_PRODUCT_LIST_NOT_NULL, "采购订单商品项列表不能为空");
        MAP.put(IS_INVOICE_VALUE_ERROR, "是否有发票参数错误");
        MAP.put(IS_NEW_VALUE_ERROR, "是否是全新机参数错误");
        MAP.put(PURCHASE_ORDER_NO_NOT_NULL, "采购订单号不能为空");
        MAP.put(PURCHASE_ORDER_NOT_EXISTS, "采购订单不存在");
        MAP.put(PURCHASE_ORDER_COMMITTED_CAN_NOT_UPDATE, "待审核状态的采购单不允许修改");
        MAP.put(PURCHASE_ORDER_CANNOT_CREATE_BY_NEW_AND_AMOUNT, "拒绝创建该采购单，原因【分公司采购单不能为大于两万元的全新机】");
        MAP.put(WAREHOUSE_NOT_EXISTS, "仓库不存在");
        MAP.put(PURCHASE_ORDER_PRODUCT_CAN_NOT_REPEAT, "采购订单项重复");
        MAP.put(PURCHASE_ORDER_COMMITTED_CAN_NOT_COMMIT_AGAIN, "已提交的采购单不能再次提交");
        MAP.put(WAREHOUSE_IN_STORAGE_LIST_NOT_NULL, "商品入仓数据不能为空");
        MAP.put(WAREHOUSE_OUT_STORAGE_LIST_NOT_NULL, "商品出仓数据不能为空");
        MAP.put(USER_CAN_NOT_OP_WAREHOUSE, "您没有该仓库的操作权限");
        MAP.put(VERIFY_USER_NOT_NULL, "审核人不能为空");
        MAP.put(PURCHASE_ORDER_COMMITTED_CAN_NOT_DELETE, "已提交的采购单不能删除");
        MAP.put(PURCHASE_DELIVERY_ORDER_NO_NOT_NULL, "采购发货单编号不能为空");
        MAP.put(PURCHASE_DELIVERY_ORDER_NOT_EXISTS, "采购发货单不存在");
        MAP.put(WAREHOUSE_NO_NOT_NULL, "仓库编号不能为空");
        MAP.put(SUPPLIER_NOT_EXISTS, "供应商不存在");
        MAP.put(PURCHASE_RECEIVE_ORDER_NO_NOT_NULL, "采购收货单编号不能为空");
        MAP.put(PURCHASE_RECEIVE_ORDER_NOT_EXISTS, "采购收货单不存在");
        MAP.put(PURCHASE_RECEIVE_ORDER_AUTO_ALLOT_YES_CAN_NOT_UPDATE, "自动流转到总仓的采购发货单不能修改");
        MAP.put(PURCHASE_RECEIVE_ORDER_STATUS_YET_CAN_NOT_UPDATE, "已签单的采购发货单不能修改");
        MAP.put(PURCHASE_RECEIVE_ORDER_PRODUCT_LIST_NOT_NULL, "采购收货单商品项列表不能为空");
        MAP.put(PURCHASE_RECEIVE_ORDER_PRODUCT_REAL_COUNT_NOT_NULL, "商品SKU实际数量填写不正确");
        MAP.put(PURCHASE_RECEIVE_ORDER_PRODUCT_SKU_ID_NOT_NULL, "采购收货单商品项商品SKU_ID不能为空");
        MAP.put(STOCK_ORDER_ALREADY_EXISTS, "入库单已经存在，不能入库");
        MAP.put(PURCHASE_ORDER_STATUS_CAN_NOT_END, "当前采购单状态不允许结束");
        MAP.put(PURCHASE_ORDER_STATUS_CAN_NOT_CONTINUE, "当前采购单状态不允许继续采购");
        MAP.put(PURCHASE_RECEIVE_ORDER_STATUS_YET_CAN_NOT_IN_AGAIN, "已入库的采购收货单不允许再次入库");
        MAP.put(PURCHASE_TYPE_ERROR, "采购单类型错误");
        MAP.put(PURCHASE_TYPE_NOT_NULL, "采购单类型不能为空");
        MAP.put(PURCHASE_PRODUCT_MATERIAL_CAN_NOT_ALL_NULL, "采购单商品项和物料项不能都为空");
        MAP.put(MATERIAL_ID_NOT_NULL, "物料ID不能为空");
        MAP.put(MATERIAL_PRICE_ERROR, "物料价格填写不正确");
        MAP.put(MATERIAL_COUNT_ERROR, "物料数量填写不正确");
        MAP.put(PURCHASE_ORDER_MATERIAL_CAN_NOT_REPEAT, "采购单物料项不能重复");
        MAP.put(PURCHASE_ORDER_MATERIAL_NOT_MAIN, "采购单物料项存在非四大件");
        MAP.put(PURCHASE_ORDER_MATERIAL_NOT_GADGET, "采购单物料项存在非小配件");
        MAP.put(PURCHASE_ORDER_MATERIAL_LIST_NOT_NULL, "采购订单物料项列表不能为空");
        MAP.put(PURCHASE_ORDER_MATERIAL_CAN_NOT_CREATE, "采购的物料总额大于100元且没有发票");
        MAP.put(PURCHASE_ORDER_SKU_MATERIAL_ERROR, "SKU物料配置错误");
        MAP.put(MATERIAL_NO_NOT_NULL, "物料编号不能为空");
        MAP.put(STOCK_ALLOCATION_WAREHOUSE_IS_NOT_SAME, "库存调拨库房必须是同一间");
        MAP.put(PURCHASE_RECEIVE_ORDER_MATERIAL_ID_NOT_NULL, "采购收货单物料项物料ID不能为空");
        MAP.put(PURCHASE_RECEIVE_ORDER_MATERIAL_REAL_COUNT_NOT_NULL, "采购收货单物料项实际物料数量不能为空");
        MAP.put(MATERIAL_CAPACITY_VALUE_NOT_NULL, "物料字面量不能为空");
        MAP.put(MATERIAL_MODEL_NOT_NULL, "物料型号不能为空");
        MAP.put(MATERIAL_TYPE_HAVE_NO_MODEL, "该物料类型没有型号");
        MAP.put(PURCHASE_RECEIVE_ORDER_MATERIAL_NO_NOT_NULL, "采购收货单物料项物料编号不能为空");
        MAP.put(CUSTOMER_CONSIGN_NOT_EXISTS, "客户收货地址不存在。");


        MAP.put(ORDER_PRODUCT_LIST_NOT_NULL, "订单商品不能为空");
        MAP.put(ORDER_CUSTOMER_CONSIGN_NOT_NULL, "订单没有选择地址");
        MAP.put(ORDER_PRODUCT_STOCK_INSUFFICIENT, "库存不足");
        MAP.put(ORDER_NOT_EXISTS, "订单不存在");
        MAP.put(ORDER_PRODUCT_EQUIPMENT_NOT_NULL, "订单商品项设备编号不能为空");
        MAP.put(ORDER_PRODUCT_EQUIPMENT_STATUS_ERROR, "该设备不存在或者该设备状态异常，请仔细检查");
        MAP.put(ORDER_PRODUCT_EQUIPMENT_SKU_NOT_SAME, "该用户购买的商品不是这个型号的，请仔细检查");
        MAP.put(ORDER_PRODUCT_EQUIPMENT_IS_NULL, "订单商品项设备编号为空");
        MAP.put(ORDER_STATUS_ERROR, "订单状态不正确，请仔细检查");
        MAP.put(ORDER_PRODUCT_EQUIPMENT_COUNT_ERROR, "订单商品设备数不正确，请仔细检查");
        MAP.put(ORDER_PRODUCT_BULK_MATERIAL_COUNT_ERROR, "订单物料数不正确，请仔细检查");
        MAP.put(ORDER_RENT_TYPE_OR_LENGTH_ERROR, "订单租赁方式或时长有误");
        MAP.put(ORDER_PAY_MODE_NOT_NULL, "订单支付方式不能为空");
        MAP.put(ORDER_PAY_STATUS_ERROR, "付款单状态异常，请检查");
        MAP.put(ORDER_REFUND_STATUS_ERROR, "退款单状态异常，请检查");
        MAP.put(ORDER_PAY_RECORD_NOT_EXISTS, "支付记录不存在");
        MAP.put(ORDER_HAVE_NO_RENT_START_TIME, "订单没有租赁开始时间");
        MAP.put(DEPLOYMENT_ORDER_PRODUCT_EQUIPMENT_STOCK_NOT_ENOUGH, "货物调拨该库房该商品库存不足。");
        MAP.put(DEPLOYMENT_ORDER_BULK_MATERIAL_STOCK_NOT_ENOUGH, "货物调拨该库房该物料库存不足。");
        MAP.put(DEPLOYMENT_ORDER_STATUS_ERROR, "调配单状态有误。");
        MAP.put(DEPLOYMENT_ORDER_HAVE_NO_THIS_ITEM, "调拨单没有这个SKU或物料单项。");
        MAP.put(ORDER_STATUS_NOT_PROCESSED, "订单未处于备货完成状态。");
        MAP.put(ORDER_PRODUCT_AMOUNT_ERROR, "订单商品价格有误。");
        MAP.put(ORDER_MATERIAL_AMOUNT_ERROR, "订单物料价格有误。");
        MAP.put(ORDER_HAVE_NO_THIS_ITEM, "订单没有这个SKU或物料单项。");
        MAP.put(ORDER_PRODUCT_EQUIPMENT_COUNT_MAX, "订单商品项的设备数已经达到最大。");
        MAP.put(ORDER_MATERIAL_BULK_COUNT_MAX, "订单物料项散料数已经达到最大。");

        MAP.put(CUSTOMER_COMPANY_NOT_NULL, "企业客户信息不能为空");
        MAP.put(CUSTOMER_COMPANY_NAME_NOT_NULL, "企业公司名称不能为空");
        MAP.put(CUSTOMER_PERSON_NOT_NULL, "客户个人信息不能为空");
        MAP.put(CUSTOMER_PERSON_NAME_NOT_NULL, "客户个人姓名不能为空");
        MAP.put(CUSTOMER_TYPE_NOT_NULL, "客户类型不能为空");
        MAP.put(CUSTOMER_COMPANY_CONNECT_NAME_NOT_NULL, "企业客户联系人姓名不能为空");
        MAP.put(CUSTOMER_NOT_NULL, "客户不存在");
        MAP.put(CUSTOMER_NO_NOT_NULL, "客户编号不能为空");
        MAP.put(CUSTOMER_RISK_MANAGEMENT_ID_NOT_NULL, "风控ID不能为空");
        MAP.put(CUSTOMER_RISK_MANAGEMENT_CREDIT_AMOUNT_NOT_NULL, "授信额度不能为空");
        MAP.put(CUSTOMER_RISK_MANAGEMENT_DEPOSIT_CYCLE_NOT_NULL, "押金期数不能为空");
        MAP.put(CUSTOMER_RISK_MANAGEMENT_PAYMENT_CYCLE_NOT_NULL, "付款期数不能为空");
        MAP.put(CUSTOMER_NOT_EXISTS, "客户不存在");
        MAP.put(CUSTOMER_RISK_MANAGEMENT_CREDIT_AMOUNT_ERROR, "授信额度不能小于0");
        MAP.put(CUSTOMER_RISK_MANAGEMENT_DEPOSIT_CYCLE_ERROR, "押金期数不能小于0");
        MAP.put(CUSTOMER_RISK_MANAGEMENT_PAYMENT_CYCLE_ERROR, "付款期数不能小于1");
        MAP.put(CUSTOMER_RISK_MANAGEMENT_NOT_EXISTS, "风控信息不存在");
        MAP.put(CUSTOMER_HAVE_NO_RETURN, "客户没有可归还商品");
        MAP.put(CUSTOMER_NOT_RENT_THIS, "客户没有租赁该商品");
        MAP.put(CUSTOMER_RETURN_TOO_MORE, "客户退还商品数超过限制");
        MAP.put(CONSIGNEE_NAME_NOT_NULL, "收货人姓名不能为空");
        MAP.put(CONSIGNEE_PHONE_NOT_NULL, "收货人电话不能为空");
        MAP.put(PROVINCE_ID_NOT_NULL, "省份ID不能为空");
        MAP.put(CITY_ID_NOT_NULL, "城市ID不能为空");
        MAP.put(DISTRICT_ID_NOT_NULL, "街道ID不能为空");
        MAP.put(ADDRESS_NOT_NULL, "地址不能为空");
        MAP.put(RETURN_COUNT_ERROR, "退还数量必须大于1");
        MAP.put(RETURN_ORDER_NO_NOT_NULL, "退还单号不能为空");
        MAP.put(EQUIPMENT_NO_NOT_NULL, "设备编号不能为空");
        MAP.put(RETURN_ORDER_NO_EXISTS, "退还单不存在");
        MAP.put(EQUIPMENT_NOT_EXISTS, "设备不存在");
        MAP.put(EQUIPMENT_NOT_RENT, "该设备不是客户在租设备，不能退还");
        MAP.put(RETURN_ORDER_IS_CHARGING_IS_NOT_NULL, "是否计租赁费用不能为空");
        MAP.put(PRODUCT_SKU_CAN_NOT_REPEAT, "SKU不能重复");
        MAP.put(MATERIAL_CAN_NOT_REPEAT, "物料不能重复");

        MAP.put(MESSAGE_TITLE_NOT_NULL,"站内信标题不能为空");
        MAP.put(MESSAGE_CONTENT_NOT_NULL,"站内信内容不能为空");
        MAP.put(MESSAGE_RECEIVER_NOT_NULL,"站内信收件人不能为空");
        MAP.put(MESSAGE_ID_NOT_NULL,"站内信ID不能为空");
        MAP.put(MESSAGE_NOT_EXISTS,"站内信不存在");
        MAP.put(MESSAGE_CAN_NOT_SEND_SELF,"站内信不能发给自己");

    }

    public static String getMessage(String code) {
        return MAP.get(code);
    }

    public static String getMessage(String code, String parmName) {
        return MAP.get(code) + " : " + parmName;
    }

    public static String clear(String code) {
        return MAP.put(code, "");
    }
}

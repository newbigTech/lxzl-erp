package com.lxzl.erp.web.controller;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.order.*;
import com.lxzl.erp.common.domain.order.pojo.ExchangeOrder;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.validGroup.AddGroup;
import com.lxzl.erp.common.domain.validGroup.UpdateGroup;
import com.lxzl.erp.core.annotation.ControllerLog;
import com.lxzl.erp.core.component.ResultGenerator;
import com.lxzl.erp.core.service.order.ExchangeOrderService;
import com.lxzl.erp.core.service.order.OrderService;
import com.lxzl.se.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.lxzl.se.common.domain.Result;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 描述: 订单控制器
 *
 * @author gaochao
 * @date 2017-11-15 14:16
 */
@Controller
@ControllerLog
@RequestMapping("/order")
public class OrderController extends BaseController {

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public Result create(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.createOrder(order);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Result update(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.updateOrder(order);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "commit", method = RequestMethod.POST)
    public Result commit(@RequestBody OrderCommitParam orderCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.commitOrder(orderCommitParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public Result pay(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.payOrder(order.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryOrderByNo", method = RequestMethod.POST)
    public Result queryOrderByNo(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, Order> serviceResult = orderService.queryOrderByNo(order.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    public Result cancel(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.cancelOrder(order.getOrderNo(),order.getCancelOrderReasonType());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "forceCancel", method = RequestMethod.POST)
    public Result forceCancel(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.forceCancelOrder(order.getOrderNo(),order.getCancelOrderReasonType());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryAllOrder", method = RequestMethod.POST)
    public Result queryAllOrder(@RequestBody OrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<Order>> serviceResult = orderService.queryAllOrder(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "confirm", method = RequestMethod.POST)
    public Result confirm(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.confirmOrder(order.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "process", method = RequestMethod.POST)
    public Result process(@RequestBody ProcessOrderParam param, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.processOrder(param);
        return resultGenerator.generate(serviceResult);
    }

    @RequestMapping(value = "delivery", method = RequestMethod.POST)
    public Result delivery(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.deliveryOrder(order);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "isNeedVerify", method = RequestMethod.POST)
    public Result isNeedVerify(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, Boolean> serviceResult = orderService.isNeedVerify(order.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "queryLastPrice", method = RequestMethod.POST)
    public Result queryLastPrice(@RequestBody LastRentPriceRequest request, BindingResult validResult) {
        ServiceResult<String, LastRentPriceResponse> serviceResult = orderService.queryLastPrice(request);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "returnEquipment", method = RequestMethod.POST)
    public Result returnEquipment(@RequestBody Map<String, String> map, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.returnEquipment(map.get("orderNo"), map.get("returnEquipmentNo"), map.get("changeEquipmentNo"), new Date());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "createOrderFirstPayAmount", method = RequestMethod.POST)
    public Result createOrderFirstPayAmount(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, Order> serviceResult = orderService.createOrderFirstPayAmount(order);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    @RequestMapping(value = "processStatementOrderByCancel", method = RequestMethod.POST)
    public Result processStatementOrderByCancel(@RequestBody Order order, BindingResult validResult) {
        return resultGenerator.generate(orderService.processStatementOrderByCancel(order.getOrderNo()));
    }
    @RequestMapping(value = "addOrderMessage", method = RequestMethod.POST)
    public Result addOrderMessage(@RequestBody Order order, BindingResult validResult) {
        return resultGenerator.generate(orderService.addOrderMessage(order));
    }
    /**
     * 当前用户待审核订单分页
     * @Author : sunzhipeng
     * @param param
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryVerifyOrder", method = RequestMethod.POST)
    public Result queryVerifyOrder(@RequestBody VerifyOrderQueryParam param, BindingResult validResult) {
        ServiceResult<String, Page<Order>> serviceResult = orderService.queryVerifyOrder(param);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    @RequestMapping(value = "addReturnOrderToTimeAxis", method = RequestMethod.POST)
    public Result addReturnOrderToTimeAxis() {
        ServiceResult<String, String> serviceResult = orderService.addReturnOrderToTimeAxis();
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    // 创建订单（包括组合商品）使用新接口避免影响
    @RequestMapping(value = "createNew", method = RequestMethod.POST)
    public Result createNew(@RequestBody @Validated({AddGroup.class}) Order order, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.createOrderNew(order);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    // 查询订单详情（包括组合商品） 使用新接口避免影响
    @RequestMapping(value = "queryOrderByNoNew", method = RequestMethod.POST)
    public Result queryOrderByNoNew(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, Order> serviceResult = orderService.queryOrderByNoNew(order.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    // 更新订单（包括组合商品） 使用新接口避免影响
    @RequestMapping(value = "updateNew", method = RequestMethod.POST)
    public Result updateNew(@RequestBody @Validated({UpdateGroup.class}) Order order, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.updateOrderNew(order);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 确认收货时发生退货修改订单并推送K3保存更改记录
     * @Author : sunzhipeng
     * @param orderConfirmChangeParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "confirmChangeOrder", method = RequestMethod.POST)
    public Result confirmChangeOrder(@RequestBody  OrderConfirmChangeParam orderConfirmChangeParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.confirmChangeOrder(orderConfirmChangeParam);
        return resultGenerator.generate(serviceResult);
    }

    /**
     * 超级管理员修改订单
     * @Author : sunzhipeng
     * @param orderConfirmChangeParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "supperUserChangeOrder", method = RequestMethod.POST)
    public Result supperUserChangeOrder(@RequestBody  OrderConfirmChangeParam orderConfirmChangeParam, BindingResult validResult) {
        return resultGenerator.generate(orderService.supperUserChangeOrder(orderConfirmChangeParam));
    }

    /**
     * 修改订单商品和配件单价，并重算订单
     *
     * @author ZhaoZiXuan
     * @date 2018/8/1 17:53
     * @param
     * @return
     */
    @RequestMapping(value = "updateOrderPrice", method = RequestMethod.POST)
    public Result updateOrderPrice(@RequestBody  Order order, BindingResult validResult) {
        return resultGenerator.generate(orderService.updateOrderPrice(order));
    }

    /**
     * 测试机订单转为租赁订单
     * @param order
     * @param validResult
     * @return
     */
    @RequestMapping(value = "testMachineOrderConvertOrder", method = RequestMethod.POST)
    public Result testMachineOrderConvertOrder(@RequestBody Order order, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = orderService.testMachineOrderConvertOrder(order);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


    /**
     * 创建中途修改单价、支付方式、租赁方式。等变更订单
     *
     * @param exchangeOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "exchangeOrder", method = RequestMethod.POST)
    public Result exchangeOrder(@RequestBody ExchangeOrder exchangeOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = exchangeOrderService.createExchangeOrder(exchangeOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 提交变更单
     *
     * @param exchangeOrderCommitParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "commitExchangeOrder", method = RequestMethod.POST)
    public Result commitExchangeOrder(@RequestBody ExchangeOrderCommitParam exchangeOrderCommitParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = exchangeOrderService.commitExchangeOrder(exchangeOrderCommitParam);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 查询变更单
     *
     * @param exchangeOrderParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryExchangeOrderByNo", method = RequestMethod.POST)
    public Result queryExchangeOrderByNo(@RequestBody ExchangeOrderParam exchangeOrderParam, BindingResult validResult) {
        ServiceResult<String, ExchangeOrder> serviceResult = exchangeOrderService.queryExchangeOrderByNo(exchangeOrderParam.getExchangeOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 根据订单号查询变更单列表
     *
     * @param exchangeOrderParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "queryByOrderNo", method = RequestMethod.POST)
    public Result queryByOrderNo(@RequestBody ExchangeOrderParam exchangeOrderParam, BindingResult validResult) {
        ServiceResult<String, Page<ExchangeOrder>> serviceResult = exchangeOrderService.queryByOrderNo(exchangeOrderParam.getOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 取消变更单
     *
     * @param exchangeOrderParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "cancelExchangeOrder", method = RequestMethod.POST)
    public Result cancelExchangeOrder(@RequestBody ExchangeOrderParam exchangeOrderParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = exchangeOrderService.cancelExchangeOrder(exchangeOrderParam.getExchangeOrderNo());
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }

    /**
     * 手动触发变更单单
     *
     * @param exchangeOrderParam
     * @param validResult
     * @return
     */
    @RequestMapping(value = "generatedOrder", method = RequestMethod.POST)
    public Result generatedOrder(@RequestBody ExchangeOrderParam exchangeOrderParam, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = exchangeOrderService.generatedOrder(exchangeOrderParam.getExchangeOrderNo(),true);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }
    /**
     * 修改变更单
     *
     * @param exchangeOrder
     * @param validResult
     * @return
     */
    @RequestMapping(value = "updateExchangeOrder", method = RequestMethod.POST)
    public Result updateExchangeOrder(@RequestBody ExchangeOrder exchangeOrder, BindingResult validResult) {
        ServiceResult<String, String> serviceResult = exchangeOrderService.updateExchangeOrder(exchangeOrder);
        return resultGenerator.generate(serviceResult.getErrorCode(), serviceResult.getResult());
    }


    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ExchangeOrderService exchangeOrderService;
}

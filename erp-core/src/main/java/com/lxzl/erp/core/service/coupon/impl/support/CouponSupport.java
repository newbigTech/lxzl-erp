package com.lxzl.erp.core.service.coupon.impl.support;

import com.lxzl.erp.common.constant.CouponStatus;
import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.erp.common.domain.coupon.pojo.Coupon;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.order.pojo.OrderProduct;
import com.lxzl.erp.common.util.BigDecimalUtil;
import com.lxzl.erp.common.util.CollectionUtil;
import com.lxzl.erp.common.util.ConverterUtil;
import com.lxzl.erp.core.service.user.impl.support.UserSupport;
import com.lxzl.erp.dataaccess.dao.mysql.coupon.CouponBatchDetailMapper;
import com.lxzl.erp.dataaccess.dao.mysql.coupon.CouponBatchMapper;
import com.lxzl.erp.dataaccess.dao.mysql.coupon.CouponMapper;
import com.lxzl.erp.dataaccess.domain.coupon.CouponBatchDO;
import com.lxzl.erp.dataaccess.domain.coupon.CouponBatchDetailDO;
import com.lxzl.erp.dataaccess.domain.coupon.CouponDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: Sunzhipeng
 * @Description:
 * @Date: Created in 2018\4\10 0010 14:14
 */
@Component
public class CouponSupport {
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private CouponBatchMapper couponBatchMapper;
    @Autowired
    private CouponBatchDetailMapper couponBatchDetailMapper;
    @Autowired
    private UserSupport userSupport;
    /**
     * 使用优惠券
     * @param order
     * @param couponList
     * @return
     */
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String useCoupon(Order order, List<Coupon> couponList){
        Date date = new Date();
        if (order == null) {
            return ErrorCode.PARAM_IS_ERROR;
        }
        if (CollectionUtil.isEmpty(couponList)) {
            return ErrorCode.COUPON_LIST_IS_EMPTY;
        }
        List<OrderProduct> orderProductList = order.getOrderProductList();
        //订单中设备总数量
        Integer totalProductCount = 0;
        for (int i = 0; i < orderProductList.size(); i++) {
            totalProductCount+=orderProductList.get(i).getProductCount();
        }
        //所选优惠券数量大于设备总数量返回错误信息
        if (couponList.size()>totalProductCount) {
            return ErrorCode.COUPON_AMOUNT_TOO_MANY;
        }
        //按照订单商品项中的商品单价对集合进行由大到小的排序
        Collections.sort(orderProductList, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                OrderProduct orderProduct1 = (OrderProduct)o1;
                OrderProduct orderProduct2 = (OrderProduct)o2;
                if(orderProduct1.getProductUnitAmount().intValue()<orderProduct2.getProductUnitAmount().intValue()){
                    return 1;
                }else if(orderProduct1.getProductUnitAmount().intValue()==orderProduct2.getProductUnitAmount().intValue()){
                    return 0;
                }else{
                    return -1;
                }
            }
        });
        List<CouponDO> couponDOList = ConverterUtil.convertList(couponList, CouponDO.class);
        //按照优惠卷集合中优惠券的优惠金额进行由大到小的排序
        Collections.sort(couponDOList, new Comparator(){
            @Override
            public int compare(Object o1, Object o2) {
                CouponDO couponDO1 = (CouponDO)o1;
                CouponDO couponDO2 = (CouponDO)o2;
                if(couponDO1.getFaceValue().intValue()<couponDO2.getFaceValue().intValue()){
                    return 1;
                }else if(couponDO1.getFaceValue().intValue()==couponDO2.getFaceValue().intValue()){
                    return 0;
                }else{
                    return -1;
                }
            }
        });
        Map<Integer,CouponBatchDetailDO> couponBatchDetailDOMap = new HashMap<>();
        Map<Integer,CouponBatchDO> couponBatchDOMap = new HashMap<>();
        int couponCount = 0;
        outterLoop:for (int i = 0; i < orderProductList.size(); i++) {
            for (int j = 0; j < orderProductList.get(i).getProductCount(); j++) {
                couponDOList.get(couponCount).setOrderId(orderProductList.get(i).getOrderId());
                couponDOList.get(couponCount).setOrderNo(order.getOrderNo());
                couponDOList.get(couponCount).setOrderProductId(orderProductList.get(i).getOrderProductId());
                BigDecimal deductionAmount ;
                if (couponDOList.get(couponCount).getFaceValue().compareTo(orderProductList.get(i).getProductUnitAmount())==1) {
                    deductionAmount=orderProductList.get(i).getProductUnitAmount();
                } else{
                    deductionAmount=couponDOList.get(couponCount).getFaceValue();
                }
                couponDOList.get(couponCount).setDeductionAmount(deductionAmount);
                couponDOList.get(couponCount).setCouponStatus(CouponStatus.COUPON_STATUS_USED);
                couponDOList.get(couponCount).setUpdateTime(date);
                couponDOList.get(couponCount).setUseTime(date);
                couponDOList.get(couponCount).setUpdateUser(userSupport.getCurrentUserId().toString());
                //记录增券状态
                if (couponBatchDetailDOMap.containsKey(couponDOList.get(couponCount).getCouponBatchDetailId())) {
                    CouponBatchDetailDO couponBatchDetailDO=couponBatchDetailDOMap.get(couponDOList.get(couponCount).getCouponBatchDetailId());
                    couponBatchDetailDO.setCouponUsedCount(couponBatchDetailDO.getCouponUsedCount()+1);
                    couponBatchDetailDO.setTotalUsedAmount(BigDecimalUtil.add(couponBatchDetailDO.getTotalUsedAmount(),couponDOList.get(couponCount).getFaceValue()));
                    couponBatchDetailDO.setTotalDeductionAmount(BigDecimalUtil.add(couponBatchDetailDO.getTotalDeductionAmount(),couponDOList.get(couponCount).getDeductionAmount()));
                    couponBatchDetailDO.setUpdateTime(date);
                    couponBatchDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    couponBatchDetailDOMap.put(couponDOList.get(couponCount).getCouponBatchDetailId(),couponBatchDetailDO);
                }else{
                    CouponBatchDetailDO couponBatchDetailDO = new CouponBatchDetailDO();
                    couponBatchDetailDO.setId(couponDOList.get(couponCount).getCouponBatchDetailId());
                    couponBatchDetailDO.setCouponUsedCount(1);
                    couponBatchDetailDO.setTotalUsedAmount(couponDOList.get(couponCount).getFaceValue());
                    couponBatchDetailDO.setTotalDeductionAmount(couponDOList.get(couponCount).getDeductionAmount());
                    couponBatchDetailDO.setUpdateTime(date);
                    couponBatchDetailDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    couponBatchDetailDOMap.put(couponDOList.get(couponCount).getCouponBatchDetailId(),couponBatchDetailDO);
                }
                //记录批次状态
                if (couponBatchDOMap.containsKey(couponDOList.get(couponCount).getCouponBatchId())) {
                    CouponBatchDO couponBatchDO = couponBatchDOMap.get(couponDOList.get(couponCount).getCouponBatchId());
                    couponBatchDO.setCouponBatchUsedCount(couponBatchDO.getCouponBatchUsedCount()+1);
                    couponBatchDO.setTotalUsedAmount(BigDecimalUtil.add(couponBatchDO.getTotalUsedAmount(),couponDOList.get(couponCount).getFaceValue()));
                    couponBatchDO.setTotalDeductionAmount(BigDecimalUtil.add(couponBatchDO.getTotalDeductionAmount(),deductionAmount));
                    couponBatchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    couponBatchDO.setUpdateTime(date);
                    couponBatchDOMap.put(couponDOList.get(couponCount).getCouponBatchId(),couponBatchDO);
                }else{
                    CouponBatchDO couponBatchDO = new CouponBatchDO();
                    couponBatchDO.setId(couponDOList.get(couponCount).getCouponBatchId());
                    couponBatchDO.setCouponBatchUsedCount(1);
                    couponBatchDO.setTotalUsedAmount(couponDOList.get(couponCount).getFaceValue());
                    couponBatchDO.setTotalDeductionAmount(deductionAmount);
                    couponBatchDO.setUpdateUser(userSupport.getCurrentUserId().toString());
                    couponBatchDO.setUpdateTime(date);
                    couponBatchDOMap.put(couponDOList.get(couponCount).getCouponBatchId(),couponBatchDO);
                }
                couponCount++;
                if (couponCount>=couponDOList.size()) {
                    break outterLoop;
                }
            }
        }
        couponMapper.updateUseList(couponDOList);
        // 获取需要更改的优惠券批次详情对象的集合
        List<CouponBatchDetailDO> couponBatchDetailDOList = new ArrayList<>();
        Iterator it = couponBatchDetailDOMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            CouponBatchDetailDO couponBatchDetailDO = (CouponBatchDetailDO) entry.getValue();
            couponBatchDetailDOList.add(couponBatchDetailDO);
        }
        couponBatchDetailMapper.updateUseList(couponBatchDetailDOList);
        List<CouponBatchDO> couponBatchDOList = new ArrayList<>();
        Iterator iq = couponBatchDOMap.entrySet().iterator();
        while (iq.hasNext()) {
            Map.Entry entry = (Map.Entry) iq.next();
            CouponBatchDO couponBatchDO = (CouponBatchDO) entry.getValue();
            couponBatchDOList.add(couponBatchDO);
        }
        couponBatchMapper.updateUseList(couponBatchDOList);
        return ErrorCode.SUCCESS;
    }
}
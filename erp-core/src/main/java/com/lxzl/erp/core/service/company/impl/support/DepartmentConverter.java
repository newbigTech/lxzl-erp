package com.lxzl.erp.core.service.company.impl.support;

import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.dataaccess.domain.company.DepartmentDO;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-03 10:35
 */
public class DepartmentConverter {
    public static List<DepartmentDO> convertTree(List<DepartmentDO> departmentDOList) {
        List<DepartmentDO> nodeList = new ArrayList<>();
        if (departmentDOList != null) {
            for (DepartmentDO node1 : departmentDOList) {
                if (node1.getParentDepartmentId().equals(CommonConstant.SUPER_DEPARTMENT_ID)) {
                    nodeList.add(node1);
                }
                for (DepartmentDO t : departmentDOList) {
                    if (t.getParentDepartmentId().equals(node1.getId())) {
                        if (node1.getChildren() == null) {
                            List<DepartmentDO> myChildren = new ArrayList<DepartmentDO>();
                            myChildren.add(t);
                            node1.setChildren(myChildren);
                        } else {
                            node1.getChildren().add(t);
                        }
                    }
                }
            }
        }

        return nodeList;
    }
}

package com.hrp.cost.service.impl;

import com.hrp.cost.api.domain.HospitalDept;
import com.hrp.cost.mapper.HospitalDeptMapper;
import com.hrp.cost.service.IHospitalDeptService;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.SpringUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.datascope.annotation.DataScope;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.domain.vo.TreeSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: Cost Accounting
 * @author: neo
 * @create: 2026-04-23 22:27
 **/
@Service
public class HospitalDeptServiceImpl implements IHospitalDeptService {
    @Autowired
    private HospitalDeptMapper deptMapper;
    
    @Override
    @DataScope(deptAlias = "d")
    public List<HospitalDept> selectDeptList(HospitalDept dept) {
        return deptMapper.selectDeptList(dept);
    }

    /**
     * 查询部门树结构信息
     * 
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    @Override
    public List<TreeSelect> selectDeptTreeList(HospitalDept dept)
    {
        List<HospitalDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
        return buildDeptTreeSelect(depts);
    }

    /**
     * 构建前端所需要树结构
     * 
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<HospitalDept> buildDeptTree(List<HospitalDept> depts)
    {
        List<HospitalDept> returnList = new ArrayList<HospitalDept>();
        List<String> tempList = depts.stream().map(HospitalDept::getDm).collect(Collectors.toList());
        for (HospitalDept dept : depts)
        {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getFldm()))
            {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     * 
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<HospitalDept> depts)
    {
        List<HospitalDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    @Override
    public HospitalDept selectDeptById(String deptId)
    {
        return deptMapper.selectDeptById(deptId);
    }

    @Override
    public boolean checkDeptNameUnique(HospitalDept dept)
    {
        HospitalDept info = deptMapper.checkDeptNameUnique(dept.getMc(), dept.getFldm());
        if (StringUtils.isNotNull(info) && !info.getDm().equals(dept.getDm()))
        {
            return false;
        }
        return true;
    }

    @Override
    public void checkDeptDataScope(String deptId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()) && StringUtils.isNotEmpty(deptId))
        {
            HospitalDept dept = new HospitalDept();
            dept.setDm(deptId);
            List<HospitalDept> depts = SpringUtils.getAopProxy(this).selectDeptList(dept);
            if (StringUtils.isEmpty(depts))
            {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

    @Override
    public int selectNormalChildrenDeptById(String deptId)
    {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }

    @Override
    public boolean hasChildByDeptId(String deptId)
    {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0;
    }

    @Override
    public boolean checkDeptExistUser(String deptId)
    {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0;
    }

    @Override
    public int insertDept(HospitalDept dept)
    {
        HospitalDept parentDept = null;
        if (StringUtils.isNotEmpty(dept.getFldm())) {
            parentDept = deptMapper.selectDeptById(dept.getFldm());
        }
        
        if (parentDept != null && parentDept.getMjpb() != null && parentDept.getMjpb() == 1) {
            parentDept.setMjpb(0);
            parentDept.setXzdm(0);
            deptMapper.updateDept(parentDept);
        }
        
        return deptMapper.insertDept(dept);
    }

    @Override
    public int updateDept(HospitalDept dept)
    {
        return deptMapper.updateDept(dept);
    }

    @Override
    public int deleteDeptById(String deptId)
    {
        return deptMapper.deleteDeptById(deptId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<HospitalDept> list, HospitalDept t)
    {
        // 得到子节点列表
        List<HospitalDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (HospitalDept tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<HospitalDept> getChildList(List<HospitalDept> list, HospitalDept t)
    {
        List<HospitalDept> tlist = new ArrayList<HospitalDept>();
        Iterator<HospitalDept> it = list.iterator();
        while (it.hasNext())
        {
            HospitalDept n = it.next();
            if (StringUtils.isNotNull(n.getFldm()) && n.getFldm().equals(t.getDm()))
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<HospitalDept> list, HospitalDept t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }

}

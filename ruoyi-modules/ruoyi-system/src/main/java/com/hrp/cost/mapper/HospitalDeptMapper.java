package com.hrp.cost.mapper;

import com.hrp.cost.api.domain.HospitalDept;
import com.ruoyi.system.api.domain.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门管理 数据层
 * 
 * @author ruoyi
 */
public interface HospitalDeptMapper
{
    /**
     * 查询部门管理数据
     * 
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<HospitalDept> selectDeptList(HospitalDept dept);


    public HospitalDept selectDeptById(String dm);

    /**
     * 根据ID查询所有子部门
     * 
     * @param dm 部门代码
     * @return 部门列表
     */
    public List<HospitalDept> selectChildrenDeptById(String dm);

    /**
     * 根据ID查询所有子部门（正常状态）
     * 
     * @param dm 部门代码
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(String dm);

    /**
     * 是否存在子节点
     * 
     * @param dm 部门代码
     * @return 结果
     */
    public int hasChildByDeptId(String dm);

    /**
     * 查询部门是否存在用户
     * 
     * @param dm 部门代码
     * @return 结果
     */
    public int checkDeptExistUser(String dm);

    /**
     * 校验部门名称是否唯一
     * 
     * @param mc 部门名称
     * @param fldm 分类代码
     * @return 结果
     */
    public HospitalDept checkDeptNameUnique(@Param("mc") String mc, @Param("fldm") String fldm);

    /**
     * 新增部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(HospitalDept dept);

    /**
     * 修改部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(HospitalDept dept);

    /**
     * 删除部门管理信息
     * 
     * @param dm 部门代码
     * @return 结果
     */
    public int deleteDeptById(String dm);
}

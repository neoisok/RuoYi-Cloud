package com.hrp.cost.service;

import com.hrp.cost.api.domain.HrpEmployee;

import java.util.List;

/**
 * 职工信息管理 服务层
 * 
 * @author ruoyi
 */
public interface IHrpEmployeeService
{
    /**
     * 查询职工信息列表
     * 
     * @param employee 职工信息
     * @return 职工信息集合
     */
    public List<HrpEmployee> selectEmployeeList(HrpEmployee employee);

    /**
     * 根据工号查询职工信息
     * 
     * @param zggh 职工工号
     * @return 职工信息
     */
    public HrpEmployee selectEmployeeById(String zggh);

    /**
     * 校验职工工号是否唯一
     * 
     * @param employee 职工信息
     * @return 结果
     */
    public boolean checkEmployeeUnique(HrpEmployee employee);

    /**
     * 校验手机号码是否唯一
     * 
     * @param employee 职工信息
     * @return 结果
     */
    public boolean checkPhoneUnique(HrpEmployee employee);

    /**
     * 校验身份证是否唯一
     * 
     * @param employee 职工信息
     * @return 结果
     */
    public boolean checkIdCardUnique(HrpEmployee employee);

    /**
     * 新增保存职工信息
     * 
     * @param employee 职工信息
     * @return 结果
     */
    public int insertEmployee(HrpEmployee employee);

    /**
     * 修改保存职工信息
     * 
     * @param employee 职工信息
     * @return 结果
     */
    public int updateEmployee(HrpEmployee employee);

    /**
     * 删除职工信息
     * 
     * @param zggh 职工工号
     * @return 结果
     */
    public int deleteEmployeeById(String zggh);

    /**
     * 批量删除职工信息
     * 
     * @param zgghs 需要删除的职工工号数组
     * @return 结果
     */
    public int deleteEmployeeByIds(String[] zgghs);

    /**
     * 导入职工信息数据
     * 
     * @param employeeList 职工数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importEmployee(List<HrpEmployee> employeeList, Boolean isUpdateSupport, String operName);
}

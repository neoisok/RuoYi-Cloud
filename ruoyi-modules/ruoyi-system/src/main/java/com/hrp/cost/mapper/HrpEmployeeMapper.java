package com.hrp.cost.mapper;

import com.hrp.cost.api.domain.HrpEmployee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 职工信息管理 数据层
 * 
 * @author ruoyi
 */
public interface HrpEmployeeMapper
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
     * @param zggh 职工工号
     * @return 结果
     */
    public HrpEmployee checkEmployeeUnique(String zggh);

    /**
     * 校验手机号码是否唯一
     * 
     * @param sjhm 手机号码
     * @return 结果
     */
    public HrpEmployee checkPhoneUnique(String sjhm);

    /**
     * 校验身份证是否唯一
     * 
     * @param sfz 身份证
     * @return 结果
     */
    public HrpEmployee checkIdCardUnique(String sfz);

    /**
     * 新增职工信息
     * 
     * @param employee 职工信息
     * @return 结果
     */
    public int insertEmployee(HrpEmployee employee);

    /**
     * 修改职工信息
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
}

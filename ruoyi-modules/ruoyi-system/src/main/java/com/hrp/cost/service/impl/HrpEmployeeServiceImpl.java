package com.hrp.cost.service.impl;

import com.hrp.cost.api.domain.HrpEmployee;
import com.hrp.cost.mapper.HrpEmployeeMapper;
import com.hrp.cost.service.IHrpEmployeeService;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 职工信息管理 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class HrpEmployeeServiceImpl implements IHrpEmployeeService
{
    @Autowired
    private HrpEmployeeMapper employeeMapper;

    /**
     * 查询职工信息列表
     * 
     * @param employee 职工信息
     * @return 职工信息集合
     */
    @Override
    public List<HrpEmployee> selectEmployeeList(HrpEmployee employee)
    {
        return employeeMapper.selectEmployeeList(employee);
    }

    /**
     * 根据工号查询职工信息
     * 
     * @param zggh 职工工号
     * @return 职工信息
     */
    @Override
    public HrpEmployee selectEmployeeById(String zggh)
    {
        return employeeMapper.selectEmployeeById(zggh);
    }

    /**
     * 校验职工工号是否唯一
     * 
     * @param employee 职工信息
     * @return 结果
     */
    @Override
    public boolean checkEmployeeUnique(HrpEmployee employee)
    {
        HrpEmployee info = employeeMapper.checkEmployeeUnique(employee.getZggh());
        if (StringUtils.isNotNull(info) && !info.getZggh().equals(employee.getZggh()))
        {
            return false;
        }
        return true;
    }

    /**
     * 校验手机号码是否唯一
     * 
     * @param employee 职工信息
     * @return 结果
     */
    @Override
    public boolean checkPhoneUnique(HrpEmployee employee)
    {
        HrpEmployee info = employeeMapper.checkPhoneUnique(employee.getSjhm());
        if (StringUtils.isNotNull(info) && !info.getZggh().equals(employee.getZggh()))
        {
            return false;
        }
        return true;
    }

    /**
     * 校验身份证是否唯一
     * 
     * @param employee 职工信息
     * @return 结果
     */
    @Override
    public boolean checkIdCardUnique(HrpEmployee employee)
    {
        HrpEmployee info = employeeMapper.checkIdCardUnique(employee.getSfz());
        if (StringUtils.isNotNull(info) && !info.getZggh().equals(employee.getZggh()))
        {
            return false;
        }
        return true;
    }

    /**
     * 新增保存职工信息
     * 
     * @param employee 职工信息
     * @return 结果
     */
    @Override
    public int insertEmployee(HrpEmployee employee)
    {
        return employeeMapper.insertEmployee(employee);
    }

    /**
     * 修改保存职工信息
     * 
     * @param employee 职工信息
     * @return 结果
     */
    @Override
    public int updateEmployee(HrpEmployee employee)
    {
        return employeeMapper.updateEmployee(employee);
    }

    /**
     * 删除职工信息
     * 
     * @param zggh 职工工号
     * @return 结果
     */
    @Override
    public int deleteEmployeeById(String zggh)
    {
        return employeeMapper.deleteEmployeeById(zggh);
    }

    /**
     * 批量删除职工信息
     * 
     * @param zgghs 需要删除的职工工号数组
     * @return 结果
     */
    @Override
    public int deleteEmployeeByIds(String[] zgghs)
    {
        return employeeMapper.deleteEmployeeByIds(zgghs);
    }

    /**
     * 导入职工信息数据
     * 
     * @param employeeList 职工数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importEmployee(List<HrpEmployee> employeeList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(employeeList) || employeeList.size() == 0)
        {
            throw new ServiceException("导入职工数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (HrpEmployee employee : employeeList)
        {
            try
            {
                // 验证是否存在这个职工
                HrpEmployee e = employeeMapper.checkEmployeeUnique(employee.getZggh());
                if (StringUtils.isNull(e))
                {
                    employeeMapper.insertEmployee(employee);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、职工工号 " + employee.getZggh() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    employeeMapper.updateEmployee(employee);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、职工工号 " + employee.getZggh() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、职工工号 " + employee.getZggh() + " 已存在");
                }
            }
            catch (Exception ex)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、职工工号 " + employee.getZggh() + " 导入失败：";
                failureMsg.append(msg + ex.getMessage());
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}

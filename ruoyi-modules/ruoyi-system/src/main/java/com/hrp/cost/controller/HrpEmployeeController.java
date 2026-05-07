package com.hrp.cost.controller;

import com.hrp.cost.api.domain.HrpEmployee;
import com.hrp.cost.service.IHrpEmployeeService;
import com.hrp.cost.service.IHospitalDeptService;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 职工信息管理 Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/hrp/employee")
public class HrpEmployeeController extends BaseController
{
    @Autowired
    private IHrpEmployeeService employeeService;

    @Autowired
    private IHospitalDeptService deptService;

    /**
     * 查询职工信息列表
     */
    @RequiresPermissions("hrp:employee:list")
    @GetMapping("/list")
    public TableDataInfo list(HrpEmployee employee)
    {
        startPage();
        List<HrpEmployee> list = employeeService.selectEmployeeList(employee);
        return getDataTable(list);
    }

    /**
     * 导出职工信息列表
     */
    @RequiresPermissions("hrp:employee:export")
    @Log(title = "职工信息管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrpEmployee employee)
    {
        List<HrpEmployee> list = employeeService.selectEmployeeList(employee);
        ExcelUtil<HrpEmployee> util = new ExcelUtil<HrpEmployee>(HrpEmployee.class);
        util.exportExcel(response, list, "职工信息数据");
    }

    @Log(title = "职工信息管理", businessType = BusinessType.IMPORT)
    @RequiresPermissions("hrp:employee:import")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<HrpEmployee> util = new ExcelUtil<HrpEmployee>(HrpEmployee.class);
        List<HrpEmployee> employeeList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = employeeService.importEmployee(employeeList, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException
    {
        ExcelUtil<HrpEmployee> util = new ExcelUtil<HrpEmployee>(HrpEmployee.class);
        util.importTemplateExcel(response, "职工信息数据");
    }

    /**
     * 根据工号获取职工信息详细信息
     */
    @RequiresPermissions("hrp:employee:query")
    @GetMapping(value = "/{zggh}")
    public AjaxResult getInfo(@PathVariable("zggh") String zggh)
    {
        return success(employeeService.selectEmployeeById(zggh));
    }

    /**
     * 新增职工信息
     */
    @RequiresPermissions("hrp:employee:add")
    @Log(title = "职工信息管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrpEmployee employee)
    {
        if (!employeeService.checkEmployeeUnique(employee))
        {
            return error("新增职工'" + employee.getZgxms() + "'失败，职工工号已存在");
        }
        if (!employeeService.checkPhoneUnique(employee))
        {
            return error("新增职工'" + employee.getZgxms() + "'失败，手机号码已存在");
        }
        if (!employeeService.checkIdCardUnique(employee))
        {
            return error("新增职工'" + employee.getZgxms() + "'失败，身份证已存在");
        }
        return toAjax(employeeService.insertEmployee(employee));
    }

    /**
     * 修改职工信息
     */
    @RequiresPermissions("hrp:employee:edit")
    @Log(title = "职工信息管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody HrpEmployee employee)
    {
        if (!employeeService.checkEmployeeUnique(employee))
        {
            return error("修改职工'" + employee.getZgxms() + "'失败，职工工号已存在");
        }
        if (!employeeService.checkPhoneUnique(employee))
        {
            return error("修改职工'" + employee.getZgxms() + "'失败，手机号码已存在");
        }
        if (!employeeService.checkIdCardUnique(employee))
        {
            return error("修改职工'" + employee.getZgxms() + "'失败，身份证已存在");
        }
        return toAjax(employeeService.updateEmployee(employee));
    }

    /**
     * 删除职工信息
     */
    @RequiresPermissions("hrp:employee:remove")
    @Log(title = "职工信息管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{zgghs}")
    public AjaxResult remove(@PathVariable String[] zgghs)
    {
        return toAjax(employeeService.deleteEmployeeByIds(zgghs));
    }

    /**
     * 获取部门树列表
     */
    @RequiresPermissions("hrp:employee:list")
    @GetMapping("/deptTree")
    public AjaxResult deptTree()
    {
        return success(deptService.selectDeptTreeList(new com.hrp.cost.api.domain.HospitalDept()));
    }
}

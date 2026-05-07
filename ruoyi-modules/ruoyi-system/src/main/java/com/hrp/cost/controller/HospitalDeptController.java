package com.hrp.cost.controller;

import com.hrp.cost.api.domain.HospitalDept;
import com.hrp.cost.service.IHospitalDeptService;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: Cost Accounting
 * @author: neo
 * @create: 2026-04-23 22:10
 **/
@RestController
@RequestMapping("/hrp/dept")
public class HospitalDeptController extends BaseController {
    @Autowired
    private IHospitalDeptService deptService;

    /**
     * 获取部门列表
     */
//    @RequiresPermissions("cost:dept:list")
    @GetMapping("/list")
    public AjaxResult list(HospitalDept dept)
    {
        List<HospitalDept> depts = deptService.selectDeptList(dept);
        return success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @RequiresPermissions("system:dept:query")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable String deptId)
    {
//        deptService.checkDeptDataScope(deptId);
        return success(deptService.selectDeptById(deptId));
    }


    /**
     * 查询部门列表（排除节点）
     */
    @RequiresPermissions("system:dept:list")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) String deptId)
    {
        List<HospitalDept> depts = deptService.selectDeptList(new HospitalDept());
        if (StringUtils.isNotEmpty(deptId))
        {
            depts.removeIf(d -> d.getDm().equals(deptId));
        }
        return success(depts);
    }

    /**
     * 新增部门
     */
    @RequiresPermissions("system:dept:add")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HospitalDept dept)
    {
        if (!deptService.checkDeptNameUnique(dept))
        {
            return error("新增部门'" + dept.getMc() + "'失败，部门名称已存在");
        }
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @RequiresPermissions("system:dept:edit")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HospitalDept dept)
    {
        String dm = dept.getDm();
        deptService.checkDeptDataScope(dm);
        if (!deptService.checkDeptNameUnique(dept))
        {
            return error("修改部门'" + dept.getMc() + "'失败，部门名称已存在");
        }
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @RequiresPermissions("system:dept:remove")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable String deptId)
    {
        if (deptService.hasChildByDeptId(deptId))
        {
            return warn("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId))
        {
            return warn("部门存在用户,不允许删除");
        }
        deptService.checkDeptDataScope(deptId);
        return toAjax(deptService.deleteDeptById(deptId));
    }
}

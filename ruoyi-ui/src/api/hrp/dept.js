import request from '@/utils/request'

// 查询部门列表
export function listDept(query) {
  return request({
    url: '/system/hrp/dept/list',
    method: 'get',
    params: query
  })
}

// 查询部门列表（排除节点） 当你编辑一个部门时，不能把它自己或它的下级部门作为自己的上级部门（否则会形成循环）。
export function listDeptExcludeChild(deptId) {
  return request({
    url: '/system/hrp/dept/list/exclude/' + deptId,
    method: 'get'
  })
}

// 查询部门详细
export function getDept(deptId) {
  return request({
    url: '/system/hrp/dept/' + deptId,
    method: 'get'
  })
}

// 新增部门
export function addDept(data) {
  return request({
    url: '/system/hrp/dept/',
    method: 'post',
    data: data
  })
}

// 修改部门
export function updateDept(data) {
  return request({
    url: '/system/hrp/dept/',
    method: 'put',
    data: data
  })
}

// 删除部门
export function delDept(deptId) {
  return request({
    url: '/system/hrp/dept/' + deptId,
    method: 'delete'
  })
}
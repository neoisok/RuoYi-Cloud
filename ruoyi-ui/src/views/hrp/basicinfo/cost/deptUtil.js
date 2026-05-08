/**
 * 科室管理工具函数
 */

/**
 * 根据 dm 在树形数据中查找科室
 * @param {string|number} dm - 科室代码
 * @param {Array} treeData - 树形数据（包含 children 的数组）
 * @returns {Object|null} 找到的科室对象，未找到返回 null
 */
export function findDeptByDm(dm, treeData) {
  if (!dm || !treeData || !treeData.length) return null;
  
  for (const item of treeData) {
    if (item.dm == dm) {
      return item;
    }
    if (item.children && item.children.length) {
      const found = findDeptByDm(dm, item.children);
      if (found) return found;
    }
  }
  return null;
}

/**
 * 根据父级 dm 查找所有子科室
 * @param {string|number} parentDm - 父级科室代码
 * @param {Array} treeData - 树形数据
 * @returns {Array} 子科室数组
 */
export function findChildrenByParentDm(parentDm, treeData) {
  const parent = findDeptByDm(parentDm, treeData);
  return parent && parent.children ? parent.children : [];
}

/**
 * 根据科室代码生成编码规则显示格式（如 010101 -> 01-01-01）
 * @param {string} ksdm - 科室代码
 * @param {string} separator - 分隔符，默认 '-'
 * @returns {string} 格式化后的编码规则
 */
export function formatCodeRule(ksdm, separator = '-') {
  if (!ksdm) return '';
  const codeStr = String(ksdm);
  const parts = codeStr.match(/.{1,2}/g);
  return parts ? parts.join(separator) : codeStr;
}

/**
 * 根据上级科室代码生成新科室代码
 * @param {string} parentCode - 上级科室代码
 * @param {Array} children - 同级子科室列表
 * @returns {string} 新科室代码
 */
export function generateDeptCode(parentCode, children = []) {
  // 获取同级子科室的最大序号
  let maxNum = 0;
  if (children && children.length) {
    const nums = children.map(child => {
      const code = child.ksdm || '';
      const lastTwo = code.slice(-2);
      const num = parseInt(lastTwo, 10);
      return isNaN(num) ? 0 : num;
    });
    maxNum = Math.max(...nums, 0);
  }
  
  const newSeq = String(maxNum + 1).padStart(2, '0');
  return parentCode ? parentCode + newSeq : newSeq;
}

/**
 * 获取根级科室的最大序号（用于生成根级编码）
 * @param {Array} treeData - 树形数据
 * @returns {number} 最大序号
 */
export function getMaxRootSeq(treeData) {
  if (!treeData || !treeData.length) return 0;
  
  const rootDepts = treeData.filter(d => !d.fldm || d.fldm === '0');
  if (!rootDepts.length) return 0;
  
  const maxNum = Math.max(...rootDepts.map(d => {
    const code = d.ksdm || '';
    const num = parseInt(code, 10);
    return isNaN(num) ? 0 : num;
  }));
  
  return maxNum;
}

/**
 * 生成新的根级科室代码
 * @param {Array} treeData - 树形数据
 * @returns {string} 新根级代码（如 03）
 */
export function generateRootCode(treeData) {
  const maxSeq = getMaxRootSeq(treeData);
  return String(maxSeq + 1).padStart(2, '0');
}

/**
 * 构建树形数据（扁平转树形）
 * @param {Array} list - 扁平数据列表
 * @param {string} idKey - id 字段名
 * @param {string} parentIdKey - 父级 id 字段名
 * @returns {Array} 树形数据
 */
export function buildTree(list, idKey = 'dm', parentIdKey = 'fldm') {
  if (!list || !list.length) return [];
  
  const tree = [];
  const map = {};
  
  // 建立映射
  list.forEach(item => {
    map[item[idKey]] = { ...item, children: [] };
  });
  
  // 构建树形
  list.forEach(item => {
    const parentId = item[parentIdKey];
    if (parentId && map[parentId]) {
      map[parentId].children.push(map[item[idKey]]);
    } else {
      tree.push(map[item[idKey]]);
    }
  });
  
  // 删除空的 children 数组
  const removeEmptyChildren = (node) => {
    if (node.children && node.children.length === 0) {
      delete node.children;
    } else if (node.children) {
      node.children.forEach(removeEmptyChildren);
    }
  };
  
  tree.forEach(removeEmptyChildren);
  return tree;
}

/**
 * 扁平化树形数据
 * @param {Array} treeData - 树形数据
 * @returns {Array} 扁平数据数组
 */
export function flattenTree(treeData) {
  const result = [];
  const traverse = (node) => {
    result.push({ ...node });
    if (node.children && node.children.length) {
      node.children.forEach(traverse);
    }
  };
  treeData.forEach(traverse);
  return result;
}
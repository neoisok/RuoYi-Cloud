<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="科室名称" prop="mc">
        <el-input
          v-model="queryParams.mc"
          placeholder="请输入科室名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="科室状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:dept:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="deptList"
      :row-style="{height:'24px'}"
      :cell-style="{padding:'0px'}"
      row-key="dm"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="ksdm" label="科室代码" width="180"></el-table-column>
      <el-table-column prop="mc" label="科室名称" width="260"></el-table-column>
      <el-table-column prop="dm" label="代码" width="100"></el-table-column>
      <el-table-column prop="fldm" label="父类代码" width="100"></el-table-column>
      <el-table-column prop="xzdm" label="科室性质" width="100">
        <template slot-scope="scope">
          <dict-tag v-if="scope.row.xzdm&&scope.row.xzdm!=='0'"
          :options="dict.type.hrp_dept_category" :value="scope.row.xzdm"/>
        </template>
      </el-table-column>
      <el-table-column prop="mjpb" label="末级" width="100"></el-table-column>
      <el-table-column prop="hspb" label="核算" width="100"></el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope" >
          <!-- 这个加上行高就正常了 flex布局 nowrap：禁止按钮文字换行，也禁止 flex 子元素换行，确保所有按钮始终横向排列在一行上。-->
          <div style="display: flex; gap: 8px; white-space: nowrap;">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['system:dept:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-plus"
              @click="handleAdd(scope.row)"
              v-hasPermi="['system:dept:add']"
            >新增</el-button>
            <el-button
              v-if="scope.row.parentId != 0"
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['system:dept:remove']"
            >删除</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改部门对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24" v-if="form.dm !== 0">
            <el-form-item label="上级科室" prop="fldm">
              <treeselect v-model="form.fldm" :options="deptOptions" :normalizer="normalizer" placeholder="选择上级科室" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="科室代码" prop="ksdm">
              <!-- <el-input-number v-model="form.ksdm" controls-position="right" :min="0" /> -->
              <el-input v-model="form.ksdm" placeholder="请输入科室代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="科室名称" prop="mc">
              <el-input v-model="form.mc" placeholder="请输入部门名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="科室性质" prop="xzdm">
              <el-select v-model="form.xzdm" placeholder="请选择科室性质">
                <el-option
                  v-for="dict in dict.type.hrp_dept_category"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                />
              </el-select>
            </el-form-item>
          </el-col>
           <el-col :span="12">
            <el-form-item label="科室状态">
              <el-radio-group v-model="form.zfpb">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="parseInt(dict.value)"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDept,getDept,listDeptExcludeChild,delDept, addDept, updateDept} from "@/api/hrp/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {findDeptByDm,formatCodeRule,findChildrenByParentDm} from "./deptUtil"
export default {
  name: "Dept1",
  dicts: ['sys_normal_disable','hrp_dept_category','hrp_dept_is_leaf','hrp_dept_is_accounting'],
  components: { Treeselect },
  data() {
    return {
      code:"",
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 表格树数据
      deptList: [],
      // 部门树选项
      deptOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        mc: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        fldm: [
          { required: true, message: "上级部门不能为空", trigger: "blur" }
        ],
        ksdm: [
          { required: true, message: "科室代码不能为空", trigger: "blur" }
        ],
        mc: [
          { required: true, message: "科室名称不能为空", trigger: "blur" }
        ],
        xzdm: [
          { required: true, message: "性质代码不能为空", trigger: "blur" }
        ],
        // email: [
        //   {
        //     type: "email",
        //     message: "请输入正确的邮箱地址",
        //     trigger: ["blur", "change"]
        //   }
        // ],
        // phone: [
        //   {
        //     pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
        //     message: "请输入正确的手机号码",
        //     trigger: "blur"
        //   }
        // ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询部门列表 */
    getList() {
      this.loading = true;
      listDept(this.queryParams).then(response => {
        this.deptList = this.handleTree(response.data, "dm","fldm");
        // this.deptList = response.data
        this.loading = false;
      });
    },
    /** 转换部门数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.dm,
        label: node.mc,
        children: node.children
      };
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    // resetFields() 重置的是表单第一次渲染时的初始值，而不是你手动修改后的值。
    // 如果只用 resetFields()，当用户先打开编辑弹窗，再打开新增弹窗时，resetFields() 可能会恢复到编辑时的数据，而不是清空。
    // 先手动重置 form 对象（清空数据），再调用 resetFields()（清除验证错误）。
    reset() {
      this.form = {
        deptId: undefined,
        parentId: undefined,
        deptName: undefined,
        orderNum: undefined,
        leader: undefined,
        phone: undefined,
        email: undefined,
        status: "0",
        zfpb: 0,
        xzdm: 1
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      console.log("handleAdd11223344");
      debugger;
      this.reset();
      if (row != undefined) {
        this.form.fldm = row.dm;
        // Generate next department code based on parent and existing children
        this.generateNextDeptCode(row.dm, row.ksdm);
      }
      this.open = true;
      this.title = "添加科室";
      listDept().then(response => {
                // this.deptList = this.handleTree(response.data, "dm","fldm");
        this.deptOptions = this.handleTree(response.data, "dm","fldm");
      });
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getDept(row.dm).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改科室";
      });
      listDeptExcludeChild(row.dm).then(response => {
        this.deptOptions = this.handleTree(response.data, "dm","fldm");
        this.form.flksdm = findDeptByDm(row.fldm, this.deptOptions).ksdm
        this.form.codeRule=formatCodeRule(this.form.flksdm)
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.dm != undefined) {
            updateDept(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDept(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除名称为"' + row.mc + '"的数据项？').then(function() {
        return delDept(row.dm);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 生成下一个科室代码 */
    generateNextDeptCode(parentDm, parentKsdm) {
      debugger;
      if (!parentDm) {
        this.form.ksdm = '';
        return;
      }
      
      // Find all direct children of the parent in the current list
      // Note: this.deptList might not be fully loaded or filtered correctly for just children if it's a tree.
      // We should use the full list or fetch children specifically. 
      // Using this.deptList which is the full tree, we need to flatten or find children.
      // A simpler approach: filter deptList for items where fldm == parentDm
      
      // const children = this.deptList.filter(item => item.fldm === parentDm);
      const children = findChildrenByParentDm(parentDm,this.deptList);
      let maxSuffix = -1;
      
      children.forEach(child => {
        const code = child.ksdm || child.dm; // Use ksdm if available, else dm
        if (code && code.startsWith(parentKsdm)) {
          const suffixStr = code.substring(parentKsdm.length);
          const suffix = parseInt(suffixStr, 10);
          if (!isNaN(suffix) && suffix > maxSuffix) {
            maxSuffix = suffix;
          }
        }
      });
      
      // Next odd number
      let nextSuffix;
      if (maxSuffix < 1) {
        nextSuffix = 1;
      } else {
        nextSuffix = maxSuffix + 2;
      }
      
      // Format suffix to 2 digits if necessary, but requirement implies simple concatenation like 010101
      // If parent is 0101, next is 010101. So suffix should be padded to 2 digits if it's less than 10? 
      // Example: 010101 -> suffix 01. 010103 -> suffix 03.
      // If nextSuffix is 1, it should be "01". If 11, "11".
      
      const formattedSuffix = nextSuffix.toString().padStart(2, '0');
      this.form.ksdm = parentKsdm + formattedSuffix;
    }
  }
};
</script>

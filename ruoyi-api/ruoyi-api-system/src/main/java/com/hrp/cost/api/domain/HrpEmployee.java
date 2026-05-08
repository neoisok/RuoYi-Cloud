package com.hrp.cost.api.domain;

import com.ruoyi.common.core.web.domain.BaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 职工信息实体类
 * 
 * @author ruoyi
 */
public class HrpEmployee extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 职工工号（主键） */
    private String zggh;

    /** 职工代码 */
    private String zgdm;

    /** 科室代码 */
    private String ksdm;

    /** 科室名称（关联查询） */
    private String ksmc;

    /** 职工姓名 */
    private String zgxms;

    /** 性别 */
    private String xb;

    /** 出生日期 */
    private Date csrq;

    /** 身份证 */
    private String sfz;

    /** 地址 */
    private String dz;

    /** 电话号码 */
    private String dhhm;

    /** 手机号码 */
    private String sjhm;

    /** 银行卡号1 */
    private String yhkh1;

    /** 输入码1 */
    private String srm1;

    /** 输入码2 */
    private String srm2;

    /** 输入码3 */
    private String srm3;

    /** 职务 */
    private String zw;

    /** 职称 */
    private String zc;

    /** 当前状态 */
    private String dqzt;

    /** 备注 */
    private String bz;

    /** 职工类别 */
    private String zgkl;

    /** 使用范围 */
    private Integer syfw;

    /** 进修科室 */
    private String jjks;

    /** 职工类别 */
    private String zglb;

    /** 职工性质 */
    private String zgxz;

    /** 成本系数 */
    private BigDecimal cbxs;

    /** 人员性质 */
    private String ryxz;

    /** 科室代码列表（用于多科室查询） */
    private List<String> ksdmList;

    public String getZggh() {
        return zggh;
    }

    public void setZggh(String zggh) {
        this.zggh = zggh;
    }

    public String getZgdm() {
        return zgdm;
    }

    public void setZgdm(String zgdm) {
        this.zgdm = zgdm;
    }

    public String getKsdm() {
        return ksdm;
    }

    public void setKsdm(String ksdm) {
        this.ksdm = ksdm;
    }

    public String getKsmc() {
        return ksmc;
    }

    public void setKsmc(String ksmc) {
        this.ksmc = ksmc;
    }

    public String getZgxms() {
        return zgxms;
    }

    public void setZgxms(String zgxms) {
        this.zgxms = zgxms;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public Date getCsrq() {
        return csrq;
    }

    public void setCsrq(Date csrq) {
        this.csrq = csrq;
    }

    public String getSfz() {
        return sfz;
    }

    public void setSfz(String sfz) {
        this.sfz = sfz;
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public String getDhhm() {
        return dhhm;
    }

    public void setDhhm(String dhhm) {
        this.dhhm = dhhm;
    }

    public String getSjhm() {
        return sjhm;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
    }

    public String getYhkh1() {
        return yhkh1;
    }

    public void setYhkh1(String yhkh1) {
        this.yhkh1 = yhkh1;
    }

    public String getSrm1() {
        return srm1;
    }

    public void setSrm1(String srm1) {
        this.srm1 = srm1;
    }

    public String getSrm2() {
        return srm2;
    }

    public void setSrm2(String srm2) {
        this.srm2 = srm2;
    }

    public String getSrm3() {
        return srm3;
    }

    public void setSrm3(String srm3) {
        this.srm3 = srm3;
    }

    public String getZw() {
        return zw;
    }

    public void setZw(String zw) {
        this.zw = zw;
    }

    public String getZc() {
        return zc;
    }

    public void setZc(String zc) {
        this.zc = zc;
    }

    public String getDqzt() {
        return dqzt;
    }

    public void setDqzt(String dqzt) {
        this.dqzt = dqzt;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getZgkl() {
        return zgkl;
    }

    public void setZgkl(String zgkl) {
        this.zgkl = zgkl;
    }

    public Integer getSyfw() {
        return syfw;
    }

    public void setSyfw(Integer syfw) {
        this.syfw = syfw;
    }

    public String getJjks() {
        return jjks;
    }

    public void setJjks(String jjks) {
        this.jjks = jjks;
    }

    public String getZglb() {
        return zglb;
    }

    public void setZglb(String zglb) {
        this.zglb = zglb;
    }

    public String getZgxz() {
        return zgxz;
    }

    public void setZgxz(String zgxz) {
        this.zgxz = zgxz;
    }

    public BigDecimal getCbxs() {
        return cbxs;
    }

    public void setCbxs(BigDecimal cbxs) {
        this.cbxs = cbxs;
    }

    public String getRyxz() {
        return ryxz;
    }

    public void setRyxz(String ryxz) {
        this.ryxz = ryxz;
    }

    public List<String> getKsdmList() {
        return ksdmList;
    }

    public void setKsdmList(List<String> ksdmList) {
        this.ksdmList = ksdmList;
    }
}

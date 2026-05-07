package com.hrp.cost.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: Cost Accounting
 * @author: neo
 * @create: 2026-04-23 22:19
 **/
public class HospitalDept implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 代码（主键） */
    private String dm;

    /** 名称 */
    private String mc;

    /** 级别 */
    private Integer jb;

    /** 分类代码 */
    private String fldm;

    /** 行政代码 */
    private Integer xzdm;

    /** 核算屏蔽（0-否，1-是） */
    private Integer hspb;

    /** 门诊屏蔽（0-否，1-是） */
    private Integer mjpb;

    /** 住院屏蔽（0-否，1-是） */
    private Integer zfpb;

    /** 作废日期 */
    private Date zfrq;

    /** 输入码1 */
    private String srm1;

    /** 输入码2 */
    private String srm2;

    /** 输入码3 */
    private String srm3;

    /** 备注 */
    private String bz;

    /** 输入屏蔽（0-否，1-是） */
    private Integer srpb;

    /** 科室代码 */
    private String ksdm;

    /** 院区代码 */
    private String yqdm;

    /** 子部门 */
    private List<HospitalDept> children = new ArrayList<HospitalDept>();

    public String getDm() {
        return dm;
    }

    public void setDm(String dm) {
        this.dm = dm;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public Integer getJb() {
        return jb;
    }

    public void setJb(Integer jb) {
        this.jb = jb;
    }

    public String getFldm() {
        return fldm;
    }

    public void setFldm(String fldm) {
        this.fldm = fldm;
    }

    public Integer getXzdm() {
        return xzdm;
    }

    public void setXzdm(Integer xzdm) {
        this.xzdm = xzdm;
    }

    public Integer getHspb() {
        return hspb;
    }

    public void setHspb(Integer hspb) {
        this.hspb = hspb;
    }

    public Integer getMjpb() {
        return mjpb;
    }

    public void setMjpb(Integer mjpb) {
        this.mjpb = mjpb;
    }

    public Integer getZfpb() {
        return zfpb;
    }

    public void setZfpb(Integer zfpb) {
        this.zfpb = zfpb;
    }

    public Date getZfrq() {
        return zfrq;
    }

    public void setZfrq(Date zfrq) {
        this.zfrq = zfrq;
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

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public Integer getSrpb() {
        return srpb;
    }

    public void setSrpb(Integer srpb) {
        this.srpb = srpb;
    }

    public String getKsdm() {
        return ksdm;
    }

    public void setKsdm(String ksdm) {
        this.ksdm = ksdm;
    }

    public String getYqdm() {
        return yqdm;
    }

    public void setYqdm(String yqdm) {
        this.yqdm = yqdm;
    }

    public List<HospitalDept> getChildren() {
        return children;
    }

    public void setChildren(List<HospitalDept> children) {
        this.children = children;
    }
}

package com.wkyc.monitor.domain;

import java.util.Date;

public class TccTransactionMail {
    private Long id;

    private Date createTime;

    private Date updateTime;

    private String globalTxId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getGlobalTxId() {
        return globalTxId;
    }

    public void setGlobalTxId(String globalTxId) {
        this.globalTxId = globalTxId;
    }

    public TccTransactionMail(String globalTxId) {
        this.globalTxId = globalTxId;
    }

    public TccTransactionMail() {
    }
}
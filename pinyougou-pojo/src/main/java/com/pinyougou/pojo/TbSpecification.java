package com.pinyougou.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 组合
 * @author 我的天
 */
@Data
public class TbSpecification implements Serializable {
    private Long id;

    private String specName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName == null ? null : specName.trim();
    }
}
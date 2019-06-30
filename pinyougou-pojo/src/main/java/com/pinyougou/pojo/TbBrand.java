package com.pinyougou.pojo;

import java.io.Serializable;


@SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
public class TbBrand  implements Serializable {
    /*  字段 类型   长度 	含义
        Id 	Bigint 	 	主键
        Name 	Varchar 	255 	品牌名称
        First_char 	Varchar 	1 	品牌首字母 */
    private Long id;

    private String name;

    private String firstChar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar == null ? null : firstChar.trim();
    }
}
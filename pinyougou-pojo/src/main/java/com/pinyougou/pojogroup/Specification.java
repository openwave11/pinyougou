package com.pinyougou.pojogroup;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Specification implements Serializable {

    private TbSpecification tbSpecification;
    private List<TbSpecificationOption> tbSpecificationOptionList;


}

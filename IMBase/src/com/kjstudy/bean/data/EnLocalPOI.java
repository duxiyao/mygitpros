package com.kjstudy.bean.data;

import java.util.List;

import org.kymjs.kjframe.database.annotate.Transient;

public class EnLocalPOI extends POIEntity{

    /** 
    * @Fields serialVersionUID : 
    * @author duxiyao 
    * @date 2015年12月1日 下午7:42:54 
    */ 
    @Transient
    private static final long serialVersionUID = -4649910701071458179L;

    private List<ContentEntity> contents;

    public List<ContentEntity> getContents() {
        return contents;
    }

    public void setContents(List<ContentEntity> contents) {
        this.contents = contents;
    }
}

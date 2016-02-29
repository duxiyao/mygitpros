package com.kjstudy.bean.data;

import org.kymjs.kjframe.database.annotate.Transient;

public class POIEntity extends EnEntity{

    /** 
    * @Fields serialVersionUID : 
    * @author duxiyao 
    * @date 2015年12月1日 下午7:44:57 
    */ 
    @Transient
    private static final long serialVersionUID = 4000633515565333994L;

    private int size;
    private int total;
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
}

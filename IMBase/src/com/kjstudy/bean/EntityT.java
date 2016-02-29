package com.kjstudy.bean;
public class EntityT<T> extends Entity {

    /** 
    * @Fields serialVersionUID : 
    * @author duxiyao 
    * @date 2015年11月26日 上午10:42:34 
    */ 
    private static final long serialVersionUID = 7486892259838490160L;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

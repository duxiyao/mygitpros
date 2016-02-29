package com.kjstudy.bean.data;

import org.kymjs.kjframe.database.annotate.Transient;

import com.kjstudy.bean.Entity;

public class EnEntity extends Entity{

    /** 
    * @Fields serialVersionUID : 
    * @author duxiyao 
    * @date 2015年12月1日 下午7:43:48 
    */ 
    @Transient
    private static final long serialVersionUID = 4162258750908693434L;

    private int status;
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    private String message;
}

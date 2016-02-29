package com.kjstudy.bean.data;

import org.kymjs.kjframe.database.annotate.Transient;

public class TSTeacherInfo extends EntityData {
    /**
     * @Fields serialVersionUID :
     * @author duxiyao
     * @date 2015年11月26日 上午10:39:16
     */
    @Transient
    private static final long serialVersionUID = -6003961029795556013L;

    private int _id;
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    private int id=-1;

    private String latlng;

    private int ubId;

    private String dataBirthday;

    private String grade;

    private String subject;

    private String artSubject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public int getUbId() {
        return ubId;
    }

    public void setUbId(int ubId) {
        this.ubId = ubId;
    }

    public String getDataBirthday() {
        return dataBirthday;
    }

    public void setDataBirthday(String dataBirthday) {
        this.dataBirthday = dataBirthday;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getArtSubject() {
        return artSubject;
    }

    public void setArtSubject(String artSubject) {
        this.artSubject = artSubject;
    }
}

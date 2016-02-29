package com.kjstudy.bean.data;

import java.util.List;

import org.kymjs.kjframe.database.annotate.Transient;

import com.kjstudy.bean.Entity;

public class ContentEntity extends Entity {

    /**
     * @Fields serialVersionUID :
     * @author duxiyao
     * @date 2015年12月1日 下午7:46:39
     */
    @Transient
    private static final long serialVersionUID = -7214573162229417258L;

    private String uid;

    private String geotable_id;

    private String title;

    private String address;

    private String province;

    private String city;

    private String district;

    private int coord_type;

    private String tags;

    private List<String> location;

    private int distance;

    private TSUserInfo uinfo;

    public TSUserInfo getUinfo() {
        return uinfo;
    }

    public void setUinfo(TSUserInfo uinfo) {
        this.uinfo = uinfo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGeotable_id() {
        return geotable_id;
    }

    public void setGeotable_id(String geotable_id) {
        this.geotable_id = geotable_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getCoord_type() {
        return coord_type;
    }

    public void setCoord_type(int coord_type) {
        this.coord_type = coord_type;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}

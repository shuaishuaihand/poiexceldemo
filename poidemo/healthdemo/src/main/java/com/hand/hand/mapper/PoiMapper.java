package com.hand.hand.mapper;
import com.hand.hand.util.PageData;
public interface PoiMapper {

    void insertmysql(PageData pd);

    int slectcountperson(String pname);

    Integer selectpidbyname(String pname);

    String selectcardidbyname(String pname1);

    Integer selectcidbyname(String cname);

}

package com.troy.keeper.management.dto;

/**
 * @author 李奥   帮我买物流详情按钮
 * @date 2017/12/14.
 */
public class HelpBuyDetailsDTO {

     private  String a;

     private  String  b;

     private  String  c;

     private  Long  id;

    private  String Information;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInformation() {
        return Information;
    }

    public void setInformation(String information) {
        Information = information;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }
}

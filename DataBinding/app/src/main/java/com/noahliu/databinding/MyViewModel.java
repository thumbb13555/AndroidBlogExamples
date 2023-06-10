package com.noahliu.databinding;

public class MyViewModel{
    public String multipliedBy11;
    public String multipliedBy21;
    public String multipliedBy31;
    public String multipliedBy41;
    public String multipliedBy51;

    public MyViewModel() {}
    public void setZero(){
        this.multipliedBy11 = null;
        this.multipliedBy21 = null;
        this.multipliedBy31 = null;
        this.multipliedBy41 = null;
        this.multipliedBy51 = null;
    }

    public void setMultipliedBy11(String multipliedBy11) {
        this.multipliedBy11 = multipliedBy11;
    }

    public void setMultipliedBy21(String multipliedBy21) {
        this.multipliedBy21 = multipliedBy21;
    }

    public void setMultipliedBy31(String multipliedBy31) {
        this.multipliedBy31 = multipliedBy31;
    }

    public void setMultipliedBy41(String multipliedBy41) {
        this.multipliedBy41 = multipliedBy41;
    }

    public void setMultipliedBy51(String multipliedBy51) {
        this.multipliedBy51 = multipliedBy51;
    }
}

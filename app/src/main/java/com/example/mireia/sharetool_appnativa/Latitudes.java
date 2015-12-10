package com.example.mireia.sharetool_appnativa;


public class Latitudes {
    private static Latitudes instance;

    // Global variable
    private Double datalt;
    private Double datalg;
    String address;

    // Restrict the constructor from being instantiated
    private Latitudes(){}

    public void setData(Double lt, Double lg){
        this.datalt=lt;
        this.datalg=lg;
    }

    public void setDataAdd(String address){
        this.address= address;
    }
    public Double getDatalt(){
        return this.datalt;
    }
    public Double getDatalg(){
        return this.datalg;
    }
    public String getDataAdd(){
        return this.address;
    }


    public static synchronized Latitudes getInstance(){
        if(instance==null){
            instance=new Latitudes();
        }
        return instance;
    }
}

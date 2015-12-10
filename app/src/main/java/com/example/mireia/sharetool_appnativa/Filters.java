package com.example.mireia.sharetool_appnativa;



public class Filters {
    private static Filters instance;

    // Global variable
    private String ordenar;
    private Double distmax;
    private Double pmax;

    // Restrict the constructor from being instantiated
    private Filters(){}

    public void setData(String o, Double d, Double p){
        this.ordenar=o;
        this.distmax=d;
        this.pmax=p;
    }
    public String getDataord(){
        return this.ordenar;
    }
    public Double getDatad(){
        return this.distmax;
    }
    public Double getDatap(){
        return this.pmax;
    }

    public static synchronized Filters getInstance(){
        if(instance==null){
            instance=new Filters();
        }
        return instance;
    }
}

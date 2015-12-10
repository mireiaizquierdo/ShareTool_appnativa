package com.example.mireia.sharetool_appnativa;

import com.parse.ParseObject;

import java.util.List;

public class obglobal {
    private static obglobal instance;

    // Global variable obglobal
    private List<ParseObject> data;

    // Restrict the constructor from being instantiated
    private obglobal(){}

    public void setData(List<ParseObject> d){
        this.data=d;
    }
    public List<ParseObject> getData(){
        return this.data;
    }

    public static synchronized obglobal getInstance(){
        if(instance==null){
            instance=new obglobal();
        }
        return instance;
    }
}

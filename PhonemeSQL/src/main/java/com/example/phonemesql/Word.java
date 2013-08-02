package com.example.phonemesql;


public class Word{

    /**
     * Private variables
     */

    private String _id;
    private String _code;

    /**
     * Constructor
     */
    public Word(){

    }

    /**
     * Overloaded Constructor
     */
    public Word(String id, String code){
        this._id = id;
        this._code=  code;
    }

    // getting ID
    public String getID(){
        return this._id;
    }

    // getting phone number
    public String getCode(){
        return this._code;
    }
}


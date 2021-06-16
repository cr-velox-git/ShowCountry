package com.example.showcountry;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

//Define table name
@Entity(tableName = "country_data")
public class MainData implements Serializable {
    // create id column
    @PrimaryKey(autoGenerate = true)
    private  int ID;

    //create text column
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "capital")
    private String capital;

    @ColumnInfo(name = "flag")
    private String flag;

    @ColumnInfo(name = "region")
    private String region;

    @ColumnInfo(name = "subregion")
    private String subregion;

    @ColumnInfo(name = "population")
    private String population;

    @ColumnInfo(name = "border")
    private String border;

    @ColumnInfo(name = "language")
    private String language;
    //Generate getter and setter


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubregion() {
        return subregion;
    }

    public void setSubregion(String subregion) {
        this.subregion = subregion;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}

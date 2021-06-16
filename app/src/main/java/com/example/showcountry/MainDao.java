package com.example.showcountry;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.google.gson.JsonArray;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainDao {
    //Insert query
    @Insert(onConflict = REPLACE)
    void insert(MainData mainData);

    //Delete query
    @Delete
    void delete(MainData mainData);

    //Delete all query
    @Delete
    void reset(List<MainData> mainData);

    //Update query
    @Query("UPDATE country_data SET name = :sName , capital = :sCapital, flag = :sFlag, region = :sRegion, subregion = :sSubregion, population = :sPopulation, border = :sBorder, language = :sLanguage WHERE ID = :sID")
    void update(int sID, String sName, String sCapital, String sFlag, String sRegion, String sSubregion, String sPopulation, String sBorder, String sLanguage);

    //get all data query
    @Query("SELECT * FROM country_data")
    List<MainData> getAll();
}

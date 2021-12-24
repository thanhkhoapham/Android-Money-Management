package com.example.admin.demomanagentthuchi.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.admin.demomanagentthuchi.Model.Mangement_loaiChi;

import java.util.ArrayList;

public class loaiChiDAO {
    SQLiteDatabase db;
    Dbhelper dbh;
    public loaiChiDAO(Context c) {
        dbh=new Dbhelper(c);
    }

    public void AddLoaiChi(Mangement_loaiChi loaiChi){
        db=dbh.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("tenLoaiChi",loaiChi.tenLoaiChi);
        db.insert("loaiChi",null,values);
    }
    public boolean Add2LoaiChi(Mangement_loaiChi loaiChi){
        db=dbh.getWritableDatabase();
        ContentValues values=new ContentValues();
        if(!ValidLoaiChi(loaiChi.tenLoaiChi)){
            values.put("tenLoaiChi",loaiChi.tenLoaiChi);
            db.insert("loaiChi",null,values);
            return true;
        }
        return  false;
    }
    public ArrayList<Mangement_loaiChi> ViewLoaiChi(){
        ArrayList<Mangement_loaiChi> list_LoaiChi=new ArrayList<Mangement_loaiChi>();

        db=dbh.getReadableDatabase();
        Cursor c=db.query("loaiChi",null,null,null,null,null,null);
        if (c.moveToFirst()){
            do {
                int _id=c.getInt(0);
                String tenLoaiChi=c.getString(1);
                Mangement_loaiChi loaiChi=new Mangement_loaiChi(_id,tenLoaiChi);
                list_LoaiChi.add(loaiChi);
            }while (c.moveToNext());
        }
        return list_LoaiChi;
    }

    public void DeleteLoaiChi(int _id){
        db=dbh.getWritableDatabase();
        db.delete("loaiChi","_id=?",new String[]{_id+""});

    }
    public void UpdateLoaiChi(Mangement_loaiChi loaiChi){
        db=dbh.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("tenLoaiChi",loaiChi.tenLoaiChi);
        db.update("loaiChi",values,"_id=?",new String[]{loaiChi._id+""});

    }
    public Cursor query_with_result(String sql){
        db= dbh.getWritableDatabase();
        return db.rawQuery(sql,null);
    }
    public Boolean ValidLoaiChi(String ten){
        db=dbh.getWritableDatabase();
        String sql="select * from loaiChi where tenLoaiChi LIKE '"+ten+"'";
        Cursor i= query_with_result(sql);
        int kq= query_with_result(sql).getCount();
        if(kq>0){
            return true;
        }else
            return false;
    }
}

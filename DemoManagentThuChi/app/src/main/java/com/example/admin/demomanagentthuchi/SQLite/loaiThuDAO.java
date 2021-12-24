package com.example.admin.demomanagentthuchi.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.admin.demomanagentthuchi.Model.Management_loaiThu;

import java.util.ArrayList;

public class loaiThuDAO {
    Dbhelper dbh;
    SQLiteDatabase db;

    public loaiThuDAO(Context c) {
        dbh = new Dbhelper(c);
    }

    public void AddLoaiThu(Management_loaiThu loaiThu) {
        db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenLoaiThu", loaiThu.tenLoaiThu);
        db.insert("loaiThu", null, values);
    }
    public boolean Add2LoaiThu(Management_loaiThu loaiThu) {
        db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(!ValidLoaiThu(loaiThu.tenLoaiThu)){
            values.put("tenLoaiThu", loaiThu.tenLoaiThu);
            db.insert("loaiThu", null, values);
            return true;
        }
        else
            return false;
    }

    public ArrayList<Management_loaiThu> ViewLoaiThu() {
        ArrayList<Management_loaiThu> listLoaiThu = new ArrayList<Management_loaiThu>();
        db = dbh.getReadableDatabase();
        Cursor c = db.query("loaiThu", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                int _id = c.getInt(0);
                String tenLoaiThu = c.getString(1);
                Management_loaiThu thu = new Management_loaiThu(_id, tenLoaiThu);
                listLoaiThu.add(thu);
            } while (c.moveToNext());
        }
        return listLoaiThu;
    }

    public void deleteLoaiThu(int _id) {
        db = dbh.getWritableDatabase();
        db.delete("loaiThu", "_id=?", new String[]{_id+""});
    }

    public void updateLoaiThu(Management_loaiThu loaiThu) {
        db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenLoaiThu", loaiThu.tenLoaiThu);
        db.update("loaiThu", values, "_id=?", new String[]{loaiThu._id + ""});
    }
    public Cursor query_with_result(String sql){
        db= dbh.getWritableDatabase();
        return db.rawQuery(sql,null);
    }
    public Boolean ValidLoaiThu(String ten){
        db=dbh.getWritableDatabase();
        String sql="select * from loaiThu where tenLoaiThu LIKE '"+ten+"'";
        Cursor i= query_with_result(sql);
        int kq= query_with_result(sql).getCount();
        if(kq>0){
            return true;
        }else
            return false;
    }
}

package com.example.ass2_i200802_i200707;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SqliteHelperHome extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "Items.db";
    public static int DATABASE_VERSION = 1;
    Context context;
    public SqliteHelperHome(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    public static class itemTable implements BaseColumns {

        public static String TABLE_NAME = "Items";
        public static String _ID = "itemID";
        public static String _ITEM_NAME = "itemName";
        public static String _USER_ID = "userId";
        public static String _ITEM_IMAGE = "itemImage";
        public static String _ITEM_DESCRIPTION = "itemDescription";
        public static String _ITEM_DATE = "itemDate";

        public static String _ITEM_PRICE = "itemPrice";

    }

    public static String CREATE_ITEM_TABLE = "CREATE TABLE " + itemTable.TABLE_NAME + "(" +
            itemTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            itemTable._ITEM_NAME + " TEXT, " +
            itemTable._USER_ID + " TEXT, " +
            itemTable._ITEM_IMAGE + " TEXT, " +
            itemTable._ITEM_DESCRIPTION + " TEXT, " +
            itemTable._ITEM_DATE + " TEXT, " +
            itemTable._ITEM_PRICE + " REAL)";

    public static String DROP_ITEM_TABLE = "DROP TABLE IF EXISTS " + itemTable.TABLE_NAME;



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITEM_TABLE);
        Toast.makeText(context, "Item table created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_ITEM_TABLE);
        onCreate(db);
    }

    public void insertItem(ItemModel item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(itemTable._ITEM_NAME, item.getItemName());
        values.put(itemTable._USER_ID, item.getUserId());
        values.put(itemTable._ITEM_IMAGE, item.getImgUrl());
        values.put(itemTable._ITEM_DESCRIPTION, item.getImgUrl());
        values.put(itemTable._ITEM_DATE, item.getImgUrl());
        values.put(itemTable._ITEM_PRICE, item.getItemPrice());

        db.insert(itemTable.TABLE_NAME, null, values);
        db.close();
    }

    public void insertAllItems(List<ItemModel> items) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values;

        for (ItemModel item : items) {
            values = new ContentValues();
            values.put(itemTable._ITEM_NAME, item.getItemName());
            values.put(itemTable._USER_ID, item.getUserId());
            values.put(itemTable._ITEM_IMAGE, item.getImgUrl());
            values.put(itemTable._ITEM_DESCRIPTION, item.getItemDescription());
            values.put(itemTable._ITEM_DATE, item.getItemDate());
            values.put(itemTable._ITEM_PRICE, item.getItemPrice());

            db.insert(itemTable.TABLE_NAME, null, values);
        }

        db.close();
    }


    @SuppressLint("Range")
    public List<ItemModel> getAllItems() {
        List<ItemModel> itemList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + itemTable.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ItemModel item = new ItemModel();
                item.setItemName(cursor.getString(cursor.getColumnIndex(itemTable._ITEM_NAME)));
                item.setUserId(cursor.getString(cursor.getColumnIndex(itemTable._USER_ID)));
                item.setImgUrl(cursor.getString(cursor.getColumnIndex(itemTable._ITEM_IMAGE)));
                item.setItemDescription(cursor.getString(cursor.getColumnIndex(itemTable._ITEM_DESCRIPTION)));
                item.setItemDate(cursor.getString(cursor.getColumnIndex(itemTable._ITEM_DATE)));
                item.setItemPrice(cursor.getString(cursor.getColumnIndex(itemTable._ITEM_PRICE)));

                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemList;
    }


    public void deleteItem(String itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(itemTable.TABLE_NAME, itemTable._ID + " = ?", new String[]{itemId});
        db.close();
    }

    public void deleteAllItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(itemTable.TABLE_NAME, null, null);
        db.close();
    }
}

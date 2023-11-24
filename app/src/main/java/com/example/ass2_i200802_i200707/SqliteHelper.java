package com.example.ass2_i200802_i200707;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SqliteHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "Chat.db";
    public static int DATABASE_VERSION = 1;

    Context context;

    public SqliteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
        this.context=context;
    }

    public static class chatTable implements BaseColumns {

        public static String TABLE_NAME = "Chat";
        public static String _ID = "messageId";
        public static String _MSG = "msg";
        public static String _DISPLAYURL = "displayUrl";
        public static String _IMGURL = "imageUrl";
        public static String _TIMESTAMP = "timestamp";

    }

    public static String CREATE_CHAT_TABLE = "CREATE TABLE " + chatTable.TABLE_NAME + "(" +
            chatTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            chatTable._MSG + " TEXT, " +
            chatTable._DISPLAYURL + " TEXT, " +
            chatTable._IMGURL + " TEXT, " +
            chatTable._TIMESTAMP + " TEXT)";

    public static String DROP_CHAT_TABLE = "DROP TABLE IF EXISTS " + chatTable.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CHAT_TABLE);
        Toast.makeText(context, "Table in DB created successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //CALL: WHEN VERSION CHANGE E.G ADD ANY COLUMN
        db.execSQL(DROP_CHAT_TABLE);
        onCreate(db);
    }


    @SuppressLint("Range")
    public List<MessageModel> getAllMessages() {
        List<MessageModel> messageList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + chatTable.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MessageModel message = new MessageModel();
                message.setMsg(cursor.getString(cursor.getColumnIndex(chatTable._MSG)));
                message.setDisplayUrl(cursor.getString(cursor.getColumnIndex(chatTable._DISPLAYURL)));
                message.setTimestamp(cursor.getString(cursor.getColumnIndex(chatTable._TIMESTAMP)));

                messageList.add(message);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return messageList;
    }

    public void insertMessage(MessageModel message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(chatTable._MSG, message.getMsg());
        values.put(chatTable._DISPLAYURL, message.getDisplayUrl());
        values.put(chatTable._TIMESTAMP, message.getTimestamp());

        db.insert(chatTable.TABLE_NAME, null, values);
        db.close();
    }


    public void deleteItem(String messageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(chatTable.TABLE_NAME, chatTable._ID + " = ?", new String[]{messageId});
        db.close();
    }


}

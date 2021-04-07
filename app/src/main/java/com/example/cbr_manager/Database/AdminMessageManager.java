package com.example.cbr_manager.Database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class AdminMessageManager {

    private List<AdminMessage> messages = new ArrayList<>();
    private static AdminMessageManager instance;
    private DatabaseHelper databaseHelper;

    private static final String admin_id = "ADMIN_ID";
    private static final String message_id = "ID";
    private static final String message_title = "TITLE";
    private static final String message_date = "DATE";
    private static final String message_location = "LOCATION";
    private static final String admin_message = "MESSAGE";
    private static final String is_synced = "IS_SYNCED";
    private static final String viewed_status = "IS_VIEWED";

    public static AdminMessageManager getInstance(Context context) {
        if (instance == null) {
            instance = new AdminMessageManager(context);
        }

        return instance;
    }

    public AdminMessageManager(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public List<AdminMessage> getMessages() {
        return messages;
    }

    public void updateList() {
        Cursor c = databaseHelper.getAllMessageInfo();

        int msgIdI = c.getColumnIndex(message_id);
        int adminIdI = c.getColumnIndex(admin_id);
        int titleI = c.getColumnIndex(message_title);
        int dateI = c.getColumnIndex(message_date);
        int locationI = c.getColumnIndex(message_location);
        int messageI = c.getColumnIndex(admin_message);
        int isSyncedI = c.getColumnIndex(is_synced);
        int viewedI = c.getColumnIndex(viewed_status);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            int adminId = c.getInt(adminIdI);
            Long msgId = c.getLong(msgIdI);
            String title = c.getString(titleI);
            String date = c.getString(dateI);
            String location = c.getString(locationI);
            String message = c.getString(messageI);
            int isSynced = c.getInt(isSyncedI);
            int viewed = c.getInt(viewedI);

            AdminMessage newMessage = new AdminMessage(adminId, msgId, title, date, location, message, isSynced, viewed);
            messages.add(newMessage);
        }
    }

    public void clear() {
        messages.clear();
    }

    public int size() {
        return messages.size();
    }

    public int numUnread() {
        return databaseHelper.numberOfUnreadMessages();
    }

}

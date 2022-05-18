package com.example.lab4.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

public class ChatDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chat.db";
    private static final int SCHEMA = 1;
    private Cursor cursor = null;

    //USER
    private final String USER_TABLE = "users";
    private final String USER_ID = "u_id";
    private final String USER_NAME = "u_name";
    private final String USER_NICKNAME = "u_nickname";
    private final String USER_EMAIL = "u_email";
    private final String USER_CHAT_COUNT = "u_chat_count";
    private final String USER_HEX = "u_hex";
    //private final String USER_MESSAGE_LIST_ID = "u_message_list_id";
    //private final String USER_MESSAGE_LIST_NAME = "u_message_list_name";

    private final String M2M_LIST_TABLE = "m2m_message_list";
    private final String M2M_ID = "ml_id";
    private final String M2M_MESSAGE_LIST_ID = "m2m_message_list_id";
    private final String M2M_MESSAGE_ID = "m2m_message_id";
    private final String M2M_USER_ID = "m2m_user_id";

    //MESSAGE_LIST
    private final String MESSAGE_LIST_TABLE = "message_list";
    private final String MESSAGE_LIST_ID = "ml_id";
    private final String MESSAGE_LIST_NAME = "ml_name";
    private final String MESSAGE_LIST_DESCRIPTION = "ml_description";
    private final String MESSAGE_LIST_USER_ID = "ml_user_id";
    private final String MESSAGE_LIST_FOREIGN_USER_ID = "ml_foreign_user_id";
    //MESSAGE
    private final String MESSAGE_TABLE = "messages";
    private final String MESSAGE_ID = "m_id";
    private final String MESSAGE_TEXT = "m_text";
    private final String MESSAGE_DATE = "m_dt";
    private final String MESSAGE_USER_NAME = "m_user_name";

    public ChatDBHelper(Context context){
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    public boolean addUser(User user, SQLiteDatabase db){
        boolean result = false;
        String name = user.getName();
        String nickname = user.getNickname();
        String email = user.getEmail();
        String hex = user.getHex();
        int counter = user.getChat_count();
        try{
            db.execSQL("INSERT INTO "+USER_TABLE+" ("+USER_NAME+", "+USER_NICKNAME+", "+USER_EMAIL+", "+USER_CHAT_COUNT+", "+USER_HEX+") VALUES " +
                    "('"+name+"','"+nickname+"', '"+email+"', "+counter+", '"+hex+"');");
            result = true;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return result;
    }
    
    public ArrayList<User> getAllUsers(SQLiteDatabase db){
        ArrayList<User> users = new ArrayList<>();
        try{
            cursor = db.rawQuery("select "+USER_NAME+", "+USER_NICKNAME+", "+USER_EMAIL+", "+USER_CHAT_COUNT+", "+USER_HEX+" from "+USER_TABLE+"", null);
            while(cursor.moveToNext()){
                String name = cursor.getString(0);
                String nickname = cursor.getString(1);
                String email = cursor.getString(2);
                int counter = cursor.getInt(3);
                String hex = cursor.getString(4);
                users.add(new User(name, nickname, email, counter, hex));
            }
            cursor.close();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return users;
    }
    public User getForeignUser(SQLiteDatabase db, String chat_name){
        User user = null;
        try{
            cursor = db.rawQuery("select "+USER_NAME+", "+USER_NICKNAME+", "+USER_EMAIL+", "+USER_CHAT_COUNT+", "+USER_HEX+" from "+MESSAGE_LIST_TABLE+" inner join "+USER_TABLE+" on "+MESSAGE_LIST_TABLE+"."+MESSAGE_LIST_FOREIGN_USER_ID+" = "+USER_TABLE+"."+USER_ID+" " +
                    "where "+MESSAGE_LIST_NAME+" = '"+chat_name+"' " , null);
            while(cursor.moveToNext()){
                String name = cursor.getString(0);
                String nickname = cursor.getString(1);
                String email = cursor.getString(2);
                int counter = cursor.getInt(3);
                String hex = cursor.getString(4);
                user = new User(name, nickname, email, counter ,hex);
            }
            cursor.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return user;
    }

    public void addMessageList(SQLiteDatabase db, User curr_user, User foreign_user){
        try{
            Date date1 = new Date();
            Date date2 = new Date();
           addListToUser(db, curr_user, foreign_user, date1);
           addListToUser(db, foreign_user, curr_user, date2);
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            cursor.close();
        }
    }
    private void addListToUser(SQLiteDatabase db, User user, User foreign_user, Date date){
        try{
            String builder = date.getHours() +
                    ":" +
                    date.getMinutes();
            int userId = getUserId(db, user);
            int foreign_id = getUserId(db, foreign_user);
            cursor.close();
            db.execSQL("insert into "+MESSAGE_LIST_TABLE+"("+MESSAGE_LIST_NAME+", "+MESSAGE_LIST_DESCRIPTION+", "+MESSAGE_LIST_USER_ID+", "+MESSAGE_LIST_FOREIGN_USER_ID+")" +
                    " values('"+foreign_user.getName()+"', '"+builder+"', "+userId+", "+foreign_id+")");
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            cursor.close();
        }
    }

    public void getMessagesList(SQLiteDatabase db){
        ArrayList<MessageList> messageLists = new ArrayList<>();
        cursor = db.rawQuery("select * from "+MESSAGE_LIST_TABLE+"", null);

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            int userId = cursor.getInt(3);
            int foreignId = cursor.getInt(4);
            System.out.println(id + " " + name + " " + description + " " + userId + " " + foreignId);
        }
    }

    public ArrayList<MessageList> getMessageLists(SQLiteDatabase db, User user){
        ArrayList<MessageList> chats = new ArrayList<>();
        try{
            int id = 0;
            cursor = db.rawQuery("select "+USER_ID+" from "+USER_TABLE+" where "+USER_EMAIL+" = '"+user.getEmail()+"';", null);
            while(cursor.moveToNext()){
                id = cursor.getInt(0);
            }
            cursor.close();
            cursor = db.rawQuery("select "+MESSAGE_LIST_NAME+", "+MESSAGE_LIST_DESCRIPTION+" from "+MESSAGE_LIST_TABLE+" where "+MESSAGE_LIST_USER_ID+" = "+id+"", null);
            String name;
            String description;
            while(cursor.moveToNext()){
                name = cursor.getString(0);
                description = cursor.getString(1);
                chats.add(new MessageList(name, description));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return chats;
    }

    public void addMessage(SQLiteDatabase db, Message message ,User user1, User user2){
        try{
            int message_id = 0;
            int user1_id = getUserId(db, user1);
            int user2_id = getUserId(db, user2);
            int message_list_id1 = getMessageListId(db, user1_id);
            int message_list_id2 = getMessageListId(db, user2_id);
            //todo Добавление только одного сообщение, но в m2m на две переписки
            db.execSQL("insert into "+MESSAGE_TABLE+"("+MESSAGE_TEXT+", "+MESSAGE_DATE+" , "+MESSAGE_USER_NAME+")" +
                    " values ('"+message.getText()+"' , '"+message.getDate()+"' , '"+message.getUser_name()+"') ");
            cursor = db.rawQuery("select "+MESSAGE_ID+" from "+MESSAGE_TABLE+" where "+MESSAGE_DATE+" = '"+message.getDate()+"'",null);
            while(cursor.moveToNext()){
                message_id = cursor.getInt(0);
            }
            cursor.close();
            addM2MRow(db, user1_id, message_list_id1, message_id);
            addM2MRow(db, user2_id, message_list_id2, message_id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private int getUserId(SQLiteDatabase db, User user){
        int user_id = 0;
        cursor = db.rawQuery("select "+USER_ID+" from "+USER_TABLE+" where "+USER_EMAIL+" = '"+user.getEmail()+"'", null);
        while(cursor.moveToNext()){
            user_id = cursor.getInt(0);
        }
        cursor.close();
        return user_id;
    }

    private int getMessageListId(SQLiteDatabase db, int userId){
        int message_list_id = 0;
        cursor = db.rawQuery("select "+MESSAGE_LIST_ID+" from "+MESSAGE_LIST_TABLE+" where "+MESSAGE_LIST_USER_ID+" ="+userId+" ", null);
        while(cursor.moveToNext()){
            message_list_id = cursor.getInt(0);
        }
        cursor.close();
        return  message_list_id;
    }
    private void addM2MRow(SQLiteDatabase db, int userId, int messageListId, int message_id){
        db.execSQL("insert into "+M2M_LIST_TABLE+"("+M2M_MESSAGE_ID+", "+M2M_MESSAGE_LIST_ID+", "+M2M_USER_ID+") values " +
                "("+message_id+", "+messageListId+", "+userId+")");
    }

    public ArrayList<Message> getAllMessages(SQLiteDatabase db, User user){
        ArrayList<Message> messages = new ArrayList<>();

        int userId = getUserId(db, user);
        int messageListId = getMessageListId(db, userId);

        cursor = db.rawQuery("select "+MESSAGE_TEXT+", "+MESSAGE_DATE+" , "+MESSAGE_USER_NAME+" from "+M2M_LIST_TABLE+" " +
                "inner join "+MESSAGE_TABLE+" on "+M2M_LIST_TABLE+"."+M2M_MESSAGE_ID+"= "+MESSAGE_TABLE+"."+MESSAGE_ID+"" +
                " where "+M2M_USER_ID+" = "+userId+" and "+M2M_MESSAGE_LIST_ID+"="+messageListId+"", null);
        while(cursor.moveToNext()){
            String text = cursor.getString(0);
            String date = cursor.getString(1);
            String user_name = cursor.getString(2);
            messages.add(new Message(text, date, user_name));
        }
        cursor.close();
        return messages;
    }

    /*
    public void updateUser(SQLiteDatabase db, User user){
        try{
            db.execSQL("update "+USER_ID+" set "+USER_CHAT_COUNT+"");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

     */

    //public ArrayList<MessageList>(SQLiteDatabase db, string )
    public User getUserByHex(SQLiteDatabase db, String hex){
        User user = null;
        try{
            String name = "";
            String nickname = "";
            String email = "";
            String user_hex = "";
            int counter = 0;
            cursor = db.rawQuery("select "+USER_NAME+", "+USER_NICKNAME+", "+USER_EMAIL+", "+USER_CHAT_COUNT+", "+USER_HEX+" from "+USER_TABLE+" where "+USER_HEX+" = '"+hex+"'", null);
            while(cursor.moveToNext()){
                name = cursor.getString(0);
                nickname = cursor.getString(1);
                email = cursor.getString(2);
                counter = cursor.getInt(3);
                user_hex = cursor.getString(4);
            }
            cursor.close();
            if(!name.equals("") && !nickname.equals("") && !email.equals("") && !user_hex.equals("")){
                user = new User(name, nickname, email, counter, user_hex);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return user;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+MESSAGE_LIST_TABLE+"("+MESSAGE_LIST_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                ""+MESSAGE_LIST_NAME+" TEXT, "+MESSAGE_LIST_DESCRIPTION+" TEXT, "+MESSAGE_LIST_USER_ID+" INTEGER, " +
                ""+MESSAGE_LIST_FOREIGN_USER_ID+" INTEGER, FOREIGN KEY ("+MESSAGE_LIST_USER_ID+") REFERENCES "+USER_TABLE+" ("+USER_ID+")," +
                "FOREIGN KEY ("+MESSAGE_LIST_FOREIGN_USER_ID+") REFERENCES "+USER_TABLE+" ("+USER_ID+"));");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+USER_TABLE+"("+USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                ""+USER_NAME+" TEXT, "+USER_NICKNAME+" TEXT, "+USER_EMAIL+" TEXT, "+USER_CHAT_COUNT+" INTEGER," +
                ""+USER_HEX+" TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+M2M_LIST_TABLE+"("+M2M_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                ""+M2M_MESSAGE_ID+" INTEGER, "+M2M_MESSAGE_LIST_ID+" INTEGER, "+M2M_USER_ID+" INTEGER ,FOREIGN KEY ("+M2M_MESSAGE_ID+") REFERENCES" +
                " "+MESSAGE_TABLE+" ("+MESSAGE_ID+"), FOREIGN KEY ("+M2M_MESSAGE_LIST_ID+") REFERENCES "+MESSAGE_LIST_TABLE+"" +
                "("+MESSAGE_LIST_ID+"), FOREIGN KEY ("+M2M_USER_ID+") REFERENCES "+USER_TABLE+" ("+USER_ID+"));");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+MESSAGE_TABLE+"("+MESSAGE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ""+MESSAGE_TEXT+" TEXT, "+MESSAGE_DATE+" TEXT" +
                ", "+MESSAGE_USER_NAME+" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGE_LIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + M2M_LIST_TABLE);
    }
}

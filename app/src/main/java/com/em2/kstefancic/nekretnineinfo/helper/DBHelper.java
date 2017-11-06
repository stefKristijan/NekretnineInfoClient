package com.em2.kstefancic.nekretnineinfo.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.em2.kstefancic.nekretnineinfo.api.model.ConstructionSystem;
import com.em2.kstefancic.nekretnineinfo.api.model.Material;
import com.em2.kstefancic.nekretnineinfo.api.model.Position;
import com.em2.kstefancic.nekretnineinfo.api.model.Purpose;
import com.em2.kstefancic.nekretnineinfo.api.model.User;

/**
 * Created by user on 3.11.2017..
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + Schema.TABLE_USER + " (" + Schema.USER_ID+" INTEGER," +
            Schema.USER_UNIQID+" VARCHAR(60),"+Schema.USERNAME+" VARCHAR(50),"+Schema.PASSWORD+" VARCHAR(50),"+Schema.FIRST_NAME+" VARCHAR(50),"+
            Schema.LAST_NAME+" VARCHAR(50),"+Schema.EMAIL+" VARCHAR(100));";
    private static final String CREATE_TABLE_POSITION = "CREATE TABLE IF NOT EXISTS " + Schema.TABLE_POSITION + " (" + Schema.POSITION_ID+" INTEGER," + Schema.POSITION+" VARCHAR(50));";
    private static final String CREATE_TABLE_MATERIAL = "CREATE TABLE IF NOT EXISTS " + Schema.TABLE_MATERIAL + " (" + Schema.MATERIAL_ID+" INTEGER," + Schema.MATERIAL+" VARCHAR(50));";
    private static final String CREATE_TABLE_PURPOSE = "CREATE TABLE IF NOT EXISTS " + Schema.TABLE_PURPOSE + " (" + Schema.PURPOSE_ID+" INTEGER," + Schema.PURPOSE+" VARCHAR(50));";
    private static final String CREATE_TABLE_CONSTRUCT_SYS = "CREATE TABLE IF NOT EXISTS " + Schema.TABLE_CONSTUCT_SYS + " (" + Schema.CONSTR_SYS_ID+" INTEGER," + Schema.CONSTR_SYS+" VARCHAR(50));";
    private static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS "+ Schema.TABLE_USER;
    private static final String DROP_TABLE_PURPOSE = "DROP TABLE IF EXISTS "+ Schema.TABLE_PURPOSE;
    private static final String DROP_TABLE_POSITION = "DROP TABLE IF EXISTS "+ Schema.TABLE_POSITION;
    private static final String DROP_TABLE_MATERIAL = "DROP TABLE IF EXISTS "+ Schema.TABLE_MATERIAL;
    private static final String DROP_TABLE_CONSTRUCT_SYS = "DROP TABLE IF EXISTS "+ Schema.TABLE_CONSTUCT_SYS;
    private static final String SELECT_USER = "SELECT * FROM " + Schema.TABLE_USER;


    private static DBHelper mDbHelper = null;

    public DBHelper(Context context){
        super(context.getApplicationContext(), Schema.DATABASE_NAME, null, Schema.SCHEMA_VERSION);
    }

    public static synchronized DBHelper getInstance (Context context){

        if(mDbHelper==null){
            mDbHelper = new DBHelper(context);
        }
        return mDbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_POSITION);
        sqLiteDatabase.execSQL(CREATE_TABLE_PURPOSE);
        sqLiteDatabase.execSQL(CREATE_TABLE_MATERIAL);
        sqLiteDatabase.execSQL(CREATE_TABLE_CONSTRUCT_SYS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_USER);
        sqLiteDatabase.execSQL(DROP_TABLE_POSITION);
        sqLiteDatabase.execSQL(DROP_TABLE_PURPOSE);
        sqLiteDatabase.execSQL(DROP_TABLE_MATERIAL);
        sqLiteDatabase.execSQL(DROP_TABLE_CONSTRUCT_SYS);
        this.onCreate(sqLiteDatabase);
    }

    public void insertUser(User user){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.USER_ID,user.getId());
        contentValues.put(Schema.USER_UNIQID,user.getuId());
        contentValues.put(Schema.USERNAME,user.getmUsername());
        contentValues.put(Schema.PASSWORD,user.getmPassword());
        contentValues.put(Schema.FIRST_NAME,user.getmFirstName());
        contentValues.put(Schema.LAST_NAME,user.getmLastName());
        contentValues.put(Schema.EMAIL,user.getmEmail());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_USER,null,contentValues);
        wdb.close();
    }

    public User getUser(){
        User user = null;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor userCursor = wdb.rawQuery(SELECT_USER,null);
        if(userCursor.moveToFirst()){
            Long id = userCursor.getLong(0);
            String uniqId = userCursor.getString(1);
            String username = userCursor.getString(2);
            String password = userCursor.getString(3);
            String firstName = userCursor.getString(4);
            String lastName = userCursor.getString(5);
            String email = userCursor.getString(6);
            user = new User(uniqId,firstName,lastName,username,password,email);
            user.setId(id);
        }
        userCursor.close();
        wdb.close();
        return user;
    }

    public void insertPosition(Position position){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.POSITION, position.getmPosition());
        contentValues.put(Schema.POSITION_ID, position.getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_POSITION,null,contentValues);
        wdb.close();
    }

    public void insertPurpose(Purpose purpose){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.PURPOSE, purpose.getmPurpose());
        contentValues.put(Schema.PURPOSE_ID, purpose.getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_PURPOSE,null,contentValues);
        wdb.close();
    }

    public void insertMaterial(Material material){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.MATERIAL, material.getmMaterial());
        contentValues.put(Schema.MATERIAL_ID, material.getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_MATERIAL,null,contentValues);
        wdb.close();
    }

    public void insertConstructSys(ConstructionSystem constructionSystem){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.CONSTR_SYS, constructionSystem.getmConstructionSystem());
        contentValues.put(Schema.CONSTR_SYS_ID, constructionSystem.getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_CONSTUCT_SYS,null,contentValues);
        wdb.close();
    }

    public static class Schema{

        private static final int SCHEMA_VERSION = 2;
        private static final String DATABASE_NAME = "nekretnine_info.db";

        //user table
        static final String TABLE_USER = "user";
        static final String USER_ID = "id";
        static final String USER_UNIQID = "unique_id";
        static final String USERNAME = "username";
        static final String PASSWORD = "password";
        static final String FIRST_NAME = "first_name";
        static final String LAST_NAME = "last_name";
        static final String EMAIL = "email";

        //position table
        static final String TABLE_POSITION = "positions";
        static final String POSITION_ID = "position_id";
        static final String POSITION = "position";

        //position table
        static final String TABLE_PURPOSE = "purposes";
        static final String PURPOSE_ID = "purpose_id";
        static final String PURPOSE = "purpose";

        static final String TABLE_MATERIAL = "materials";
        static final String MATERIAL_ID = "material_id";
        static final String MATERIAL = "material";
        //construction system table
        static final String TABLE_CONSTUCT_SYS = "construction_systems";
        static final String CONSTR_SYS_ID = "constr_sys_id";
        static final String CONSTR_SYS = "construction_system";
    }
}

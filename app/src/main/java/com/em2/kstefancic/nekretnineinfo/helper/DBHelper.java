package com.em2.kstefancic.nekretnineinfo.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.em2.kstefancic.nekretnineinfo.api.model.Building;
import com.em2.kstefancic.nekretnineinfo.api.model.ImagePath;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.CeilingMaterial;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.ConstructionSystem;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Material;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Position;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Purpose;
import com.em2.kstefancic.nekretnineinfo.api.model.User;

/**
 * Created by user on 3.11.2017..
 */

public class DBHelper extends SQLiteOpenHelper {

    //CREATING TABLES
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE IF NOT EXISTS " + Schema.TABLE_USER + " (" +
                                            Schema.USER_ID+" INTEGER PRIMARY KEY," +
                                            Schema.USER_UNIQID+" VARCHAR(60),"+
                                            Schema.USERNAME+" VARCHAR(50),"+
                                            Schema.PASSWORD+" VARCHAR(50),"+
                                            Schema.FIRST_NAME+" VARCHAR(50),"+
                                            Schema.LAST_NAME+" VARCHAR(50),"+
                                            Schema.EMAIL+" VARCHAR(100),"+
                                            Schema.ENABLED+" BOOLEAN);";

    private static final String CREATE_TABLE_POSITION =
            "CREATE TABLE IF NOT EXISTS " + Schema.TABLE_POSITION + " (" +
                                            Schema.POSITION_ID+" INTEGER PRIMARY KEY," +
                                            Schema.POSITION+" VARCHAR(50));";

    private static final String CREATE_TABLE_MATERIAL =
            "CREATE TABLE IF NOT EXISTS " + Schema.TABLE_MATERIAL + " (" +
                                            Schema.MATERIAL_ID+" INTEGER PRIMARY KEY," +
                                            Schema.MATERIAL+" VARCHAR(50));";

    private static final String CREATE_TABLE_PURPOSE =
            "CREATE TABLE IF NOT EXISTS " + Schema.TABLE_PURPOSE + " (" +
                                            Schema.PURPOSE_ID+" INTEGER PRIMARY KEY," +
                                            Schema.PURPOSE+" VARCHAR(50));";

    private static final String CREATE_TABLE_CONSTRUCT_SYS =
            "CREATE TABLE IF NOT EXISTS " + Schema.TABLE_CONSTUCT_SYS + " (" +
                                            Schema.CONSTR_SYS_ID+" INTEGER PRIMARY KEY," +
                                            Schema.CONSTR_SYS+" VARCHAR(50));";

    private static final String CREATE_TABLE_CEILING_MATERIAL=
            "CREATE TABLE IF NOT EXISTS " + Schema.TABLE_CEILING_MATERIAL + " (" +
                                            Schema.CEILING_MATERIAL_ID+" INTEGER PRIMARY KEY," +
                                            Schema.CEILING_MATERIAL+" VARCHAR(50));";

    private static final String CREATE_TABLE_BUILDING=
            "CREATE TABLE IF NOT EXISTS "+ Schema.TABLE_BUILDING + " ("+
                                            Schema.BUILDING_ID +" BIGINT PRIMARY KEY,"+
                                            Schema.BRUTO_AREA + " DOUBLE,"+
                                            Schema.CADASTRAL_PARTICLE +" VARCHAR(10),"+
                                            Schema.CITY +" VARCHAR(255),"+
                                            Schema.DATE+" TIMESTAMP,"+
                                            Schema.FLOOR_AREA+ " DOUBLE,"+
                                            Schema.FLOOR_HEIGHT +" DOUBLE,"+
                                            Schema.FULL_HEIGHT+" DOUBLE,"+
                                            Schema.LENGTH +" DOUBLE,"+
                                            Schema.NUMBER_OF_FLOORS +" INTEGER,"+
                                            Schema.ORIENTATION +" INTEGER,"+
                                            Schema.PROPER_GROUND_PLAN+" BOOLEAN,"+
                                            Schema.STATE +" VARCHAR(100),"+
                                            Schema.STREET +" VARCHAR(150),"+
                                            Schema.STREET_NUM +" INTEGER,"+
                                            Schema.STREET_CHAR +" CHAR,"+
                                            Schema.SYNCHRONIZED +" BOOLEAN,"+
                                            Schema.WIDTH +" DOUBLE,"+
                                            Schema.YEAR_OF_BUILD +" VARCHAR(10),"+
                                            Schema.B_CEILING_MATERIAL_ID +" INTEGER,"+
                                            Schema.B_CONSTRUCT_SYS_ID +" INTEGER,"+
                                            Schema.B_MATERIAL_ID+" INTEGER,"+
                                            Schema.B_POSITION_ID +" BIGINT,"+
                                            Schema.B_PURPOSE_ID +" INTEGER,"+
                                            Schema.B_USER_ID+" BIGINT," +
                                            "CONSTRAINT building_user_fk FOREIGN KEY("+Schema.B_USER_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.USER_ID+"),"+
                                            "CONSTRAINT building_material_fk FOREIGN KEY("+Schema.B_MATERIAL_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.MATERIAL_ID+"),"+
                                            "CONSTRAINT building_c_material_fk FOREIGN KEY("+Schema.B_CEILING_MATERIAL_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.CEILING_MATERIAL_ID+"),"+
                                            "CONSTRAINT building_constr_sys_fk FOREIGN KEY("+Schema.B_CONSTRUCT_SYS_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.CONSTR_SYS_ID+"),"+
                                            "CONSTRAINT building_position_fk FOREIGN KEY("+Schema.B_POSITION_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.POSITION_ID+"),"+
                                            "CONSTRAINT building_purpose_fk FOREIGN KEY("+Schema.B_PURPOSE_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.PURPOSE_ID+"),"+
                                            "CONSTRAINT unique_building UNIQUE ("+Schema.STREET+", "+Schema.STREET_NUM+", "+Schema.STREET_CHAR+", "+Schema.CADASTRAL_PARTICLE+"));";

    private static final String CREATE_TABLE_IMAGE_PATH=
            "CREATE TABLE IF NOT EXISTS " + Schema.TABLE_IMAGES + " (" +
                    Schema.IMAGE_ID +" INTEGER PRIMARY KEY," +
                    Schema.IMAGE_PATH+" VARCHAR(255),"+
                    Schema.IMAGE+" BLOB,"+
                    Schema.IP_BUILDING_ID+ " BIGINT,"+
                    "CONSTRAINT image_building FOREIGN KEY("+Schema.IP_BUILDING_ID+") REFERENCES "+Schema.TABLE_BUILDING+"("+Schema.BUILDING_ID+"));";

    //DROPING TABLES
    private static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS "+ Schema.TABLE_USER;
    private static final String DROP_TABLE_PURPOSE = "DROP TABLE IF EXISTS "+ Schema.TABLE_PURPOSE;
    private static final String DROP_TABLE_POSITION = "DROP TABLE IF EXISTS "+ Schema.TABLE_POSITION;
    private static final String DROP_TABLE_MATERIAL = "DROP TABLE IF EXISTS "+ Schema.TABLE_MATERIAL;
    private static final String DROP_TABLE_CONSTRUCT_SYS = "DROP TABLE IF EXISTS "+ Schema.TABLE_CONSTUCT_SYS;
    private static final String DROP_TABLE_CEILING_MATERIAL = "DROP TABLE IF EXISTS "+ Schema.TABLE_CEILING_MATERIAL;
    private static final String DROP_TABLE_BUILDING = "DROP TABLE IF EXISTS " + Schema.TABLE_BUILDING;

    //SELECTING TABLES
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
        sqLiteDatabase.execSQL(CREATE_TABLE_BUILDING);
        sqLiteDatabase.execSQL(CREATE_TABLE_CEILING_MATERIAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_USER);
        sqLiteDatabase.execSQL(DROP_TABLE_POSITION);
        sqLiteDatabase.execSQL(DROP_TABLE_PURPOSE);
        sqLiteDatabase.execSQL(DROP_TABLE_MATERIAL);
        sqLiteDatabase.execSQL(DROP_TABLE_CONSTRUCT_SYS);
        sqLiteDatabase.execSQL(DROP_TABLE_BUILDING);
        sqLiteDatabase.execSQL(DROP_TABLE_CEILING_MATERIAL);
        this.onCreate(sqLiteDatabase);
    }


    /*
    USER QUERIES
    - insertUser(User user)
    - deleteUserTable()
    - getUser()

     */
    public void insertUser(User user){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.USER_ID,user.getId());
        contentValues.put(Schema.USER_UNIQID,user.getuId());
        contentValues.put(Schema.USERNAME,user.getUsername());
        contentValues.put(Schema.PASSWORD,user.getPassword());
        contentValues.put(Schema.FIRST_NAME,user.getFirstName());
        contentValues.put(Schema.LAST_NAME,user.getLastName());
        contentValues.put(Schema.EMAIL,user.getEmail());
        contentValues.put(Schema.ENABLED,user.isEnabled());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_USER,null,contentValues);
        wdb.close();
    }

    public void deleteUserTable() {
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.execSQL(DROP_TABLE_USER);
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


    /*
    POSITION QUERIES
    - insertPosition(Position position)
     */
    public void insertPosition(Position position){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.POSITION, position.getPosition());
        contentValues.put(Schema.POSITION_ID, position.getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_POSITION,null,contentValues);
        wdb.close();
    }
    /*
        PURPOSE QUERIES
        - inserPurpose(Purpose purpose)
         */
    public void insertPurpose(Purpose purpose){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.PURPOSE, purpose.getPurpose());
        contentValues.put(Schema.PURPOSE_ID, purpose.getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_PURPOSE,null,contentValues);
        wdb.close();
    }

    /*
    MATERIAL QUERIES
    - insertMaterial(Material material)
     */

    public void insertMaterial(Material material){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.MATERIAL, material.getMaterial());
        contentValues.put(Schema.MATERIAL_ID, material.getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_MATERIAL,null,contentValues);
        wdb.close();
    }

    /*
    CONSTRUCTION SYSTEM QUERIES
    - insertConstructSys(ConstructionSystem constructionSystem)
     */

    public void insertConstructSys(ConstructionSystem constructionSystem){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.CONSTR_SYS, constructionSystem.getConstructionSystem());
        contentValues.put(Schema.CONSTR_SYS_ID, constructionSystem.getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_CONSTUCT_SYS,null,contentValues);
        wdb.close();
    }

    /*
     CEILING MATERIAL QUERIES
     - insertCeilingMaterial(CeilingMaterial ceilingMaterial)
     */
    public void insertCeilingMaterial(CeilingMaterial ceilingMaterial){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.CEILING_MATERIAL, ceilingMaterial.getCeilingMaterial());
        contentValues.put(Schema.CEILING_MATERIAL_ID, ceilingMaterial.getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_CEILING_MATERIAL,null,contentValues);
        wdb.close();
    }

    /*
    BUILDING QUERIES
    - insertBuilding(Building building)
     */

    public void insertBuilding(Building building){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.BUILDING_ID, building.getId());
        contentValues.put(Schema.BRUTO_AREA, building.getBrutoArea());
        contentValues.put(Schema.CADASTRAL_PARTICLE, building.getCadastralParticle());
        contentValues.put(Schema.CITY, building.getCity());
        contentValues.put(Schema.DATE, String.valueOf(building.getDate()));
        contentValues.put(Schema.FLOOR_AREA, building.getFloorArea());
        contentValues.put(Schema.FLOOR_HEIGHT, building.getFloorHeight());
        contentValues.put(Schema.FULL_HEIGHT, building.getFullHeight());
        contentValues.put(Schema.LENGTH, building.getLength());
        contentValues.put(Schema.NUMBER_OF_FLOORS, building.getNumberOfFloors());
        contentValues.put(Schema.ORIENTATION, String.valueOf(building.getOrientation()));
        contentValues.put(Schema.PROPER_GROUND_PLAN, building.isProperGroundPlan());
        contentValues.put(Schema.STATE, building.getState());
        contentValues.put(Schema.STREET, building.getStreet());
        contentValues.put(Schema.STREET_CHAR, String.valueOf(building.getStreetNumberChar()));
        contentValues.put(Schema.STREET_NUM, building.getStreetNumber());
        contentValues.put(Schema.SYNCHRONIZED, building.isSynchronizedWithDatabase());
        contentValues.put(Schema.WIDTH, building.getWidth());
        contentValues.put(Schema.YEAR_OF_BUILD, building.getYearOfBuild());
        contentValues.put(Schema.B_CEILING_MATERIAL_ID, building.getCeilingMaterial().getId());
        contentValues.put(Schema.B_CONSTRUCT_SYS_ID, building.getConstructionSystem().getId());
        contentValues.put(Schema.B_USER_ID, building.getUser().getId());
        contentValues.put(Schema.B_MATERIAL_ID, building.getMaterial().getId());
        contentValues.put(Schema.B_PURPOSE_ID, building.getPurpose().getId());
        contentValues.put(Schema.B_POSITION_ID, building.getPosition().getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_BUILDING,null,contentValues);
        wdb.close();

    }

    /*
    IMAGE PATH QUERIES
    - insertImage()
     */

    public void insertImage(ImagePath imagePath, byte[] imageBytes, long buildingId){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.IMAGE_ID,imagePath.getId());
        contentValues.put(Schema.IMAGE_PATH,imagePath.getImagePath());
        contentValues.put(Schema.IMAGE,imageBytes);
        contentValues.put(Schema.IP_BUILDING_ID,buildingId);
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_IMAGES,null, contentValues);
        wdb.close();
    }



    public static class Schema{

        private static final int SCHEMA_VERSION = 8;
        private static final String DATABASE_NAME = "nekretnine_info.db";

        //USER table
        static final String TABLE_USER = "user";
        static final String USER_ID = "id";
        static final String USER_UNIQID = "unique_id";
        static final String USERNAME = "username";
        static final String PASSWORD = "password";
        static final String FIRST_NAME = "first_name";
        static final String LAST_NAME = "last_name";
        static final String EMAIL = "email";
        static final String ENABLED ="enabled";

        //POSITION table
        static final String TABLE_POSITION = "positions";
        static final String POSITION_ID = "position_id";
        static final String POSITION = "position";

        //PURPOSE table
        static final String TABLE_PURPOSE = "purposes";
        static final String PURPOSE_ID = "purpose_id";
        static final String PURPOSE = "purpose";

        static final String TABLE_MATERIAL = "materials";
        static final String MATERIAL_ID = "material_id";
        static final String MATERIAL = "material";
        //CONSTRUCTION SYSTEM table
        static final String TABLE_CONSTUCT_SYS = "construction_systems";
        static final String CONSTR_SYS_ID = "constr_sys_id";
        static final String CONSTR_SYS = "construction_system";

        //CEILING MATERIAL table
        static final String TABLE_CEILING_MATERIAL= "ceiling_materials";
        static final String CEILING_MATERIAL_ID = "ceiling_material_id";
        static final String CEILING_MATERIAL = "ceiling_material";

        //BUILDING table;
        static final String TABLE_BUILDING = "buildings";
        static final String BUILDING_ID = "building_id";
        static final String DATE = "date";
        static final String YEAR_OF_BUILD = "year_of_build";
        static final String PROPER_GROUND_PLAN="proper_ground_plan";
        static final String B_USER_ID="user_id";
        static final String B_PURPOSE_ID ="purpose_id";
        static final String B_POSITION_ID = "position_id";
        static final String B_MATERIAL_ID = "material_id";
        static final String B_CEILING_MATERIAL_ID = "ceiling_material_id";
        static final String B_CONSTRUCT_SYS_ID = "construction_sys_id";
        static final String SYNCHRONIZED = "synchronized";
        static final String ORIENTATION = "orientation";
        static final String CADASTRAL_PARTICLE = "cadastral_particle";
        static final String STREET = "street";
        static final String STREET_NUM = "street_num";
        static final String STREET_CHAR = "street_char";
        static final String CITY = "city";
        static final String STATE ="state";
        static final String WIDTH = "width";
        static final String LENGTH = "length";
        static final String FLOOR_AREA = "floor_area";
        static final String BRUTO_AREA = "bruto_area";
        static final String FLOOR_HEIGHT = "floor_height";
        static final String FULL_HEIGHT = "full_height";
        static final String NUMBER_OF_FLOORS = "num_of_floors";

        //IMAGE PATH table
        static final String TABLE_IMAGES = "images";
        static final String IMAGE_ID = "image_path_id";
        static final String IMAGE_PATH = "image_path";
        static final String IMAGE = "image";
        static final String IP_BUILDING_ID ="building_id";


    }
}

package com.kstefancic.nekretnineinfo.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.kstefancic.nekretnineinfo.api.model.Building;
import com.kstefancic.nekretnineinfo.api.model.BuildingLocation;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.CeilingMaterial;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.ConstructionSystem;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Material;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Position;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Purpose;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Roof;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Sector;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.City;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.State;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.Street;
import com.kstefancic.nekretnineinfo.api.model.User;
import com.kstefancic.nekretnineinfo.api.model.localDBdto.LocalImage;

import java.nio.channels.SelectableChannel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by user on 3.11.2017..
 */

public class DBHelper extends SQLiteOpenHelper {

    //CREATING TABLES
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + Schema.TABLE_USER + " (" +
                                            Schema.USER_ID+" BIGINT PRIMARY KEY," +
                                            Schema.USER_UNIQID+" VARCHAR(255) NOT NULL UNIQUE,"+
                                            Schema.USERNAME+" VARCHAR(50) NOT NULL,"+
                                            Schema.PASSWORD+" VARCHAR(255) NOT NULL,"+
                                            Schema.FIRST_NAME+" VARCHAR(50) NOT NULL,"+
                                            Schema.LAST_NAME+" VARCHAR(50) NOT NULL,"+
                                            Schema.EMAIL+" VARCHAR(100) NOT NULL,"+
                                            Schema.ENABLED+" BOOLEAN  NOT NULL);";

    private static final String CREATE_TABLE_POSITION =
            "CREATE TABLE " + Schema.TABLE_POSITION + " (" +
                                            Schema.POSITION_ID+" INTEGER PRIMARY KEY," +
                                            Schema.POSITION+" VARCHAR(50) NOT NULL);";

    private static final String CREATE_TABLE_MATERIAL =
            "CREATE TABLE " + Schema.TABLE_MATERIAL + " (" +
                                            Schema.MATERIAL_ID+" INTEGER PRIMARY KEY," +
                                            Schema.MATERIAL+" VARCHAR(50) NOT NULL);";

    private static final String CREATE_TABLE_SECTOR=
            "CREATE TABLE " + Schema.TABLE_SECTOR + " (" +
                                            Schema.SECTOR_ID+" INTEGER PRIMARY KEY," +
                                            Schema.SECTOR_NAME+" VARCHAR(100) NOT NULL);";

    private static final String CREATE_TABLE_ROOF=
            "CREATE TABLE " + Schema.TABLE_ROOF + " (" +
                    Schema.ROOF_ID+" INTEGER PRIMARY KEY," +
                    Schema.ROOF_TYPE+" VARCHAR(100) NOT NULL);";

    private static final String CREATE_TABLE_PURPOSE =
            "CREATE TABLE " + Schema.TABLE_PURPOSE + " (" +
                                            Schema.PURPOSE_ID+" INTEGER PRIMARY KEY," +
                                            Schema.PURPOSE+" VARCHAR(50) NOT NULL," +
                                            Schema.P_SECTOR_ID+" INTEGER NOT NULL," +
                                            "CONSTRAINT purpose_sector_fk FOREIGN KEY("+Schema.P_SECTOR_ID+") REFERENCES "+Schema.TABLE_SECTOR+"("+Schema.SECTOR_ID+"))";


    private static final String CREATE_TABLE_CONSTRUCT_SYS =
            "CREATE TABLE " + Schema.TABLE_CONSTUCT_SYS + " (" +
                                            Schema.CONSTR_SYS_ID+" INTEGER PRIMARY KEY," +
                                            Schema.CONSTR_SYS+" VARCHAR(100) NOT NULL);";

    private static final String CREATE_TABLE_CEILING_MATERIAL=
            "CREATE TABLE " + Schema.TABLE_CEILING_MATERIAL + " (" +
                                            Schema.CEILING_MATERIAL_ID+" INTEGER PRIMARY KEY," +
                                            Schema.CEILING_MATERIAL+" VARCHAR(50) NOT NULL);";

    private static final String CREATE_TABLE_BUILDING_LOCATION=
            "CREATE TABLE "+ Schema.TABLE_BUILDING_LOCATION + " ("+
                    Schema.LOCATION_ID +" BIGINT PRIMARY KEY,"+
                    Schema.STREET + " VARCHAR(150) NOT NULL,"+
                    Schema.STREET_NUM+" INTEGER NOT NULL,"+
                    Schema.STREET_CHAR+ " CHAR,"+
                    Schema.CITY+" VARCHAR(100) NOT NULL,"+
                    Schema.STATE+" VARCHAR(100) NOT NULL,"+
                    Schema.CADASTRAL_PARTICLE+" VARCHAR(11) NOT NULL,"+
                    Schema.LOC_BUILDING_ID+ " BIGINT NOT NULL,"+
                    "CONSTRAINT location_building_fk FOREIGN KEY ("+Schema.LOC_BUILDING_ID+") REFERENCES "+Schema.TABLE_BUILDING+"("+Schema.BUILDING_ID+"),"+
                    "CONSTRAINT unique_location UNIQUE ("+Schema.STREET+", "+Schema.STREET_NUM+", "+Schema.STREET_CHAR+", "+Schema.CADASTRAL_PARTICLE+"));";

    private static final String CREATE_TABLE_BUILDING=
            "CREATE TABLE "+ Schema.TABLE_BUILDING + " ("+
                                            Schema.BUILDING_ID +" BIGINT PRIMARY KEY,"+
                                            Schema.BUILDING_UID +" VARCHAR(255) NOT NULL UNIQUE,"+
                                            Schema.BRUTO_AREA + " DOUBLE NOT NULL,"+
                                            Schema.BASEMENT_BRUTO_AREA + " DOUBLE,"+
                                            Schema.RESIDENTIAL_BRUTO_AREA + " DOUBLE,"+
                                            Schema.BUSINESS_BRUTO_AREA + " DOUBLE,"+
                                            Schema.DATE+" TIMESTAMP NOT NULL,"+
                                            Schema.FLOOR_HEIGHT +" DOUBLE NOT NULL,"+
                                            Schema.FULL_HEIGHT+" DOUBLE NOT NULL,"+
                                            Schema.LENGTH +" DOUBLE NOT NULL,"+
                                            Schema.NUMBER_OF_FLOORS +" INTEGER NOT NULL,"+
                                            Schema.PROPER_GROUND_PLAN+" BOOLEAN NOT NULL,"+
                                            Schema.SYNCHRONIZED +" BOOLEAN NOT NULL,"+
                                            Schema.WIDTH +" DOUBLE NOT NULL,"+
                                            Schema.YEAR_OF_BUILD +" VARCHAR(10) NOT NULL,"+
                                            Schema.B_CEILING_MATERIAL_ID +" INTEGER NOT NULL,"+
                                            Schema.B_CONSTRUCT_SYS_ID +" INTEGER NOT NULL,"+
                                            Schema.B_MATERIAL_ID+" INTEGER NOT NULL,"+
                                            Schema.B_POSITION_ID +" INTEGER NOT NULL,"+
                                            Schema.B_PURPOSE_ID +" INTEGER NOT NULL,"+
                                            Schema.B_ROOF_ID +" INTEGER NOT NULL,"+
                                            Schema.B_USER_ID+" BIGINT NOT NULL," +
                                            "CONSTRAINT building_user_fk FOREIGN KEY("+Schema.B_USER_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.USER_ID+"),"+
                                            "CONSTRAINT building_roof_fk FOREIGN KEY("+Schema.B_ROOF_ID+") REFERENCES "+Schema.TABLE_ROOF+"("+Schema.ROOF_ID+"),"+
                                            "CONSTRAINT building_material_fk FOREIGN KEY("+Schema.B_MATERIAL_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.MATERIAL_ID+"),"+
                                            "CONSTRAINT building_c_material_fk FOREIGN KEY("+Schema.B_CEILING_MATERIAL_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.CEILING_MATERIAL_ID+"),"+
                                            "CONSTRAINT building_constr_sys_fk FOREIGN KEY("+Schema.B_CONSTRUCT_SYS_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.CONSTR_SYS_ID+"),"+
                                            "CONSTRAINT building_position_fk FOREIGN KEY("+Schema.B_POSITION_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.POSITION_ID+"),"+
                                            "CONSTRAINT building_purpose_fk FOREIGN KEY("+Schema.B_PURPOSE_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.PURPOSE_ID+"))";

    private static final String CREATE_TABLE_IMAGES =
            "CREATE TABLE " + Schema.TABLE_IMAGES + " (" +
                    Schema.IMAGE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Schema.IMAGE_PATH+" VARCHAR(255) NOT NULL,"+
                    Schema.IMAGE+" BLOB NOT NULL,"+
                    Schema.IP_BUILDING_ID+ " BIGINT NOT NULL,"+
                    "CONSTRAINT image_building_fk FOREIGN KEY("+Schema.IP_BUILDING_ID+") REFERENCES "+Schema.TABLE_BUILDING+"("+Schema.BUILDING_ID+"));";


    private static final String CREATE_TABLE_STATES=
            "CREATE TABLE " + Schema.TABLE_STATES + " (" +
                    Schema.STATE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Schema.STATE_NAME+" VARCHAR(100) NOT NULL)";

    private static final String CREATE_TABLE_CITIES=
            "CREATE TABLE " + Schema.TABLE_CITY + " (" +
                    Schema.CITY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Schema.CITY_NAME+" VARCHAR(255) NOT NULL,"+
                    Schema.CITY_STATE_ID+" INTEGER NOT NULL,"+
                    "CONSTRAINT city_state_fk FOREIGN KEY("+Schema.CITY_STATE_ID+") REFERENCES "+Schema.TABLE_STATES+"("+Schema.STATE_ID+"));";

    private static final String CREATE_TABLE_STREETS=
            "CREATE TABLE " + Schema.TABLE_STREET + " (" +
                    Schema.STREET_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Schema.STREET_NAME+" VARCHAR(255) NOT NULL,"+
                    Schema.STREET_CITY_ID+" INTEGER NOT NULL,"+
                    "CONSTRAINT street_city_fk FOREIGN KEY("+Schema.STREET_CITY_ID+") REFERENCES "+Schema.TABLE_CITY+"("+Schema.CITY_ID+"));";

    //DROPING TABLES
    private static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS "+ Schema.TABLE_USER;
    private static final String DROP_TABLE_PURPOSE = "DROP TABLE IF EXISTS "+ Schema.TABLE_PURPOSE;
    private static final String DROP_TABLE_POSITION = "DROP TABLE IF EXISTS "+ Schema.TABLE_POSITION;
    private static final String DROP_TABLE_MATERIAL = "DROP TABLE IF EXISTS "+ Schema.TABLE_MATERIAL;
    private static final String DROP_TABLE_CONSTRUCT_SYS = "DROP TABLE IF EXISTS "+ Schema.TABLE_CONSTUCT_SYS;
    private static final String DROP_TABLE_CEILING_MATERIAL = "DROP TABLE IF EXISTS "+ Schema.TABLE_CEILING_MATERIAL;
    private static final String DROP_TABLE_BUILDING = "DROP TABLE IF EXISTS " + Schema.TABLE_BUILDING;
    private static final String DROP_TABLE_IMAGES = "DROP TABLE IF EXISTS "+Schema.TABLE_IMAGES;
    private static final String DROP_TABLE_SECTOR = "DROP TABLE IF EXISTS " +Schema.TABLE_SECTOR;
    private static final String DROP_TABLE_LOCATION = "DROP TABLE IF EXISTS "+Schema.TABLE_BUILDING_LOCATION;
    private static final String DROP_TABLE_ROOF = "DROP TABLE IF EXISTS "+Schema.TABLE_ROOF;
    private static final String DROP_TABLE_STATES = "DROP TABLE IF EXISTS "+Schema.TABLE_STATES;
    private static final String DROP_TABLE_STREETS = "DROP TABLE IF EXISTS "+Schema.TABLE_STREET;
    private static final String DROP_TABLE_CITIES = "DROP TABLE IF EXISTS "+Schema.TABLE_CITY;


    //DELETING DATA
    private static final String DELETE_TABLE_USER = "DELETE FROM "+ Schema.TABLE_USER;
    private static final String DELETE_TABLE_PURPOSE = "DELETE FROM "+ Schema.TABLE_PURPOSE;
    private static final String DELETE_TABLE_POSITION = "DELETE FROM "+ Schema.TABLE_POSITION;
    private static final String DELETE_TABLE_MATERIAL ="DELETE FROM "+ Schema.TABLE_MATERIAL;
    private static final String DELETE_TABLE_CONSTRUCT_SYS = "DELETE FROM "+ Schema.TABLE_CONSTUCT_SYS;
    private static final String DELETE_TABLE_CEILING_MATERIAL = "DELETE FROM "+ Schema.TABLE_CEILING_MATERIAL;
    private static final String DELETE_TABLE_BUILDING = "DELETE FROM " + Schema.TABLE_BUILDING;
    private static final String DELETE_TABLE_IMAGES = "DELETE FROM "+Schema.TABLE_IMAGES;
    private static final String DELETE_TABLE_SECTOR = "DELETE FROM "+Schema.TABLE_SECTOR;
    private static final String DELETE_TABLE_LOCATIONS = "DELETE FROM "+Schema.TABLE_BUILDING_LOCATION;
    private static final String DELETE_TABLE_ROOF = "DELETE FROM "+Schema.TABLE_ROOF;
    private static final String DELETE_TABLE_CITY = "DELETE FROM "+Schema.TABLE_CITY;
    private static final String DELETE_TABLE_STREET = "DELETE FROM "+Schema.TABLE_STREET;
    private static final String DELETE_TABLE_STATES = "DELETE FROM "+Schema.TABLE_STATES;

    //SELECTING TABLES
    private static final String SELECT_USER = "SELECT * FROM " + Schema.TABLE_USER;
    private static final String SELECT_BUILDINGS = "SELECT * FROM " + Schema.TABLE_BUILDING;



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
        sqLiteDatabase.execSQL(CREATE_TABLE_IMAGES);
        sqLiteDatabase.execSQL(CREATE_TABLE_SECTOR);
        sqLiteDatabase.execSQL(CREATE_TABLE_BUILDING_LOCATION);
        sqLiteDatabase.execSQL(CREATE_TABLE_ROOF);
        sqLiteDatabase.execSQL(CREATE_TABLE_STATES);
        sqLiteDatabase.execSQL(CREATE_TABLE_CITIES);
        sqLiteDatabase.execSQL(CREATE_TABLE_STREETS);
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
        sqLiteDatabase.execSQL(DROP_TABLE_IMAGES);
        sqLiteDatabase.execSQL(DROP_TABLE_SECTOR);
        sqLiteDatabase.execSQL(DROP_TABLE_LOCATION);
        sqLiteDatabase.execSQL(DROP_TABLE_ROOF);
        sqLiteDatabase.execSQL(DROP_TABLE_STATES);
        sqLiteDatabase.execSQL(DROP_TABLE_STREETS);
        sqLiteDatabase.execSQL(DROP_TABLE_CITIES);
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

    public void deleteAllTables(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(DELETE_TABLE_USER);
        sqLiteDatabase.execSQL(DELETE_TABLE_POSITION);
        sqLiteDatabase.execSQL(DELETE_TABLE_PURPOSE);
        sqLiteDatabase.execSQL(DELETE_TABLE_MATERIAL);
        sqLiteDatabase.execSQL(DELETE_TABLE_CONSTRUCT_SYS);
        sqLiteDatabase.execSQL(DELETE_TABLE_BUILDING);
        sqLiteDatabase.execSQL(DELETE_TABLE_CEILING_MATERIAL);
        sqLiteDatabase.execSQL(DELETE_TABLE_IMAGES);
        sqLiteDatabase.execSQL(DELETE_TABLE_SECTOR);
        sqLiteDatabase.execSQL(DELETE_TABLE_LOCATIONS);
        sqLiteDatabase.execSQL(DELETE_TABLE_ROOF);
        sqLiteDatabase.execSQL(DELETE_TABLE_STREET);
        sqLiteDatabase.execSQL(DELETE_TABLE_STATES);
        sqLiteDatabase.execSQL(DELETE_TABLE_CITY);
        sqLiteDatabase.close();
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
            boolean enabled = userCursor.getInt(7) > 0;
            user = new User(uniqId,firstName,lastName,username,password,email);
            user.setEnabled(enabled);
            user.setId(id);
        }
        userCursor.close();
        wdb.close();
        return user;
    }


    /*
    POSITION QUERIES
    - insertPosition(Position position)
    - getPositionById(int positionId)
    - getAllPositions()
     */
    public void insertPosition(Position position){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.POSITION, position.getPosition());
        contentValues.put(Schema.POSITION_ID, position.getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_POSITION,null,contentValues);
        wdb.close();
    }

    public Position getPositionById(int positionId){
        String getPositionQuery = "SELECT * FROM "+Schema.TABLE_POSITION+" WHERE "+Schema.POSITION_ID+"="+positionId;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor positionCursor = wdb.rawQuery(getPositionQuery,null);
        Position positionObj = null;
        if(positionCursor.moveToFirst()){
            int id = positionCursor.getInt(0);
            String position = positionCursor.getString(1);
            positionObj=new Position(id,position);
        }
        positionCursor.close();
        wdb.close();
        return positionObj;
    }

    public List<Position> getAllPositions(){
        List<Position> positions = new ArrayList<>();
        String getPositionQuery = "SELECT * FROM "+Schema.TABLE_POSITION;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor positionCursor = wdb.rawQuery(getPositionQuery,null);
        if(positionCursor.moveToFirst()){
            do{
                int id = positionCursor.getInt(0);
                String position = positionCursor.getString(1);
                Position positionObj=new Position(id,position);
                positions.add(positionObj);
            }while(positionCursor.moveToNext());
        }
        positionCursor.close();
        wdb.close();
        return positions;
    }

  /*
    ROOF QUERIES
    - insertRoof(Roof roof)
    - getRoofById(int roofId)
    - getAllRoofs()
     */
    public void insertRoof(Roof roof){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.ROOF_ID, roof.getId());
        contentValues.put(Schema.ROOF_TYPE, roof.getRoofType());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_ROOF,null,contentValues);
        wdb.close();
    }

    public Roof getRoofById(int roofId){
        String getRoofQuery = "SELECT * FROM "+Schema.TABLE_ROOF+" WHERE "+Schema.ROOF_ID+"="+roofId;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor roofCursor = wdb.rawQuery(getRoofQuery,null);
        Roof roofObj = null;
        if(roofCursor.moveToFirst()){
            int id = roofCursor.getInt(0);
            String roofType = roofCursor.getString(1);
            roofObj=new Roof(id,roofType);
        }
        roofCursor.close();
        wdb.close();
        return roofObj;
    }

    public List<Roof> getAllRoofs(){
        List<Roof> roofs = new ArrayList<>();
        String getRoofQuery = "SELECT * FROM "+Schema.TABLE_ROOF;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor roofCursor = wdb.rawQuery(getRoofQuery,null);
        if(roofCursor.moveToFirst()){
            do{
                int id = roofCursor.getInt(0);
                String roofType = roofCursor.getString(1);
                Roof roofObj=new Roof(id,roofType);
                roofs.add(roofObj);
            }while(roofCursor.moveToNext());
        }
        roofCursor.close();
        wdb.close();
        return roofs;
    }


    /*
        PURPOSE QUERIES
        - inserPurpose(Purpose purpose)
        - getPurposeById(int purposeId)
        - getAllPurposes()
         */
    public void insertPurpose(Purpose purpose){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.PURPOSE, purpose.getPurpose());
        contentValues.put(Schema.PURPOSE_ID, purpose.getId());
        contentValues.put(Schema.P_SECTOR_ID, purpose.getSector().getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_PURPOSE,null,contentValues);
        wdb.close();
    }

    public Purpose getPurposeById(int purposeId){
        String getPurposeQuery = "SELECT * FROM "+Schema.TABLE_PURPOSE+" WHERE "+Schema.PURPOSE_ID+"="+purposeId;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor purposeCursor = wdb.rawQuery(getPurposeQuery,null);
        Purpose purposeObj = null;
        if(purposeCursor.moveToFirst()){
            int id = purposeCursor.getInt(0);
            String purpose = purposeCursor.getString(1);
            int sectorId = purposeCursor.getInt(2);
            Sector sector = this.getSectorById(sectorId);
            purposeObj=new Purpose(id,purpose);
            purposeObj.setSector(sector);
        }
        purposeCursor.close();
        wdb.close();
        return purposeObj;
    }

    public List<Purpose> getAllPurposes(){
        List<Purpose> purposes = new ArrayList<>();
        String getPurposesQ = "SELECT * FROM "+Schema.TABLE_PURPOSE;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor purposeCursor = wdb.rawQuery(getPurposesQ,null);
        if(purposeCursor.moveToFirst()){
            do{
                int id = purposeCursor.getInt(0);
                String purpose = purposeCursor.getString(1);
                int sectorId = purposeCursor.getInt(2);
                Sector sector = this.getSectorById(sectorId);
                Purpose purposeObj=new Purpose(id,purpose);
                purposeObj.setSector(sector);
                purposes.add(purposeObj);
            }while(purposeCursor.moveToNext());
        }
        purposeCursor.close();
        wdb.close();
        return purposes;
    }

    /*
    SECTOR QUERIES
    - insertSector(Sector sector)
    - getSectorById(int sectorId)
    - getAllSectors()
     */
    public void insertSector(Sector sector){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.SECTOR_ID, sector.getId());
        contentValues.put(Schema.SECTOR_NAME, sector.getSectorName());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_SECTOR,null,contentValues);
        wdb.close();
    }

    public Sector getSectorById(int sectorId){
        String getSectorQuery = "SELECT * FROM "+Schema.TABLE_SECTOR+" WHERE "+Schema.SECTOR_ID+"="+sectorId;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor sectorCursor = wdb.rawQuery(getSectorQuery,null);
        Sector sectorObj = null;
        if(sectorCursor.moveToFirst()){
            int id = sectorCursor.getInt(0);
            String sector = sectorCursor.getString(1);
            sectorObj=new Sector(id,sector);
        }
        sectorCursor.close();
        wdb.close();
        return sectorObj;
    }

    public List<Sector> getAllSectors(){
        List<Sector> sectors = new ArrayList<>();
        String getSectorsQuery = "SELECT * FROM "+Schema.TABLE_SECTOR;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor sectorCursor = wdb.rawQuery(getSectorsQuery,null);
        if(sectorCursor.moveToFirst()){
            do{
                int id = sectorCursor.getInt(0);
                String sector = sectorCursor.getString(1);
                Sector sectorObj=new Sector(id,sector);
                sectors.add(sectorObj);
            }while(sectorCursor.moveToNext());
        }
        sectorCursor.close();
        wdb.close();
        return sectors;
    }


    /*
    MATERIAL QUERIES
    - insertMaterial(Material material)
    - getMaterialById(int materialId)
    - getAllMaterials()
     */

    public void insertMaterial(Material material){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.MATERIAL, material.getMaterial());
        contentValues.put(Schema.MATERIAL_ID, material.getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_MATERIAL,null,contentValues);
        wdb.close();
    }

    public Material getMaterialById(int materialId){
        String getMaterialQuery = "SELECT * FROM "+Schema.TABLE_MATERIAL+" WHERE "+Schema.MATERIAL_ID+"="+materialId;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor materialCursor = wdb.rawQuery(getMaterialQuery,null);
        Material materialObj = null;
        if(materialCursor.moveToFirst()){
            int id = materialCursor.getInt(0);
            String material = materialCursor.getString(1);
            materialObj=new Material(id,material);
        }
        materialCursor.close();
        wdb.close();
        return materialObj;
    }

    public List<Material> getAllMaterials(){
        List<Material> materials = new ArrayList<>();
        String getMaterialsQ = "SELECT * FROM "+Schema.TABLE_MATERIAL;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor materialCursor = wdb.rawQuery(getMaterialsQ,null);
        if(materialCursor.moveToFirst()){
            do{
                int id = materialCursor.getInt(0);
                String material = materialCursor.getString(1);
                Material materialObj=new Material(id,material);
                materials.add(materialObj);
            }while(materialCursor.moveToNext());
        }
        materialCursor.close();
        wdb.close();
        return materials;
    }

    /*
    CONSTRUCTION SYSTEM QUERIES
    - insertConstructSys(ConstructionSystem constructionSystem)
    - getConstructionSystemById(int constructionSystemId
    - getAllConstructionSystems()
     */

    public void insertConstructSys(ConstructionSystem constructionSystem){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.CONSTR_SYS, constructionSystem.getConstructionSystem());
        contentValues.put(Schema.CONSTR_SYS_ID, constructionSystem.getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_CONSTUCT_SYS,null,contentValues);
        wdb.close();
    }

    public ConstructionSystem getConstructionSystemById(int constructionSystemId){
        String getConstrSystQuery = "SELECT * FROM "+Schema.TABLE_CONSTUCT_SYS+" WHERE "+Schema.CONSTR_SYS_ID+"="+constructionSystemId;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor constrSysCursor = wdb.rawQuery(getConstrSystQuery,null);
        ConstructionSystem constrSystObj = null;
        if(constrSysCursor.moveToFirst()){
            int id = constrSysCursor.getInt(0);
            String constrSyst = constrSysCursor.getString(1);
            constrSystObj=new ConstructionSystem(id,constrSyst);
        }
        constrSysCursor.close();
        wdb.close();
        return constrSystObj;
    }

    public List<ConstructionSystem> getAllConstructionSystems(){
        List<ConstructionSystem> constructionSystems = new ArrayList<>();
        String getConstrSysQ = "SELECT * FROM "+Schema.TABLE_CONSTUCT_SYS;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor constSysCursor = wdb.rawQuery(getConstrSysQ,null);
        if(constSysCursor.moveToFirst()){
            do{
                int id = constSysCursor.getInt(0);
                String constructionSys = constSysCursor.getString(1);
                ConstructionSystem constSysObj=new ConstructionSystem(id,constructionSys);
                constructionSystems.add(constSysObj);
            }while(constSysCursor.moveToNext());
        }
        constSysCursor.close();
        wdb.close();
        return constructionSystems;
    }

    /*
     CEILING MATERIAL QUERIES
     - insertCeilingMaterial(CeilingMaterial ceilingMaterial)
     - getCeilingMaterialById(int ceilingMaterialId)
     - getAllCeilingMaterials()
     */
    public void insertCeilingMaterial(CeilingMaterial ceilingMaterial){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.CEILING_MATERIAL, ceilingMaterial.getCeilingMaterial());
        contentValues.put(Schema.CEILING_MATERIAL_ID, ceilingMaterial.getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_CEILING_MATERIAL,null,contentValues);
        wdb.close();
    }

    public CeilingMaterial getCeilingMaterialById(int ceilingMaterialId){
        String getCeilingMaterialQuery = "SELECT * FROM "+Schema.TABLE_CEILING_MATERIAL+" WHERE "+Schema.CEILING_MATERIAL_ID+"="+ceilingMaterialId;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor ceilingMaterialCursor = wdb.rawQuery(getCeilingMaterialQuery,null);
        CeilingMaterial ceilingMaterialObj = null;
        if(ceilingMaterialCursor.moveToFirst()){
            int id = ceilingMaterialCursor.getInt(0);
            String ceilingMaterial = ceilingMaterialCursor.getString(1);
            ceilingMaterialObj=new CeilingMaterial(id,ceilingMaterial);
        }
        ceilingMaterialCursor.close();
        wdb.close();
        return ceilingMaterialObj;
    }

    public List<CeilingMaterial> getAllCeilingMaterials(){
        List<CeilingMaterial> ceilingMaterials = new ArrayList<>();
        String getCeilingMatQ = "SELECT * FROM "+Schema.TABLE_CEILING_MATERIAL;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor ceilingMatCursor = wdb.rawQuery(getCeilingMatQ,null);
        if(ceilingMatCursor.moveToFirst()){
            do{
                int id = ceilingMatCursor.getInt(0);
                String ceilingMaterial = ceilingMatCursor.getString(1);
                CeilingMaterial ceilingMatObj=new CeilingMaterial(id,ceilingMaterial);
                ceilingMaterials.add(ceilingMatObj);
            }while(ceilingMatCursor.moveToNext());
        }
        ceilingMatCursor.close();
        wdb.close();
        return ceilingMaterials;
    }

    /*
    STATE QUERIES
    - insertState(State state)
    - getAllStates()
     */
    public void insertState(State state){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.STATE_ID, state.getId());
        contentValues.put(Schema.STATE_NAME, state.getStateName());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_STATES,null,contentValues);
        wdb.close();
    }


    public List<State> getAllStates(){
        List<State> states = new ArrayList<>();
        String getStatesQ = "SELECT * FROM "+Schema.TABLE_STATES;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor stateCursor = wdb.rawQuery(getStatesQ,null);
        if(stateCursor.moveToFirst()){
            do{
                int id = stateCursor.getInt(0);
                String stateName = stateCursor.getString(1);
                State state=new State(id,stateName);
                states.add(state);
            }while(stateCursor.moveToNext());
        }
        stateCursor.close();
        wdb.close();
        return states;
    }

    /*
    CITY QUERIES
   - insertCity(City city)
   - getAllCities()
    */
    public void insertCity(City city){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.CITY_ID, city.getId());
        contentValues.put(Schema.CITY_NAME, city.getCityName());
        contentValues.put(Schema.CITY_STATE_ID, city.getStateId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_CITY,null,contentValues);
        wdb.close();
    }


    public List<City> getAllCities(){
        List<City> cities = new ArrayList<>();
        String getCityQ = "SELECT * FROM "+Schema.TABLE_CITY;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor cityCursor = wdb.rawQuery(getCityQ,null);
        if(cityCursor.moveToFirst()){
            do{
                int id = cityCursor.getInt(0);
                String cityName = cityCursor.getString(1);
                int stateId = cityCursor.getInt(2);
                City city = new City(id,cityName,stateId);
                cities.add(city);
            }while(cityCursor.moveToNext());
        }
        cityCursor.close();
        wdb.close();
        return cities;
    }

    /*
    STREET QUERIES
   - insertStreet(Street street)
   - getAllStreets()
    */
    public void insertStreet(Street street){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.STREET_ID, street.getId());
        contentValues.put(Schema.STREET_NAME, street.getStreetName());
        contentValues.put(Schema.STREET_CITY_ID, street.getCityId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_STREET,null,contentValues);
        wdb.close();
    }


    public List<Street> getAllStreets(){
        List<Street> streets = new ArrayList<>();
        String getStreetsQ = "SELECT * FROM "+Schema.TABLE_STREET;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor streetCursor = wdb.rawQuery(getStreetsQ,null);
        if(streetCursor.moveToFirst()){
            do{
                int id = streetCursor.getInt(0);
                String streetName = streetCursor.getString(1);
                int cityId = streetCursor.getInt(2);
                Street street= new Street(id,streetName,cityId);
                streets.add(street);
            }while(streetCursor.moveToNext());
        }
        streetCursor.close();
        wdb.close();
        return streets;
    }

    /*
    BUILDING QUERIES
    - insertBuilding(Building building)
    - getAllBuildings()
    - getMaxId()
     */

    public void insertBuilding(Building building){
        Log.d("DB_INSERT_BUILD",building.toString());
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.BUILDING_ID, building.getId());
        contentValues.put(Schema.BUILDING_UID, building.getuId());
        contentValues.put(Schema.BRUTO_AREA, building.getBrutoArea());
        contentValues.put(Schema.DATE, String.valueOf(building.getDate()));
        contentValues.put(Schema.FLOOR_HEIGHT, building.getFloorHeight());
        contentValues.put(Schema.FULL_HEIGHT, building.getFullHeight());
        contentValues.put(Schema.LENGTH, building.getLength());
        contentValues.put(Schema.NUMBER_OF_FLOORS, building.getNumberOfFloors());
        contentValues.put(Schema.PROPER_GROUND_PLAN, building.isProperGroundPlan());
        contentValues.put(Schema.SYNCHRONIZED, building.isSynchronizedWithDatabase());
        contentValues.put(Schema.WIDTH, building.getWidth());
        contentValues.put(Schema.BASEMENT_BRUTO_AREA, building.getBasementBrutoArea());
        contentValues.put(Schema.BUSINESS_BRUTO_AREA, building.getBusinessBrutoArea());
        contentValues.put(Schema.RESIDENTIAL_BRUTO_AREA, building.getResidentialBrutoArea());
        contentValues.put(Schema.YEAR_OF_BUILD, building.getYearOfBuild());
        contentValues.put(Schema.B_CEILING_MATERIAL_ID, building.getCeilingMaterial().getId());
        contentValues.put(Schema.B_CONSTRUCT_SYS_ID, building.getConstructionSystem().getId());
        contentValues.put(Schema.B_USER_ID, building.getUser().getId());
        contentValues.put(Schema.B_MATERIAL_ID, building.getMaterial().getId());
        contentValues.put(Schema.B_PURPOSE_ID, building.getPurpose().getId());
        contentValues.put(Schema.B_POSITION_ID, building.getPosition().getId());
        contentValues.put(Schema.B_ROOF_ID,building.getRoof().getId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_BUILDING,null,contentValues);
        wdb.close();

        insertBuildingLocations(building.getLocations(), building.getId());
    }

    public List<Building> getAllBuildings(){
        List<Building> buildings = new ArrayList<>();
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor buildingCursor = wdb.rawQuery(SELECT_BUILDINGS,null);
        if(buildingCursor.moveToFirst()){
            do{
                Long id = buildingCursor.getLong(0);
                String uId = buildingCursor.getString(1);
                double brutoArea= buildingCursor.getDouble(2);
                double basementBrutoArea= buildingCursor.getDouble(3);
                double residentialBrutoArea= buildingCursor.getDouble(4);
                double businessBrutoArea= buildingCursor.getDouble(5);
                Timestamp date = Timestamp.valueOf(buildingCursor.getString(6));
                double floorHeight = buildingCursor.getDouble(7);
                double fullHeight= buildingCursor.getDouble(8);
                double length= buildingCursor.getDouble(9);
                int numberOfFloors= buildingCursor.getInt(10);
                boolean properGroundPlan = buildingCursor.getInt(11) > 0;
                boolean isSynchronized=buildingCursor.getInt(12) > 0;
                double width= buildingCursor.getDouble(13);
                String yearOfBuild = buildingCursor.getString(14);
                int ceilingMatId= buildingCursor.getInt(15);
                int constrSysId= buildingCursor.getInt(16);
                int materialId= buildingCursor.getInt(17);
                int positionId= buildingCursor.getInt(18);
                int purposeId= buildingCursor.getInt(19);
                int roofId= buildingCursor.getInt(20);
                long userId=buildingCursor.getLong(21);
                Building building = new Building(uId,date,yearOfBuild,properGroundPlan);
                building.setCeilingMaterial(this.getCeilingMaterialById(ceilingMatId));
                building.setDimensions(width,length,brutoArea,floorHeight,fullHeight,numberOfFloors,residentialBrutoArea,basementBrutoArea,businessBrutoArea);
                building.setPurpose(this.getPurposeById(purposeId));
                building.setUser(this.getUser());
                building.setRoof(this.getRoofById(roofId));
                building.setPosition(this.getPositionById(positionId));
                building.setMaterial(this.getMaterialById(materialId));
                building.setConstructionSystem(this.getConstructionSystemById(constrSysId));
                building.setSynchronizedWithDatabase(isSynchronized);
                building.setId(id);
                Log.d("GET ALL BUILD DB",building.toString());
                buildings.add(building);
            }while(buildingCursor.moveToNext());
        }
        buildingCursor.close();
        wdb.close();
        return buildings;
    }

    public long getMaxId(){
        String query="SELECT "+ Schema.BUILDING_ID+ " FROM "+ Schema.TABLE_BUILDING+
                " ORDER BY " + Schema.BUILDING_ID+ " DESC LIMIT 1";
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor idCursor = wdb.rawQuery(query,null);
        long id=0;
        if(idCursor.moveToFirst()){
           id = idCursor.getLong(0);
        }
        idCursor.close();
        wdb.close();
        return id;
    }

    /*
    BUILDING LOCATION QUERIES
    - insertBuildingLocation(BuildingLocation buildingLocation)
    - insertBuildingLocations(List<BuildingLocation> locations, long buildingId)
    - getBuildingLocationByBuildingId(long buildingID)
     */

    public void insertBuildingLocation(BuildingLocation buildingLocation){
        ContentValues contentValues = new ContentValues();
        Random random = new Random();
        contentValues.put(Schema.LOCATION_ID,random.nextInt());
        contentValues.put(Schema.STREET,buildingLocation.getStreet());
        contentValues.put(Schema.STREET_NUM,buildingLocation.getStreetNumber());
        contentValues.put(Schema.STREET_CHAR, String.valueOf(buildingLocation.getStreetNumberChar()));
        contentValues.put(Schema.CADASTRAL_PARTICLE,buildingLocation.getCadastralParticle());
        contentValues.put(Schema.CITY, buildingLocation.getCity());
        contentValues.put(Schema.STATE,buildingLocation.getState());
        contentValues.put(Schema.IP_BUILDING_ID,buildingLocation.getBuildingId());
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_BUILDING_LOCATION,null, contentValues);
        wdb.close();
    }

    public void insertBuildingLocations(List<BuildingLocation> buildingLocations, long buildingId){
        SQLiteDatabase wdb = this.getWritableDatabase();

        for(BuildingLocation buildingLocation : buildingLocations){
            ContentValues contentValues = new ContentValues();
            Random random = new Random();
            contentValues.put(Schema.LOCATION_ID,random.nextInt());
            contentValues.put(Schema.STREET,buildingLocation.getStreet());
            contentValues.put(Schema.STREET_NUM,buildingLocation.getStreetNumber());
            contentValues.put(Schema.STREET_CHAR, String.valueOf(buildingLocation.getStreetNumberChar()));
            contentValues.put(Schema.CADASTRAL_PARTICLE,buildingLocation.getCadastralParticle());
            contentValues.put(Schema.CITY, buildingLocation.getCity());
            contentValues.put(Schema.STATE,buildingLocation.getState());
            contentValues.put(Schema.IP_BUILDING_ID,buildingId);
            wdb.insert(Schema.TABLE_BUILDING_LOCATION,null, contentValues);

        }
        wdb.close();
    }

    public List<BuildingLocation> getBuildingLocationsByBuildingId(long buildingId){

        String locationQ = "SELECT * FROM "+Schema.TABLE_BUILDING_LOCATION+ " WHERE "+Schema.LOC_BUILDING_ID+"="+buildingId;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor locationCursor = wdb.rawQuery(locationQ,null);
        List<BuildingLocation> locations= new ArrayList<>();
        if(locationCursor.moveToFirst()){
            do{
                long id = locationCursor.getLong(0);
                String street= locationCursor.getString(1);
                int streetNum = locationCursor.getInt(2);
                char streetChar = locationCursor.getString(3).charAt(0);
                String city = locationCursor.getString(4);
                String state = locationCursor.getString(5);
                String cadastralParticle = locationCursor.getString(6);
                long buildingIdDB = locationCursor.getLong(7);
                BuildingLocation buildingLocation = new BuildingLocation(street,streetNum,streetChar,city,state,cadastralParticle);
                buildingLocation.setId(id);
                Log.d("BUILDING LOC DB",buildingLocation.toString());
                locations.add(buildingLocation);
            }while (locationCursor.moveToNext());
        }
        locationCursor.close();
        wdb.close();
        return locations;
    }

    /*
    IMAGE PATH QUERIES
    - insertImage()
    - getImagesByBuildingId(long buildingId)
     */

    public void insertImage(/*String imagePath, */byte[] imageBytes, long buildingId){
        ContentValues contentValues = new ContentValues();
        Random random = new Random();
        contentValues.put(Schema.IMAGE_ID,random.nextInt());
        contentValues.put(Schema.IMAGE_PATH,"jkkw");
        contentValues.put(Schema.IMAGE,imageBytes);
        contentValues.put(Schema.IP_BUILDING_ID,buildingId);
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(Schema.TABLE_IMAGES,null, contentValues);
        wdb.close();
    }

    public List<LocalImage> getImagesByBuildingId(long buildingId){

        String imageQuery = "SELECT * FROM "+Schema.TABLE_IMAGES+ " WHERE "+Schema.IP_BUILDING_ID+"="+buildingId;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor imageCursor = wdb.rawQuery(imageQuery,null);
        List<LocalImage> images= new ArrayList<>();
        if(imageCursor.moveToFirst()){
            do{
                int id = imageCursor.getInt(0);
                String imagePath= imageCursor.getString(1);
                byte[] imgBytes = imageCursor.getBlob(2);
                long buildingIdDB = imageCursor.getLong(3);
                Bitmap imgBitmap = BitmapFactory.decodeByteArray(imgBytes,0,imgBytes.length);
                LocalImage localImage = new LocalImage(id,imgBitmap,buildingIdDB);
                Log.d("LOCAL IMAGE DB",localImage.toString());
                images.add(localImage);
            }while (imageCursor.moveToNext());
        }
        imageCursor.close();
        wdb.close();
        return images;
    }

    public void deleteBuildings() {

        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.execSQL(DELETE_TABLE_BUILDING);
        wdb.execSQL(DELETE_TABLE_LOCATIONS);
        wdb.execSQL(DELETE_TABLE_IMAGES);
        wdb.close();

    }


    public static class Schema{

        private static final int SCHEMA_VERSION = 1;
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
        static final String P_SECTOR_ID = "sector_id";

        //SECTOR table
        static  final String TABLE_SECTOR = "sectors";
        static final String SECTOR_ID = "id";
        static final String SECTOR_NAME = "sector_name";

        //MATERIAL TABLE
        static final String TABLE_MATERIAL = "materials";
        static final String MATERIAL_ID = "material_id";
        static final String MATERIAL = "material";

        //ROOF TABLE
        static final String TABLE_ROOF = "roofs";
        static final String ROOF_ID = "id";
        static final String ROOF_TYPE = "roof_type";


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
        static final String BUILDING_UID = "unique_id";
        static final String DATE = "date";
        static final String YEAR_OF_BUILD = "year_of_build";
        static final String PROPER_GROUND_PLAN="proper_ground_plan";
        static final String B_USER_ID="user_id";
        static final String B_PURPOSE_ID ="purpose_id";
        static final String B_POSITION_ID = "position_id";
        static final String B_MATERIAL_ID = "material_id";
        static final String B_CEILING_MATERIAL_ID = "ceiling_material_id";
        static final String B_CONSTRUCT_SYS_ID = "construction_sys_id";
        static final String B_ROOF_ID = "roof_id";
        static final String SYNCHRONIZED = "synchronized";
        static final String WIDTH = "width";
        static final String LENGTH = "length";
        static final String BRUTO_AREA = "bruto_area";
        static final String RESIDENTIAL_BRUTO_AREA = "residential_bruto_area";
        static final String BASEMENT_BRUTO_AREA = "basement_bruto_area";
        static final String BUSINESS_BRUTO_AREA = "business_bruto_area";
        static final String FLOOR_HEIGHT = "floor_height";
        static final String FULL_HEIGHT = "full_height";
        static final String NUMBER_OF_FLOORS = "num_of_floors";

        //BUILDING LOCATION table
        static final String TABLE_BUILDING_LOCATION = "building_location";
        static final String LOCATION_ID = "id";
        static final String CADASTRAL_PARTICLE = "cadastral_particle";
        static final String STREET = "street";
        static final String STREET_NUM = "street_num";
        static final String STREET_CHAR = "street_char";
        static final String CITY = "city";
        static final String STATE ="state";
        static final String LOC_BUILDING_ID = "building_id";

        //IMAGE PATH table
        static final String TABLE_IMAGES = "images";
        static final String IMAGE_ID = "image_path_id";
        static final String IMAGE_PATH = "image_path";
        static final String IMAGE = "image";
        static final String IP_BUILDING_ID ="building_id";

        //STATE table
        static final String TABLE_STATES = "states";
        static final String STATE_ID= "state_id";
        static final String STATE_NAME = "state_name";

        //CITY table
        static final String TABLE_CITY = "cities";
        static final String CITY_ID= "city_id";
        static final String CITY_NAME = "city_name";
        static final String CITY_STATE_ID = "state_id";

        //STREET table
        static final String TABLE_STREET = "streets";
        static final String STREET_ID= "id";
        static final String STREET_NAME = "street_name";
        static final String STREET_CITY_ID = "city_id";


    }
}

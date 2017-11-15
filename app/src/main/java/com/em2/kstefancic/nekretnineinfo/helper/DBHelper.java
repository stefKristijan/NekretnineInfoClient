package com.em2.kstefancic.nekretnineinfo.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.em2.kstefancic.nekretnineinfo.api.model.Building;
import com.em2.kstefancic.nekretnineinfo.api.model.ImagePath;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.CeilingMaterial;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.ConstructionSystem;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Material;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Position;
import com.em2.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.Purpose;
import com.em2.kstefancic.nekretnineinfo.api.model.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

/**
 * Created by user on 3.11.2017..
 */

public class DBHelper extends SQLiteOpenHelper {

    //CREATING TABLES
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + Schema.TABLE_USER + " (" +
                                            Schema.USER_ID+" INTEGER PRIMARY KEY," +
                                            Schema.USER_UNIQID+" VARCHAR(255),"+
                                            Schema.USERNAME+" VARCHAR(50),"+
                                            Schema.PASSWORD+" VARCHAR(255),"+
                                            Schema.FIRST_NAME+" VARCHAR(50),"+
                                            Schema.LAST_NAME+" VARCHAR(50),"+
                                            Schema.EMAIL+" VARCHAR(100),"+
                                            Schema.ENABLED+" BOOLEAN);";

    private static final String CREATE_TABLE_POSITION =
            "CREATE TABLE " + Schema.TABLE_POSITION + " (" +
                                            Schema.POSITION_ID+" INTEGER PRIMARY KEY," +
                                            Schema.POSITION+" VARCHAR(50));";

    private static final String CREATE_TABLE_MATERIAL =
            "CREATE TABLE " + Schema.TABLE_MATERIAL + " (" +
                                            Schema.MATERIAL_ID+" INTEGER PRIMARY KEY," +
                                            Schema.MATERIAL+" VARCHAR(50));";

    private static final String CREATE_TABLE_PURPOSE =
            "CREATE TABLE " + Schema.TABLE_PURPOSE + " (" +
                                            Schema.PURPOSE_ID+" INTEGER PRIMARY KEY," +
                                            Schema.PURPOSE+" VARCHAR(50));";

    private static final String CREATE_TABLE_CONSTRUCT_SYS =
            "CREATE TABLE " + Schema.TABLE_CONSTUCT_SYS + " (" +
                                            Schema.CONSTR_SYS_ID+" INTEGER PRIMARY KEY," +
                                            Schema.CONSTR_SYS+" VARCHAR(50));";

    private static final String CREATE_TABLE_CEILING_MATERIAL=
            "CREATE TABLE " + Schema.TABLE_CEILING_MATERIAL + " (" +
                                            Schema.CEILING_MATERIAL_ID+" INTEGER PRIMARY KEY," +
                                            Schema.CEILING_MATERIAL+" VARCHAR(50));";

    private static final String CREATE_TABLE_BUILDING=
            "CREATE TABLE "+ Schema.TABLE_BUILDING + " ("+
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
                                            Schema.ORIENTATION +" VARCHAR(10),"+
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
                                            Schema.B_POSITION_ID +" INTEGER,"+
                                            Schema.B_PURPOSE_ID +" INTEGER,"+
                                            Schema.B_USER_ID+" BIGINT," +
                                            "CONSTRAINT building_user_fk FOREIGN KEY("+Schema.B_USER_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.USER_ID+"),"+
                                            "CONSTRAINT building_material_fk FOREIGN KEY("+Schema.B_MATERIAL_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.MATERIAL_ID+"),"+
                                            "CONSTRAINT building_c_material_fk FOREIGN KEY("+Schema.B_CEILING_MATERIAL_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.CEILING_MATERIAL_ID+"),"+
                                            "CONSTRAINT building_constr_sys_fk FOREIGN KEY("+Schema.B_CONSTRUCT_SYS_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.CONSTR_SYS_ID+"),"+
                                            "CONSTRAINT building_position_fk FOREIGN KEY("+Schema.B_POSITION_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.POSITION_ID+"),"+
                                            "CONSTRAINT building_purpose_fk FOREIGN KEY("+Schema.B_PURPOSE_ID+") REFERENCES "+Schema.TABLE_USER+"("+Schema.PURPOSE_ID+"),"+
                                            "CONSTRAINT unique_building UNIQUE ("+Schema.STREET+", "+Schema.STREET_NUM+", "+Schema.STREET_CHAR+", "+Schema.CADASTRAL_PARTICLE+"));";

    private static final String CREATE_TABLE_IMAGES =
            "CREATE TABLE " + Schema.TABLE_IMAGES + " (" +
                    Schema.IMAGE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
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
    private static final String DROP_TABLE_IMAGES = "DROP TABLE IF EXISTS "+Schema.TABLE_IMAGES;

    //DELETING DATA
    private static final String DELETE_TABLE_USER = "DELETE FROM "+ Schema.TABLE_USER;
    private static final String DELETE_TABLE_PURPOSE = "DELETE FROM "+ Schema.TABLE_PURPOSE;
    private static final String DELETE_TABLE_POSITION = "DELETE FROM "+ Schema.TABLE_POSITION;
    private static final String DELETE_TABLE_MATERIAL ="DELETE FROM "+ Schema.TABLE_MATERIAL;
    private static final String DELETE_TABLE_CONSTRUCT_SYS = "DELETE FROM "+ Schema.TABLE_CONSTUCT_SYS;
    private static final String DELETE_TABLE_CEILING_MATERIAL = "DELETE FROM "+ Schema.TABLE_CEILING_MATERIAL;
    private static final String DELETE_TABLE_BUILDING = "DELETE FROM " + Schema.TABLE_BUILDING;
    private static final String DELETE_TABLE_IMAGES = "DELETE FROM "+Schema.TABLE_IMAGES;

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
        PURPOSE QUERIES
        - inserPurpose(Purpose purpose)
        - getPurposeById(int purposeId)
        - getAllPurposes()
         */
    public void insertPurpose(Purpose purpose){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.PURPOSE, purpose.getPurpose());
        contentValues.put(Schema.PURPOSE_ID, purpose.getId());
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
            purposeObj=new Purpose(id,purpose);
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
                Purpose purposeObj =new Purpose(id,purpose);
                purposes.add(purposeObj);
            }while(purposeCursor.moveToNext());
        }
        purposeCursor.close();
        wdb.close();
        return purposes;
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
        String getCeilingMatQ = "SELECT * FROM "+Schema.TABLE_POSITION;
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
    BUILDING QUERIES
    - insertBuilding(Building building)
    - getAllBuildings()
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

    public List<Building> getAllBuildings(){
        List<Building> buildings = new ArrayList<>();
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor buildingCursor = wdb.rawQuery(SELECT_BUILDINGS,null);
        if(buildingCursor.moveToFirst()){
            do{
                Long id = buildingCursor.getLong(0);
                double brutoArea= buildingCursor.getDouble(1);
                String cadastralParticle = buildingCursor.getString(2);
                String city = buildingCursor.getString(3);
                Timestamp date = Timestamp.valueOf(buildingCursor.getString(4));
                double floorArea = buildingCursor.getDouble(5);
                double floorHeight = buildingCursor.getDouble(6);
                double fullHeight= buildingCursor.getDouble(7);
                double length= buildingCursor.getDouble(8);
                int numberOfFloors= buildingCursor.getInt(9);
                Building.Orientation orientation= Building.Orientation.valueOf(buildingCursor.getString(10));
                boolean properGroundPlan = buildingCursor.getInt(11) > 0;
                String state = buildingCursor.getString(12);
                String street = buildingCursor.getString(13);
                int streetNum= buildingCursor.getInt(14);
                char streetChar =buildingCursor.getString(15).charAt(0);
                boolean isSynchronized=buildingCursor.getInt(16) > 0;
                double width= buildingCursor.getDouble(17);
                String yearOfBuild = buildingCursor.getString(18);
                int ceilingMatId= buildingCursor.getInt(19);
                int constrSysId= buildingCursor.getInt(20);
                int materialId= buildingCursor.getInt(21);
                int positionId= buildingCursor.getInt(22);
                int purposeId= buildingCursor.getInt(23);
                long userId=buildingCursor.getLong(24);
                Building building = new Building(date,yearOfBuild,properGroundPlan);
                building.setDimensions(width,length,floorArea,brutoArea,floorHeight,fullHeight,numberOfFloors);
                building.setLocation(cadastralParticle,street,streetNum,streetChar,city,state,orientation,this.getPositionById(positionId));
                building.setCeilingMaterial(this.getCeilingMaterialById(ceilingMatId));
                building.setPurpose(this.getPurposeById(purposeId));
                building.setUser(this.getUser());
                building.setMaterial(this.getMaterialById(materialId));
                building.setConstructionSystem(this.getConstructionSystemById(constrSysId));
                building.setSynchronizedWithDatabase(isSynchronized);
                building.setId(id);
                buildings.add(building);
            }while(buildingCursor.moveToNext());
        }
        buildingCursor.close();
        wdb.close();
        return buildings;
    }

    /*
    IMAGE PATH QUERIES
    - insertImage()
    - getImagesByBuildingId(long buildingId)
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

    public List<Bitmap> getImagesByBuildingId(long buildingId){

        String imageQuery = "SELECT "+Schema.IMAGE+" FROM "+Schema.TABLE_IMAGES+ " WHERE "+Schema.IP_BUILDING_ID+"="+buildingId;
        SQLiteDatabase wdb = this.getWritableDatabase();
        Cursor imageCursor = wdb.rawQuery(imageQuery,null);
        List<Bitmap> images= new ArrayList<>();
        if(imageCursor.moveToFirst()){
            do{
                byte[] imgBytes = imageCursor.getBlob(0);
                Bitmap imgBitmap = BitmapFactory.decodeByteArray(imgBytes,0,imgBytes.length);
                images.add(imgBitmap);
            }while (imageCursor.moveToNext());
        }
        imageCursor.close();
        wdb.close();
        return images;
    }



    public static class Schema{

        private static final int SCHEMA_VERSION = 3;
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

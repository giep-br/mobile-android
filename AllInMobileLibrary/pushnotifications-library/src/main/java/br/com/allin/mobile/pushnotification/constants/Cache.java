package br.com.allin.mobile.pushnotification.constants;

public class Cache {
    public static String TABLE_NAME = "cache";
    public static String DB_FIELD_ID = "id";
    public static String DB_FIELD_URL = "url";
    public static String DB_FIELD_JSON = "json";
    public static String DB_NAME = "allin.db";

    public static String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s " +
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s VARCHAR, %s VARCHAR);",
                    Cache.TABLE_NAME, Cache.DB_FIELD_ID,
                    Cache.DB_FIELD_URL, Cache.DB_FIELD_JSON);

    public static String SELECT = String.format("SELECT %s, %s, %s " +
                    "FROM %s", Cache.DB_FIELD_ID, Cache.DB_FIELD_URL,
                    Cache.DB_FIELD_JSON, Cache.TABLE_NAME);
}

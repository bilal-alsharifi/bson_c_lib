package com.livio.BSON;

import java.util.HashMap;
import java.util.List;
import cz.adamh.utils.NativeUtils;

public class BsonEncoder {
    private static final String libName = "javabson";

    static {
        try {
          System.loadLibrary(libName);
        }
        catch (UnsatisfiedLinkError err) {
          try {
            NativeUtils.loadLibraryFromJar("/natives/" + System.mapLibraryName(libName));
          }
          catch (Exception ex) {
            System.out.println("Error loading native library");
            System.exit(1);
          }
        }
    }
    
    @SuppressWarnings("unchecked")
    public static byte[] encodeToBytes(HashMap<String, Object> map) throws ClassCastException {
        long bsonRef = initializeBsonObject();
        long arrayRef = -1;

        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value instanceof List){
                arrayRef = buildBsonArray((List<Object>) value);
                bson_object_put_array(bsonRef, key, arrayRef);
            } else if (value instanceof Integer) {
                bson_object_put_int32(bsonRef, key, (Integer) value);
            } else if (value instanceof Long) {
                bson_object_put_int64(bsonRef, key, (Long) value);
            } else if (value instanceof String) {
                bson_object_put_string(bsonRef, key, (String) value);
            } else if (value instanceof Boolean) {
                bson_object_put_bool(bsonRef, key, (Boolean) value);
            } else if (value instanceof Double) {
                bson_object_put_double(bsonRef, key, (Double) value);
            }
        }

        byte[] bytes = bson_object_to_bytes(bsonRef);

        deinitializeBsonObject(bsonRef);

        return bytes;
    }

    public static HashMap<String, Object> decodeFromBytes(byte[] bytes){

        HashMap<String, Object> map = new HashMap<String, Object>();

        long bsonRef = bson_object_from_bytes(bytes);
        map = bson_object_get_hashmap(bsonRef);
        return map;
    }

    private static long buildBsonArray(List<Object> elements) {

        long bsonRef = initializeBsonArray();

        for(Object e : elements){
            if (e instanceof Integer) {
                bson_array_add_int32(bsonRef, (Integer) e);
            } else if (e instanceof Long) {
                bson_array_add_int64(bsonRef, (Long) e);
            } else if (e instanceof String) {
                bson_array_add_string(bsonRef, (String) e);
            } else if (e instanceof Boolean) {
                bson_array_add_bool(bsonRef, (Boolean) e);
            } else if (e instanceof Double) {
                bson_array_add_double(bsonRef, (Double) e);
            }
        }

        return bsonRef;
    }

    // BSON Object Methods

    private static native long initializeBsonObject();

    private static native void deinitializeBsonObject(long bsonRef);

    private static native boolean bson_object_put_array(long bsonRef, String key, long arrayRef);

    private static native boolean bson_object_put_int32(long bsonRef, String key, int value);

    private static native boolean bson_object_put_int64(long bsonRef, String key, long value);

    private static native boolean bson_object_put_string(long bsonRef, String key, String value);

    private static native boolean bson_object_put_bool(long bsonRef, String key, boolean value);

    private static native boolean bson_object_put_double(long bsonRef, String key, double value);

    private static native byte[] bson_object_to_bytes(long bsonRef);

    private static native long bson_object_from_bytes(byte[] data);

    private static native HashMap<String, Object> bson_object_get_hashmap(long bsonRef);

    // BSON Array methods

    private static native long initializeBsonArray();

    private static native void deinitializeBsonArray(long bsonRef);

    private static native boolean bson_array_add_int32(long bsonRef, int value);

    private static native boolean bson_array_add_int64(long bsonRef, long value);

    private static native boolean bson_array_add_string(long bsonRef, String value);

    private static native boolean bson_array_add_bool(long bsonRef, boolean value);

    private static native boolean bson_array_add_double(long bsonRef, double value);
}



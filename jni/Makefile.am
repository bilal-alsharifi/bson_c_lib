AM_CFLAGS = -I${JNI_INCLUDE_DIR} -Wall

include_HEADERS = ../src/bson_object.h ../src/bson_array.h ../src/bson_util.h ../src/emhashmap/emhashmap.h

lib_LTLIBRARIES = libjavabson.la
libjavabson_la_SOURCES = bson_jni.c ../src/bson_object.c ../src/bson_array.c ../src/bson_util.c ../src/emhashmap/emhashmap.c

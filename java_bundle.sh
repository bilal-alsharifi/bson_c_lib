./configure --with-java-wrapper=yes
make
sudo make install
cd java
javac com/livio/BSON/BsonEncoder.java cz/adamh/utils/NativeUtils.java
mkdir natives
cp ../jni/.libs/libjavabson.* ./natives/
jar cf bson_java_lib.jar com/livio/BSON/BsonEncoder.class cz/adamh/utils/NativeUtils.class natives/*
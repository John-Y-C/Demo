//
// Created by Administrator on 2016/8/19/019.
//


#include<jni.h>
//jobject传的是调用该方法的类
//增加jni规定的native interface必须要加入的两个参数
//增加JNI规定的函数名，java语言里映射到C语言里的方法名字要做修改，避免重名
jint Java_com_example_jnidemo_MyNativeUtils_caculate(JNIEnv* evn,jobject jo,jint x,jint y)
{
    jint z=x+y;
    return z;
}
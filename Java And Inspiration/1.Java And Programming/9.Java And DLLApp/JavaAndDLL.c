#include "windows.h"
#include "JavaAndDLL.h"
int i=0;
JNIEXPORT jint JNICALL Java_JavaAndDLL_get(JNIEnv *e, jclass c)
{
	return i;
}
JNIEXPORT void JNICALL Java_JavaAndDLL_set(JNIEnv *e, jclass c, jint j)
{
	i=j;
}

package com.hackx.unclassified

import java.io.FileOutputStream

object ClassManipulator {
    def saveClassFile(obj: AnyRef): Unit = {
        val classLoader = obj.getClass.getClassLoader
        val className = obj.getClass.getName
        val classFile = className.replace('.', '/') + ".class"
        val stream = classLoader.getResourceAsStream(classFile)

        // just use the class simple name as the file name
        val outputFile = className.split('.').last + ".class"
        val fileStream = new FileOutputStream(outputFile)
        var data = stream.read()
        while (data != -1) {
            fileStream.write(data)
            data = stream.read()
        }
        fileStream.flush()
        fileStream.close()
    }
}

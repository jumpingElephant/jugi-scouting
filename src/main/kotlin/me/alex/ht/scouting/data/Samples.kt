package me.alex.ht.scouting.data

import java.io.File


fun getSamplesFile(): File {
    val fileLocation = getLocalDiretory()
    val file = File(fileLocation, "samples.dat")
    if (!fileLocation.exists()) {
        fileLocation.mkdirs()
    }
    return file
}

object ScoutCommentsSample {
    fun getSample(): String {
        val samplesFile = getSamplesFile()
        return if (samplesFile.exists()) {
            samplesFile.readText()
        } else {
            ""
        }
    }
}
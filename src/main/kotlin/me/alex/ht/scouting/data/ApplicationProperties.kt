package me.alex.ht.scouting.data

import java.io.File
import java.io.FileInputStream
import java.util.*

fun getUserHome(): String = System.getProperty("user.home")
fun getLocalDiretory(): File {
    val userHome = getUserHome()
    return File("$userHome/.jugi-scouting/")
}

fun getPropertyFile(): File {
    val file = File(getLocalDiretory(), "app.properties")
    if (!getLocalDiretory().exists()) {
        getLocalDiretory().mkdirs()
    }
    return file
}

class ApplicationProperties {
    private val properties = Properties()

    private val developmentModeKey = "develop"

    init {
        val propertyFile = getPropertyFile()
        if (propertyFile.exists()) {
            properties.load(FileInputStream(propertyFile))
        } else {
            propertyFile.createNewFile()
        }
    }

    fun isDevMode(): Boolean {
        val propertyValue = properties.getProperty(developmentModeKey)
        return propertyValue == "true"
    }
}
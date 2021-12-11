package me.alex.ht.scouting.data

import me.alex.ht.scouting.parser.ScoutComment
import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter

object Exporter {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    fun exportDatesAndCommentsNewLineSeparated(scoutComments: List<ScoutComment>) {
        val fileLocation = File(getLocalDiretory(), "exports")
        val commentsFile = File(fileLocation, "comments-export_${Instant.now().toEpochMilli()}.dat")
        val datesFile = File(fileLocation, "dates-export_${Instant.now().toEpochMilli()}.dat")
        val dateAcceptedCommentsFile = File(fileLocation, "full-export_${Instant.now().toEpochMilli()}.csv")
        if (fileLocation.exists().not()) {
            commentsFile.parentFile.mkdirs()
        }
        commentsFile.writeText(scoutComments.joinToString(separator = "\n") {
            it.comment.replace("""\n""".toRegex(), "")
        })
        datesFile.writeText(scoutComments.joinToString(separator = "\n") {
            (it.postedAt?.format(dateTimeFormatter) ?: "")
        })
        dateAcceptedCommentsFile.writeText(scoutComments.joinToString(separator = "\n") {
            (it.postedAt?.format(dateTimeFormatter) ?: "") + "," +
                    (it.accepted?.let { accepted -> if (accepted) "Ja" else "Nein" } ?: "") + "," +
                    "\"" + it.comment.replace("""\n""".toRegex(), "") + "\""
        })
    }
}
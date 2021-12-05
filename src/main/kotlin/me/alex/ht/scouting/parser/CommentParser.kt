package me.alex.ht.scouting.parser

object CommentParser {

    fun parse(comments: String): List<ScoutComment> {

        val introRegexList = listOf(
            """Er ist (?<age>\d+) Jahre alt und hört auf den Namen (?<name>.*)\.""".toRegex(),
            """Okay, der Kandidat dieser Woche ist (?<age>\d+) Jahre alt und heißt (?<name>.*)\.""".toRegex()
        )

        val introMatchResults = introRegexList
            .flatMap { it.findAll(comments) }
            .sortedBy { matchResult -> matchResult.range.first }
            .toList()

        return introMatchResults
            .map { introMatchResult ->
                val ageMatchGroup = introMatchResult.groups["age"]
                val nameMatchGroup = introMatchResult.groups["name"]
                ScoutComment(nameMatchGroup!!.value, Age(ageMatchGroup!!.value.toInt(), null))
            }
            .toList()
    }
}

data class ScoutComment(val name: String, val age: Age)

data class Age(val years: Int, val days: Int?)
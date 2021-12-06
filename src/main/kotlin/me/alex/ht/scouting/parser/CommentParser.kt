package me.alex.ht.scouting.parser

object CommentParser {

    val introRegexList = listOf(
        """Er ist (?<age>\d+) Jahre alt und hört auf den Namen (?<name>.*)\.""".toRegex(),
        """der Kandidat dieser Woche ist (?<age>\d+) Jahre alt und heißt (?<name>.*)\.""".toRegex(),
        """zur Sache: (?<name>.*) ist (?<age>\d+) Jahre alt""".toRegex(),
        """\b(?<name>.*) könnte ein interessanter Spieler sein. Er ist (?<age>\d+) Jahre alt""".toRegex(),
        """Er sagte mir, er heiße (?<name>.*) und sei (?<age>\d+) Jahre alt""".toRegex(),
        """Sein Name ist (?<name>.*), und er ist (?<age>\d+) Jahre alt""".toRegex(),
        """Er trägt den Namen (?<name>.*) und ist (?<age>\d+) Jahre alt""".toRegex(),
        """His name is (?<name>.*) and he is (?<age>\d+) years old""".toRegex(),
        """\b(?<name>.*) looks promising. He is (?<age>\d+) years old""".toRegex(),
    )
    val skillRegexList = listOf(
        """Dieser Spieler hat es bis jetzt in Sachen (?<skill>.*) auf (?<value>.*) gebracht""".toRegex(),
        """Ohne weiteres Training wird dieser Spieler in Sachen (?<skill>.*) nicht über (?<value>.*) hinauskommen""".toRegex(),
        """Ich würde seine Fähigkeiten in (?<skill>.*) zurzeit ungefähr auf (?<value>.*) schätzen""".toRegex(),
        """This player has Skill (?<value>.*) (?<skill>.*) at this point in his career""".toRegex(),
        """Without any further training, this player will remain with (?<value>.*) (?<skill>.*)""".toRegex(),
        """Right now I would say his (?<skill>.*) capabilities are around the (?<value>.*) level""".toRegex(),
    )
    val potentialRegexList = listOf(
        """Ehe du dich versiehst, könnte dieser Spieler in (?<skill>.*) (?<value>.*) werden, falls du ihn richtig trainierst""".toRegex(),
        """Wenn sich der Kerl richtig entwickelt, könnte er in meinen Augen in (?<skill>.*) (?<value>.*) werden, bis zu seiner Aufnahme in die erste Mannschaft""".toRegex(),
        """Wenn er die Chance erhält, seine Fähigkeiten in (?<skill>.*) zu verbessern, könnte dieser Spieler darin (?<value>.*) werden""".toRegex(),
        """Coach this player right and he will reach his potential of (?<value>.*) (?<skill>.*) before you know it""".toRegex(),
        """If he develops well, I would say this chap can emerge with (?<value>.*) (?<skill>.*) before joining the senior squad""".toRegex(),
        """Given the chance to improve his (?<skill>.*) skills, this guy might well reach (?<value>.*) in that department""".toRegex(),
    )
    val allrounderRegexList = listOf(
        """Ich würde sagen, seine Fähigkeiten als Allrounder sind als (?<value>.*) einzustufen""".toRegex(),
        """Seine Fähigkeiten sind meiner Einschätzung nach insgesamt (?<value>.*)""".toRegex(),
        """Mit seinen Fähigkeiten, die ich insgesamt als (?<value>.*) bezeichnen würde""".toRegex(),
    )
    val headerRegexList = listOf(
        """Er ist ein Naturtalent für Zweikämpfe hoch in der Luft und wird in der Lage sein, diesen Vorteil sowohl in der Offensive als auch in der Defensive zu nutzen""".toRegex(),
        """Dieser Junge ist größer als die meisten anderen in seinem Alter, diesen Vorteil weiß er zu nutzen\. Auf sein anständiges Kopfballspiel kannst du dich verlassen""".toRegex(),
        """Dieser Spieler ist auf dem Platz hinsichtlich einer Fähigkeit besonders präsent\. Seine Kopfballstärke macht ihn zu einem besonderen Spezialisten""".toRegex(),
    )
    val powerfulRegexList = listOf(
        """Dieser Spieler ist ganz schön durchsetzungsstark für sein Alter\. Mit seiner immensen Physis dürfte er - und da bin ich mir sicher - einen Vorteil gegenüber seinen Gegenspielern haben, wenn es zu einer robusten Auseinandersetzung kommt""".toRegex(),
        """Einfach nur ein Junge, der von seinem Erscheinungsbild her durchsetzungsstark zu sein scheint\. Ich bin gespannt, wie sich das auf sein Spiel auswirkt""".toRegex(),
        """Er hat den Körperbau eines erwachsenen Mannes - das verschafft ihm einen gewissen Vorteil, um so durchsetzungsstark zu sein""".toRegex(),
    )
    val unpredictableRegexList = listOf(
        """Nun, was soll ich sagen? Er ist ein ewiges Rätsel, als Spieler unberechenbar\. Falls du weißt, wie du dir das zu Nutze machen kannst, könnte er von Interesse für dich sein""".toRegex(),
        """Wenn du über diesen Spieler nachdenkst, vergiss nicht, dass er einfach unberechenbar ist und mitunter außer Rand und Band gerät\. Einerseits kann er geistige Aussetzer haben, andererseits auch geniale Momente""".toRegex(),
        """Der Spieler ist unberechenbar, und ich weiß nicht, ob wir ihn in der Hinsicht ändern können\. Es ist einfach seine Art""".toRegex(),
    )
    val technicalRegexList = listOf(
        """Man kann immer wieder beobachten, wie dieser Junge im Training mit dem Ball zaubert - das ist eines seiner wesentlichen Merkmale""".toRegex(),
        """Dieser Spieler ist ein Ballzauberer, das kann man schon von weitem sehen\. Bedenke das, wenn du dein Team zusammenstellst""".toRegex(),
        """Es ist schwer diesen Spieler einzuschätzen, ohne seine Ballzauberer-Fähigkeit zu erwähnen\. Er scheint mit dem Ball einiges anstellen zu können""".toRegex(),
    )
    val quickRegexList = listOf(
        """Dieser Spieler ist zweifellos schnell, was für einen Fußballer in seinem Alter niemals verkehrt sein kann""".toRegex(),
        """Der hier ist ein kleiner Flitzer - immer in Bewegung, schneller als viele andere\. Ich hoffe, er lernt diese Fähigkeit auf dem Feld zu nutzen""".toRegex(),
        """Dieser Spieler ist wertvoll, er ist sehr schnell\. Was er jetzt nur noch lernen muss, ist Fußball zu spielen""".toRegex(),
    )
    val resilientRegexList = listOf(
        """Ich hörte davon, dass sich dieser Spieler nach einer erlittenen Verletzung schneller als viele andere erholt\. Hoffentlich muss er uns das niemals beweisen""".toRegex(),
        """Auch wenn dieser Junge nach nichts besonderem aussieht, so sagen uns zumindest die Ärzte, dass er bei einer Verletzung ansprechende Rehabilitationskräfte freisetzt""".toRegex(),
        """An diesem Spieler sitzt unglaublich gutes Heilfleisch, ich schätze, dass ist irgendwie genetisch bedingt""".toRegex(),
    )
    val supporterRegexList = listOf(
        """Dieser junge Mann ist sich nie zu schade, seinen Freunden zu helfen, auch wenn das für ihn zusätzliche Arbeit bedeutet\. Diesen Charakterzug zeigt er auch immer wieder auf dem Spielfeld""".toRegex(),
        """Dieser junge Mann scheint mir mannschaftsdienlich zu sein\. Jemand, der nicht nur seine Aufgaben erledigt, sondern auch seine Mitspieler unterstützt""".toRegex(),
        """Eine Bemerkung sei mir erlaubt, viele junge Spieler denken heute nur noch an sich selbst und ihre Karriere\. Bei diesem Kandidaten ist das anders, für ihn steht der Erfolg des Teams über allem""".toRegex(),
    )

    fun parse(comments: String): List<ScoutComment> {

        val introMatchResults = listMatchResults(comments, introRegexList)

        val skillMatchResults = listMatchResults(comments, skillRegexList)
        val potentialMatchResults = listMatchResults(comments, potentialRegexList)
        val allrounderMatchResults = listMatchResults(comments, allrounderRegexList)
        val headerMatchResults = listMatchResults(comments, headerRegexList)
        val powerfulMatchResults = listMatchResults(comments, powerfulRegexList)
        val unpredictableMatchResults = listMatchResults(comments, unpredictableRegexList)
        val technicalMatchResults = listMatchResults(comments, technicalRegexList)
        val quickMatchResults = listMatchResults(comments, quickRegexList)
        val resilientMatchResults = listMatchResults(comments, resilientRegexList)
        val supporterMatchResults = listMatchResults(comments, supporterRegexList)

        val eachPlayerCount = introMatchResults
            .mapNotNull { it.groups["name"]?.value }.groupingBy { it }
            .eachCount()

        return introMatchResults.mapIndexed { index, introMatchResult ->
            val nextIntroMatchResult = introMatchResults.getOrNull(index + 1)
            val associatedSkillMatchResult =
                findAssociatedMatchResults(skillMatchResults, introMatchResult, nextIntroMatchResult).firstOrNull()
            val associatedPotentialMatchResult = findAssociatedMatchResults(
                potentialMatchResults, introMatchResult, nextIntroMatchResult
            ).firstOrNull()

            val ageMatchGroup = introMatchResult.groups["age"]
            val nameMatchGroup = introMatchResult.groups["name"]
            val skill = associatedSkillMatchResult?.let {
                skillTypeOf(associatedSkillMatchResult.groups["skill"]?.value ?: "")?.let {
                    skillLevelOf(associatedSkillMatchResult.groups["value"]!!.value)?.let { skillLevelName ->
                        Skill(
                            it, skillLevelName
                        )
                    }
                }
            }
            val potential = associatedPotentialMatchResult?.let {
                skillTypeOf(associatedPotentialMatchResult.groups["skill"]?.value ?: "")?.let {
                    skillLevelOf(associatedPotentialMatchResult.groups["value"]!!.value)?.let { skillLevelName ->
                        Skill(
                            it, skillLevelName
                        )
                    }
                }
            }
            ScoutComment(
                nameMatchGroup!!.value,
                Age(ageMatchGroup!!.value.toInt(), null),
                skill,
                potential,
                eachPlayerCount.getOrDefault(nameMatchGroup.value, 0)
            )
        }
    }

    private fun findAssociatedMatchResults(
        skillMatchResults: List<MatchResult>, introMatchResult: MatchResult, nextIntroMatchResult: MatchResult?
    ) = skillMatchResults.dropWhile { paMatchResult -> paMatchResult.range.last < introMatchResult.range.first }
        .takeWhile { paMatchResult ->
            if (nextIntroMatchResult != null) {
                paMatchResult.range.last < nextIntroMatchResult.range.first
            } else {
                true
            }
        }

    private fun listMatchResults(comments: String, regexes: List<Regex>) =
        regexes.flatMap { it.findAll(comments) }.sortedBy { matchResult -> matchResult.range.first }.toList()
}

data class ScoutComment(val name: String, val age: Age, val skill: Skill?, val potential: Skill?, val occurrences: Int)

data class Age(val years: Int, val days: Int?)

fun skillTypeOf(name: String): SkillType? {
    return when (name) {
        "Torwart" -> SkillType.GK
        "Goalkeeping" -> SkillType.GK
        "Verteidigung" -> SkillType.DF
        "Defending" -> SkillType.DF
        "Spielaufbau" -> SkillType.PM
        "Playmaking" -> SkillType.PM
        "Flügelspiel" -> SkillType.WI
        "Winger" -> SkillType.WI
        "Torschuss" -> SkillType.SC
        "Scoring" -> SkillType.SC
        "Passspiel" -> SkillType.PS
        "Passing" -> SkillType.PS
        else -> null
    }
}

val levels_german = listOf(
    "göttlich",
    "galaktisch",
    "märchenhaft",
    "mythisch",
    "außerirdisch",
    "gigantisch",
    "übernatürlich",
    "Weltklasse",
    "fantastisch",
    "brillant",
    "großartig",
    "hervorragend",
    "sehr gut",
    "gut",
    "passabel",
    "durchschnittlich",
    "schwach",
    "armselig",
    "erbärmlich",
    "katastrophal",
    "nicht vorhanden"
).reversed()
val levels_english = listOf(
    "divine",
    "utopian",
    "magical",
    "mythical",
    "extra-terrestrial",
    "titanic",
    "supernatural",
    "world class",
    "magnificent",
    "brilliant",
    "outstanding",
    "formidable",
    "excellent",
    "solid",
    "passable",
    "inadequate",
    "weak",
    "poor",
    "wretched",
    "disastrous",
    "non-existent",
).reversed()

fun skillLevelOf(name: String): SkillLevel? {
    if (levels_german.contains(name)) {
        return SkillLevel(levels_german.indexOf(name))
    }
    if (levels_english.contains(name)) {
        return SkillLevel(levels_english.indexOf(name))
    }
    return null
}

enum class SkillType(val label: String) {
    GK("Torwart"),
    DF("Verteidigung"),
    PM("Spielaufbau"),
    WI("Flügelspiel"),
    SC("Torschuss"),
    PS("Passspiel"),
    SP("Standards");
}

data class SkillLevel(val value: Int) {
    fun label(): String {
        return levels_german[value]
    }
}


data class Skill(val type: SkillType, val level: SkillLevel)
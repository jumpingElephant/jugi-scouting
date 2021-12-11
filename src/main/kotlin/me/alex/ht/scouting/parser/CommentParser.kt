package me.alex.ht.scouting.parser

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object CommentParser {

    private val introRegexList = listOf(
        """Er ist (?<age>\d+) Jahre alt und hört auf den Namen (?<name>.*)\.""".toRegex(),
        """Okay, der Kandidat dieser Woche ist (?<age>\d+) Jahre alt und heißt (?<name>.*)\.""".toRegex(),
        """Kommen wir gleich zur Sache: (?<name>.*) ist (?<age>\d+) Jahre alt, und was ich von ihm gesehen habe, hat mich echt beeindruckt""".toRegex(),
        """\b(?<name>.*) könnte ein interessanter Spieler sein\. Er ist (?<age>\d+) Jahre alt und scheint das gewisse Etwas zu haben""".toRegex(),
        """Er sagte mir, er heiße (?<name>.*) und sei (?<age>\d+) Jahre alt""".toRegex(),
        """Mir scheint, ich habe ein Nachwuchstalent für dich gefunden\. Sein Name ist (?<name>.*), und er ist (?<age>\d+) Jahre alt""".toRegex(),
        """Wir haben einen aussichtsreichen Kandidaten zu beurteilen\. Er trägt den Namen (?<name>.*) und ist (?<age>\d+) Jahre alt""".toRegex(),
        """I think I have found a skilled youngster for you\. His name is (?<name>.*) and he is (?<age>\d+) years old""".toRegex(),
        """\b(?<name>.*) looks promising\. He is (?<age>\d+) years old and seems to have something extra""".toRegex(),
    )
    private val skillRegexList = listOf(
        """Dieser Spieler hat es bis jetzt in Sachen (?<skill>.*) auf (?<value>.*) gebracht""".toRegex(),
        """Ohne weiteres Training wird dieser Spieler in Sachen (?<skill>.*) nicht über (?<value>.*) hinauskommen""".toRegex(),
        """Ich würde seine Fähigkeiten in (?<skill>.*) zurzeit ungefähr auf (?<value>.*) schätzen""".toRegex(),
        """This player has Skill (?<value>.*) (?<skill>.*) at this point in his career""".toRegex(),
        """Without any further training, this player will remain with (?<value>.*) (?<skill>.*)""".toRegex(),
        """Right now I would say his (?<skill>.*) capabilities are around the (?<value>.*) level""".toRegex(),
    )
    private val potentialRegexList = listOf(
        """Ehe du dich versiehst, könnte dieser Spieler in (?<skill>.*) (?<value>.*) werden, falls du ihn richtig trainierst""".toRegex(),
        """Wenn sich der Kerl richtig entwickelt, könnte er in meinen Augen in (?<skill>.*) (?<value>.*) werden, bis zu seiner Aufnahme in die erste Mannschaft""".toRegex(),
        """Wenn er die Chance erhält, seine Fähigkeiten in (?<skill>.*) zu verbessern, könnte dieser Spieler darin (?<value>.*) werden""".toRegex(),
        """Coach this player right and he will reach his potential of (?<value>.*) (?<skill>.*) before you know it""".toRegex(),
        """If he develops well, I would say this chap can emerge with (?<value>.*) (?<skill>.*) before joining the senior squad""".toRegex(),
        """Given the chance to improve his (?<skill>.*) skills, this guy might well reach (?<value>.*) in that department""".toRegex(),
    )
    private val allrounderRegexList = listOf(
        """Ich würde sagen, seine Fähigkeiten als Allrounder sind als (?<value>.*) einzustufen""".toRegex(),
        """Seine Fähigkeiten sind meiner Einschätzung nach insgesamt (?<value>.*)""".toRegex(),
        """Mit seinen Fähigkeiten, die ich insgesamt als (?<value>.*) bezeichnen würde""".toRegex(),
        """I would say as an allrounder, he's (?<value>.*)""".toRegex(),
        """His overall abilities are (?<value>.*), in my opinion""".toRegex(),
        """With his (?<value>.*) overall skills, this might be the player for us""".toRegex(),
    )
    private val headerRegexList = listOf(
        """Er ist ein Naturtalent für Zweikämpfe hoch in der Luft und wird in der Lage sein, diesen Vorteil sowohl in der Offensive als auch in der Defensive zu nutzen""".toRegex(),
        """Dieser Junge ist größer als die meisten anderen in seinem Alter, diesen Vorteil weiß er zu nutzen\. Auf sein anständiges Kopfballspiel kannst du dich verlassen""".toRegex(),
        """Dieser Spieler ist auf dem Platz hinsichtlich einer Fähigkeit besonders präsent\. Seine Kopfballstärke macht ihn zu einem besonderen Spezialisten""".toRegex(),
        """He is a natural in the air, and he will be able to use this edge both offensively and in defence""".toRegex(),
        """The boy is taller than most of his age, and he knows how to use the advantage\. Count on him to do a decent header""".toRegex(),
        """This player has got stature on the pitch - he will develop into a nice head specialist""".toRegex(),
    )
    private val powerfulRegexList = listOf(
        """Dieser Spieler ist ganz schön durchsetzungsstark für sein Alter\. Mit seiner immensen Physis dürfte er - und da bin ich mir sicher - einen Vorteil gegenüber seinen Gegenspielern haben, wenn es zu einer robusten Auseinandersetzung kommt""".toRegex(),
        """Einfach nur ein Junge, der von seinem Erscheinungsbild her durchsetzungsstark zu sein scheint\. Ich bin gespannt, wie sich das auf sein Spiel auswirkt""".toRegex(),
        """Er hat den Körperbau eines erwachsenen Mannes - das verschafft ihm einen gewissen Vorteil, um so durchsetzungsstark zu sein""".toRegex(),
        """This player is powerful for his age\. With that large physique I'm sure he can get an advantage over his opponents when the going gets tough""".toRegex(),
        """Still just a boy but with a powerful frame\. We should ask his mother what she has been feeding him""".toRegex(),
        """He has the physique of a grown man - it gives him a certain edge to be this powerful""".toRegex(),
    )
    private val unpredictableRegexList = listOf(
        """Nun, was soll ich sagen\? Er ist ein ewiges Rätsel, als Spieler unberechenbar\. Falls du weißt, wie du dir das zu Nutze machen kannst, könnte er von Interesse für dich sein""".toRegex(),
        """Wenn du über diesen Spieler nachdenkst, vergiss nicht, dass er einfach unberechenbar ist und mitunter außer Rand und Band gerät\. Einerseits kann er geistige Aussetzer haben, andererseits auch geniale Momente""".toRegex(),
        """Der Spieler ist unberechenbar, und ich weiß nicht, ob wir ihn in der Hinsicht ändern können\. Es ist einfach seine Art""".toRegex(),
        """Well, what can I say\? He keeps you guessing - it's an unpredictable player, if you know how to use him he might be of interest to you""".toRegex(),
        """When you consider this player, don't forget he has a wild streak - he is unpredictable, one part madman, one part genius""".toRegex(),
        """The player is unpredictable, and I am not sure we can do much about that - it's just his way""".toRegex(),
    )
    private val technicalRegexList = listOf(
        """Man kann immer wieder beobachten, wie dieser Junge im Training mit dem Ball zaubert - das ist eines seiner wesentlichen Merkmale""".toRegex(),
        """Dieser Spieler ist ein Ballzauberer, das kann man schon von weitem sehen\. Bedenke das, wenn du dein Team zusammenstellst""".toRegex(),
        """Es ist schwer diesen Spieler einzuschätzen, ohne seine Ballzauberer-Fähigkeit zu erwähnen\. Er scheint mit dem Ball einiges anstellen zu können""".toRegex(),
        """You can often see this guy doing beautiful moves in training - it is one of his defining traits""".toRegex(),
        """This player is technical, you can see that from far away. Consider that when you build the team""".toRegex(),
        """It is hard to evaluate this player without mentioning his technical skills, he seems to have a natural love for the ball""".toRegex(),
    )
    private val quickRegexList = listOf(
        """Dieser Spieler ist zweifellos schnell, was für einen Fußballer in seinem Alter niemals verkehrt sein kann""".toRegex(),
        """Der hier ist ein kleiner Flitzer - immer in Bewegung, schneller als viele andere\. Ich hoffe, er lernt diese Fähigkeit auf dem Feld zu nutzen""".toRegex(),
        """Dieser Spieler ist wertvoll, er ist sehr schnell\. Was er jetzt nur noch lernen muss, ist Fußball zu spielen""".toRegex(),
        """This player is certainly quick, which is never a bad thing for a footballer in this day and age""".toRegex(),
        """This is a little speedball - always on the move, and faster than most. I hope he can learn to use that ability on the field""".toRegex(),
        """This kind of player is useful, he is very quick, now all he needs to learn is to play football as well""".toRegex(),
    )
    private val resilientRegexList = listOf(
        """Ich hörte davon, dass sich dieser Spieler nach einer erlittenen Verletzung schneller als viele andere erholt\. Hoffentlich muss er uns das niemals beweisen""".toRegex(),
        """Auch wenn dieser Junge nach nichts besonderem aussieht, so sagen uns zumindest die Ärzte, dass er bei einer Verletzung ansprechende Rehabilitationskräfte freisetzt""".toRegex(),
        """An diesem Spieler sitzt unglaublich gutes Heilfleisch, ich schätze, dass ist irgendwie genetisch bedingt""".toRegex(),
        """One thing I heard about this player is that he heals faster than most when he gets injured\. Let's hope he never has to show us that""".toRegex(),
        """This guy looks nothing special, but at least the doctors tell us he has a good capacity for rehabilitation when he has been injured""".toRegex(),
        """There's good healing flesh on this player, I guess it's some kind of genetic thing""".toRegex(),
    )
    private val supporterRegexList = listOf(
        """Dieser junge Mann ist sich nie zu schade, seinen Freunden zu helfen, auch wenn das für ihn zusätzliche Arbeit bedeutet\. Diesen Charakterzug zeigt er auch immer wieder auf dem Spielfeld""".toRegex(),
        """Dieser junge Mann scheint mir mannschaftsdienlich zu sein\. Jemand, der nicht nur seine Aufgaben erledigt, sondern auch seine Mitspieler unterstützt""".toRegex(),
        """Eine Bemerkung sei mir erlaubt, viele junge Spieler denken heute nur noch an sich selbst und ihre Karriere\. Bei diesem Kandidaten ist das anders, für ihn steht der Erfolg des Teams über allem""".toRegex(),
        """This young man always goes out of his way to help his friends, even if it means some extra work\. It is an asset in life, but also on the pitch""".toRegex(),
        """This boy has the makings of a great support player, someone who will not only do his own part, but help others shine as well""".toRegex(),
        """I don’t want to sound like a whiny old git, but many youngsters of today are only in this game to get famous\. This one is different\. All he wants is for the team to excel""".toRegex(),
    )
    private val metadataRegex = """(?<datetime>\d{2}\.\d{2}\.\d{4} \d{2}:\d{2})""".toRegex()

    val postDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

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
        val metadataMatchResults = listMatchResults(comments, listOf(metadataRegex))

        val eachPlayerCount = introMatchResults
            .mapNotNull { it.groups["name"]?.value }.groupingBy { it }
            .eachCount()

        val playerAcceptedRegex = """\b(ja|nein)\b""".toRegex(RegexOption.IGNORE_CASE)
        return introMatchResults.mapIndexed { index, introMatchResult ->
            val nextIntroMatchResult = introMatchResults.getOrNull(index + 1)
            val associatedSkillMatchResult =
                findAssociatedMatchResults(skillMatchResults, introMatchResult, nextIntroMatchResult).firstOrNull()
            val associatedPotentialMatchResult = findAssociatedMatchResults(
                potentialMatchResults, introMatchResult, nextIntroMatchResult
            ).firstOrNull()
            val associatedAllrounderMatchResult =
                findAssociatedMatchResults(allrounderMatchResults, introMatchResult, nextIntroMatchResult).firstOrNull()
            val associatedSpecialityMatchResults = findAssociatedMatchResults(
                listOfNotNull(
                    headerMatchResults,
                    powerfulMatchResults,
                    unpredictableMatchResults,
                    technicalMatchResults,
                    quickMatchResults,
                    resilientMatchResults,
                    supporterMatchResults
                ).flatten(),
                introMatchResult,
                nextIntroMatchResult
            ).firstOrNull()
            val metadataMatchResult = findPrefixedMatchResults(metadataMatchResults, introMatchResult)

            val sortedByAppearanceMatchResults = listOfNotNull(
                introMatchResult,
                associatedSkillMatchResult,
                associatedPotentialMatchResult,
                associatedAllrounderMatchResult,
                associatedSpecialityMatchResults
            ).sortedBy { it.range.first }.toList()
            val comment = comments.subSequence(
                sortedByAppearanceMatchResults.first().range.first,
                sortedByAppearanceMatchResults.last().range.last + 1
            )
            val accepted = playerAcceptedRegex
                .find(
                    comments.subSequence(
                        sortedByAppearanceMatchResults.last().range.last,
                        minOf(
                            sortedByAppearanceMatchResults.last().range.last + 100,
                            comments.length,
                            nextIntroMatchResult?.range?.first ?: Int.MAX_VALUE
                        )
                    )
                )
                ?.value?.let {
                    if (it.equals("Ja", ignoreCase = true)) {
                        null // not safe, cause phone call starts with: Ja, bitte? Ah, hallo ...
                    } else if (it.equals("Nein", ignoreCase = true)) {
                        false
                    } else {
                        null
                    }
                } ?: (
                    nextIntroMatchResult?.range?.first?.let { nextIntroBeginn ->
                        nextIntroBeginn > (metadataMatchResults
                            .getOrNull(metadataMatchResults.indexOf(metadataMatchResult) + 1)
                            ?.range?.first ?: Int.MAX_VALUE)
                    })


            val ageMatchGroup = introMatchResult.groups["age"]
            val nameMatchGroup = introMatchResult.groups["name"]

            val skill = extractSkill(associatedSkillMatchResult)
            val potential = extractSkill(associatedPotentialMatchResult)
            val postedAt = metadataMatchResult
                ?.let {
                    LocalDateTime.parse(
                        metadataMatchResult.groups["datetime"]?.value,
                        postDateTimeFormatter
                    )
                }

            ScoutComment(
                nameMatchGroup!!.value,
                Age(ageMatchGroup!!.value.toInt(), null),
                postedAt,
                skill,
                potential,
                accepted,
                eachPlayerCount.getOrDefault(nameMatchGroup.value, 0),
                comment
            )
        }
    }

    private fun extractSkill(associatedPotentialMatchResult: MatchResult?): Skill? {
        return associatedPotentialMatchResult?.let {
            skillTypeOf(associatedPotentialMatchResult.groups["skill"]?.value ?: "")?.let {
                skillLevelOf(associatedPotentialMatchResult.groups["value"]!!.value)?.let { skillLevelName ->
                    Skill(
                        it, skillLevelName
                    )
                }
            }
        }
    }

    private fun findAssociatedMatchResults(
        skillMatchResults: List<MatchResult>, matchResult: MatchResult, nextMatchResult: MatchResult?
    ) = skillMatchResults.dropWhile { it.range.last < matchResult.range.first }
        .takeWhile {
            if (nextMatchResult != null) {
                it.range.last < nextMatchResult.range.first
            } else {
                true
            }
        }

    private fun findPrefixedMatchResults(
        feasibleMatchResults: List<MatchResult>, matchResult: MatchResult
    ) = feasibleMatchResults.takeWhile { it.range.first < matchResult.range.last }.lastOrNull()

    private fun listMatchResults(comments: String, regexes: List<Regex>) =
        regexes.flatMap { it.findAll(comments) }.sortedBy { matchResult -> matchResult.range.first }.toList()
}

data class ScoutComment(
    val name: String,
    val age: Age,
    val postedAt: LocalDateTime?,
    val skill: Skill?,
    val potential: Skill?,
    val accepted: Boolean?,
    val occurrences: Int,
    val comment: CharSequence
)

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
@file:Suppress("FunctionName")

package me.alex.ht.scouting

import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.alex.ht.scouting.data.Exporter
import me.alex.ht.scouting.data.ScoutCommentsSample
import me.alex.ht.scouting.parser.CommentParser
import me.alex.ht.scouting.parser.ScoutComment

val applicationProperties = LocalApplicationProperties

@Suppress("FunctionName")
@Composable
fun App() {

    val appState = applicationProperties.current
    var scoutComments by remember { mutableStateOf(TextFieldValue(if (appState.isDevMode()) ScoutCommentsSample.getSample() else "")) }
    var scoutCommentList: List<ScoutComment> by remember { mutableStateOf(CommentParser.parse(scoutComments.text)) }

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 10.dp,
            )
            {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = { scoutComments = TextFieldValue(""); scoutCommentList = emptyList() }) {
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = "alles leeren"
                        )
                        Text("alles leeren")
                    }
                    Button(onClick = {
                        scoutCommentList = CommentParser.parse(scoutComments.text)
                    }) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Scoutkommentare auswerten"
                        )
                        Text("auswerten")
                    }
                    Button(onClick = {
                        Exporter.exportDatesAndCommentsNewLineSeparated(scoutCommentList)
                    }) {
                        Icon(
                            imageVector = Icons.Default.SaveAlt,
                            contentDescription = "Kommentare speichern"
                        )
                        Text("Kommentare speichern")
                    }
                }
            }
        }
    ) {
        Row {
            // Eingabefeld für Scoutkommentare
            TextField(
                scoutComments,
                modifier = Modifier.fillMaxWidth(0.5f),
                onValueChange = {
                    scoutComments = it
                },
                label = { Text(text = "Scoutkommentare") },
            )
            // Ergebnisbereich
            Preview(scoutCommentList)
        }
    }

}

@Composable
fun Preview(scoutCommentList: List<ScoutComment>) {
    Column {
        var filterText by remember { mutableStateOf(TextFieldValue("")) }
        Row {
            Text(
                "${scoutCommentList.size} Spieler",
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, top = 5.dp)
            )
            TextField(
                filterText,
                onValueChange = { filterText = it },
                label = { Text("Suche") },
                trailingIcon = {
                    Button({ filterText = TextFieldValue("") }, shape = RectangleShape) {
                        Icon(
                            imageVector = Icons.Default.Backspace,
                            contentDescription = "clear"
                        )
                    }
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            val scrollState = rememberLazyListState()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 12.dp),
                state = scrollState,
            ) {
                items(
                    items = (if (filterText.text.isBlank()) {
                        scoutCommentList
                    } else {
                        val regex = filterText.text
                            .replace("ä", "a")
                            .replace("ö", "o")
                            .replace("ü", "u")
                            .replace("ß", "s")
                            .replace("""\W+""".toRegex(), "")
                            .toRegex(options = setOf(RegexOption.IGNORE_CASE, RegexOption.CANON_EQ))
                        scoutCommentList.filter {
                            it.name.replace("ä", "a")
                                .replace("ö", "o")
                                .replace("ü", "u")
                                .replace("ß", "s")
                                .contains(regex)
                        }
                    })
                ) { scoutComment ->
                    Card(
                        modifier = Modifier
                            .padding(0.dp, 5.dp)
                            .fillMaxWidth(),
                        elevation = 10.dp,
                    ) {
                        Column(modifier = Modifier.padding(5.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                scoutComment.postedAt?.let {
                                    Text(
                                        scoutComment.postedAt.format(CommentParser.postDateTimeFormatter),
                                    )
                                }
                                Text(
                                    " (${scoutComment.occurrences})",
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(
                                    scoutComment.name,
                                )
                                Text(
                                    "Alter: ${scoutComment.age.years}",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End
                                )
                            }
                            scoutComment.skill?.let { Text("Fähigkeit: ${it.type.label}=${it.level.label()}") }
                            scoutComment.potential?.let { Text("Potential: ${it.type.label}=${it.level.label()}") }
                        }
                    }
                }
            }
            // https://github.com/JetBrains/compose-jb/tree/master/tutorials/Desktop_Components
            VerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                style = ScrollbarStyle(
                    minimalHeight = 16.dp,
                    thickness = 8.dp,
                    shape = MaterialTheme.shapes.small,
                    hoverDurationMillis = 300,
                    unhoverColor = MaterialTheme.colors.primary.copy(alpha = 0.42f),
                    hoverColor = MaterialTheme.colors.primary,
                ),
                adapter = rememberScrollbarAdapter(scrollState)
            )
        }
    }
}
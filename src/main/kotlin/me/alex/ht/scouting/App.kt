@file:Suppress("FunctionName")

package me.alex.ht.scouting

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.isTypedEvent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.alex.ht.scouting.data.Exporter
import me.alex.ht.scouting.data.ScoutCommentsSample
import me.alex.ht.scouting.parser.CommentParser
import me.alex.ht.scouting.parser.ScoutComment
import java.awt.event.InputEvent
import java.awt.event.KeyEvent.VK_END
import java.awt.event.KeyEvent.VK_HOME

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
                    Button(onClick = {
                        scoutComments = TextFieldValue(ScoutCommentsSample.getSample())
                        scoutCommentList = CommentParser.parse(scoutComments.text)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ReplayCircleFilled,
                            contentDescription = "Datei laden"
                        )
                        Text("Datei laden")
                    }
                    Button(onClick = { scoutComments = TextFieldValue(""); scoutCommentList = emptyList() }) {
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = "leeren"
                        )
                        Text("leeren")
                    }
                    Button(onClick = {
                        scoutCommentList = CommentParser.parse(scoutComments.text)
                    }) {
                        Icon(
                            imageVector = Icons.Default.PlaylistPlay,
                            contentDescription = "Scoutkommentare auswerten"
                        )
                        Text("auswerten")
                    }
                    Button(onClick = {
                        Exporter.exportDatesAndCommentsNewLineSeparated(scoutCommentList)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ImportExport,
                            contentDescription = "Exportieren"
                        )
                        Text("Exportieren")
                    }
                }
            }
        }
    ) {
        Row {
            // Eingabefeld f??r Scoutkommentare
            Box(modifier = Modifier.fillMaxHeight()) {
                val scrollState = rememberScrollState()
                val coroutineScope = rememberCoroutineScope()

                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxHeight()
                ) {
                    TextField(
                        scoutComments,
                        modifier = Modifier.fillMaxWidth(0.5f)
                            .onKeyEvent {
                                if ((it.type == KeyEventType.KeyUp
                                            && !it.isTypedEvent
                                            && it.nativeKeyEvent.modifiersEx == InputEvent.CTRL_DOWN_MASK)
                                ) {
                                    if (it.nativeKeyEvent.keyCode == VK_HOME) {
                                        scoutComments = scoutComments.copy(selection = TextRange(0))
                                        coroutineScope.launch { scrollState.scrollTo(0) }
                                    } else if (it.nativeKeyEvent.keyCode == VK_END) {
                                        scoutComments = scoutComments.copy(selection = TextRange(Int.MAX_VALUE))
                                        coroutineScope.launch { scrollState.scrollTo(scrollState.maxValue) }
                                    }
                                }
                                false
                            },
                        onValueChange = {
                            scoutComments = it
                        },
                        label = { Text(text = "Scoutkommentare") },
                    )
                }
                VerticalScrollbar(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight(),
                    style = ScrollbarStyle(
                        minimalHeight = 24.dp,
                        thickness = 8.dp,
                        shape = MaterialTheme.shapes.small,
                        hoverDurationMillis = 300,
                        unhoverColor = MaterialTheme.colors.primary.copy(alpha = 0.42f),
                        hoverColor = MaterialTheme.colors.primary,
                    ),
                    adapter = rememberScrollbarAdapter(scrollState)
                )
            }            // Ergebnisbereich
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
                        scoutCommentList.filter(function(filterText.text))
                    })
                ) { scoutComment ->
                    Card(
                        modifier = Modifier
                            .padding(0.dp, 5.dp)
                            .fillMaxWidth(),
                        elevation = 10.dp,
                    ) {
                        PlayerPreview(scoutComment)
                    }
                }
            }
            // https://github.com/JetBrains/compose-jb/tree/master/tutorials/Desktop_Components
            VerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                style = ScrollbarStyle(
                    minimalHeight = 24.dp,
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

@Composable
private fun PlayerPreview(scoutComment: ScoutComment) {
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
            scoutComment.accepted
                ?.let {
                    Text(
                        if (it) "aufgenommen" else "abgelehnt",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
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
        scoutComment.speciality?.let { Text(it) }
        scoutComment.skill?.let { Text("F??higkeit: ${it.type.label}=${it.level.label()}") }
        scoutComment.potential?.let { Text("Potential: ${it.type.label}=${it.level.label()}") }
    }
}

private fun function(text: String): (ScoutComment) -> Boolean = fun(it: ScoutComment): Boolean {
    val regex = text
        .replace("??", "a")
        .replace("??", "o")
        .replace("??", "u")
        .replace("??", "s")
        .replace("""\W+""".toRegex(), "")
        .toRegex(options = setOf(RegexOption.IGNORE_CASE, RegexOption.CANON_EQ))
    return it.name
        .replace("??", "a")
        .replace("??", "o")
        .replace("??", "u")
        .replace("??", "s")
        .contains(regex)
}
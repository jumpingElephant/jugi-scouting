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
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import me.alex.ht.scouting.parser.CommentParser
import me.alex.ht.scouting.parser.ScoutComment

@Suppress("FunctionName")
@Composable
fun App() {

    var scoutComments by remember { mutableStateOf(TextFieldValue("")) }
    var scoutCommentList: List<ScoutComment> by remember { mutableStateOf(emptyList()) }

    Scaffold(
        topBar = {
            TopAppBar(
                elevation = 10.dp,
            )
            {
                Button(onClick = { scoutComments = TextFieldValue(""); scoutCommentList = emptyList() }) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = "Scoutkommentare leeren"
                    )
                    Text("Neue Kommentare")
                }
            }
        }
    ) {
        Row {
            // Eingabefeld für Scoutkommentare
            TextField(
                scoutComments,
//                maxLines = 19,
                modifier = Modifier.fillMaxWidth(0.5f),
                onValueChange = {
                    scoutComments = it
                    scoutCommentList = CommentParser.parse(scoutComments.text)
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
        Text(
            "${scoutCommentList.size} Spieler gefunden",
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, top = 5.dp)
        )
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
                items(scoutCommentList) { scoutComment ->
                    Card(
                        modifier = Modifier
                            .padding(0.dp, 5.dp)
                            .fillMaxWidth(),
                        elevation = 10.dp,
                    ) {
                        Column(modifier = Modifier.padding(5.dp)) {
                            Text(
                                "Name: ${scoutComment.name} (${scoutComment.occurrences})",
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Text(
                                "Alter: ${scoutComment.age.years}",
                                modifier = Modifier.fillMaxWidth(),
                            )
                            scoutComment.skill?.let { Text("Fähigkeit: ${it.type.label}=${it.level.label()}") }
                            scoutComment.potential?.let { Text("Potential: ${it.type.label}=${it.level.label()}") }
                        }
                    }
                }
            }
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
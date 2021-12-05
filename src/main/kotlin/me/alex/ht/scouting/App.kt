package me.alex.ht.scouting

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
                maxLines = 19,
                modifier = Modifier.fillMaxWidth(0.5f),
                onValueChange = {
                    scoutComments = it
                    scoutCommentList = CommentParser.parse(scoutComments.text)
                },
                label = { Text(text = "Scoutkommentare") },
            )
            // Ergebnisbereich
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                val stateVertical = rememberScrollState(0)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(stateVertical)
                        .padding(end = 12.dp, bottom = 12.dp)
                ) {
                    Column {
                        Text("${scoutCommentList.size} Spieler gefunden")
                        Column(
                            verticalArrangement = Arrangement.spacedBy(7.dp),
                            modifier = Modifier.fillMaxHeight(1F)
                        ) {
                            scoutCommentList.map { scoutComment ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    elevation = 10.dp,
                                ) {
                                    Column(modifier = Modifier.padding(5.dp)) {
                                        Text(
                                            "Name: ${scoutComment.name}",
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                        Text(
                                            "Alter: ${scoutComment.age.years}",
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                    }
                                }
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
                    adapter = rememberScrollbarAdapter(stateVertical)
                )
            }
        }
    }

}
data class ScoreSubjectData(val name: String, val type: String)

var testDataSet = listOf(
    ScoreSubjectData("Calgary Flames","team"),
    ScoreSubjectData("Toronto Maple Leafs","team"),
    ScoreSubjectData("Pittsburgh Penguins","team"),
    ScoreSubjectData("Saskatchewan Roughriders","team"),
    ScoreSubjectData("NHL","league"),
    ScoreSubjectData("CFL","league"),
    ScoreSubjectData("Jordan Romano","player"),
    ScoreSubjectData("Yimi García","player"),
    ScoreSubjectData("Brayan Bello","player"),
    ScoreSubjectData("Connor Wong","player"),
    ScoreSubjectData("Mark Giordano","player"),
    ScoreSubjectData("Jakub Lauko","player")
    )

val pageVerId = mapOf<String,String>(
    "player" to "com.fivemobile.thescore:id/txt_player_name",
    "league" to "com.fivemobile.thescore:id/titleTextView",
    "team" to "com.fivemobile.thescore:id/titleTextView"
)

val tabVerText = mapOf<String,String>(
    "player" to "INFO",
    "league" to "STANDINGS",
    "team" to "ROSTER"
)
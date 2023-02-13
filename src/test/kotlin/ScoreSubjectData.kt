data class ScoreSubjectData(val name: String, var type: String){
    fun main(agrs: Array<String>){
        val t1 = ScoreSubjectData("Calgary Flames","team")
        val t2 = ScoreSubjectData("Toronto Maple Leafs","team")
        val t3 = ScoreSubjectData("Pittsburgh Penguins","team")
        val t4 = ScoreSubjectData("Saskatchewan Roughriders","team")
        val l1 = ScoreSubjectData("NHL","league")
        val l2 = ScoreSubjectData("CFL","league")
        val p1 = ScoreSubjectData("Jordan Romano","player")
        val p2 = ScoreSubjectData("Yimi García","player")
        val p3 = ScoreSubjectData("Brayan Bello","player")
        val p4 = ScoreSubjectData("Connor Wong","player")
        val p5 = ScoreSubjectData("Mark Giordano","player")
        val p6 = ScoreSubjectData("Jakub Lauko","player")
    }
}

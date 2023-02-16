import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class TheScoreTestSetHP001 : AppiumSetup() {

    val logMe = LittleLogger
    val scoreOps = TheScoreOperations

    @ParameterizedTest(name = "test The Score navigation functionality with each variation: {0}")
    @MethodSource("grabData")
    fun runTestSet(subj: ScoreSubjectData) {
        logMe.addLineToReport("{\"test\": \"full test\", \"subject\": \"${subj.name}\", \"steps\": [")
        scoreOps.theScoreHomeScreen()
        scoreOps.step1PickSubject(subj)
        scoreOps.step2VerifySubjectPage(subj)
        scoreOps.step3SelectSubtab(subj.type)
        scoreOps.step4VerifySubtabMatchStep1(subj)
        scoreOps.step5VerifyBackNav(subj,2)
        scoreOps.step5VerifyBackNav(subj,1)
        logMe.addLineToReport("]}\r\n")
        assert(true)
    }

    @Test
    fun testReport(){
        print(logMe.reportBlob)
        assert(true)
    }

    companion object {
        @JvmStatic
        fun grabData(): List<ScoreSubjectData> {
            return testDataSet
        }
    }
}
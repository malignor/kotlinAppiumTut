import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TheScoreOperations : AppiumSetup() {

    fun startTheScore() {
        var appState = 0
        var giveUpAfter = 50
        var waits = 0

        while (appState<1 && waits<giveUpAfter){
            try {
                if (driver.findElementById(searchBarId).isDisplayed) {appState=2}
            } catch (exception: Exception) {
                Thread.sleep(20)
                print(".")
                waits++
            }
            try {
                if (driver.findElementById(getStartedButtonId).isDisplayed) {appState=1}
            } catch (exception: Exception) {
                Thread.sleep(20)
                print(".")
                waits++
            }
        }
        if (appState==1){
            println("state 1")
            driver.findElementById(getStartedButtonId).click()
            driver.findElementById(continue1Id).click()
            driver.findElementByXPath(pickAnyFaveXpath).click()
            driver.findElementById(continue2Id).click()
            driver.findElementById(doneStartingId).click()
            Thread.sleep(20)
            driver.findElementById(killInitAddId).click()
            appState=2
        }
        if (appState==2) {
            println("state 2")
        } else {
            println("Looks like it's a dud - TheScore is not running!")
        }
    }

    fun step1PickSubject(subject: ScoreSubjectData) {
        driver.findElementById(searchBarId).click()
        driver.findElementById(searchBarTextId)
            .sendKeys(subject.name)
        driver.findElementById(searchResultId).click()
    }

    fun step2VerifySubjectPage(subject: ScoreSubjectData) {
        println("STEP 2 BEGIN")
        if (subject.type == "player") {
            val pageLabel = driver.findElementById("com.fivemobile.thescore:id/txt_player_name").getAttribute("text")
            assertEquals(subject.name, pageLabel)
        } else if (subject.type == "league") {
            val pageLabel = driver.findElementById("com.fivemobile.thescore:id/titleTextView").getAttribute("text")
            assertEquals(subject.name, pageLabel)
        } else if (subject.type == "team") {
            val pageLabel = driver.findElementById("com.fivemobile.thescore:id/team_name").getAttribute("text")
            assertEquals(subject.name, pageLabel)
        }
    }

    fun step3SelectSubtab(category: String) {
        println("STEP 3 BEGIN")
        if (category == "player") {
            driver.findElementByAccessibilityId(playerInfoTabAccId).click()
        } else if (category == "league") {
            driver.findElementByXPath(leagueStandingsTabXpath).click()
        } else if (category == "team") {
            driver.findElementByXPath(teamStatsTabXpath).click()
        }
    }

    fun step4VerifySubtabMatchStep1(subject: ScoreSubjectData) {
        println("STEP 4 BEGIN")
        if (subject.type == "player") {
            val tabHeader = driver.findElementByAccessibilityId(playerInfoTabAccId)
            assertTrue(tabHeader.isEnabled)
        } else if (subject.type == "league") {
            val tabHeader = driver.findElementByXPath(leagueStandingsTabXpath)
            assertTrue(tabHeader.isEnabled)
        } else if (subject.type == "team") {
            val tabHeader = driver.findElementByXPath(teamStatsTabXpath)
            assertTrue(tabHeader.isEnabled)
        }
    }

    fun step5VerifyBackNav(subject: ScoreSubjectData, depth: Int) {
        println("STEP 5 BEGIN AT DEPTH $depth")
        driver.findElementByAccessibilityId("Navigate up").click()
        if (depth == 2) {
            if (subject.type == "player") {
                val pageLabel = driver.findElementById("com.fivemobile.thescore:id/txt_player_name").getAttribute("text")
                assertEquals(subject.name, pageLabel)
            } else if (subject.type == "league") {
                val pageLabel = driver.findElementById("com.fivemobile.thescore:id/titleTextView").getAttribute("text")
                assertEquals(subject.name, pageLabel)
            } else if (subject.type == "team") {
                val pageLabel = driver.findElementById("com.fivemobile.thescore:id/team_name").getAttribute("text")
                assertEquals(subject.name, pageLabel)
            }
        } else {
            assert(false)
        }
    }

    fun closeTheScore(){
        driver.closeApp()
    }

    @ParameterizedTest(name = "test The Score navigation functionality with each variation: {0}")
    @MethodSource("grabData")
    fun runTestSet(subj: ScoreSubjectData) {
        println("Running test with subject: "+subj.name)
        startTheScore()
        step1PickSubject(subj)
        step2VerifySubjectPage(subj)
        step3SelectSubtab(subj.name)
        step4VerifySubtabMatchStep1(subj)
        step5VerifyBackNav(subj,2)
        step5VerifyBackNav(subj,1)
        closeTheScore()
    }

    companion object {
        @JvmStatic
        fun grabData(): List<ScoreSubjectData> {
            return testDataSet
        }
    }

}
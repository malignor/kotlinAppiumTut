import io.appium.java_client.MobileElement
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TheScoreOperations : AppiumSetup() {

    //https://www.javadoc.io/static/io.appium/java-client/4.1.0/io/appium/java_client/android/AndroidDriver.html#currentActivity()

    var screenState = 0
        /*  0 = not open at all
            1 = first time user screen; go through initial prompts
            2 = main screen with search bar up top and league button on bottom right
            3 = mid-search, picking from options
            4 = player or team or league screen
            5 = player or team or league subtab
         */

    private fun checkScreenState(){
        try {
            if (driver.findElementsById(searchBarId).isNotEmpty() && driver.findElementsByXPath(leaguesButton).isNotEmpty()) {screenState=2}
            else if (driver.findElementsById(searchBarTextId).isNotEmpty()) {screenState=3}
            else if (driver.findElementsById(playerLabelId).isNotEmpty() || driver.findElementsById(teamLabelId).isNotEmpty() || driver.findElementsById(leagueLabelId).isNotEmpty()) {
                screenState = if (
                    (driver.findElementsByAccessibilityId(playerInfoTabAccId).isNotEmpty() && driver.findElementByAccessibilityId(playerInfoTabAccId).isSelected)
                    || (driver.findElementsByXPath(teamScheduleTabXpath).isNotEmpty() && driver.findElementByXPath(teamScheduleTabXpath).isSelected)
                    || (driver.findElementsByXPath(leagueStandingsTabXpath).isNotEmpty() && driver.findElementByXPath(leagueStandingsTabXpath).isSelected))
                {5} else {4}
            }
            else if (driver.findElementsById(getStartedButtonId).isNotEmpty()) {screenState=1}
            else {screenState=0}
        } catch (exception: Exception) {
            print("UNKNOWN STATE")
            screenState=0
        }
    }

    private fun checkCrashed(){
        if (screenState<1){
            println("App crashed!")
            assert(false)
        }
    }

    fun theScoreHomeScreen() {
        var giveUpAfter = 50
        var waits = 0

        while (screenState<1 && waits<giveUpAfter){
            dismissModal()
            checkScreenState()
        }
        while (screenState!=2 && waits<giveUpAfter) {
            println("state=$screenState")
            if (screenState == 1) {
                driver.findElementById(getStartedButtonId).click()
                driver.findElementById(continue1Id).click()
                if (driver.findElementsById(disallowLocationId).isNotEmpty()) {
                    driver.findElementById(disallowLocationId).click()
                }
                driver.findElementByXPath(pickAnyFaveXpath).click()
                driver.findElementById(continue2Id).click()
                if (driver.findElementsById(disallowLocationId).isNotEmpty()) {
                    driver.findElementById(disallowLocationId).click()
                }
                driver.findElementById(doneStartingId).click()
                dismissModal(20)
                checkScreenState()
            } else if (screenState == 2) {
                dismissModal()
            } else if (screenState == 3) {
                dismissModal()
                if (driver.findElementsByXPath(newsButtonXpath).isNotEmpty()) {
                    driver.findElementByXPath(newsButtonXpath).click()
                } else if (driver.findElementsByAccessibilityId(backButtonId).isNotEmpty()) {
                    driver.findElementByAccessibilityId(backButtonId).click()
                }
                checkScreenState()
            } else if (screenState == 4) {
                dismissModal()
                if (driver.findElementsByXPath(newsButtonXpath).isNotEmpty()) {
                    driver.findElementByXPath(newsButtonXpath).click()
                } else if (driver.findElementsByAccessibilityId(backButtonId).isNotEmpty()) {
                    driver.findElementByAccessibilityId(backButtonId).click()
                }
                checkScreenState()
            } else if (screenState == 5) {
                dismissModal()
                if (driver.findElementsByXPath(newsButtonXpath).isNotEmpty()) {
                    driver.findElementByXPath(newsButtonXpath).click()
                } else if (driver.findElementsByAccessibilityId(backButtonId).isNotEmpty()) {
                    driver.findElementByAccessibilityId(backButtonId).click()
                }
                checkScreenState()
            } else {
                println("Looks like it's a dud - TheScore is not running!")
                waits=giveUpAfter
            }
        }
    }

    fun dismissModal(giveUpAfter: Int = 1) {
        var waits = 0
        var dismissed = false
        while (!dismissed && waits < giveUpAfter) {
            if (driver.findElementsById(killInitAddId).isNotEmpty()) {
                driver.findElementById(killInitAddId).click()
                dismissed = true
            } else {
                Thread.sleep(20)
                waits++
            }
        }
    }

    fun step1PickSubject(subject: ScoreSubjectData) {
        checkCrashed()
        println("STEP 1 BEGIN")
        if (subject.type=="league"){
            driver.findElementByXPath(leaguesButton).click()

            // dismiss the Edit popup as a workaround; it can interfere with the league selection
            driver.findElementById(leaguesPrepId).click()
            Thread.sleep(50)
            driver.findElementById(leaguesPrepId).click()
            Thread.sleep(50)

            val leagueSet: List<MobileElement> = driver.findElementsByXPath(leagueOptionXPath)
            for (leagueOpt in leagueSet){
                val optTxt = leagueOpt.getAttribute("text")
                //println("Text ("+subject.name+") = "+optTxt)
                if (optTxt.equals(subject.name)){
                    //println("Found! clicking...")
                    leagueOpt.click()
                    Thread.sleep(50)
                    break
                }
            }
        } else {
            driver.findElementById(searchBarId).click()
            driver.findElementById(searchBarTextId)
                .sendKeys(subject.name)
            driver.findElementById(searchResultId).click()
        }
    }

    fun step2VerifySubjectPage(subject: ScoreSubjectData) {
        checkCrashed()
        println("STEP 2 BEGIN")
        if (subject.type == "player") {
            val pageLabel = driver.findElementById(playerLabelId).getAttribute("text")
            assertEquals(subject.name, pageLabel)
        } else if (subject.type == "league") {
            val pageLabel = driver.findElementById(leagueLabelId).getAttribute("text")
            assertEquals(subject.name, pageLabel)
        } else if (subject.type == "team") {
            val pageLabel = driver.findElementById(teamLabelId).getAttribute("text")
            assertEquals(subject.name, pageLabel)
        }
    }

    fun step3SelectSubtab(category: String) {
        checkCrashed()
        println("STEP 3 BEGIN")
        if (category == "player") {
            driver.findElementByAccessibilityId(playerInfoTabAccId).click()
        }
        else if (category == "league") {
            driver.findElementByXPath(leagueStandingsTabXpath).click()
        }
        else if (category == "team") {
            driver.findElementByAccessibilityId(teamScheduleTabAccId).click()
        }
        Thread.sleep(20)
    }

    fun step4VerifySubtabMatchStep1(subject: ScoreSubjectData) {
        checkCrashed()
        println("STEP 4 BEGIN")
        when (subject.type) {
            "player" -> {
                val tabHeader = driver.findElementByAccessibilityId(playerInfoTabAccId)
                assertTrue(tabHeader.isSelected)
            }
            "league" -> {
                val tabHeader = driver.findElementByXPath(leagueStandingsTabXpath)
                assertTrue(tabHeader.isSelected)
            }
            "team" -> {
                val tabHeader = driver.findElementByAccessibilityId(teamScheduleTabAccId)
                assertTrue(tabHeader.isSelected)
            }
        }

    }

    fun step5VerifyBackNav(subject: ScoreSubjectData, depth: Int) {
        checkCrashed()
        if (screenState<1){
            println("App crashed!")
            assert(false)
        }
        println("STEP 5 BEGIN WITH DEPTH $depth AT STATE $screenState")
        if (subject.type=="league" && depth<2){
            println("no back button here.")
            if (driver.findElementsByXPath(newsButtonXpath).isNotEmpty()) {
                driver.findElementByXPath(newsButtonXpath).click()
                assert(true)
                return
            } else {
                assert(false)
                return
            }
        }
        driver.findElementByAccessibilityId(backButtonId).click()
        checkScreenState()
        println("Going Back moved us to state $screenState")
        if (depth==2 && screenState==3 && subject.type!="league") {
            val searchText = driver.findElementById(searchBarTextId).getAttribute("text")
            assertEquals(subject.name, searchText)
        } else if (depth==2 && screenState==4 && subject.type=="league") {
            val pageLabel = driver.findElementById(leagueLabelId).getAttribute("text")
            assertEquals("Leagues", pageLabel)
            assert(true)
        } else if (depth==1 && screenState==2) {
            println("Back to home!")
            assert(true)
        } else {
            println("Not as expected!")
            assert(false)
        }
    }

    @ParameterizedTest(name = "test The Score navigation functionality with each variation: {0}")
    @MethodSource("grabData")
    fun runTestSet(subj: ScoreSubjectData) {
        println("Running test with subject: "+subj.name)
        theScoreHomeScreen()
        step1PickSubject(subj)
        step2VerifySubjectPage(subj)
        step3SelectSubtab(subj.type)
        step4VerifySubtabMatchStep1(subj)
        step5VerifyBackNav(subj,2)
        step5VerifyBackNav(subj,1)
        println("\r\n")
    }

    companion object {
        @JvmStatic
        fun grabData(): List<ScoreSubjectData> {
            return testDataSet
        }
    }

}
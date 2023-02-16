import io.appium.java_client.MobileElement
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TheScoreOperations : AppiumSetup() {

    //https://www.javadoc.io/static/io.appium/java-client/4.1.0/io/appium/java_client/android/AndroidDriver.html#currentActivity()

    companion object {

    var screenState = 0
    /*  0 = not open at all
            1 = first time user screen; go through initial prompts
            2 = main screen with search bar up top and league button on bottom right
            3 = mid-search, picking from options
            4 = player or team or league screen
            5 = player or team or league subtab
         */

    val logMe = LittleLogger

    private fun checkScreenState() {
        try {
            if (driver.findElementsById(searchBarId).isNotEmpty() && driver.findElementsByXPath(leaguesButton)
                    .isNotEmpty()
            ) {
                screenState = 2
            } else if (driver.findElementsById(searchBarTextId).isNotEmpty()) {
                screenState = 3
            } else if (driver.findElementsById(playerLabelId).isNotEmpty() || driver.findElementsById(teamLabelId)
                    .isNotEmpty() || driver.findElementsById(leagueLabelId).isNotEmpty()
            ) {
                screenState = if (
                    (driver.findElementsByAccessibilityId(playerInfoTabAccId)
                        .isNotEmpty() && driver.findElementByAccessibilityId(playerInfoTabAccId).isSelected)
                    || (driver.findElementsByXPath(teamScheduleTabXpath).isNotEmpty() && driver.findElementByXPath(
                        teamScheduleTabXpath
                    ).isSelected)
                    || (driver.findElementsByXPath(leagueStandingsTabXpath).isNotEmpty() && driver.findElementByXPath(
                        leagueStandingsTabXpath
                    ).isSelected)
                ) {
                    5
                } else {
                    4
                }
            } else if (driver.findElementsById(getStartedButtonId).isNotEmpty()) {
                screenState = 1
            } else {
                screenState = 0
            }
        } catch (exception: Exception) {
            logMe.addToReport("UNKNOWN STATE")
            screenState = 0
        }
    }

    private fun checkCrashed() {
        if (screenState < 1) {
            logMe.addLineToReport("App crashed!")
            assert(false)
        }
    }

    private fun dismissModal(giveUpAfter: Int = 1) {
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

    fun theScoreHomeScreen() {
        var giveUpAfter = 50
        var waits = 0

        while (screenState < 1 && waits < giveUpAfter) {
            dismissModal()
            checkScreenState()
        }
        logMe.addToReport("  {\"step\": \"0\", \"description\": \"check at home screen\", \"screen state\": \"${screenState}\", \"actions\": [")
        while (waits < giveUpAfter) {
            if (screenState == 1) {
                logMe.addToReport("\"action\": \"initial onboarding\", ")
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
                logMe.addToReport("\"action\": \"none\"]")
                dismissModal()
                break
            } else if (screenState == 3) {
                logMe.addToReport("\"action\": \"navigate to home screen\", ")
                dismissModal()
                if (driver.findElementsByXPath(newsButtonXpath).isNotEmpty()) {
                    driver.findElementByXPath(newsButtonXpath).click()
                } else if (driver.findElementsByAccessibilityId(backButtonId).isNotEmpty()) {
                    driver.findElementByAccessibilityId(backButtonId).click()
                }
                checkScreenState()
            } else if (screenState == 4) {
                logMe.addToReport("\"action\": \"navigate to home screen\", ")
                dismissModal()
                if (driver.findElementsByXPath(newsButtonXpath).isNotEmpty()) {
                    driver.findElementByXPath(newsButtonXpath).click()
                } else if (driver.findElementsByAccessibilityId(backButtonId).isNotEmpty()) {
                    driver.findElementByAccessibilityId(backButtonId).click()
                }
                checkScreenState()
            } else if (screenState == 5) {
                logMe.addToReport("\"action\": \"navigate to home screen\", ")
                dismissModal()
                if (driver.findElementsByXPath(newsButtonXpath).isNotEmpty()) {
                    driver.findElementByXPath(newsButtonXpath).click()
                } else if (driver.findElementsByAccessibilityId(backButtonId).isNotEmpty()) {
                    driver.findElementByAccessibilityId(backButtonId).click()
                }
                checkScreenState()
            } else {
                logMe.addToReport("\"action\": \"NULL - THESCORE IS NOT RUNNING\", ")
                break
            }
        }
        logMe.addLineToReport("},")
    }

    fun step1PickSubject(subject: ScoreSubjectData) {
        checkCrashed()
        logMe.addToReport("  {\"step\": \"1\", \"description\": \"pick a subject\", \"subject\": \"${subject.name}\", \"subjectType\": \"${subject.type}\", \"result\": ")
        if (subject.type == "league") {
            driver.findElementByXPath(leaguesButton).click()

            // dismiss the Edit popup as a workaround; it can interfere with the league selection
            driver.findElementById(leaguesPrepId).click()
            Thread.sleep(50)
            driver.findElementById(leaguesPrepId).click()
            Thread.sleep(50)

            val leagueSet: List<MobileElement> = driver.findElementsByXPath(leagueOptionXPath)
            for (leagueOpt in leagueSet) {
                val optTxt = leagueOpt.getAttribute("text")
                //addLineToReport("Text ("+subject.name+") = "+optTxt)
                if (optTxt.equals(subject.name)) {
                    //addLineToReport("Found! clicking...")
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
        logMe.addLineToReport("\"success\"},")
    }

    fun step2VerifySubjectPage(subject: ScoreSubjectData) {
        checkCrashed()
        logMe.addToReport("  {\"step\": \"2\", \"description\": \"verify correct subject page\", \"subject\": \"${subject.name}\", \"subjectType\": \"${subject.type}\", \"result\": ")
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
        logMe.addLineToReport("\"success (${subject.type}label=${subject.name})\"},")
    }

    fun step3SelectSubtab(category: String) {
        checkCrashed()
        logMe.addToReport("  {\"step\": \"3\", \"description\": \"navigate to a subtab\", \"subject\": \"N/A\", \"subjectType\": \"${category}\", \"result\": ")
        var tabType: String = ""
        if (category == "player") {
            driver.findElementByAccessibilityId(playerInfoTabAccId).click()
            tabType = "info"
        } else if (category == "league") {
            driver.findElementByXPath(leagueStandingsTabXpath).click()
            tabType = "standings"
        } else if (category == "team") {
            driver.findElementByAccessibilityId(teamScheduleTabAccId).click()
            tabType = "schedule"
        }
        Thread.sleep(20)
        logMe.addLineToReport("\"success ($tabType tab selected)\"},")
    }

    fun step4VerifySubtabMatchStep1(subject: ScoreSubjectData) {
        checkCrashed()
        logMe.addToReport("  {\"step\": \"4\", \"description\": \"verify subtab matches subject\", \"subject\": \"${subject.name}\", \"subjectType\": \"${subject.type}\", \"result\": ")
        var tabType: String = ""
        when (subject.type) {
            "player" -> {
                val tabHeader = driver.findElementByAccessibilityId(playerInfoTabAccId)
                assertTrue(tabHeader.isSelected)
                tabType = "info"
            }

            "league" -> {
                val tabHeader = driver.findElementByXPath(leagueStandingsTabXpath)
                assertTrue(tabHeader.isSelected)
                tabType = "standings"
            }

            "team" -> {
                val tabHeader = driver.findElementByAccessibilityId(teamScheduleTabAccId)
                assertTrue(tabHeader.isSelected)
                tabType = "schedule"
            }
        }
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
        logMe.addLineToReport("\"success (${subject.type}label=${subject.name}, $tabType tab is selected)\"},")
    }

    fun step5VerifyBackNav(subject: ScoreSubjectData, depth: Int) {
        checkCrashed()
        if (screenState < 1) {
            logMe.addLineToReport("App crashed!")
            assert(false)
        }
        logMe.addToReport("  {\"step\": \"5\", \"description\": \"navigate back using app button, verify proper screen\", \"subject\": \"${subject.name}\", \"subjectType\": \"${subject.type}\", \"depth\": \"${depth}\", \"screenstate\": \"${screenState}\", \"result\": ")
        if (subject.type == "league" && depth < 2) {
            logMe.addLineToReport("\"skipped (no back button from this screen, by design)\"}")
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
        if (depth == 2 && screenState == 3 && subject.type != "league") {
            val searchText = driver.findElementById(searchBarTextId).getAttribute("text")
            assertEquals(subject.name, searchText)
            logMe.addLineToReport("\"success (navigation moved to state $screenState, search screen with field contents = ${subject.name})\"},")
        } else if (depth == 2 && screenState == 4 && subject.type == "league") {
            val pageLabel = driver.findElementById(leagueLabelId).getAttribute("text")
            assertEquals("Leagues", pageLabel)
            assert(true)
            logMe.addLineToReport("\"success (navigation moved to state $screenState, search screen page label = Leagues)\"},")
        } else if (depth == 1 && screenState == 2) {
            assert(true)
            logMe.addLineToReport("\"success (navigation moved to state $screenState)\"}")
        } else {
            logMe.addLineToReport("\"FAILED (navigation moved to state $screenState)\"}")
            assert(false)
        }
    }
    }
}
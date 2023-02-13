import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import kotlin.test.assertEquals

class TheScoreTests : AppiumSetup() {
    @Test
    fun `Open The Score`() {

        //use findByElement to figure out what to click
        driver.findElement(By.id(DIGIT_5)).click()
        driver.findElement(By.id(MULTIPLICATION)).click()
        driver.findElement(By.id(DIGIT_8)).click()
        driver.findElement(By.id(EQUAL)).click()

        //store result from (the only) TextView into "result"
        val result = driver.findElement(By.className("android.widget.TextView")).text

        // now check that it's 40
        assertEquals("40", result)
    }

    fun `Step 1 - Open a league, team or player page using parameterized approach`() {}

    fun `Step 2 - Verify that the expected page opens correctly`() {}

    fun `Step 3 - Tab on a sub-tab of choice`() {}

    fun `Step 4 - Verify that you are on the correct tab and the data displayed is appropriate to step 1`() {}

    fun `Step 5 - Verify that the back navigation returns you to the previous page correctly`() {}
}
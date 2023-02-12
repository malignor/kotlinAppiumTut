import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import kotlin.test.assertEquals

class MultiplicationTests : AppiumSetup() {
    @Test
    fun `Simple multiplication give correct result`() {

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
}
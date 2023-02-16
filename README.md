# TheScore Appium/Kotlin Test Example

This is a Kotlin project that uses Appium to test TheScore, an Android app. The project includes a sample test suite that verifies some of the app's functionality.

### Approach
* **GOAL**: to use the Parameterized test annotation and its features, to create a robust test that handles various subjects (players, teams, leagues).
* **STRUCTURE**: In order to retain a continuous test session with a single subject, across all the test steps, I put them all under one test.
  * Each step is its own function, with JSON formatted logging. This logging is meant to offset the simplified reporting from the one all-inclusive test.
  * Wherever I found code about to be duplicated, I create a new private function, to be shared.
* **TEST DATA**: Variety was important, and implied which subtabs to use for test steps 3 and 4. The data was defined separately ([here](src/test/kotlin/ScoreSubjectData.kt)), so that it can be edited or extended, or commented out. 
  * Teams include CFL teams, NHL teams, and the Blue Jays
  * Leagues include CFL and NHL
  * Players include NHL and Baseball players
* **ELEMENT DEFINITION**: I avoided XPath whenever possible, but there were a few cases where it appeared unavoidable.
  * Defined in a separate file ([here](src/test/kotlin/ScoreContentConst.kt)). This allows them to be changed or updated, or the file can be replaced for a new APK with different elements. 

### Coverage
From my estimation, this functions as a smoke test. It has substantial percentage for happy path coverage... I would estimate 15% coverage of the happy path scenarios, but it's a vital & commonly used 15%. The coverage of this test is as per the assessment requirements.

## Requirements

To run the tests in this project, you will need the following:

* An Android device or emulator with the app installed. _I recommend Android Studio, of which I used Electric Eel, 2022.1.1 Patch 1_
* JDK 1.8 or later. _I used Java SDK 11, Kotlin v 1.7_
* Android SDK and ADB installed on your system. _I used ADB 1.0.41, v33.0.2-8557947_
* Appium installed on your system, which likely requires npm

## Setup

To set up the project, follow these steps:

1. Clone the repository to your local machine.
2. Open the project in your IDE of choice.
3. Install the required dependencies by running `./gradlew build` in your terminal.
4. Connect your Android device or emulator to your machine and start the Appium server.
5. Make sure to install TheScore (see the .apk file in the root directory [here](com.fivemobile.thescore_23.1.0-23010_minAPI24(arm64-v8a,armeabi-v7a,x86,x86_64)(nodpi)_apkmirror.com.apk)) on you emulator or device.
6. Update the _**appium.properties**_ file with the appropriate device name and app package and activity names. See the values in [src/test/kotlin/AppiumSetup.kt](src/test/kotlin/AppiumSetup.kt) for specifics.

## Running the tests

To run the test suite, use the following command in your terminal:

`./gradlew test`

This will execute the tests and generate reports in 
* [build/reports/tests/test/index.html]()
* [build/test-junit-xml/TEST-TheScoreOperations.xml]()
* [build/test-results/test/TEST-TheScoreOperations.xml]()

They aren't in this git repo, but you can see them after you execute.
Default reports are basic, but I added some verbosity in JSON format.
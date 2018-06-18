# MRM Android Application
[![CircleCI](https://circleci.com/gh/andela/mrm-mobile/tree/develop.svg?style=svg)](https://circleci.com/gh/andela/mrm-mobile/tree/develop) [![Maintainability](https://api.codeclimate.com/v1/badges/a333e35d4f89a6b3283b/maintainability)](https://codeclimate.com/repos/5b164ef55882d702b20005b2/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/a333e35d4f89a6b3283b/test_coverage)](https://codeclimate.com/repos/5b164ef55882d702b20005b2/test_coverage)

The MRM (Meeting Room Management) Android app.

## Project Setup

This project is built with Gradle, the [Android Gradle plugin](http://tools.android.com/tech-docs/new-build-system/user-guide). Follow the steps below to set up the project locally.

* Clone [MRM Android](https://github.com/andela/mrm-mobile) inside your working folder.
* Start Android Studio
* Select "Open Project" and select the generated root Project folder.
* Setup the Keystore configuration by following the steps below;
    1. Create a directory called keystores in your project.
    2. Create two directories `mock` and `prod` within the project app level directory `app/src`.
    3. Create two directories `debug` and `release` inside `mock` and `prod` directories respectively.
    4. Download `keystore.properties` and `keystore.jks` files located in the pinned items in the `converge-android` slack channel and put those into the `keystores` directory.
    5. Download `google-services.json` from the `converge-android` channel and paste it into the project app level directory `app/`, and also into each `debug` and `release` directory that you created earlier.
    6. Ensure that the files are ignored from `.gitignore`
    7. Rebuild the project.
    8. Your projects should have these file paths at this point.
        ```
        keystores/
        app/google-services.json
        app/src/mock/debug/google-services.json
        app/src/mock/release/google-services.json
        app/src/prod/debug/google-services.json
        app/src/prod/release/google-services.json
        ```
* You may be prompted with "Unlinked gradle project" -> Select "Import gradle project" and select.
the option to use the gradle wrapper
* You may also be prompted to change to the appropriate SDK folder for your local machine
* Once the project has compiled -> run the project!

## Running the application on an emulator or actual device

There can be many ways of running your application in Android Studio.
* First off, lets check out running on an emulator
    * We shall start by installing the emulator into your android studio.

    Installing the Android SDK manager is the first step. Follow these steps to install the system image.

     1. Select `Tools` in the task bar.
     2. Go to `SDK manager`.
     3. Select the Android version. (Android 8.1 Oreo /API Level 27 or later).
     4. Click install.

    * We shall then proceed to run the emulator:

        1. Open the project in your android studio.
        2. click the run button in android studio.
        3. Proceed to `create new virtual device`.
        4. Select the category below:
            - `Tablet`
        5. Proceed to the type of tablet to use and click next.
        6. Select the latest version and the preferred API version `27 or later`.
        7. Click finish.
        8. Now when you click run, you will be able to select the device chosen and proceed to view your work on the emulator

     * Installing the google play services on our emulator.
     Follow this link
     https://medium.com/@dai_shi/installing-google-play-services-on-an-android-studio-emulator-fffceb2c28a1

     * While updating the google play services one can follow this link
     https://developers.google.com/android/guides/setup
     http://www.androiddocs.com/google/play-services/setup.html#

## Running the tests to show coverage

To ensure that a user is looking at the most recent build, the can;
1. Run the command `./gradlew clean` to make sure that they have no pre-existing reports or
2. Navigating to the `Gradle` button in the right side of Android Studio, select the `build` directory followed by the `clean` task to ensure they are lookiung at a clean build.

To run the tests locally the Dev/user has two options which are;

1. Within Android studio, click on the `Gradle` tab to the right end of android studio, select `app`, `Tasks`, `Verification` then `createMockDebugCoverageReport` to run the tests and generate the coverage report.
2. Within Android studio, in the terminal window enter the command `./gradlew createMockDebugCoverageReport` to generate the coverage report for the project.

At this point, the user can check inside the directory `app/build/reports/coverage/mock/debug/index.html` to view the current coverage rport

## Android Test
[![Build Status](https://travis-ci.com/ksananth87/Android-test.svg?branch=master)](https://travis-ci.com/ksananth87/Android-test)
[![codecov](https://codecov.io/gh/ksananth87/Android-test/branch/master/graph/badge.svg)](https://codecov.io/gh/ksananth87/Android-test)
[![awsome-kotlin](https://camo.githubusercontent.com/a0afa2c788fcce72a8d0983a6d6c11cfeaf7f5a9/68747470733a2f2f6b6f746c696e2e6c696e6b2f617765736f6d652d6b6f746c696e2e737667)](https://camo.githubusercontent.com/a0afa2c788fcce72a8d0983a6d6c11cfeaf7f5a9/68747470733a2f2f6b6f746c696e2e6c696e6b2f617765736f6d652d6b6f746c696e2e737667)
[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/dashboard?id=ksananth87_Test)

## Key points to highlight about this kata:
* Followed Test Driven Development
* Used kotlin
* Used MVVM architectural pattern 
* Used Android's LiveData to notify changes to Activity
* Added Test cases for all business logic in Model and ViewModel
* Added Android Test cases to test acceptance criteria.
* Integrated with Continuous Integration tool (Travis CI) [![Build Status](https://travis-ci.com/ksananth87/Android-test.svg?branch=master)](https://travis-ci.com/ksananth87/Android-test) , Code coverage tool (codecov) [![codecov](https://codecov.io/gh/ksananth87/Android-test/branch/master/graph/badge.svg)](https://codecov.io/gh/ksananth87/Android-test) and SonarQube [![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/dashboard?id=ksananth87_Test) for checking code quality.
* Used Constraint Layout for designing UI

## Libraries used

| Libraries        | Why           |
| ------------- |:-------------:|
| `androidx.navigation:navigation-fragment-ktx:2.2.0`      | Navigation |
| `androidx.navigation:navigation-ui-ktx:2.2.0`      |       |
| `com.github.bumptech.glide:glide:4.10.0`  | Load images      |
| `com.squareup.retrofit2:retrofit:2.7.0`  | REST Client      |
| `io.reactivex.rxjava2:rxandroid:2.1.0`  | Asynchronous     |
| `androidx.lifecycle:lifecycle-extensions:2.1.0`  | LiveData      |
| TEST |      |
| `com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0`  | Mock dependencies     |
| `junit:junit:4.12`  | Junit      |
| Android Test  |       |
| `org.mockito:mockito-android:2.18.3`  | Mock Dependencies    |
| `com.android.support.test:runner:1.0.2`  | Runner and rules      |

## Run
To run this project (make sure mobile is connected and usb debugging is enabled):

```
$ ./gradlew build installDebug
```
To run tests (Junit and Espresso):

```
$ ./gradlew build connectedCheck
```

To generate report of tests:

```
$ ./gradlew build jacocoTestReport assembleAndroidTest createDebugCoverageReport
```
## Project flow
![](https://github.com/ksananth87/Android-test/blob/master/project_structure.png)

## Project folder structure
![](https://github.com/ksananth87/Android-test/blob/master/project.png)

## Output
![](https://github.com/ksananth87/Android-test/blob/master/screenshot.gif)

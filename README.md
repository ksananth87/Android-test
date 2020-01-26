
## Android Test
[![Build Status](https://travis-ci.com/ksananth87/Android-test.svg?branch=master)](https://travis-ci.com/ksananth87/Android-test)
[![codecov](https://codecov.io/gh/ksananth87/Android-test/branch/master/graph/badge.svg)](https://codecov.io/gh/ksananth87/Android-test)


## Key points to highlight about this kata:
* Followed Test Driven Development
* Used MVVM architectural pattern to seperate development of the UI and business logic
* Used Android's LiveData to notify changes to Activity
* Added Test cases for all business logic in Model and ViewModel
* Added Android Test cases to test the View.
* Integrated with Continous Integration tool (Travis CI) [![Build Status](https://travis-ci.com/ksananth87/Android-test.svg?branch=master)](https://travis-ci.com/ksananth87/Android-test) and Code coverage tool (codecov) [![codecov](https://codecov.io/gh/ksananth87/Android-test/branch/master/graph/badge.svg)](https://codecov.io/gh/ksananth87/Android-test).
* Used Constraint Layout for designing UI

## Libraries used

| Library                                                  | Why                                                |
| ------------- |:-------------:                           |
| `androidx.navigation:navigation-fragment-ktx:2.2.0`      | Navigation                                         |
| `androidx.navigation:navigation-ui-ktx:2.2.0`            |                                            |
| `com.github.bumptech.glide:glide:4.10.0`                 | Load Image                                          |

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

## Output
![](https://github.com/ksananth87/Android-test/blob/master/screenshot.gif)

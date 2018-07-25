# Testing

This document specifies which classes and functions are tested and which are not. This is an important step before implementing tests, because you'd be wasting a lot of time by testing unimportant code while possibly leaving no time left to test vital functionality.

Any new SDK functionality should follow these rules before implementing its functionality, providing a structured way to work using the Test Driven Development method.

## Unit Tests

| Level | Coverage | Importance | Remark |
| ----- | -------- | ---------- | ------ |
| O2MC | all | 1 | Used by app developers. Must not crash at any point. |
| Managers | most | 2 | Most methods define vital 'high level' functionality |
| Util | all | 3 | (Mostly) used by managers. Provides testable lower level functionality. |
| Domain | - |- | Contains POJOs. Senseless to test these since you'd be testing the language Java. |
|       |          |            |        |
|       |          |            |        |
|       |          |            |        |

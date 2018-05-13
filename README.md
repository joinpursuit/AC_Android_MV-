# Android MV*

### Branch: Version Three - Model-View-Presenter

Problems from version two that have been solved:
* Activities and `onCreate()` methods are even more concise and readable
* Activities (a.k.a. views) are unaware of the data layer

Problems that remain in this version:
* No way to keep data in memory on device rotation
* View and presenter contracts may be considered cumbersome

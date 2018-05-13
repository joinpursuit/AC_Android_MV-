# Android MV*

### Branch: Version Two - Some Separation of Concerns

Problems from version one that have been solved:
* Package structure updated to create "UI" and "data" layers
* Activities and `onCreate()` methods are very concise and readable
* Database operations have been moved to a background thread
* Activities would no longer need to be changed if you want to switch to a different networking or database library
* `MyDoggoManager` could be unit tested (would require mocking)

Problems that remain in this version:
* No way to keep data in memory on device rotation
* Activities are still aware of the data layer of the app

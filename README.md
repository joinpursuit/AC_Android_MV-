# Android MV*

### Branch: Version Two - Some Separation of Concerns

Problems from version one that have been solved:
* Activities and `onCreate()` methods are very concise and readable
* Database operations have been moved to a background thread
* Activities would no longer need to be changed if you want to switch to a different networking or database library

Problems that remain in this version:
* `MyDoggoManager` is still difficult to unit test (but not impossible)
* No way to keep data in memory on device rotation
* Activities are still aware of the data layer of the app

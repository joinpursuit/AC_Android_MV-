# Android MV*

### Branch: Version One - No Separation of Concerns

Problems in this version:
* The activities (specifically their `onCreate()` methods) are full of complicated logic and very difficult to read
* The only way to test that logic would be with complex Espresso UI tests, because you can't unit test an activity
* The activities (and really the whole app) are tightly coupled to the specific networking (Retrofit) and database (SQLiteOpenHelper) libraries used - could not switch to a different library without having to change code all over the app
* The database operations are happening on the main thread! Performance is very slow!

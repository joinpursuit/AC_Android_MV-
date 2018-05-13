# Android MV*

### A.K.A. Separation of Concerns

Branches:
* [Version One](https://github.com/C4Q/AC_Android_MV-/tree/version_one): All logic in the Activities w/ no separation of concerns
* [Version Two](https://github.com/C4Q/AC_Android_MV-/tree/version_two): Networking and database logic moved to a "manager" class to clean up the Activities
* [Version Three](https://github.com/C4Q/AC_Android_MV-/tree/version_three): Add presenters between activities and data manager to implement Model-View-Presenter
* [Version Four](https://github.com/C4Q/AC_Android_MV-/tree/version_four): Replace presenters with ViewModel from Android Architecture Components to implement Google's version of MVVM (which is really more like MVP w/o the contracts)

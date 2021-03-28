# locator

A simple Service Locator Library for Kotlin Multiplatform. <br/>
<img alt="GitHub tag (latest by date)" src="https://img.shields.io/github/v/tag/chRyNaN/locator">

## Using the library

* Create a custom Module Interface extending from the `Module` interface

```kotlin
interface ScreenModule : Module {

    val presenter: ScreenPresenter

    val navigator: ScreenNavigator
}
```

* Create the Module Implementation

```kotlin
class ScreenModuleImpl(view: ScreenView) : ScreenModule {

    override val presenter: ScreenPresenter = ScreenPresenter(view = view)

    override val navigator: ScreenNavigator = ScreenNavigator(view = view)
}
```

* Implement the `LocatesWith` interface

```kotlin
class Screen : BaseScreen(),
    ScreenView,
    LocatesWith<ScreenModule> {

    override val module: ScreenModule = ScreenModuleImpl(view = this)

    ...
}
```

* Access values provided by the Module:

```kotlin
class Screen : BaseScreen(),
    ScreenView,
    LocatesWith<ScreenModule> {

    override val module: ScreenModule = ScreenModuleImpl(view = this)

    private val presenter: ScreenPresenter by locate { presenter }

    private val navigator: ScreenNavigator? by locateOrNull { navigator }

    override fun onCreate() {
        attachToDependencyGraph()
        // Could also access the fields by the module property
        module.presenter
    }

    override fun onDestroy() {
        detachFromDependencyGraph()
    }
}
```

## Customization

The Modules can contain any fields or functions that are accessible in the `locate` or `locateOrNull` delegates. This is
due to the delegates being scoped to the Module.

```kotlin
class MyModule(private val myView: ScreenView) : Module {

    val fieldAssignedOnce: String = "Field Assigned Once"

    val nullableField: String? = null

    val lazyField: String by lazy { "Lazy Field" }

    val getterField: String
        get() = "Getter Field"

    val presenter: MyPresenter = MyPresenter(view = myView)

    fun otherPresenter(otherView: ScreenView) = MyPresenter(view = otherView)
}

class Screen : BaseScreen(),
    ScreenView,
    LocatesWith<MyModule> {

    override val module: MyModule = MyModule(view = this)

    val fieldAssignedOnce by locate { fieldAssignedOnce }

    val nullableField by locate { nullableField }

    internal val lazyField by locate { lazyField }

    private val getterField by locate { getterField }

    private val presenter by locate { presenter }

    private val otherPresenter by locate { otherPresenter(otherView = this) }
}
```

## Delegates

* The `locate` function can return a type (nullable or non-nullable) if the field is present. Meaning that the module is
  defined. Otherwise an exception will be thrown. The exception thrown could either be a `ModuleNotInitializedException`
  or a `ModuleClassCastException`.

* The `locateOrNull` acts just like the `locate` function but returns `null` instead of throwing an exception.

## Dependency Graph Building

You create the Dependency Graph using inheritance with the Modules.

Consider the following `Module` that lives as long as the Application is running:

```kotlin
interface WebModule : Module {

    val baseUrl: String
}

class WebModuleImpl : WebModule {

    override val baseUrl: String = "https://chrynan.com"
}

class Application : BaseApplication(),
    LocatesWith<WebModule> {

    override val module: WebModule = WebModuleImpl()

    override fun onCreate() {
        attachToDependencyGraph()
    }

    override fun onDestroy() {
        detachFromDependencyGraph()
    }
}
```

Then a `Module` that depends on the above Module, may look like this:

```kotlin
interface ScreenModule : Module,
    WebModule {

    val presenter: ScreenPresenter

    val navigator: ScreenNavigator
}

class ScreenModuleImpl(view: ScreenView) : ScreenModule,
    WebModule by dependencyGraph() {

    override val presenter: ScreenPresenter = ScreenPresenter(view = view, baseUrl = baseUrl)

    override val navigator: ScreenNavigator = ScreenNavigator(view = view)
}

class Screen : BaseScreen(),
    ScreenView,
    LocatesWith<ScreenModule> {

    override val module: ScreenModule = ScreenModuleImpl(view = this)

    private val presenter: ScreenPresenter by locate { presenter }

    private val navigator: ScreenNavigator? by locateOrNull { navigator }

    override fun onCreate() {
        attachToDependencyGraph()
        // Could also access the fields by the module property
        module.presenter
    }

    override fun onDestroy() {
        detachFromDependencyGraph()
    }
}
```

The `dependencyGraph()` function looks for the correct `Module` type through the currently attached modules. If it isn't
present it will fail and the application will crash. Alternatively, you could explicitly provide the module:

```kotlin
interface ScreenModule : Module,
    WebModule {

    val presenter: ScreenPresenter

    val navigator: ScreenNavigator
}

class ScreenModuleImpl(
    webModule: WebModule,
    view: ScreenView
) : ScreenModule,
    WebModule by webModule {

    override val presenter: ScreenPresenter = ScreenPresenter(view = view, baseUrl = baseUrl)

    override val navigator: ScreenNavigator = ScreenNavigator(view = view)
}
```

## Building the library

The library is provided through [Repsy.io](https://repsy.io). Refer to
the [releases page](https://github.com/chRyNaN/locator/releases) for the latest version. <br/>
<img alt="GitHub tag (latest by date)" src="https://img.shields.io/github/v/tag/chRyNaN/locator">

### Repository

```kotlin
repositories {
    maven { url = "https://repo.repsy.io/mvn/chrynan/public" }
}
```

### Dependencies

```kotlin
implementation("com.chrynan.locator:locator-core:$VERSION")
```

## Documentation

More detailed documentation is available in the [docs](docs) folder. The entry point to the documentation can be
found [here](docs/index.md).

## License

```
Copyright 2021 chRyNaN

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

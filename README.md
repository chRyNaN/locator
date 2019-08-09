# locator
A very simple Service Locator Library for Kotlin Multiplatform

## Using the library

* Create a Module Interface extending from the `Module` interface
```kotlin
interface ScreenModule : Module {

    val presenter: ScreenPresenter

    val navigator: ScreenNavigator
}
```

* Create Module Implementation
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

The Modules can contain any fields or functions that are accessible in the `locate` or `locateOrNull` delegates. This is due to the delegates being scoped to the Module.
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

* The `locate` function can return a type (nullable or non-nullable) if the field is present. Meaning that the module is defined. Otherwise an exception will be thrown. The exception thrown could either be a `ModuleNotInitializedException` or a `ModuleClassCastException`.

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

The `dependencyGraph()` function looks for the correct `Module` type through the currently attached modules. If it isn't present it will fail and the application will crash. Alternatively, you could explicitly provide the module:
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

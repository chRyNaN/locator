package com.chrynan.locator

interface LocatesWith<M : Module> {

    val module: M
}

inline fun <reified M : Module> LocatesWith<M>.attachToDependencyGraph() = DependencyGraph.attachModule(module)

inline fun <reified M : Module> LocatesWith<M>.detachFromDependencyGraph() = DependencyGraph.detachModule<M>()

inline fun <reified M : Module> attachToDependencyGraph(module: M) = DependencyGraph.attachModule(module)

inline fun <reified M : Module> detachFromDependencyGraph() = DependencyGraph.detachModule<M>()

inline fun <reified M : Module, R> LocatesWith<M>.locate(noinline moduleAccessor: M.() -> R) =
    Locator(moduleAccessor = moduleAccessor, module = module)

inline fun <reified M : Module, R> LocatesWith<M>.locateOrNull(noinline moduleAccessor: M.() -> R) =
    LocatorOrNull(moduleAccessor = moduleAccessor, module = module)
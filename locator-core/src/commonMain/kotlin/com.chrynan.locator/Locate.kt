@file:Suppress("unused")

package com.chrynan.locator

import com.chrynan.locator.graph.DependencyGraph
import com.chrynan.locator.graph.GraphPosition
import kotlin.reflect.KClass

interface Locate<M : Module> {

    val module: M

    val position: GraphPosition
}

class LocateImpl<M : Module>(
    override val module: M,
    override val position: GraphPosition
) : Locate<M>

inline fun <reified M : Module> module(module: M): Locate<M> {
    val position = DependencyGraph.bind(M::class, module)

    return LocateImpl(module = module, position = position)
}

inline fun <reified M : Module, R> Locate<M>.locate(noinline moduleAccessor: M.() -> R): Locator<M, R> =
    Locator(moduleAccessor = moduleAccessor, module = module)

inline fun <reified M : Module, R> Locate<M>.locateOrNull(noinline moduleAccessor: M.() -> R): LocatorOrNull<M, R> =
    LocatorOrNull(moduleAccessor = moduleAccessor, module = module)

inline fun <reified L : Module, reified M : Module, R> Locate<L>.locateWith(noinline moduleAccessor: M.() -> R): LocatorWith<L, M, R> =
    LocatorWith(kClass = M::class, position = position, moduleAccessor = moduleAccessor)

internal inline fun <reified L : Module, reified M : Module, R> Locate<L>.locateWithOrNull(noinline moduleAccessor: M.() -> R): LocatorWithOrNull<L, M, R> =
    LocatorWithOrNull(kClass = M::class, position = position, moduleAccessor = moduleAccessor)


class TestModule : Module

class OtherTestModule : Module

fun Locate<TestModule>.test() {
    withModule<OtherTestModule>().locate<> { "" }
}
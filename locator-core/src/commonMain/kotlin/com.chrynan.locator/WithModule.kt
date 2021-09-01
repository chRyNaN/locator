package com.chrynan.locator

import kotlin.reflect.KClass

data class WithModule<M : Module>(
    val locate: Locate<*>,
    val withKClass: KClass<M>
)

internal inline fun <reified L : Module, reified M : Module, R> WithModule<M>.locate(noinline moduleAccessor: M.() -> R): LocatorWith<*, M, R> =
    LocatorWith<L, M, R>(kClass = withKClass, position = locate.position, moduleAccessor = moduleAccessor)

inline fun <reified M : Module> Locate<*>.withModule(): WithModule<M> = WithModule(this, M::class)

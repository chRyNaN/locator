package com.chrynan.locator

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Locator<M : Module, R>(
    private val moduleAccessor: M.() -> R,
    private val module: M
) : ReadOnlyProperty<LocatesWith<M>, R> {

    override fun getValue(thisRef: LocatesWith<M>, property: KProperty<*>): R = moduleAccessor.invoke(module)
}

class LocatorOrNull<M : Module, R>(
    private val moduleAccessor: M.() -> R,
    private val module: M
) : ReadOnlyProperty<LocatesWith<M>, R?> {

    override fun getValue(thisRef: LocatesWith<M>, property: KProperty<*>): R? =
        try {
            moduleAccessor.invoke(module)
        } catch (e: Exception) {
            null
        }
}
package com.chrynan.locator

import com.chrynan.locator.graph.GraphPosition
import com.chrynan.locator.graph.findModule
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class Locator<M : Module, R>(
    private val moduleAccessor: M.() -> R,
    private val module: M
) : ReadOnlyProperty<Locate<M>, R> {

    override fun getValue(thisRef: Locate<M>, property: KProperty<*>): R = moduleAccessor.invoke(module)
}

class LocatorOrNull<M : Module, R>(
    private val moduleAccessor: M.() -> R,
    private val module: M
) : ReadOnlyProperty<Locate<M>, R?> {

    override fun getValue(thisRef: Locate<M>, property: KProperty<*>): R? =
        try {
            moduleAccessor.invoke(module)
        } catch (e: Exception) {
            null
        }
}

class LocatorWith<L : Module, M : Module, R>(
    private val kClass: KClass<M>,
    private val position: GraphPosition,
    private val moduleAccessor: M.() -> R
) : ReadOnlyProperty<Locate<L>, R> {

    override fun getValue(thisRef: Locate<L>, property: KProperty<*>): R {
        val module = position.findModule(kClass)
        return moduleAccessor.invoke(module)
    }
}

class LocatorWithOrNull<L : Module, M : Module, R>(
    private val kClass: KClass<M>,
    private val position: GraphPosition,
    private val moduleAccessor: M.() -> R
) : ReadOnlyProperty<Locate<L>, R?> {

    override fun getValue(thisRef: Locate<L>, property: KProperty<*>): R? =
        try {
            val module = position.findModule(kClass)
            moduleAccessor.invoke(module)
        } catch (e: Exception) {
            null
        }
}

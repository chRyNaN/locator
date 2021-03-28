@file:Suppress("unused")

package com.chrynan.locator.graph

import com.chrynan.locator.Module
import kotlin.reflect.KClass

@PublishedApi
internal object DependencyGraph {

    val graph = ModuleGraph()

    fun <M : Module> bind(kclass: KClass<M>, module: M): GraphPosition = TODO()

    fun <M : Module> unbind(kclass: KClass<M>, module: M) {
        TODO()
    }
}

internal inline fun <reified M : Module> DependencyGraph.bind(module: M): GraphPosition = bind(M::class, module)

internal inline fun <reified M : Module> DependencyGraph.unbind(module: M) = unbind(M::class, module)

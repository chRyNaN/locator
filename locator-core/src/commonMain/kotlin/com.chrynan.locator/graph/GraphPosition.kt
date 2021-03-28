@file:Suppress("unused")

package com.chrynan.locator.graph

import com.chrynan.locator.Module
import kotlin.reflect.KClass

data class GraphPosition internal constructor(
    internal val node: ModuleNode
)

@Suppress("UNCHECKED_CAST")
internal fun <M : Module> GraphPosition.findModule(kclass: KClass<M>): M = node.findModule(kclass = kclass)

internal inline fun <reified M : Module> GraphPosition.findModule(): M = node.findModule()

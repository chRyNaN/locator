package com.chrynan.locator

import kotlin.reflect.KClass

@PublishedApi
internal object DependencyGraph {

    val modules: MutableMap<KClass<*>, Module> = mutableMapOf()

    inline fun <reified M : Module> getModule(): M {
        val module =
            modules[M::class] ?: throw ModuleNotInitializedException("Module not found for KClass = ${M::class}")

        return module as? M ?: throw ModuleClassCastException(M::class)
    }

    inline fun <reified M : Module> attachModule(module: M) {
        modules[M::class] = module
    }

    inline fun <reified M : Module> detachModule() {
        modules.remove(M::class)
    }
}
@file:Suppress("unused")

package com.chrynan.locator.graph

import com.chrynan.locator.Module
import kotlin.reflect.KClass

internal interface ModuleNode {

    var parent: ModuleNode?

    val children: MutableSet<ModuleNode>

    val modules: MutableSet<Module>
}

internal class ModuleNodeImpl : ModuleNode {

    override var parent: ModuleNode? = null

    override val children: MutableSet<ModuleNode> = mutableSetOf()

    override val modules: MutableSet<Module> = mutableSetOf()
}

internal fun ModuleNode(
    parent: ModuleNode? = null,
    children: Set<ModuleNode> = emptySet(),
    modules: Set<Module> = emptySet()
): ModuleNode {
    val module = ModuleNodeImpl()
    module.parent = parent
    module.children.addAll(children)
    module.modules.addAll(modules)
    return module
}

internal val ModuleNode.siblings: Set<ModuleNode>
    get() = parent?.children?.filterNot { it == this }?.toSet() ?: emptySet()

internal fun ModuleNode.attachToParent(parent: ModuleNode) {
    parent.attachChild(this)
}

internal fun ModuleNode.attachChild(child: ModuleNode) {
    child.detachFromParent()

    val added = children.add(child)

    if (added) {
        child.parent = this
    }
}

internal fun ModuleNode.detachFromParent() {
    parent?.children?.remove(this)
    parent = null
}

internal fun ModuleNode.detachChild(child: ModuleNode) {
    val removed = children.remove(child)

    if (removed) {
        child.parent = null
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <M : Module> ModuleNode.findModule(kclass: KClass<M>): M {
    var currentNode: ModuleNode? = this

    do {
        val module = currentNode?.modules?.firstOrNull { it::class == kclass }

        if (module != null) return module as M

        currentNode = currentNode?.parent
    } while (currentNode?.parent != null)

    throw error("No Module found for kclass = $kclass")
}

internal inline fun <reified M : Module> ModuleNode.findModule(): M = findModule(M::class)

package com.chrynan.locator

import com.chrynan.locator.graph.DependencyGraph

interface Module

inline fun <reified M : Module> dependencyGraph(): M = DependencyGraph.getModule()

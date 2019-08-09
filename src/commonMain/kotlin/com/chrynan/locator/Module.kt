package com.chrynan.locator

interface Module

inline fun <reified M : Module> dependencyGraph(): M = DependencyGraph.getModule()
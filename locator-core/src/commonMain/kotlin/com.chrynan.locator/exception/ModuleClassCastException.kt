package com.chrynan.locator.exception

import kotlin.reflect.KClass

data class ModuleClassCastException(val kClass: KClass<*>) :
    ClassCastException("Module does not implement KClass = $kClass")

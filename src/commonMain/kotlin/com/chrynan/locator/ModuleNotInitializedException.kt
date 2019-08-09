package com.chrynan.locator

class ModuleNotInitializedException(message: String? = null) :
    RuntimeException("Module was not initialized. Message = $message")
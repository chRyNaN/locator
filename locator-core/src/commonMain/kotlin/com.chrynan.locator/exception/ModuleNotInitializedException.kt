package com.chrynan.locator.exception

class ModuleNotInitializedException(message: String? = null) :
    RuntimeException("Module was not initialized. Message = $message")

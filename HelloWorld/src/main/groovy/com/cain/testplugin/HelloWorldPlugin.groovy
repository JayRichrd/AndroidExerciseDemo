package com.cain.testplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class HelloWorldPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        println "Hello World, I am from gradle plugin"
    }
}
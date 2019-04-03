package com.tencent.qgame.cain.lifecycle

import org.gradle.api.Plugin
import org.gradle.api.Project

class LifeCyclePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println "Hello World, I am from life cycle plugin."
    }
}
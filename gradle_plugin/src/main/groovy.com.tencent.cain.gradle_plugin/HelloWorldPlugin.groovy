package groovy.com.tencent.cain.gradle_plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class HelloWorldPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        println "Hello World"
    }
}
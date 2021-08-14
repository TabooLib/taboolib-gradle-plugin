package io.izzel.taboolib.gradle;

import org.gradle.api.Project;

import java.io.InputStream;

/**
 * taboolib-gradle-plugin
 * io.izzel.taboolib.gradle.KotlinFactory
 *
 * @author sky
 * @since 2021/8/14 1:43 下午
 */
public class Bridge {

    public static OptimizeFileReader newOptimizeFileReader(Project project, InputStream inputStream) {
        return new OptimizeFileReader(project, inputStream);
    }
}

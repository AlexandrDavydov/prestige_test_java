package com.prestige.utils;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class TestFinderUtils {

    public static List<MethodDeclaration> findTestMethods(Path root) throws IOException {
        List<MethodDeclaration> methods = new ArrayList<>();

        Files.walk(root)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        CompilationUnit cu = StaticJavaParser.parse(path);

                        cu.findAll(MethodDeclaration.class).stream()
                                .filter(method -> method.isAnnotationPresent("Test"))
                                .forEach(methods::add);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        return methods;
    }

    public static void main(String[] args) throws IOException {
        Path testDir = Paths.get("src/test/java");

        List<MethodDeclaration> methods = findTestMethods(testDir);

        for (MethodDeclaration method : methods) {
            System.out.println(method.getNameAsString());
            System.out.println(method);
            System.out.println("--------------------");
        }
    }
}
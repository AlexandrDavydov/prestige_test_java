package com.prestige.utils;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.prestige.models.TestDescription;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.prestige.utils.TestFinderUtils.findTestMethods;

public class GetTestSuit {
    static ArrayList<String> words_to_remove = new ArrayList<>(List.of("uiTestFragments."));
    public static class Main {
        public static void main(String[] args) throws IOException {
            Path testDir = Paths.get("src/test/java/com/prestige");


            List<MethodDeclaration> methods = findTestMethods(testDir);
            List<TestDescription>  testsDescription = new ArrayList<>();

            for (MethodDeclaration method : methods) {
                String methodName = method.getNameAsString();

                TestDescription step = TestDescription.builder()
                        .testId(methodName.substring(methodName.indexOf("test_") + 5, methodName.lastIndexOf("_")))
                        .testName(methodName.substring(methodName.lastIndexOf("_")+1))
                        .testSteps(getSteps(method))
                        .build();

                testsDescription.add(step);
            }
            HtmlReportGenerator.generateHtmlReport(testsDescription, "testSuite.html");
        }
        private static ArrayList<String> getSteps(MethodDeclaration method){
            String bodyText = method.getBody().toString();
            ArrayList<String> bodyLinesList = new ArrayList<>();
            Collections.addAll(bodyLinesList, bodyText.split("\\r?\\n"));
            bodyLinesList = new ArrayList<>(bodyLinesList.subList(1, bodyLinesList.size() - 1));
            ArrayList<String> steps = new ArrayList<>();
            for (String s : bodyLinesList) {
                if (s.endsWith(";")){
                    s=s.substring(0,s.length()-1);
                }
                steps.add(removeWords(s).trim());
            }
            return steps;
        }

        private static String removeWords(String line) {
            for (int i = 0; i < words_to_remove.size(); i++) {
                line = line.replaceAll(words_to_remove.get(i), "");
            }
            return line;
        }
    }
}

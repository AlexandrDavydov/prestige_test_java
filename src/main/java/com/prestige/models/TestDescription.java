package com.prestige.models;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder()
public class TestDescription {
    String testId;
    String testName;
    ArrayList<String> testSteps = new ArrayList<>();
}

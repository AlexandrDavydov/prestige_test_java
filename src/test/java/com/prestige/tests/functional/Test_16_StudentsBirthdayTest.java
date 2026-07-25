package com.prestige.tests.functional;

import com.prestige.adapters.DbAdapter;
import com.prestige.base.BaseTest;
import com.prestige.models.Student;
import com.prestige.pages.AddStudentPage;
import com.prestige.pages.DashboardPage;
import com.prestige.pages.StudentsPage;
import com.prestige.utils.DataUtils;
import com.prestige.utils.StudentFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.ResourceLock;

import static com.prestige.tests.TestGroups.LOCK_STUDENT;
import static com.prestige.tests.TestGroups.STUDENT;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test_16_StudentsBirthdayTest extends BaseTest {
    Student studentBirthdayYesterdayData;
    Student studentBirthdayTodayData;
    Student studentBirthdayTomorrowData;

    @Test
    @Tag(STUDENT)
    public void test_16_StudentBirthday() {
        uiTestFragments.login();
        checkStudentsWhoseBirthdayIsVisibleOnTheMainPage();
    }

    @BeforeEach
    void beforeTest() {
        createStudentHoHadBirthdayYesterdayUsingDbAdapter();
        createStudentHoHasBirthdayTodayUsingDbAdapter();
        createStudentHoWillHaveBirthdayTomorrowUsingDbAdapter();
    }

    public void createStudentHoWillHaveBirthdayTomorrowUsingDbAdapter(){
        studentBirthdayTomorrowData = StudentFactory.createRandomStudent();
        studentBirthdayTomorrowData.setBirthday(DataUtils.getFutureDate(+1,"yyyy-MM-dd"));
        studentBirthdayTomorrowData.setId(new DbAdapter().addStudent(studentBirthdayTomorrowData));
        testData.addStudent(studentBirthdayTomorrowData);
    }

    public void createStudentHoHasBirthdayTodayUsingDbAdapter(){
        studentBirthdayTodayData = StudentFactory.createRandomStudent();
        studentBirthdayTodayData.setBirthday(DataUtils.getFutureDate(0,"yyyy-MM-dd"));
        studentBirthdayTodayData.setId(new DbAdapter().addStudent(studentBirthdayTodayData));
        testData.addStudent(studentBirthdayTodayData);
    }

    public void createStudentHoHadBirthdayYesterdayUsingDbAdapter(){
        studentBirthdayYesterdayData = StudentFactory.createRandomStudent();
        studentBirthdayYesterdayData.setBirthday(DataUtils.getFutureDate(-1,"yyyy-MM-dd"));
        studentBirthdayYesterdayData.setId(new DbAdapter().addStudent(studentBirthdayYesterdayData));
        testData.addStudent(studentBirthdayYesterdayData);
    }

    public void checkStudentsWhoseBirthdayIsVisibleOnTheMainPage(){
        DashboardPage dashboardPage = new DashboardPage(page);
        dashboardPage.checkStudentWithBirthdayYesterdayPresent(studentBirthdayYesterdayData.getFirstAndLastName());
        dashboardPage.checkStudentWithBirthdayTodayPresent(studentBirthdayTodayData.getFirstAndLastName());
        dashboardPage.checkStudentWithBirthdayTomorrowPresent(studentBirthdayTomorrowData.getFirstAndLastName());
    }
}
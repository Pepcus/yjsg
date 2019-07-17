package com.pepcus.appstudent.entity;

import com.opencsv.bean.CsvBindByName;

/**
 * class used to wrapped CSV data
 */
public class StudentWrapper {

    @CsvBindByName
    private String id;
    @CsvBindByName
    private String day1;
    @CsvBindByName
    private String day2;
    @CsvBindByName
    private String day3;
    @CsvBindByName
    private String day4;
    @CsvBindByName
    private String day5;
    @CsvBindByName
    private String day6;
    @CsvBindByName
    private String day7;
    @CsvBindByName
    private String day8;

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "father_name")
    private String fatherName;

    @CsvBindByName(column = "gender")
    private String gender;

    @CsvBindByName(column = "age")
    private String age;

    @CsvBindByName(column = "education")
    private String education;

    @CsvBindByName(column = "occupation")
    private String occupation;

    @CsvBindByName
    private String motherMobile;

    @CsvBindByName(column = "father_mobile")
    private String mobile;

    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "address")
    private String address;

    @CsvBindByName(column = "bus_num")
    private String busNumber;

    @CsvBindByName(column = "bus_stop")
    private String busStop;

    @CsvBindByName
    private String printStatus;

    @CsvBindByName
    private String remark;

    @CsvBindByName(column = "class_attended_2016")
    private String classAttended2016;

    @CsvBindByName(column = "class_attended_2017")
    private String classAttended2017;

    @CsvBindByName(column = "class_attended_2018")
    private String classAttended2018;

    @CsvBindByName(column = "class_attended_2019")
    private String classAttended2019;

    @CsvBindByName(column = "class_room_no_2016")
    private String classRoomNo2016;

    @CsvBindByName(column = "class_room_no_2017")
    private String classRoomNo2017;

    @CsvBindByName(column = "class_room_no_2018")
    private String classRoomNo2018;

    @CsvBindByName(column = "class_room_no_2019")
    private String classRoomNo2019;

    @CsvBindByName(column = "attendance_2016")
    private String attendance2016;

    @CsvBindByName(column = "attendance_2017")
    private String attendance2017;

    @CsvBindByName(column = "attendance_2018")
    private String attendance2018;

    @CsvBindByName(column = "attendance_2019")
    private String attendance2019;

    @CsvBindByName(column = "marks_ 2016")
    private String marks2016;

    @CsvBindByName(column = "marks_2017")
    private String marks2017;

    @CsvBindByName(column = "marks_2018")
    private String marks2018;

    @CsvBindByName(column = "marks_2019")
    private String marks2019;

    @CsvBindByName
    private String optIn2018;

    @CsvBindByName(column = "Y/N")
    private String optIn2019;

    @CsvBindByName
    private String reprint;

    @CsvBindByName
    private String secret_key;

    @CsvBindByName
    private String created_date;

    @CsvBindByName
    private String last_modified_date;

    public String getReprint() {
        return reprint;
    }

    public void setReprint(String reprint) {
        this.reprint = reprint;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getLast_modified_date() {
        return last_modified_date;
    }

    public void setLast_modified_date(String last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public String getDay8() {
        return day8;
    }

    public void setDay8(String day8) {
        this.day8 = day8;
    }

    public String getOptIn2019() {
        return optIn2019;
    }

    public void setOptIn2019(String optIn2019) {
        this.optIn2019 = optIn2019;
    }

    public StudentWrapper() {
        super();
    }

    public StudentWrapper(String id, String day1, String day2) {
        super();
        this.id = id;
        this.day1 = day1;
        this.day2 = day2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay1() {
        return day1;
    }

    public void setDay1(String day1) {
        this.day1 = day1;
    }

    public String getDay2() {
        return day2;
    }

    public void setDay2(String day2) {
        this.day2 = day2;
    }

    public String getDay3() {
        return day3;
    }

    public void setDay3(String day3) {
        this.day3 = day3;
    }

    public String getDay4() {
        return day4;
    }

    public void setDay4(String day4) {
        this.day4 = day4;
    }

    public String getDay5() {
        return day5;
    }

    public void setDay5(String day5) {
        this.day5 = day5;
    }

    public String getDay6() {
        return day6;
    }

    public void setDay6(String day6) {
        this.day6 = day6;
    }

    public String getDay7() {
        return day7;
    }

    public void setDay7(String day7) {
        this.day7 = day7;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMotherMobile() {
        return motherMobile;
    }

    public void setMotherMobile(String motherMobile) {
        this.motherMobile = motherMobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusStop() {
        return busStop;
    }

    public void setBusStop(String busStop) {
        this.busStop = busStop;
    }

    public String getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(String printStatus) {
        this.printStatus = printStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getClassAttended2016() {
        return classAttended2016;
    }

    public void setClassAttended2016(String classAttended2016) {
        this.classAttended2016 = classAttended2016;
    }

    public String getClassAttended2017() {
        return classAttended2017;
    }

    public void setClassAttended2017(String classAttended2017) {
        this.classAttended2017 = classAttended2017;
    }

    public String getClassAttended2018() {
        return classAttended2018;
    }

    public void setClassAttended2018(String classAttended2018) {
        this.classAttended2018 = classAttended2018;
    }

    public String getClassAttended2019() {
        return classAttended2019;
    }

    public void setClassAttended2019(String classAttended2019) {
        this.classAttended2019 = classAttended2019;
    }

    public String getClassRoomNo2016() {
        return classRoomNo2016;
    }

    public void setClassRoomNo2016(String classRoomNo2016) {
        this.classRoomNo2016 = classRoomNo2016;
    }

    public String getClassRoomNo2017() {
        return classRoomNo2017;
    }

    public void setClassRoomNo2017(String classRoomNo2017) {
        this.classRoomNo2017 = classRoomNo2017;
    }

    public String getClassRoomNo2018() {
        return classRoomNo2018;
    }

    public void setClassRoomNo2018(String classRoomNo2018) {
        this.classRoomNo2018 = classRoomNo2018;
    }

    public String getClassRoomNo2019() {
        return classRoomNo2019;
    }

    public void setClassRoomNo2019(String classRoomNo2019) {
        this.classRoomNo2019 = classRoomNo2019;
    }

    public String getAttendance2016() {
        return attendance2016;
    }

    public void setAttendance2016(String attendance2016) {
        this.attendance2016 = attendance2016;
    }

    public String getAttendance2017() {
        return attendance2017;
    }

    public void setAttendance2017(String attendance2017) {
        this.attendance2017 = attendance2017;
    }

    public String getAttendance2018() {
        return attendance2018;
    }

    public void setAttendance2018(String attendance2018) {
        this.attendance2018 = attendance2018;
    }

    public String getAttendance2019() {
        return attendance2019;
    }

    public void setAttendance2019(String attendance2019) {
        this.attendance2019 = attendance2019;
    }

    public String getMarks2016() {
        return marks2016;
    }

    public void setMarks2016(String marks2016) {
        this.marks2016 = marks2016;
    }

    public String getMarks2017() {
        return marks2017;
    }

    public void setMarks2017(String marks2017) {
        this.marks2017 = marks2017;
    }

    public String getMarks2018() {
        return marks2018;
    }

    public void setMarks2018(String marks2018) {
        this.marks2018 = marks2018;
    }

    public String getMarks2019() {
        return marks2019;
    }

    public void setMarks2019(String marks2019) {
        this.marks2019 = marks2019;
    }

    public String getOptIn2018() {
        return optIn2018;
    }

    public void setOptIn2018(String optIn2018) {
        this.optIn2018 = optIn2018;
    }

    @Override
    public String toString() {
        return "StudentUploadAttendance [id=" + id + ", day1=" + day1 + ", day2=" + day2 + ", day3=" + day3 + ", day4="
                + day4 + ", day5=" + day5 + ", day6=" + day6 + ", day7=" + day7 + "]";
    }

}

package com.myservice.example.users;

public class UserInfo {

    private String name;

    private boolean doneTesting;
    private boolean doneRightTesting;


    public UserInfo(String name, boolean doneTesting, boolean doneRightTesting) {
        this.name = name;
        this.doneTesting = doneTesting;
        this.doneRightTesting = doneRightTesting;
    }

    public boolean isDoneTesting() {
        return doneTesting;
    }

    void setDoneTesting() {
        this.doneTesting = true;
    }

    public boolean isDoneRightTesting() {
        return doneRightTesting;
    }

    void setDoneRightTesting(boolean doneRightTesting) {
        this.doneRightTesting = doneRightTesting;
    }

    public String getName() {
        return name;
    }

}

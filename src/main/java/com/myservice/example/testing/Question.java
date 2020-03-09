package com.myservice.example.testing;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Question {

    private Integer id;

    @JsonIgnore
    private int q_type;

    private String question;

    @JsonIgnore
    private String right_answer;

    public Question(int id, int q_type, String question, String right_answer) {
        this.id = id;
        this.q_type = q_type;
        this.question = question;
        this.right_answer = right_answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQ_type() {
        return q_type;
    }

    public void setQ_type(int q_type) {
        this.q_type = q_type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRight_answer() {
        return right_answer;
    }

    public void setRight_answer(String right_answer) {
        this.right_answer = right_answer;
    }
}

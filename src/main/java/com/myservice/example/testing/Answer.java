package com.myservice.example.testing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Answer {

    @JsonIgnore
    private int id;

    private String answer;

    private int question_id;

    @JsonIgnore
    private String right_answer;

    public Answer (int id, String answer, int question_id, String right_answer) {
        this.id = id;
        this.answer = answer;
        this.question_id = question_id;
        this.right_answer = right_answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getRight_answer() {
        return right_answer;
    }

    public void setRight_answer(String  right_answer) {
        this.right_answer = right_answer;
    }
}

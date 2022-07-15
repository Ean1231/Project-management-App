package com.example.pimoscanner;

import android.widget.ImageView;

public class QuestionModel {
    private String questions, c1, c2, c3, c4, c5;
    private int correctAnsno;
    static ImageView mImageView;

    public QuestionModel(String questions, String c1, String c2, String c3, String c4, String c5, int correctAnsno, ImageView mImageView) {
        this.questions = questions;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.c4 = c4;
        this.c5 = c5;
        this.correctAnsno = correctAnsno;
        this.mImageView = mImageView;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public void setmImageView(ImageView mImageView) {
        this.mImageView = mImageView;
    }

    public ImageView getmIamgeView() {
        return mImageView;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public String getC3() {
        return c3;
    }

    public void setC3(String c3) {
        this.c3 = c3;
    }

    public String getC4() {
        return c4;
    }

    public void setC4(String c4) {
        this.c4 = c4;
    }

    public String getC5() {
        return c5;
    }

    public void setC5(String c5) {
        this.c5 = c5;
    }

    public int getCorrectAnsno() {
        return correctAnsno;
    }

    public void setCorrectAnsno(int correctAnsno) {
        this.correctAnsno = correctAnsno;
    }


}

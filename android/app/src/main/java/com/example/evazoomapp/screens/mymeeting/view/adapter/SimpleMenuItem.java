package com.example.evazoomapp.screens.mymeeting.view.adapter;


public class SimpleMenuItem {

    private int mAction = 0;
    private String mLabel;

    public SimpleMenuItem() {

    }

    public SimpleMenuItem(int action, String label) {
        mAction = action;
        mLabel = label;
    }

    @Override
    public String toString() {
        return mLabel;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public int getAction() {
        return mAction;
    }

    public void setAction(int action) {
        mAction = action;
    }

}
package com.inheritx.standardedittext;

public interface SimpleTextChangedWatcher {
    // newText : Current Text
    // isError : error state
    void onTextChanged(String newText, boolean isError);
}

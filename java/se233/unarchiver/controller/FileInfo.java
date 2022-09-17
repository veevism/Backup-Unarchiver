package se233.unarchiver.controller;

import javafx.collections.ObservableList;

public class FileInfo {

//    private final String name;
    private final ObservableList<String> tranfercontent;

    public FileInfo (ObservableList<String> tranfercontent) {
//        this.name = name;
        this.tranfercontent = tranfercontent;
    }

//    public String getName() {
//        return name;
//    }

    public ObservableList<String> getTranfercontent() {
        return tranfercontent;
    }
}

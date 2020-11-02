package com.t12.prototype;

public class Person_albumtab {
    String folderName;
    String count;

    public Person_albumtab(String folderName, String count) {
        this.folderName = folderName;
        this.count = count;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}

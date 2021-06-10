package com.example.linkagelayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 下方数据
 */
public class Entity {
    private String leftTitle;
    private List<String> rightDatas;

    public String getLeftTitle() {
        return leftTitle == null ? "" : leftTitle;
    }

    public List<String> getRightDatas() {
        if (rightDatas == null) {
            return new ArrayList<>();
        }
        return rightDatas;
    }

    public void setLeftTitle(String leftTitle) {
        this.leftTitle = leftTitle;
    }

    public void setRightDatas(List<String> rightDatas) {
        this.rightDatas = rightDatas;
    }
}

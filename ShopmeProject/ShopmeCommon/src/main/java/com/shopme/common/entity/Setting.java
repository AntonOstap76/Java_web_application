package com.shopme.common.entity;

import jakarta.persistence.*;

//create an entity class for settings
@Entity
@Table(name="settings")
public class Setting {

//    name of the setting
    @Id
    @Column(name="`key`", nullable = false, length = 128)
    private String key;


//    possible value of the setting
    @Column(nullable = false, length = 1024)
    private String value;

//    for category of settings
    @Enumerated(EnumType.STRING)
    @Column(length = 45 , nullable = false)
    private SettingCategory category;

    public Setting(String key, String value, SettingCategory category) {
        this.key = key;
        this.value = value;
        this.category = category;
    }

    public Setting() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SettingCategory getSettingCategory() {
        return category;
    }

    public void setSettingCategory(SettingCategory settingCategory) {
        this.category = settingCategory;
    }

}

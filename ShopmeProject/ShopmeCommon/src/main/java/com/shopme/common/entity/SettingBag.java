package com.shopme.common.entity;

import java.util.List;

//for collections of setting and utility methods
public class SettingBag {
    private List<Setting> listSettings;

    public SettingBag(List<Setting> listSettings) {
        this.listSettings = listSettings;
    }

    public Setting get(String key){
        int index = listSettings.indexOf(new Setting(key));

        if(index>=0){
            return listSettings.get(index);
        }
        return null;
    }

    public String getValue(String key){
        Setting setting = get(key);

        if(setting != null){
            return setting.getValue();
        }
        return null;
    }

    // updating setting for a given key and value
    public void update(String key, String value){
        Setting setting = get(key);
        if(setting!=null && value !=null){
             setting.setValue(value);
        }
    }

    public List<Setting> list(){
        return listSettings;
    }

}

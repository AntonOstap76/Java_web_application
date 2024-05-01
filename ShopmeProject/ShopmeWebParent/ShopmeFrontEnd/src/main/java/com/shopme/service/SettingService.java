package com.shopme.service;


import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;


    public List<Setting> getGeneralSettings() {

        return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

}


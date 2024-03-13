package com.shopme.admin.repository.setting;


import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting,String> {

    public List<Setting> findByCategory(SettingCategory category);
}

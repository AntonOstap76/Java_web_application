package com.shopme.admin.export.user;


import com.shopme.admin.export.AbstractExporter;
import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {

    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {

        super.setResponseHeader(response, "text/csv", ".csv", "users_");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                                                        CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"User ID", "First Name", "LastName", "E-mail", "Roles", "Enabled"};
        String[] fieldMapping = {"id" , "firstName", "LastName", "email", "roles", "enabled"};
        csvWriter.writeHeader(csvHeader);

        for(User user:listUsers){
            csvWriter.write(user,fieldMapping);
        }

        csvWriter.close();
    }

}

package ua.org.nalabs.javalessons.javafx.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(String v) throws Exception {
        return LocalDateTime.parse(v);
    }

    @Override
    public String marshal(LocalDateTime v) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime birthday2 = LocalDateTime.parse(java.sql.Timestamp.valueOf(v).toString(), formatter);

//            statement.setDate(6, java.sql.Date.valueOf(birthday) );
//            statement.setTimestamp(6, java.sql.Timestamp.valueOf(birthday));

        return java.sql.Timestamp.valueOf(birthday2).toString();
    }
}

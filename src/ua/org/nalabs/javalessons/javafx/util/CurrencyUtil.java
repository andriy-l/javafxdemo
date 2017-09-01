package ua.org.nalabs.javalessons.javafx.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ua.org.nalabs.javalessons.javafx.model.NBUCurrency;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CurrencyUtil {

    public static List<String> getAllCurrencies()
    {
        Set<String> toret = new HashSet<>();
        Locale[] locs = Locale.getAvailableLocales();

        for(Locale loc : locs) {
            try {
                Currency currency = Currency.getInstance( loc );

                if ( currency != null ) {
                    toret.add( currency.getCurrencyCode() );
                }
            } catch(Exception exc)
            {
                // Locale not found
            }
        }

        return new ArrayList<>(toret);
    }


    public static double getCurrencyNow (String currencyName) { // throws MalformedURLException, IOException {
        double gbpCurrency = 0;
        String url = "http://bank.gov.ua/NBUStatService/v1/statdirectory/exchange";
//        https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json
//        GregorianCalendar gc = new GregorianCalendar();

        String charset = "UTF-8";
        HttpURLConnection httpConnection = null;
        try {
            httpConnection = (HttpURLConnection) new URL(url).openConnection();
        } catch (MalformedURLException ex) {
            Logger.getLogger(CurrencyUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CurrencyUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        httpConnection.setRequestProperty("Accept-Charset", charset);
        InputStream response = null;
        try {
            response = httpConnection.getInputStream();
        }catch (IOException ioe) {
            Logger.getLogger(CurrencyUtil.class.getName()).log(Level.SEVERE, null, ioe);
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        NodeList nodeList;
        try {
            builder = factory.newDocumentBuilder();
            try{
                document = builder.parse(new InputSource(response));
            }catch (MalformedURLException mue) {
                Logger.getLogger(CurrencyUtil.class.getName()).log(Level.SEVERE, null, mue);
            }
            nodeList = document.getElementsByTagName("currency");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node currency = nodeList.item(i);
                if (currency.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) currency;
                    if ((e.getElementsByTagName("cc").item(0).getTextContent()).equals(currencyName)) {
                        gbpCurrency = Double.parseDouble(e.getElementsByTagName("rate").item(0).getTextContent());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(CurrencyUtil.class.getName()).log(Level.SEVERE, null, e);
            // e.printStackTrace();
            gbpCurrency = -1;
        }
        return gbpCurrency;
    }

    public static double getCurrencyForDate (String currencyName, LocalDate date) { // throws MalformedURLException, IOException {
        double gbpCurrency = 0;
        int intMonth = date.getMonth().ordinal();
        String strMonth = intMonth < 10 ? "0"+intMonth : intMonth+"";
        String strDate = date.getYear()+""+strMonth+""+date.getDayOfMonth();
        String url = "http://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode="+currencyName+"&date="+strDate+"&json";
//        String url = "http://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode="+currencyName+"&date=20160516&json";
//        https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode=EUR&date=YYYYMMDD
//        https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json
//        GregorianCalendar gc = new GregorianCalendar();

        String charset = "UTF-8";
        HttpURLConnection httpConnection = null;
        try {
            httpConnection = (HttpURLConnection) new URL(url).openConnection();
        } catch (MalformedURLException ex) {
            Logger.getLogger(CurrencyUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CurrencyUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        httpConnection.setRequestProperty("Accept-Charset", charset);
        InputStream response = null;
        try {
            response = httpConnection.getInputStream();
        }catch (IOException ioe) {
            Logger.getLogger(CurrencyUtil.class.getName()).log(Level.SEVERE, null, ioe);
        }

        Gson gson = new Gson();
        try(Scanner scanner = new Scanner(response)) {

            if(scanner.hasNextLine()) {
                List<NBUCurrency> currencyList = gson.fromJson(scanner.nextLine(), new TypeToken<List<NBUCurrency>>(){}.getType());
                BigDecimal rate = currencyList.get(0).getRate();
                gbpCurrency = rate.doubleValue();
            }
        }

        return gbpCurrency;
    }

    public static void main(String[] args) {
        System.out.println(getCurrencyForDate("USD", LocalDate.of(2016, 5, 16)));
        System.out.println(getCurrencyForDate("EUR", LocalDate.of(2017, 8, 6)));
//        System.out.println(getCurrencyNow("USD"));
    }
}

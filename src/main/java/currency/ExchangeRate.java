package currency;

import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.resthub.common.exception.NotFoundException;
import java.text.ParseException;

public class ExchangeRate {
    //Since Bank API deals with another type of codes, this map is aimed to
    //reflect ISO codes into so-called Parent Codes.
    //The map is being filled once at the application start.
    static Map<String,String> codesMap;

    private final String code;
    private final String rate;
    private final String date;

    static void constructCodesMap(){
        RestTemplate restTemplate = new RestTemplate();
        String allCurrency = restTemplate.getForObject("http://www.cbr.ru/scripts/XML_valFull.asp", String.class);
        codesMap = new HashMap<String,String>();
        Matcher m = Pattern.compile("<ParentCode>(R\\d{5})    </ParentCode><ISO_Num_Code>\\d{2,3}</ISO_Num_Code><ISO_Char_Code>(\\D{3})</ISO_Char_Code>").matcher(allCurrency);
        while (m.find()) {
            codesMap.put(m.group(2),m.group(1));
        }
    }

    public ExchangeRate(String code, String date) throws IllegalArgumentException, NotFoundException{
        this.code = code;
        this.date = date;

        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date parsedDate = null;
                try {
                        parsedDate = oldDateFormat.parse(date);
                }
                catch(ParseException e){
                        throw new IllegalArgumentException("Requested date can't be parsed");
                }
                String reqDate = newDateFormat.format(parsedDate);

                String reqCode = codesMap.get(code);
                if (reqCode == null){
                                throw new IllegalArgumentException("Requested code doesn't correspond to any currency");
                }

                Map<String,String> urlVariables = new HashMap<String,String>();
                urlVariables.put("date1", reqDate);
                urlVariables.put("date2", reqDate);
                urlVariables.put("code", reqCode);

                RestTemplate restTemplate = new RestTemplate();
                String rateResponse = restTemplate.getForObject("http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1={date1}&date_req2={date2}&VAL_NM_RQ={code}", String.class, urlVariables);
                Matcher matcher = Pattern.compile("<Value>(.*)</Value>").matcher(rateResponse);
                if (!matcher.find()){
                        throw new NotFoundException("No information available for requested code and date");
                }
        this.rate = matcher.group(1);
    }

    public String getCode() {
        return code;
    }

    public String getRate() {
        return rate;
    }

    public String getDate() {
        return date;
    }

}


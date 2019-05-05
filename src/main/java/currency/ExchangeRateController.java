package currency;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.springframework.http.HttpStatus;
import org.resthub.common.exception.NotFoundException;

@RestController
public class ExchangeRateController {

    @RequestMapping(value = {"/api/rate/{code}/{date}", "/api/rate/{code}"}, method = RequestMethod.GET)
        public ExchangeRate rate
        (@PathVariable String code, @PathVariable(name = "date", required = false) String date) {
        if (date == null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = dateFormat.format(new Date());
            System.out.println("date: " + date);
        }
        return new ExchangeRate(code, date);
    }

        @ExceptionHandler(value = IllegalArgumentException.class)
        public ResponseEntity<Object>  handleIllegalArgumentException(IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        @ExceptionHandler(value = NotFoundException.class)
        public ResponseEntity<Object>  handleNotFoundException(NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
}


package com.cloud.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BirthDayWishController {

	@Autowired
	BirthDayWishService birthdayService;

	@PutMapping("/hello/{nameOfPerson}")
	private Object saveName(@RequestBody BirthdayWish birthdayWish, @PathVariable("nameOfPerson") String nameOfPerson) {
		if (!validtaeString(nameOfPerson)) {
			return "Ony";
		}
		try {
			birthdayWish.setName(nameOfPerson);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
			Date InputDate = sdf.parse(birthdayWish.getDateofBirth());
			Date today = sdf.parse(sdf.format(new Date()));
			if (InputDate.after(today)) {
				birthdayWish.setDateofBirth("");
				return "InputDate is after today";
			}
			birthdayService.saveOrUpdate(birthdayWish);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return "204 No content";
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/hello/{name}")
	private String getName(@PathVariable("name") String nameOfPerson) {
		List<BirthdayWish> birthdayWish = birthdayService.getBirthdayWish(nameOfPerson);
		String msg = "";
		if (birthdayWish.size() > 0) {
			LocalDate today = LocalDate.now();
			String bDate[] = birthdayWish.get(0).getDateofBirth().split("_", 3);
			// Today's date
			LocalDate birthday = LocalDate.of(today.getYear(), Integer.parseInt(bDate[1]), Integer.parseInt(bDate[2]));
			// Period period = Period.between(birthday, today);
			final long days = ChronoUnit.DAYS.between(today, birthday);
			if (days == 0)
				msg = "Happy Birthday " + birthdayWish.get(0).getName();
			else
				msg = "Hello " + birthdayWish.get(0).getName() + ", your next birth is on after " + days;
			if (days < 0) {
				birthday = LocalDate.of(today.getYear() + 1, Integer.parseInt(bDate[1]), Integer.parseInt(bDate[2]));
				msg = "Hello " + birthdayWish.get(0).getName() + ", your next birth is on after "
						+ ChronoUnit.DAYS.between(today, birthday);
				;
			}
		} else
			msg = " OOPS, Record Not Found";
		return msg;
	}

	public boolean validtaeString(String str) {
		str = str.toLowerCase();
		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char ch = charArray[i];
			if (!(ch >= 'a' && ch <= 'z')) {
				return false;
			}
		}
		return true;
	}
}
package com.cloud.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BirthDayWishService {

	@Autowired
	BirthDayWishRepository personRepository;

	public List<BirthdayWish> getBirthdayWish(String name) {
		List<BirthdayWish> persons = new ArrayList<BirthdayWish>();
		personRepository.findAll().forEach(birthdayWish -> {
			if (birthdayWish.getName().equalsIgnoreCase(name)) {
				persons.add(birthdayWish);
			}
		});
		return persons;
	}

	public void saveOrUpdate(BirthdayWish birthdayWish) {
		personRepository.save(birthdayWish);

	}
}

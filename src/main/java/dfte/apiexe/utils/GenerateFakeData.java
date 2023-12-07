package dfte.apiexe.utils;

import java.util.Locale;

import com.github.javafaker.Faker;

public class GenerateFakeData {
	Locale locale = new Locale("en", "US");
	Faker faker = new Faker(locale);

	public String getData(String key) {
		switch (key) {
		case "firstName":
			return faker.name().firstName();
		case "lastName":
			return faker.name().lastName();
		case "fullName":
			return faker.name().fullName();
		case "streetAddress1":
			return faker.address().streetAddress();
		case "streetAddress2":
			return faker.address().streetName();
		case "phoneNo":
			return faker.phoneNumber().phoneNumber();
		case "cityName":
			return faker.address().cityName();
		case "state":
			return faker.address().state();
		case "stateCode":
			return faker.address().stateAbbr();
		case "postalCode":
			return faker.address().zipCode();
		case "countryCode":
			return faker.address().countryCode();
		case "country":
			return faker.address().country();
		default:
			return null;

		}

	}

}

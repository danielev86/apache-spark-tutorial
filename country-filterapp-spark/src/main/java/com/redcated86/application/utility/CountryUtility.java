package com.redcated86.application.utility;

import com.redcated86.application.bean.Country;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CountryUtility {

    public static List<Country> generateCountries(){
        List<Country> countries = new ArrayList<>();
        int randomNum;
        Country country = null;
        for (int i = 0;  i<2000000; i++){
            randomNum = ThreadLocalRandom.current().nextInt(0, 6 + 1);
            switch (randomNum){
                case 0:
                    country = new Country("ITA", "Italy");
                    break;
                case 1:
                    country = new Country("UK", "United kingdom");
                    break;
                case 2:
                    country = new Country("SWE", "Sweden");
                    break;
                case 3:
                    country = new Country("GER", "Germany");
                    break;
                case 4:
                    country = new Country("ESP", "Spain");
                    break;
                case 5:
                    country = new Country("FRA", "France");
                    break;
                case 6:
                    country = new Country("SWI", "Switzerland");
                    break;
            }
            countries.add(country);
        }

        return countries;
    }

}

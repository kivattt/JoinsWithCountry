package com.kiva.joinswithcountry;

import com.fox2code.foxloader.loader.ServerMod;
import com.fox2code.foxloader.registry.CommandCompat;
import com.kiva.joinswithcountry.commands.CountryJoins;

public class JoinsWithCountryServer extends JoinsWithCountry implements ServerMod {
    public static GetCountryFromIP getCountryFromIP;

    @Override
    public void onInit() {
        getCountryFromIP = new GetCountryFromIP();

        CommandCompat.registerCommand(new CountryJoins());
        System.out.println("JoinsWithCountry initialized");
    }
}

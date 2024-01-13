package com.kiva.joinswithcountry.commands;

import com.fox2code.foxloader.network.ChatColors;
import com.fox2code.foxloader.network.NetworkPlayer;
import com.fox2code.foxloader.registry.CommandCompat;
import com.kiva.joinswithcountry.JoinsWithCountry;
import com.kiva.joinswithcountry.JoinsWithCountryServer;

public class CountryJoins extends CommandCompat {
    public CountryJoins(){
        super("countryjoins", true);
    }

    public String commandSyntax(){
        return ChatColors.YELLOW + "/countryjoins";
    }

    @Override
    public void onExecute(String[] args, NetworkPlayer commandExecutor) {
        commandExecutor.displayChatMessage(ChatColors.GREEN + "JoinsWithCountry lookup stats:");
        commandExecutor.displayChatMessage("Cache hits    " + ChatColors.GRAY + " : " + JoinsWithCountryServer.getCountryFromIP.cacheHits);
        commandExecutor.displayChatMessage("Web requests" + ChatColors.GRAY + " : " + JoinsWithCountryServer.getCountryFromIP.webRequests);
        commandExecutor.displayChatMessage("IPs cached   " + ChatColors.GRAY + " : " + JoinsWithCountryServer.getCountryFromIP.getNCachedIPs());
    }
}

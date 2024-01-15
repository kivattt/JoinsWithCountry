package com.kiva.joinswithcountry;

import com.fox2code.foxloader.loader.ServerMod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketAddress;
import java.net.URL;
import java.util.HashMap;

public class GetCountryFromIP {
    private HashMap<String, String> ipCountryCache = new HashMap<>();
    public int cacheHits = 0, webRequests = 0;

    public int getNCachedIPs(){
        return ipCountryCache.size();
    }

    public String socketAddressToIP(final SocketAddress socketAddress){
        final String address = socketAddress.toString();
        return address.substring(1, address.indexOf(':'));
    }

    // Only allow a-z, A-Z and ' '
    public String sanitizeCountryName(final String countryName){
        StringBuilder ret = new StringBuilder();

        final String countryNameLower = countryName.toLowerCase();
        String allowedChars = "abcdefghijklmnopqrstuvwxyz ";
        for (int i = 0; i < countryName.length(); i++){
            if (allowedChars.indexOf(countryNameLower.charAt(i)) == -1)
                continue;

            ret.append(countryName.charAt(i));
        }

        return ret.toString();
    }

    public String getCountryFromIP(final SocketAddress ipAddress){
        if (this.ipCountryCache.containsKey(socketAddressToIP(ipAddress))) {
            ServerMod.getGameInstance().log(JoinsWithCountry.logPrefix + "IP: " + socketAddressToIP(ipAddress) + ", Country: " + ipCountryCache.get(socketAddressToIP(ipAddress)) + ", from cache");
            ++cacheHits;
            return ipCountryCache.get(socketAddressToIP(ipAddress));
        }

        ++webRequests;

        String countryFound = "";

        final String jsonKeyForCountryName = "  \"geoplugin_countryName\":\"";

        try {
            // 120 Requests per minute cap
            URL url = new URL("http://www.geoplugin.net/json.gp?ip=" + socketAddressToIP(ipAddress));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;

            // Hacky parsing
            while (true){
                try {
                    line = bufferedReader.readLine();
                } catch(IOException e){
                    break;
                }

                if (line == null)
                    break;

                int countryNameIdx = line.indexOf(jsonKeyForCountryName);
                if (countryNameIdx == -1)
                    continue;

                countryFound = line.substring(countryNameIdx + jsonKeyForCountryName.length(), line.length() - 2);
                break;
            }
        } catch(Exception e){
        }

        countryFound = sanitizeCountryName(countryFound);
        ipCountryCache.put(socketAddressToIP(ipAddress), countryFound);

        ServerMod.getGameInstance().log(JoinsWithCountry.logPrefix + "IP: " + socketAddressToIP(ipAddress) + ", Country: " + countryFound + ", from web");
        return countryFound;
    }
}

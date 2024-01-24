package com.kiva.joinswithcountry.server.mixins;

import com.fox2code.foxloader.loader.ServerMod;
import com.fox2code.foxloader.network.ChatColors;
import com.kiva.joinswithcountry.JoinsWithCountryServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.server.ServerConfigurationManager;
import net.minecraft.src.server.packets.NetLoginHandler;
import net.minecraft.src.server.packets.NetworkManager;
import net.minecraft.src.server.packets.Packet;
import net.minecraft.src.server.packets.Packet3Chat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.net.SocketAddress;

@Mixin(NetLoginHandler.class)
public class MixinNetLoginHandler {
    @Shadow private MinecraftServer mcServer;

    @Shadow public NetworkManager netManager;

    @Redirect(method = "doLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/src/server/ServerConfigurationManager;sendPacketToAllPlayers(Lnet/minecraft/src/server/packets/Packet;)V"))
    public void onSendJoinMsg(ServerConfigurationManager instance, Packet i){
        // A single player wouldn't see their join message anyway, don't waste possible api lookups to this
        if (ServerMod.getOnlineNetworkPlayers().isEmpty())
            return;

        String username = ((Packet3Chat)i).message;
        final int usernameEndIdx = username.indexOf(' ');
        // Fallback to the default join message if ' ' is not found. Should never happen
        if (usernameEndIdx == -1){
            this.mcServer.configManager.sendPacketToAllPlayers(i);
            return;
        }

        // Hacky way to get the username
        username = username.substring(2, usernameEndIdx);
        final SocketAddress ipAddress = this.netManager.getRemoteAddress();
        final String country = JoinsWithCountryServer.getCountryFromIP.getCountryFromIP(ipAddress);

        this.mcServer.configManager.sendPacketToAllPlayers(new Packet3Chat(ChatColors.YELLOW + username + " joined from " + country));
    }
}

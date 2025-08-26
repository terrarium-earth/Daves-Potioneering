package tfar.davespotioneering.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import tfar.davespotioneering.client.GauntletHUDCommon;

import java.util.stream.IntStream;

public class ClientPacketHandler {

    public static void registerClientMessages() {
        ClientPlayNetworking.registerGlobalReceiver(S2CGauntletCooldownsPacket.PACKET_TYPE, (packet, context) -> S2CGauntletCooldownsPacket.apply(packet));
    }
}

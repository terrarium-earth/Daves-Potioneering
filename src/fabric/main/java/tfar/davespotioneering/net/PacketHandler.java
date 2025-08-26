package tfar.davespotioneering.net;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class PacketHandler {
    public static void registerPayloadTypes() {
        PayloadTypeRegistry.playS2C().register(C2SGauntletCyclePacket.PACKET_TYPE, C2SGauntletCyclePacket.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(C2SPotionInjectorPacket.PACKET_TYPE, C2SPotionInjectorPacket.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(S2CGauntletCooldownsPacket.PACKET_TYPE, S2CGauntletCooldownsPacket.STREAM_CODEC);
    }

    public static void registerMessages() {
        ServerPlayNetworking.registerGlobalReceiver(C2SGauntletCyclePacket.PACKET_TYPE, (packet, context) -> C2SGauntletCyclePacket.apply(context.player(), packet));
        ServerPlayNetworking.registerGlobalReceiver(C2SPotionInjectorPacket.PACKET_TYPE, (packet, context) -> C2SPotionInjectorPacket.apply(context.player(), packet));
    }
}

package tfar.davespotioneering.net;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import tfar.davespotioneering.DavesPotioneering;

@EventBusSubscriber(modid = DavesPotioneering.MODID, bus = EventBusSubscriber.Bus.MOD)
public class PacketHandler {

    @SubscribeEvent
    public static void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                C2SGauntletCyclePacket.PACKET_TYPE,
                C2SGauntletCyclePacket.STREAM_CODEC,
                (packet, context) -> C2SGauntletCyclePacket.apply((ServerPlayer) context.player(), packet)
        );

        registrar.playToServer(
                C2SPotionInjectorPacket.PACKET_TYPE,
                C2SPotionInjectorPacket.STREAM_CODEC,
                (packet, context) -> C2SPotionInjectorPacket.apply((ServerPlayer) context.player(), packet)
        );

        registrar.playToClient(
                S2CGauntletCooldownsPacket.PACKET_TYPE,
                S2CGauntletCooldownsPacket.STREAM_CODEC,
                (packet, context) -> S2CGauntletCooldownsPacket.apply(packet)
        );
    }
}

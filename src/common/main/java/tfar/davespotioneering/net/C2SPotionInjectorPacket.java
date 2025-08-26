package tfar.davespotioneering.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import tfar.davespotioneering.DavesPotioneering;
import tfar.davespotioneering.menu.CPotionInjectorMenu;

public record C2SPotionInjectorPacket(int button) implements CustomPacketPayload {
    public static final ResourceLocation ID = DavesPotioneering.id("potion_injector");
    public static final Type<C2SPotionInjectorPacket> PACKET_TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, C2SPotionInjectorPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            C2SPotionInjectorPacket::button,
            C2SPotionInjectorPacket::new
    );

    public static void apply(ServerPlayer player, C2SPotionInjectorPacket packet) {
        if (player == null) return;
        if (player.containerMenu instanceof CPotionInjectorMenu container) {
            container.handleButton(packet.button());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}

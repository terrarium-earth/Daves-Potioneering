package tfar.davespotioneering.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import tfar.davespotioneering.DavesPotioneering;
import tfar.davespotioneering.item.CGauntletItem;

public record C2SGauntletCyclePacket(boolean up) implements CustomPacketPayload {
    public static final ResourceLocation ID = DavesPotioneering.id("gauntlet_cycle");
    public static final Type<C2SGauntletCyclePacket> PACKET_TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, C2SGauntletCyclePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            C2SGauntletCyclePacket::up,
            C2SGauntletCyclePacket::new
    );

    public static void apply(ServerPlayer player, C2SGauntletCyclePacket packet) {
        player.getServer().execute(() -> {
            if (packet.up()) {
                CGauntletItem.cycleGauntletForward(player);
            } else {
                CGauntletItem.cycleGauntletBackward(player);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}

package wolforce.greygoo;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod("greygoo")
public class GreyGoo {

	public static final String grey_goo_regName = "greygoo:grey_goo";

	@ObjectHolder(grey_goo_regName)
	public static GreyGooBlock grey_goo;

	public GreyGoo() {
		Config.init();
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	static class RegistryEvents {
		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			grey_goo = new GreyGooBlock(Block.Properties.copy(Blocks.STONE).randomTicks().strength(5));
			grey_goo.setRegistryName(grey_goo_regName);
			event.getRegistry().register(grey_goo);
		}

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			BlockItem blockItem = new BlockItem(grey_goo, new Item.Properties());
			blockItem.setRegistryName("greygoo:grey_goo");
			event.getRegistry().register(blockItem);
		}
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
	static class ModEvents {
		@SubscribeEvent
		public static void playerGoo(LivingUpdateEvent event) {
			if (event.getEntityLiving().level.isClientSide)
				return;

			if (Math.random() < Config.getPlayerSpreadChance() && event.getEntityLiving() instanceof ServerPlayer player) {
				if (player.isCreative())
					return;
				GreyGoo.grey_goo.spread(player.level, player.blockPosition().below(2));
			}
		}
	}
}

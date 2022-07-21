package wolforce.greygoo;

import static wolforce.greygoo.GreyGoo.grey_goo;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class GreyGooBlock extends Block {

	private static final int UPDATE = Block.UPDATE_CLIENTS;

	public GreyGooBlock(Properties props) {
		super(props);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (entity instanceof LivingEntity)
			entity.hurt(DamageSource.WITHER, 1.0F);
		super.stepOn(level, pos, state, entity);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (world.isClientSide)
			return;

		spread(world, pos.relative(Direction.getRandom(random)));
		if (world.getBlockState(pos.above()).isAir()) {
			boolean isSurrounded = true;
			for (Direction dir : Direction.values()) {
				BlockState dirState = world.getBlockState(pos.relative(dir));
				if (!dirState.isAir() && dirState.getBlock() != grey_goo)
					isSurrounded = false;
			}

			if (isSurrounded) {

				int update = Math.random() < .01 ? Block.UPDATE_ALL : Block.UPDATE_CLIENTS;

				if (Config.makesPillars()) {
					float pillarHeight = shouldMakePillar(pos);

					if (pillarHeight > 0 && world.getBlockState(pos.above()).isAir()) {

						if (pos.getY() > pillarHeight)
							world.setBlock(pos, Blocks.AIR.defaultBlockState(), update);
						if (pos.getY() < pillarHeight)
							world.setBlock(pos.above(), defaultBlockState(), update);

					} else
						world.setBlock(pos, Blocks.AIR.defaultBlockState(), update);

				} else
					world.setBlock(pos, Blocks.AIR.defaultBlockState(), update);
				return;
			}
		}

	}

	//
	//

	float shouldMakePillar(BlockPos pos) {

		Random r = new Random((pos.getX() + "" + pos.getZ()).hashCode());

		if (r.nextDouble() > .01)
			return 0;

		int pillarh1 = (int) (100 + r.nextDouble() * 30);
		int pillarh2 = (int) (100 + r.nextDouble() * 15);
		int pillarh3 = (int) (100 + r.nextDouble() * 5);

		int d = Config.pillarDistance();
		int dx = d;
		int dz = d;

		int x = pos.getX();
		int z = pos.getZ();
		if (x % dx == 0 && z % dz == 0)
			return pillarh1;

		//

		if ((x + 1) % dx == 0 && z % dz == 0)
			return pillarh2;

		if ((x - 1) % dx == 0 && z % dz == 0)
			return pillarh2;

		if (x % dx == 0 && (z + 1) % dz == 0)
			return pillarh2;

		if (x % dx == 0 && (z - 1) % dz == 0)
			return pillarh2;

		//

		if ((x - 1) % dx == 0 && (z - 1) % dz == 0)
			return pillarh3;

		if ((x + 1) % dx == 0 && (z - 1) % dz == 0)
			return pillarh3;

		if ((x - 1) % dx == 0 && (z + 1) % dz == 0)
			return pillarh3;

		if ((x + 1) % dx == 0 && (z + 1) % dz == 0)
			return pillarh3;

		return 0;
	}

	public void trySpread(Level level, BlockPos pos) {
		if (Math.random() < Config.getSpreadChance() && canTransformBlock(level, pos))
			spread(level, pos);
	}

	public void spread(Level world, BlockPos pos) {

		Random random = new Random();

		int dirx = random.nextInt(3) - 1;
		int diry = random.nextInt(3) - 1;
		int dirz = random.nextInt(3) - 1;

		for (int i = 0; i < 10; i++) {
			if (canTransformBlock(world, pos))
				world.setBlock(pos, grey_goo.defaultBlockState(), UPDATE);
			else
				return;

			pos = pos.offset(dirx, diry, dirz);
			double rand = Math.random();

			if (rand < .1)
				dirx = random.nextInt(3) - 1;
			else if (rand < .2)
				diry = random.nextInt(3) - 1;
			else if (rand < .3)
				dirz = random.nextInt(3) - 1;

		}
	}

	public static boolean canTransformBlock(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		Block block = state.getBlock();
		return !state.isAir() && block != Blocks.BEDROCK && block != GreyGoo.grey_goo;
	}

}

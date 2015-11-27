package ccl2of4.magdump.entity.bullet;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityBullet762NATO extends EntityBullet {

    public EntityBullet762NATO(World world) {
        super(world);
    }

    public EntityBullet762NATO(World world, Entity entity) {
        super(world, entity);
    }

    @Override
    public float getMuzzleVelocity() {
        return 5.0F;
    }

    @Override
    public float getSpread() {
        return 2.0F;
    }

    @Override
    public float getAirResistance()
    {
        return 0.99F;
    }

    @Override
    public float getGravity()
    {
        return 0.05F;
    }

}

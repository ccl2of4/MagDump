package ccl2of4.magdump.entity.bullet;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityBullet45ACP extends EntityBullet {

    public EntityBullet45ACP(World world) {
        super(world);
    }

    public EntityBullet45ACP(World world, Entity entity) {
        super(world, entity);
    }

    @Override
    public float getMuzzleVelocity() {
        return 5.0F;
    }

    @Override
    public float getSpread() {
        return 1.0F;
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
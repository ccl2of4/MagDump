package ccl2of4.magdump.entity.cartridge;

import ccl2of4.magdump.entity.bullet.EntityBullet;
import ccl2of4.magdump.entity.bullet.EntityBullet762NATO;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityCartridge762NATO extends EntityCartridge {

    public EntityCartridge762NATO(World world) {
        super(world);
    }
    public EntityCartridge762NATO(World world, Entity entity) {
        super(world, entity);
    }

    @Override
    protected int getNumberOfBullets() {
        return 1;
    }

    @Override
    protected Class<? extends EntityBullet> getBulletClass() {
        return EntityBullet762NATO.class;
    }
}

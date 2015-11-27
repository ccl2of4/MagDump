package ccl2of4.magdump.entity.cartridge;

import ccl2of4.magdump.entity.bullet.EntityBulletBuckshot;
import ccl2of4.magdump.entity.bullet.EntityBullet;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityCartridgeBuckshot extends EntityCartridge {

    public EntityCartridgeBuckshot(World world) {
        super(world);
    }
    public EntityCartridgeBuckshot(World world, Entity entity) {
        super(world, entity);
    }

    @Override
    protected int getNumberOfBullets() {
        return 9;
    }

    @Override
    protected Class<? extends EntityBullet> getBulletClass() {
        return EntityBulletBuckshot.class;
    }
}

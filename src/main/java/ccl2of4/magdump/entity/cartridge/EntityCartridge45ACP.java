package ccl2of4.magdump.entity.cartridge;

import ccl2of4.magdump.entity.bullet.EntityBullet;
import ccl2of4.magdump.entity.bullet.EntityBullet45ACP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityCartridge45ACP extends EntityCartridge {

    public EntityCartridge45ACP(World world) {
        super(world);
    }
    public EntityCartridge45ACP(World world, Entity entity) {
        super(world, entity);
    }

    @Override
    protected int getNumberOfBullets() {
        return 1;
    }

    @Override
    protected Class<? extends EntityBullet> getBulletClass() {
        return EntityBullet45ACP.class;
    }
}

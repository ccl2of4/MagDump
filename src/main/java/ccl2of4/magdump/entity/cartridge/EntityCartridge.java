package ccl2of4.magdump.entity.cartridge;

import ccl2of4.magdump.entity.bullet.EntityBullet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;

public abstract class EntityCartridge extends EntityThrowable {

    public EntityCartridge(World world) {
        super(world);
    }
    public EntityCartridge(World world, Entity entity) {
        super(world, (EntityLivingBase)entity);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        spawnBullets();
        setDead();
    }

    protected abstract int getNumberOfBullets();
    protected abstract Class<? extends EntityBullet> getBulletClass();

    @Override
    protected void onImpact(MovingObjectPosition p_70184_1_) {}

    private void spawnBullets() {
        if (!shouldSpawnBullets()) {
            return;
        }

        for (int i = 0; i < getNumberOfBullets(); ++i) {
            EntityBullet entity = makeBullet();
            this.worldObj.spawnEntityInWorld(entity);
        }
    }

    private EntityBullet makeBullet() {
        try {
            return getBulletClass()
                    .getConstructor(World.class, Entity.class)
                    .newInstance(worldObj, getThrower());
        } catch (ReflectiveOperationException e) {
            System.out.println("Error instantiating class " + getBulletClass());
        }
        return null;
    }

    private boolean shouldSpawnBullets() {
        return !this.worldObj.isRemote && this.getThrower() != null;
    }

}

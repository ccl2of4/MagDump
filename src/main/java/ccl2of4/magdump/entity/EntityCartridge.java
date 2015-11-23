package ccl2of4.magdump.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityCartridge extends EntityThrowable {

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

    @Override
    protected void onImpact(MovingObjectPosition p_70184_1_) {}

    private void spawnBullets() {
        if (!shouldSpawnBullets()) {
            return;
        }

        for (int i = 0; i < 9; ++i) {
            EntityBullet entity = new EntityBullet(this.worldObj, this.getThrower());
            this.worldObj.spawnEntityInWorld(entity);
        }
    }

    private boolean shouldSpawnBullets() {
        return !this.worldObj.isRemote && this.getThrower() != null;
    }

}

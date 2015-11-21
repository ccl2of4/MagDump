package ccl2of4.magdump.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public abstract class EntityCartridge extends EntityThrowable {

    public EntityCartridge(World world)
    {
        super(world);
    }

    public EntityCartridge(World world, EntityLivingBase entityLiving)
    {
        super(world, entityLiving);
    }

    public EntityCartridge(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public float getMuzzleVelocity() {
        return muzzleVelocity;
    }

    public void setMuzzleVelocity(float muzzleVelocity) {
        this.muzzleVelocity = muzzleVelocity;
    }

    @Override
    protected void entityInit() {
        setMuzzleVelocity(5.0f);
    }

    @Override
    protected void onImpact(MovingObjectPosition movingObjectPosition) {
        if (movingObjectPosition.entityHit != null) {
            movingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 1.0f);
        }
        if (!this.worldObj.isRemote) {
            this.setDead();
        }
    }

    @Override
    protected float func_70182_d() {
        return muzzleVelocity;
    }

    private float muzzleVelocity;
}

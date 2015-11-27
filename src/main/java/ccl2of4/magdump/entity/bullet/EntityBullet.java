package ccl2of4.magdump.entity.bullet;

import cpw.mods.fml.common.registry.IThrowableEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

public abstract class EntityBullet extends EntityArrow implements IThrowableEntity {

    public EntityBullet(World world) {
        super(world);
        setSize(0.5F, 0.5F);
    }

    public EntityBullet(World world, Entity entity) {
        this(world);
        shootingEntity = entity;
        setLocationAndAngles(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, entity.rotationYaw, entity.rotationPitch);
        posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
        posY -= 0.1D;
        posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
        setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
        motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
        motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F);
        setThrowableHeading(motionX, motionY, motionZ, getMuzzleVelocity(), getSpread());
    }

    protected abstract float getMuzzleVelocity();
    protected abstract float getSpread();
    protected abstract float getAirResistance();
    protected abstract float getGravity();

    @Override
    public Entity getThrower()
    {
        return shootingEntity;
    }
    @Override
    public void setThrower(Entity entity) {
        shootingEntity = entity;
    }

    @Override
    public void setThrowableHeading(double x, double y, double z, float speed, float deviation) {
        float f2 = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= f2;
        y /= f2;
        z /= f2;
        x += rand.nextGaussian() * 0.0075F * deviation;
        y += rand.nextGaussian() * 0.0075F * deviation;
        z += rand.nextGaussian() * 0.0075F * deviation;
        x *= speed;
        y *= speed;
        z *= speed;
        motionX = x;
        motionY = y;
        motionZ = z;
        float f3 = MathHelper.sqrt_double(x * x + z * z);
        prevRotationYaw = rotationYaw = (float) ((Math.atan2(x, z) * 180D) / Math.PI);
        prevRotationPitch = rotationPitch = (float) ((Math.atan2(y, f3) * 180D) / Math.PI);
        ticksInGround = 0;
    }

    @Override
    public void onUpdate() {

        if (aimRotation()/* && prevRotationPitch == 0.0F && prevRotationYaw == 0.0F*/) {
            float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            prevRotationYaw = rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch = (float) ((Math.atan2(motionY, f) * 180D) / Math.PI);
        }

        Block i = worldObj.getBlock(xTile, yTile, zTile);
        if (i != null) {
            i.setBlockBoundsBasedOnState(worldObj, xTile, yTile, zTile);
            AxisAlignedBB axisalignedbb = i.getCollisionBoundingBoxFromPool(worldObj, xTile, yTile, zTile);
            if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3.createVectorHelper(posX, posY, posZ))) {
                inGround = true;
            }
        }

        if (inGround) {
            Block j = worldObj.getBlock(xTile, yTile, zTile);
            int k = worldObj.getBlockMetadata(xTile, yTile, zTile);
            if (j == inTile && k == inData) {
                ticksInGround++;
                int t = getMaxLifetime();
                if (t != 0 && ticksInGround >= t) {
                    setDead();
                }
                return;
            } else {
                inGround = false;
                ticksInGround = 0;
                ticksInAir = 0;
            }
        }

        ticksInAir++;

        Vec3 vec3d = Vec3.createVectorHelper(posX, posY, posZ);
        Vec3 vec3d1 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
        MovingObjectPosition movingobjectposition = worldObj.func_147447_a(vec3d, vec3d1, false, true, false);
        vec3d = Vec3.createVectorHelper(posX, posY, posZ);
        vec3d1 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
        if (movingobjectposition != null)
        {
            vec3d1 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        Entity entity = null;
        @SuppressWarnings("unchecked")
        List<Entity> list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
        double d = 0.0D;
        for (int l = 0; l < list.size(); l++) {
            Entity entity1 = list.get(l);
            if (!entity1.canBeCollidedWith() || entity1 == shootingEntity && ticksInAir < 5) {
                continue;
            }
            float f4 = 0.3F;
            AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(f4, f4, f4);
            MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3d, vec3d1);
            if (movingobjectposition1 == null) {
                continue;
            }
            double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);
            if (d1 < d || d == 0.0D) {
                entity = entity1;
                d = d1;
            }
        }

        if (entity != null) {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null) {
            if (movingobjectposition.entityHit != null)
            {
                onEntityHit(movingobjectposition.entityHit);
            } else
            {
                onGroundHit(movingobjectposition);
            }
        }

        posX += motionX;
        posY += motionY;
        posZ += motionZ;

        if (aimRotation()) {
            float f2 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180D) / Math.PI);
            for (rotationPitch = (float) ((Math.atan2(motionY, f2) * 180D) / Math.PI); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F)
            {}
            for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F)
            {}
            for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F)
            {}
            for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F)
            {}
            rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
            rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
        }

        float res = getAirResistance();
        float grav = getGravity();
        if (isInWater()) {
            for (int i1 = 0; i1 < 4; i1++) {
                float f6 = 0.25F;
                worldObj.spawnParticle("bubble", posX - motionX * f6, posY - motionY * f6, posZ - motionZ * f6, motionX, motionY, motionZ);
            }
            res *= 0.80808080F;
        }

        motionX *= res;
        motionY *= res;
        motionZ *= res;
        motionY -= grav;
        setPosition(posX, posY, posZ);
        func_145775_I();
    }

    public void onEntityHit(Entity entity) {
        int prevhurtrestime = entity.hurtResistantTime;
        entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 1.0F);
        entity.hurtResistantTime = prevhurtrestime;

        if (!this.worldObj.isRemote) {
            this.setDead();
        }

        // bounceBack();
        applyEntityHitEffects(entity);
    }

    public void applyEntityHitEffects(Entity entity) {
        if (isBurning() && !(entity instanceof EntityEnderman)) {
            entity.setFire(5);
        }
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityliving = (EntityLivingBase) entity;
            if (knockBack > 0) {
                float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
                if (f > 0.0F) {
                    entity.addVelocity(motionX * knockBack * 0.6D / f, 0.1D, motionZ * knockBack * 0.6D / f);
                }
            }
            if (shootingEntity instanceof EntityLivingBase) {
                EnchantmentHelper.func_151384_a(entityliving, this.shootingEntity);
                EnchantmentHelper.func_151385_b((EntityLivingBase) this.shootingEntity, entityliving);
            }
            if (shootingEntity instanceof EntityPlayerMP && shootingEntity != entity && entity instanceof EntityPlayer) {
                ((EntityPlayerMP) shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0));
            }
        }
    }

    public void onGroundHit(MovingObjectPosition mop) {
        xTile = mop.blockX;
        yTile = mop.blockY;
        zTile = mop.blockZ;
        inTile = worldObj.getBlock(xTile, yTile, zTile);
        inData = worldObj.getBlockMetadata(xTile, yTile, zTile);
        motionX = mop.hitVec.xCoord - posX;
        motionY = mop.hitVec.yCoord - posY;
        motionZ = mop.hitVec.zCoord - posZ;
        float f1 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
        posX -= motionX / f1 * 0.05D;
        posY -= motionY / f1 * 0.05D;
        posZ -= motionZ / f1 * 0.05D;
        inGround = true;
        playHitSound();

        if (inTile != null) {
            inTile.onEntityCollidedWithBlock(worldObj, xTile, yTile, zTile, this);
        }
    }

    protected void bounceBack() {
        motionX *= -0.1D;
        motionY *= -0.1D;
        motionZ *= -0.1D;
        rotationYaw += 180F;
        prevRotationYaw += 180F;
        ticksInAir = 0;
    }

    @Override
    public void setVelocity(double d, double d1, double d2) {
        motionX = d;
        motionY = d1;
        motionZ = d2;
        if (aimRotation() && prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(d * d + d2 * d2);
            prevRotationYaw = rotationYaw = (float) ((Math.atan2(d, d2) * 180D) / Math.PI);
            prevRotationPitch = rotationPitch = (float) ((Math.atan2(d1, f) * 180D) / Math.PI);
            setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
            ticksInGround = 0;
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short) xTile);
        nbttagcompound.setShort("yTile", (short) yTile);
        nbttagcompound.setShort("zTile", (short) zTile);
        nbttagcompound.setByte("inTile", (byte) Block.getIdFromBlock(inTile));
        nbttagcompound.setByte("inData", (byte) inData);
        nbttagcompound.setByte("shake", (byte) arrowShake);
        nbttagcompound.setBoolean("inGround", inGround);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        xTile = nbttagcompound.getShort("xTile");
        yTile = nbttagcompound.getShort("yTile");
        zTile = nbttagcompound.getShort("zTile");
        inTile = Block.getBlockById(nbttagcompound.getByte("inTile") & 0xFF);
        inData = nbttagcompound.getByte("inData") & 0xFF;
        inGround = nbttagcompound.getBoolean("inGround");
    }

    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    private int inData;
    private boolean inGround;
    private int ticksInGround;
    private int ticksInAir;
    private int knockBack;

    // None of this is used.
    @Override public void setIsCritical(boolean critical) {}
    @Override public boolean getIsCritical() { return false; }
    @Override public void setKnockbackStrength(int i)
    {
        knockBack = i;
    }
    @Override @SideOnly(Side.CLIENT) public float getShadowSize()
    {
        return 0.0F;
    }
    @Override protected boolean canTriggerWalking()
    {
        return false;
    }
    public boolean aimRotation()
    {
        return true;
    }
    public int getMaxLifetime() { return 1200; }
    public void playHitSound() {}
}
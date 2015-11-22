package ccl2of4.magdump.entity;

import cpw.mods.fml.common.registry.IThrowableEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

public abstract class EntityCartridge extends Entity implements IProjectile, IThrowableEntity {

    public EntityCartridge(World world) {
        super(world);
        this.setSize(0.25F, 0.25F);
    }

    public EntityCartridge(World world, Entity entity) {
        super(world);
        this.thrower = entity;
        this.setSize(0.25F, 0.25F);
        this.setLocationAndAngles(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ, entity.rotationYaw, entity.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        float f = 0.4F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * f);
        this.motionY = (double)(-MathHelper.sin((this.rotationPitch) / 180.0F * (float)Math.PI) * f);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.getMuzzleVelocity(), 0.0F);
    }

    public EntityCartridge(World world, double x, double y, double z) {
        super(world);
        this.ticksInGround = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(x, y, z);
        this.yOffset = 0.0F;
    }

    public float getMuzzleVelocity() {
        return muzzleVelocity;
    }
    public void setMuzzleVelocity(float muzzleVelocity) {
        this.muzzleVelocity = muzzleVelocity;
    }
    public float getSpread() {
        return spread;
    }
    public void setSpread(float spread) {
        this.spread = spread;
    }

    @Override
    protected void entityInit() {
        setMuzzleVelocity(5.0f);
        setSpread(0.0F);
    }

    @Override
    public void setThrowableHeading(double x, double y, double z, float speed, float deviation) {
        float f2 = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= f2;
        y /= f2;
        z /= f2;
        x += this.rand.nextGaussian() * 0.007499999832361937D * deviation;
        y += this.rand.nextGaussian() * 0.007499999832361937D * deviation;
        z += this.rand.nextGaussian() * 0.007499999832361937D * deviation;
        x *= speed;
        y *= speed;
        z *= speed;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f3 = MathHelper.sqrt_double(x * x + z * z);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, f3) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    @Override
    public void onUpdate()
    {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();

        if (this.inGround) {

            if (this.worldObj.getBlock(this.xTile, this.yTile, this.zTile) == this.inTile) {
                ++this.ticksInGround;

                if (this.ticksInGround >= 200) {
                    this.setDead();
                }

                return;
            }

            this.inGround = false;
            this.ticksInGround = 0;
            this.ticksInAir = 0;
        }
        else
        {
            ++this.ticksInAir;
        }

        Vec3 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
        vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (movingobjectposition != null)
        {
            vec31 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        Entity entity = null;
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
        double d0 = 0.0D;

        for (int j = 0; j < list.size(); ++j)
        {
            Entity entity1 = (Entity)list.get(j);

            if (entity1.canBeCollidedWith() && (entity1 != getThrower() || this.ticksInAir >= 5))
            {
                float f = 0.3F;
                AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double)f, (double)f, (double)f);
                MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

                if (movingobjectposition1 != null)
                {
                    double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

                    if (d1 < d0 || d0 == 0.0D)
                    {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }

            if (entity != null)
            {
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }

        if (movingobjectposition != null)
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Blocks.portal)
            {
                this.setInPortal();
            }
            else
            {
                this.onImpact(movingobjectposition);
            }
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f1) * 180.0D / Math.PI);
             this.rotationPitch - this.prevRotationPitch < -180.0F;
             this.prevRotationPitch -= 360.0F) ;

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float resistance = 0.99F;
        float gravity = this.getGravityVelocity();

        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                float f4 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f4, this.posY - this.motionY * (double)f4, this.posZ - this.motionZ * (double)f4, this.motionX, this.motionY, this.motionZ);
            }
            resistance = 0.8F;
        }

        this.motionX *= (double)resistance;
        this.motionY *= (double)resistance;
        this.motionZ *= (double)resistance;
        this.motionY -= (double)gravity;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    protected float getGravityVelocity() {
        return 0.03F;
    }

    protected void onImpact(MovingObjectPosition movingObjectPosition) {
        if (movingObjectPosition.entityHit == null) {
            onGroundImpact(movingObjectPosition);
        } else {
            onEntityImpact(movingObjectPosition);
        }
    }

    void onEntityImpact(MovingObjectPosition movingObjectPosition) {
        Entity entity = movingObjectPosition.entityHit;
        int prevHurtResistantTime = entity.hurtResistantTime;
        entity.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 1.0f);
        entity.hurtResistantTime = prevHurtResistantTime;
        setDead();
    }

    void onGroundImpact(MovingObjectPosition movingObjectPosition) {
        xTile = movingObjectPosition.blockX;
        yTile = movingObjectPosition.blockY;
        zTile = movingObjectPosition.blockZ;
        inTile = worldObj.getBlock(xTile, yTile, zTile);

        motionX = movingObjectPosition.hitVec.xCoord - posX;
        motionY = movingObjectPosition.hitVec.yCoord - posY;
        motionZ = movingObjectPosition.hitVec.zCoord - posZ;
        float f1 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
        posX -= motionX / f1 * 0.05D;
        posY -= motionY / f1 * 0.05D;
        posZ -= motionZ / f1 * 0.05D;
        inGround = true;

        if (inTile != null) {
            inTile.onEntityCollidedWithBlock(worldObj, xTile, yTile, zTile, this);
        }

    }

    public void setThrower(Entity entity) {
        thrower = entity;
    }

    public Entity getThrower() {
        if (this.thrower == null && this.throwerName != null && this.throwerName.length() > 0) {
            this.thrower = this.worldObj.getPlayerEntityByName(this.throwerName);
        }
        return this.thrower;
    }

    public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
        p_70014_1_.setShort("xTile", (short)this.xTile);
        p_70014_1_.setShort("yTile", (short)this.yTile);
        p_70014_1_.setShort("zTile", (short)this.zTile);
        p_70014_1_.setByte("inTile", (byte)Block.getIdFromBlock(this.inTile));
        p_70014_1_.setByte("inGround", (byte)(this.inGround ? 1 : 0));

        if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower != null && this.thrower instanceof EntityPlayer) {
            this.throwerName = this.thrower.getCommandSenderName();
        }

        p_70014_1_.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
    }

    public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
        this.xTile = p_70037_1_.getShort("xTile");
        this.yTile = p_70037_1_.getShort("yTile");
        this.zTile = p_70037_1_.getShort("zTile");
        this.inTile = Block.getBlockById(p_70037_1_.getByte("inTile") & 255);
        this.inGround = p_70037_1_.getByte("inGround") == 1;
        this.throwerName = p_70037_1_.getString("ownerName");

        if (this.throwerName != null && this.throwerName.length() == 0) {
            this.throwerName = null;
        }
    }

    private float muzzleVelocity;
    private float spread;

    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile;
    private boolean inGround;
    private Entity thrower;
    private String throwerName;
    private int ticksInGround;
    private int ticksInAir;
    private static final String __OBFID = "CL_00001723";
}

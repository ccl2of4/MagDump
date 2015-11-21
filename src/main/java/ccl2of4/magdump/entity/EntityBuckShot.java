package ccl2of4.magdump.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityBuckShot extends EntityCartridge {

    public EntityBuckShot(World world)
    {
        super(world);
    }
    public EntityBuckShot(World world, EntityLivingBase entityLiving)
    {
        super(world, entityLiving);
    }
    public EntityBuckShot(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

}

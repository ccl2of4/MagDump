package ccl2of4.magdump.entity;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityBuckShot extends EntityCartridge {

    public EntityBuckShot(World world) {
        super(world);
    }
    public EntityBuckShot(World world, Entity entity) {
        super(world, entity);
    }
    public EntityBuckShot(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        spawnPellets();
        setDead();
    }

    private void spawnPellets() {
        if (!shouldSpawnPellets()) {
            return;
        }

        for (int i = 0; i < 9; ++i) {
            EntityThirtyCal entity = new EntityThirtyCal(this.worldObj, this.getThrower());
            this.worldObj.spawnEntityInWorld(entity);
        }
    }

    private boolean shouldSpawnPellets() {
        return !this.worldObj.isRemote && this.getThrower() != null;
    }

}

package br.alkazuz.tesouros.entities;

import br.alkazuz.tesouros.Tesouros;
import net.minecraft.server.v1_5_R3.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R3.util.UnsafeList;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CustomEntities {

    private static final Map<String, Field> selectorFields = new HashMap<>();

    static {
        try {
            Field goalField = EntityLiving.class.getDeclaredField("goalSelector");
            Field targetField = EntityLiving.class.getDeclaredField("targetSelector");
            goalField.setAccessible(true);
            targetField.setAccessible(true);
            selectorFields.put("goalSelector", goalField);
            selectorFields.put("targetSelector", targetField);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static PathfinderGoalSelector getSelector(EntityLiving entity, String fieldName) {
        try {
            Field field = selectorFields.get(fieldName);
            return (PathfinderGoalSelector) field.get(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void resetTargetSelector(PathfinderGoalSelector targetSelector) {
        try {
            Field field = PathfinderGoalSelector.class.getDeclaredField("a");
            field.setAccessible(true);
            List list = (List) field.get(targetSelector);
            list.clear();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void spawnEntity(Location location, EntityLiving entity, UUID uuid) {
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();

        entity.setPosition(location.getX(), location.getY(), location.getZ());
        entity.getBukkitEntity().setMetadata("custom", new FixedMetadataValue(Tesouros.getInstance(), true));
        entity.getBukkitEntity().setMetadata("tesouroid", new FixedMetadataValue(Tesouros.getInstance(), uuid.toString()));

        worldServer.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);

       if (entity instanceof EntityCreature) {
           PathfinderGoalSelector goalSelector = getSelector(entity, "goalSelector");
           PathfinderGoalSelector targetSelector = getSelector(entity, "targetSelector");

           resetTargetSelector(targetSelector);

           if (entity instanceof EntitySkeleton) {
               EntitySkeleton skeleton = (EntitySkeleton) entity;
               skeleton.setSkeletonType(0);
               skeleton.setEquipment(0, new ItemStack(Item.BOW));

               goalSelector.a(4, new PathfinderGoalArrowAttack(skeleton, 0.25F, 20, 60, 15.0F));

               targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(skeleton, EntityHuman.class, 16.0F, 0, true));

               skeleton.bJ();
           } else  if (entity instanceof EntitySpider) {
           } else {
               setupGoals(goalSelector, targetSelector, entity);
           }
       }


    }

    private static void setupGoals(PathfinderGoalSelector goalSelector, PathfinderGoalSelector targetSelector, EntityLiving entity) {
        goalSelector.a(0, new PathfinderGoalFloat(entity));
        goalSelector.a(2, new PathfinderGoalMeleeAttack(entity, 1.0F, false));
        goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction((EntityCreature) entity, 1.0F));
        goalSelector.a(6, new PathfinderGoalMoveThroughVillage((EntityCreature) entity, 1.0F, false));
        goalSelector.a(7, new PathfinderGoalRandomStroll((EntityCreature) entity, 1.0F));
        goalSelector.a(8, new PathfinderGoalLookAtPlayer(entity, EntityHuman.class, 8.0F));
        goalSelector.a(8, new PathfinderGoalRandomLookaround(entity));
        targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(entity, EntityHuman.class, 16.0F, 0, true));
    }

    public static void spawnZombiePigman(Location location, UUID uuid) {
        spawnEntity(location, new EntityPigZombie(((CraftWorld) location.getWorld()).getHandle()), uuid);
    }

    public static void spawnBlaze(Location location, UUID uuid) {
        spawnEntity(location, new EntityBlaze(((CraftWorld) location.getWorld()).getHandle()), uuid);
    }

    public static void spawnWitch(Location location, UUID uuid) {
        spawnEntity(location, new EntityWitch(((CraftWorld) location.getWorld()).getHandle()), uuid);
    }

    public static void spawnSkeleton(Location location, UUID uuid) {
        spawnEntity(location, new EntitySkeleton(((CraftWorld) location.getWorld()).getHandle()), uuid);
    }

    public static void spawnSpider(Location location, UUID uuid) {
        spawnEntity(location, new EntitySpider(((CraftWorld) location.getWorld()).getHandle()), uuid);
    }

    public static void spawnSlime(Location location, UUID uuid) {
        spawnEntity(location, new EntitySlime(((CraftWorld) location.getWorld()).getHandle()), uuid);
    }

    public static void spawnZombie(Location location, UUID uuid) {
        spawnEntity(location, new EntityZombie(((CraftWorld) location.getWorld()).getHandle()), uuid);
    }

    public static void spawnCreeper(Location location, UUID uuid) {
        spawnEntity(location, new EntityCreeper(((CraftWorld) location.getWorld()).getHandle()), uuid);
    }

    public static void spawnEnderman(Location location, UUID uuid) {
        spawnEntity(location, new EntityEnderman(((CraftWorld) location.getWorld()).getHandle()), uuid);
    }

    public static void spawnCaveSpider(Location location, UUID uuid) {
        spawnEntity(location, new EntityCaveSpider(((CraftWorld) location.getWorld()).getHandle()), uuid);
    }

}
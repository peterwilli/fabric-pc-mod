package com.emeraldodin.minecraft.pcmod.item;

import com.emeraldodin.minecraft.pcmod.client.PCModClient;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;

public class PlacableOrderableItem extends OrderableItem {
    private Constructor<? extends Entity> constructor;
    private SoundEvent placeSound;
    public final boolean wallTV;

    public PlacableOrderableItem(Settings settings, Class<? extends Entity> entityPlaced, SoundEvent placeSound, int price, boolean wallTV) {
        super(settings, price);
        this.wallTV = wallTV;
        this.placeSound = placeSound;
        try {
            constructor = entityPlaced.getConstructor(World.class, Double.class, Double.class, Double.class, Vec3d.class, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PlacableOrderableItem(Settings settings, Class<? extends Entity> entityPlaced, SoundEvent placeSound, int price) {
        this(settings, entityPlaced, placeSound, price, false);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && hand == Hand.MAIN_HAND) {
            user.getStackInHand(hand).decrement(1);
            HitResult hr = user.raycast(5, 0f, false);
            Entity ek;
            try {
                ek = constructor.newInstance(world,
                        hr.getPos().getX(),
                        hr.getPos().getY(),
                        hr.getPos().getZ(),
                        new Vec3d(user.getPos().x,
                                hr.getPos().getY(),
                                user.getPos().z), user.getUuid().toString());
                world.spawnEntity(ek);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (world.isClient) {
            world.playSound(PCModClient.thePreviewEntity.getX(),
                    PCModClient.thePreviewEntity.getY(),
                    PCModClient.thePreviewEntity.getZ(),
                    placeSound,
                    SoundCategory.BLOCKS, 1, 1, true);
        }

        return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, user.getStackInHand(hand));
    }

}
package com.Fishmod.mod_LavaCow.entities.flying;

import javax.annotation.Nullable;

import com.Fishmod.mod_LavaCow.client.Modconfig;
import com.Fishmod.mod_LavaCow.init.FishItems;
import com.Fishmod.mod_LavaCow.util.LootTableHandler;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class EntityPtera extends EntityFlyingMob {
	private static final DataParameter<Integer> SKIN_TYPE = EntityDataManager.<Integer>createKey(EntityPtera.class, DataSerializers.VARINT);
	
	public EntityPtera(World worldIn) {
		super(worldIn);
		this.setSize(1.6F, 0.8F);
	}
	
	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true).setUnseenMemoryTicks(160));
	}
	
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(Modconfig.Ptera_Attack);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Modconfig.Ptera_Health);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1D);
	}
	
	public boolean getCanSpawnHere() {
		return this.dimension == DimensionType.OVERWORLD.getId()
				&& this.world.canSeeSky(new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ))
				&& super.getCanSpawnHere();
	}
	
    protected void entityInit() {
    	super.entityInit();
        this.getDataManager().register(SKIN_TYPE, Integer.valueOf(0));
    }
    
    public float getEyeHeight() {
    	return this.height * 0.35F;
    }

   /**
    * Called to update the entity's position/logic.
    */
   public void onUpdate() {
      super.onUpdate();

      if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
         this.setDead();
      }

   }
   
   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData entityLivingData) {
	   if(BiomeDictionary.hasType(this.getEntityWorld().getBiome(this.getPosition()), Type.DRY))
		   this.setSkin(1);
	   return super.onInitialSpawn(difficulty, entityLivingData);
	}
   
   public int getSkin()
   {
       return ((Integer)this.dataManager.get(SKIN_TYPE)).intValue();
   }

   public void setSkin(int skinType)
   {
       this.dataManager.set(SKIN_TYPE, Integer.valueOf(skinType));
   }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setInteger("Variant", getSkin());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		setSkin(nbt.getInteger("Variant"));
	}
	
	public SoundCategory getSoundCategory() {
		return SoundCategory.HOSTILE;
	}

	protected SoundEvent getAmbientSound() {
		return FishItems.ENTITY_PTERA_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return FishItems.ENTITY_PTERA_HURT;
	}

	protected SoundEvent getDeathSound() {
		return FishItems.ENTITY_PTERA_DEATH;
	}

	@Nullable
	protected ResourceLocation getLootTable() {
		return LootTableHandler.PTERA;
	}

	/**
	* Returns the volume for the sounds this mob makes.
	*/
	protected float getSoundVolume() {
		return 0.7F;
	}
}

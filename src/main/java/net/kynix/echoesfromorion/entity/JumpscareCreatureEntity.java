package net.kynix.echoesfromorion.entity;

import net.kynix.echoesfromorion.MySoundEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal; // Yeni: Oyuncuya bakma hedefi

import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity; // HostileEntity yerine MobEntity kullanıyoruz
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

public class JumpscareCreatureEntity extends MobEntity { // MobEntity'den türetildi

    private boolean hasJumpscared = false;
    private boolean isCharging = false;
    private int chargeTicks = 0;

    public JumpscareCreatureEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 0; // Bu bir düşman olmadığı için XP vermesin
    }

    // Yaratığın temel nitelikleri (can, hareket hızı vb.)
    public static DefaultAttributeContainer.Builder createJumpscareCreatureAttributes() {
        // MobEntity için temel nitelikleri başlatıyoruz
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 10.0) // Daha az can
                .add(EntityAttributes.MOVEMENT_SPEED, 0.23) // Normal yürüme hızı
                .add(EntityAttributes.FOLLOW_RANGE, 32.0); // Oyuncuyu ne kadar uzaktan takip edeceği
        // Saldırı gücü eklemiyoruz çünkü saldırmayacak
    }

    // Yaratığın davranışları (AI)
    @Override
    protected void initGoals() {
        // Hedefleme (oyuncuyu takip etmeyecek, sadece bakacak)
        // Eğer oyuncuyu takip etmesini istiyorsak, PathAwareEntity'den türetip PathfindingEntityGoal eklememiz gerekir.
        // Şimdilik sadece "bakma" ve "dolaşma" hedefleri yeterli.
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f)); // Oyuncuya bakma
        //this.goalSelector.add(9, new WanderAroundGoal(this, 1.0D)); // Rastgele dolaşma

        // NOT: Pathfinding (yol bulma) olmadan MobEntity hareket edecektir, ancak "akıllıca" takip etmeyecektir.
        // Eğer oyuncuyu takip etmesini istiyorsak, MobEntity yerine PathAwareEntity'den türetmeli ve
        // PlayerEntity için PathfindingGoal eklemeliyiz.
        // Ancak amacımız hızla ışınlanma/yürüme olduğu için, bunu manuel olarak tick metodunda kontrol edebiliriz.
    }

    // Her tick'te çağrılan metot
    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient) {
            return;
        }

        LivingEntity target = this.getWorld().getClosestPlayer(this, 32.0); // En yakın oyuncuyu bul

        if (target instanceof PlayerEntity) {
            double distance = this.squaredDistanceTo(target);

            // Adım 1: Oyuncuyu uzaktan gördüğünde "şarj olmaya" başlasın
            // Örneğin 15 bloktan yakınsa ve henüz şarj olmuyorsa
            if (!isCharging && distance < 15.0 * 15.0) {
                isCharging = true;
                chargeTicks = 0;
            }

            // Adım 2: Şarj halindeyken hızlansın ve oyuncuya doğru hareket etsin
            if (isCharging) {
                chargeTicks++;
                // Belirli bir tick sayısından sonra hızlansın
                if (chargeTicks > 20) { // 1 saniye (20 tick) sonra hızlansın
                    this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.8); // Hızlı koşma hızı
                    this.getNavigation().startMovingTo(target, 1.0D); // Oyuncuya doğru hareket et (AI yoluyla)
                }

                // Adım 3: Belirli bir mesafeye yaklaştığında jumpscare'i tetikle
                if (!hasJumpscared && distance < 4.0 * 4.0) { // Örneğin 4 bloktan daha yakın
                    this.getWorld().playSound(
                            null,
                            target.getBlockPos(),
                            MySoundEvent.JUMPSCARE_ROAR,
                            SoundCategory.HOSTILE, // Ses kategorisi HOSTILE kalabilir çünkü korkutucu bir ses
                            10.0f,
                            1.0f
                    );

                    hasJumpscared = true;
                    // Jumpscare olduktan sonra yaratığı yok edebilir veya yavaşlatabiliriz.
                    this.discard(); // Yaratığı yok et
                }
            }
        } else {
            // Hedef yoksa, şarj durumunu ve jumpscare bayrağını sıfırla
            if (isCharging) {
                isCharging = false;
                this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.23); // Hızı normale döndür
            }
            hasJumpscared = false;
        }
    }
}
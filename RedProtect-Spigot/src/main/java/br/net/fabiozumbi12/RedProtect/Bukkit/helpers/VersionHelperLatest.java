/*
 * Copyright (c) 2020 - @FabioZumbi12
 * Last Modified: 02/07/2020 19:01.
 *
 * This class is provided 'as-is', without any express or implied warranty. In no event will the authors be held liable for any
 *  damages arising from the use of this class.
 *
 * Permission is granted to anyone to use this class for any purpose, including commercial plugins, and to alter it and
 * redistribute it freely, subject to the following restrictions:
 * 1 - The origin of this class must not be misrepresented; you must not claim that you wrote the original software. If you
 * use this class in other plugins, an acknowledgment in the plugin documentation would be appreciated but is not required.
 * 2 - Altered source versions must be plainly marked as such, and must not be misrepresented as being the original class.
 * 3 - This notice may not be removed or altered from any source distribution.
 *
 * Esta classe é fornecida "como está", sem qualquer garantia expressa ou implícita. Em nenhum caso os autores serão
 * responsabilizados por quaisquer danos decorrentes do uso desta classe.
 *
 * É concedida permissão a qualquer pessoa para usar esta classe para qualquer finalidade, incluindo plugins pagos, e para
 * alterá-lo e redistribuí-lo livremente, sujeito às seguintes restrições:
 * 1 - A origem desta classe não deve ser deturpada; você não deve afirmar que escreveu a classe original. Se você usar esta
 *  classe em um plugin, uma confirmação de autoria na documentação do plugin será apreciada, mas não é necessária.
 * 2 - Versões de origem alteradas devem ser claramente marcadas como tal e não devem ser deturpadas como sendo a
 * classe original.
 * 3 - Este aviso não pode ser removido ou alterado de qualquer distribuição de origem.
 */

package br.net.fabiozumbi12.RedProtect.Bukkit.helpers;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Core.config.Category.FlagGuiCategory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class VersionHelperLatest implements VersionHelper {

    @Override
    public String getVersion() {
        return "Latest";
    }

    public Set<Location> getPortalLocations(PortalCreateEvent e) {
        return e.getBlocks().stream().map(BlockState::getLocation).collect(Collectors.toSet());
    }

    public boolean denyEntLingPot(ProjectileHitEvent e, World world) {
        return RedProtect.get().getUtil().denyPotion(((LingeringPotionSplashEvent) e).getEntity().getItem(), world);
    }

    public Entity getEntLingPot(ProjectileHitEvent e) {
        return e.getEntity();
    }

    public ProjectileSource getPlayerLingPot(ProjectileHitEvent e) {
        return e.getEntity().getShooter();
    }

    public Block getBlockRelative(Block block) {
        if (block.getState() instanceof Sign) {
            Directional dir = (Directional) block.getBlockData();
            return block.getRelative(dir.getFacing().getOppositeFace());
        }
        return null;
    }

    public void toggleDoor(Block b) {
        if (b.getBlockData() instanceof Openable openable) {
            openable.setOpen(!openable.isOpen());
            b.setBlockData(openable);
        }
    }

    public boolean isOpenable(Block b) {
        return b.getBlockData() instanceof Openable;
    }


    @Override
    public FlagGuiCategory setGuiItems(FlagGuiCategory guiRoot) {
        RedProtect.get().logger.info("Loading GuiFlags for 1.14+");
        if (guiRoot.gui_separator.material.isEmpty())
            guiRoot.gui_separator.material = Material.WHITE_STAINED_GLASS_PANE.name();

        if (guiRoot.gui_flags.isEmpty()) {
            guiRoot.gui_flags.put("allow-effects", new FlagGuiCategory.GuiFlag(Material.BLAZE_ROD.name(), 16));
            guiRoot.gui_flags.put("allow-fly", new FlagGuiCategory.GuiFlag(Material.FEATHER.name(), 8));
            guiRoot.gui_flags.put("allow-home", new FlagGuiCategory.GuiFlag(Material.COMPASS.name(), 2));
            guiRoot.gui_flags.put("allow-potions", new FlagGuiCategory.GuiFlag(Material.POTION.name(), 26));
            guiRoot.gui_flags.put("allow-spawner", new FlagGuiCategory.GuiFlag(Material.LEAD.name(), 10));
            guiRoot.gui_flags.put("build", new FlagGuiCategory.GuiFlag(Material.GRASS.name(), 13));
            guiRoot.gui_flags.put("button", new FlagGuiCategory.GuiFlag(Material.STONE_BUTTON.name(), 6));
            guiRoot.gui_flags.put("can-grow", new FlagGuiCategory.GuiFlag(Material.WHEAT.name(), 27));
            guiRoot.gui_flags.put("chest", new FlagGuiCategory.GuiFlag(Material.TRAPPED_CHEST.name(), 3));
            guiRoot.gui_flags.put("door", new FlagGuiCategory.GuiFlag(Material.ACACIA_DOOR.name(), 0));
            guiRoot.gui_flags.put("ender-chest", new FlagGuiCategory.GuiFlag(Material.ENDER_CHEST.name(), 22));
            guiRoot.gui_flags.put("fire", new FlagGuiCategory.GuiFlag(Material.BLAZE_POWDER.name(), 9));
            guiRoot.gui_flags.put("fishing", new FlagGuiCategory.GuiFlag(Material.FISHING_ROD.name(), 28));
            guiRoot.gui_flags.put("flow", new FlagGuiCategory.GuiFlag(Material.WATER_BUCKET.name(), 29));
            guiRoot.gui_flags.put("flow-damage", new FlagGuiCategory.GuiFlag(Material.LAVA_BUCKET.name(), 30));
            guiRoot.gui_flags.put("gravity", new FlagGuiCategory.GuiFlag(Material.SAND.name(), 7));
            guiRoot.gui_flags.put("iceform-player", new FlagGuiCategory.GuiFlag(Material.PACKED_ICE.name(), 4));
            guiRoot.gui_flags.put("iceform-world", new FlagGuiCategory.GuiFlag(Material.ICE.name(), 31));
            guiRoot.gui_flags.put("leaves-decay", new FlagGuiCategory.GuiFlag(Material.OAK_LEAVES.name(), 18));
            guiRoot.gui_flags.put("lever", new FlagGuiCategory.GuiFlag(Material.LEVER.name(), 5));
            guiRoot.gui_flags.put("minecart", new FlagGuiCategory.GuiFlag(Material.MINECART.name(), 25));
            guiRoot.gui_flags.put("mob-loot", new FlagGuiCategory.GuiFlag(Material.MYCELIUM.name(), 32));
            guiRoot.gui_flags.put("passives", new FlagGuiCategory.GuiFlag(Material.SADDLE.name(), 33));
            guiRoot.gui_flags.put("press-plate", new FlagGuiCategory.GuiFlag(Material.LIGHT_WEIGHTED_PRESSURE_PLATE.name(), 17));
            guiRoot.gui_flags.put("pvp", new FlagGuiCategory.GuiFlag(Material.STONE_SWORD.name(), 20));
            guiRoot.gui_flags.put("smart-door", new FlagGuiCategory.GuiFlag(Material.IRON_DOOR.name(), 1));
            guiRoot.gui_flags.put("spawn-animals", new FlagGuiCategory.GuiFlag(Material.EGG.name(), 34));
            guiRoot.gui_flags.put("spawn-monsters", new FlagGuiCategory.GuiFlag(Material.PUMPKIN.name(), 35));
            guiRoot.gui_flags.put("teleport", new FlagGuiCategory.GuiFlag(Material.ENDER_PEARL.name(), 19));
            guiRoot.gui_flags.put("use-potions", new FlagGuiCategory.GuiFlag(Material.GLASS_BOTTLE.name(), 26));
            guiRoot.gui_flags.put("redstone", new FlagGuiCategory.GuiFlag(Material.REDSTONE.name(), 14));
            guiRoot.gui_flags.put("block-transform", new FlagGuiCategory.GuiFlag(Material.BOOKSHELF.name(), 12));
        }

        for (String key : RedProtect.get().getConfigManager().getDefFlagsValues().keySet()) {
            guiRoot.gui_flags.putIfAbsent(key, new FlagGuiCategory.GuiFlag(Material.GOLDEN_APPLE.name(), 0));
        }
        return guiRoot;
    }

    @Override
    public boolean existParticle(String particle) {
        return Arrays.stream(Particle.values()).anyMatch((it) -> it.name().equalsIgnoreCase(particle));
    }

    @Override
    public boolean spawnParticle​(World world, String particle, double x, double y, double z) {
        Optional<Particle> optional = Arrays.stream(Particle.values())
                .filter((it) -> it.name().equalsIgnoreCase(particle))
                .findAny();
        if (optional.isPresent()) {
            world.spawnParticle(optional.get(), x, y, z, 1, 0, 0, 0, 0);
            return true;
        } else return false;
    }
}


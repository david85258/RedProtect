/*
 * Copyright (c) 2012-2023 - @FabioZumbi12
 * Last Modified: 29/05/2023 15:36
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
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PermissionHandler {

    public boolean hasCommandPerm(CommandSender sender, String perm) {
        String adminPerm = "redprotect.command.admin." + perm;
        String userPerm = "redprotect.command." + perm;
        return this.hasPerm(sender, adminPerm) || this.hasPerm(sender, userPerm);
    }

    public boolean hasFlagPerm(Player p, String flag) {
        String adminPerm = "redprotect.flag.admin." + flag;
        String userPerm = "redprotect.flag." + flag;
        return this.hasPerm(p, adminPerm) || this.hasPerm(p, userPerm);
    }

    public boolean hasPermOrBypass(Player p, String perm) {
        return hasPerm(p, perm) || hasPerm(p, perm + ".bypass");
    }

    public boolean hasPerm(Player player, String perm) {
        return player != null && (hasVaultPerm(player, perm) || hasVaultPerm(player, "redprotect.command.admin"));
    }

    public boolean hasPerm(CommandSender sender, String perm) {
        return sender != null && (hasVaultPerm(sender, perm) || hasVaultPerm(sender, "redprotect.command.admin"));
    }

    public boolean hasRegionPermMember(Player p, String s, Region poly) {
        return regionPermMember(p, s, poly);
    }

    public boolean hasRegionPermAdmin(Player p, String s, Region poly) {
        return regionPermAdmin(p, s, poly);
    }

    public boolean hasRegionPermAdmin(CommandSender sender, String s, Region poly) {
        return !(sender instanceof Player) || regionPermAdmin((Player) sender, s, poly);
    }

    public boolean hasRegionPermLeader(Player p, String s, Region poly) {
        return regionPermLeader(p, s, poly);
    }

    public boolean hasRegionPermLeader(CommandSender sender, String s, Region poly) {
        return !(sender instanceof Player) || regionPermLeader((Player) sender, s, poly);
    }

    public int getPlayerBlockLimit(Player p) {
        return getBlockLimit(p);
    }

    public int getPlayerClaimLimit(Player p) {
        return getClaimLimit(p);
    }

    private boolean regionPermLeader(Player p, String s, Region poly) {
        String adminperm = "redprotect.command.admin." + s;
        String userperm = "redprotect.command." + s;
        if (poly == null) {
            return this.hasPerm(p, adminperm) || this.hasPerm(p, userperm);
        }
        if (denyWorldPermission(p, userperm, poly.getWorld()) && denyWorldPermission(p, adminperm, poly.getWorld())){
            return false;
        }
        return this.hasPerm(p, adminperm) || (this.hasPerm(p, userperm) && poly.isLeader(p));
    }

    private boolean regionPermAdmin(Player p, String s, Region poly) {
        String adminperm = "redprotect.command.admin." + s;
        String userperm = "redprotect.command." + s;
        if (poly == null) {
            return this.hasPerm(p, adminperm) || this.hasPerm(p, userperm);
        }
        if (denyWorldPermission(p, userperm, poly.getWorld()) && denyWorldPermission(p, adminperm, poly.getWorld())){
            return false;
        }
        return this.hasPerm(p, adminperm) || (this.hasPerm(p, userperm) && (poly.isLeader(p) || poly.isAdmin(p)));
    }

    private boolean regionPermMember(Player p, String s, Region poly) {
        String adminperm = "redprotect.command.admin." + s;
        String userperm = "redprotect.command." + s;
        if (poly == null) {
            return this.hasPerm(p, adminperm) || this.hasPerm(p, userperm);
        }
        if (denyWorldPermission(p, userperm, poly.getWorld()) && denyWorldPermission(p, adminperm, poly.getWorld())){
            return false;
        }
        return this.hasPerm(p, adminperm) || (this.hasPerm(p, userperm) && (poly.isLeader(p) || poly.isAdmin(p) || poly.isMember(p)));
    }

    private boolean denyWorldPermission(Player player, String perm, String world){
        if (!RedProtect.get().getConfigManager().configRoot().region_settings.region_perm_per_world) return false;
        return !this.hasPerm(player, perm + ".world." + world) && !this.hasPerm(player, perm + ".world.*");
    }

    private boolean hasVaultPerm(CommandSender player, String perm) {
        if (RedProtect.get().getConfigManager().configRoot().permissions_limits.useVault && player instanceof Player && RedProtect.get().permission != null) {
            return RedProtect.get().permission.playerHas(((Player) player).getWorld().getName(), (Player) player, perm);
        }
        return player.hasPermission(perm);
    }

    public int getPurgeLimit(Player player) {
        int limit = RedProtect.get().getConfigManager().configRoot().purge.canpurge_limit;
        List<Integer> limits = new ArrayList<>();
        Set<PermissionAttachmentInfo> perms = player.getEffectivePermissions();
        if (limit > 0) {
            for (PermissionAttachmentInfo perm : perms) {
                if (perm.getPermission().startsWith("redprotect.canpurge-limit.")) {
                    String pStr = perm.getPermission().replaceAll("[^-?0-9]+", "");
                    if (!pStr.isEmpty()) {
                        limits.add(Integer.parseInt(pStr));
                    }
                }
            }
        }
        if (limits.size() > 0) {
            limit = Collections.max(limits);
        }
        return limit;
    }

    private int getBlockLimit(Player player) {
        int limit = RedProtect.get().getConfigManager().configRoot().region_settings.limit_amount;
        List<Integer> limits = new ArrayList<>();
        Set<PermissionAttachmentInfo> perms = player.getEffectivePermissions();
        if (limit > 0) {
            if (!hasVaultPerm(player, "redprotect.limits.blocks.unlimited")) {
                for (PermissionAttachmentInfo perm : perms) {
                    if (perm.getPermission().startsWith("redprotect.limits.blocks.")) {
                        if (RedProtect.get().getConfigManager().configRoot().region_settings.blocklimit_per_world && !hasVaultPerm(player, perm.getPermission()))
                            continue;
                        String pStr = perm.getPermission().replaceAll("[^-?0-9]+", "");
                        if (!pStr.isEmpty()) {
                            limits.add(Integer.parseInt(pStr));
                        }
                    }
                }
            } else {
                return -1;
            }
        }
        if (limits.size() > 0) {
            limit = Collections.max(limits);
        }

        // Add blocks given by time
        limit += RedProtect.get().getBlockManager().getBlockLimit(player);

        return limit;
    }

    private int getClaimLimit(Player player) {
        int limit = RedProtect.get().getConfigManager().configRoot().region_settings.claim.amount_per_player;
        List<Integer> limits = new ArrayList<>();
        Set<PermissionAttachmentInfo> perms = player.getEffectivePermissions();
        if (limit > 0) {
            if (!hasVaultPerm(player, "redprotect.limits.claim.unlimited")) {
                for (PermissionAttachmentInfo perm : perms) {
                    if (perm.getPermission().startsWith("redprotect.limits.claim.")) {
                        if (RedProtect.get().getConfigManager().configRoot().region_settings.claim.claimlimit_per_world && !hasVaultPerm(player, perm.getPermission()))
                            continue;
                        String pStr = perm.getPermission().replaceAll("[^-?0-9]+", "");
                        if (!pStr.isEmpty()) {
                            limits.add(Integer.parseInt(pStr));
                        }

                    }
                }
            } else {
                return -1;
            }
        }
        if (limits.size() > 0) {
            limit = Collections.max(limits);
        }
        return limit;
    }
}

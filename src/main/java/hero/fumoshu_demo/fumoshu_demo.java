package fumoshu_demo;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class fumoshu_demo extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("fms").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                ItemStack item = player.getInventory().getItemInMainHand();

                if (item == null || item.getType() == Material.AIR) {
                    player.sendMessage("拿个东西!");
                    return false;
                }

                // 检查玩家背包中是否有普通书，如果有则删除
                ItemStack regularBookInInventory = null;
                for (ItemStack stack : player.getInventory().getContents()) {
                    if (stack != null && stack.getType() == Material.BOOK) {
                        regularBookInInventory = stack;
                        break;
                    }
                }

                if (regularBookInInventory != null) {
                    player.getInventory().remove(regularBookInInventory);
                    player.sendMessage("ok书没了.");
                }

                // 创建附魔书
                ItemStack enchantmentBook = new ItemStack(Material.ENCHANTED_BOOK);
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) enchantmentBook.getItemMeta();

                // 将原始物品的附魔转移到附魔书
                item.getEnchantments().forEach((enchantment, level) -> {
                    meta.addStoredEnchant(enchantment, level, true);
                });

                enchantmentBook.setItemMeta(meta);

                // 将附魔书放入玩家的背包
                player.getInventory().setItemInMainHand(enchantmentBook);

                // 删除原始物品
                item.setAmount(0);

                player.sendMessage("Your item has been converted into an enchantment book.");
                return true;
            }
            return false;
        });
    }

    @Override
    public void onDisable() {
        // 插件关闭时的逻辑（如果有的话）
    }
}


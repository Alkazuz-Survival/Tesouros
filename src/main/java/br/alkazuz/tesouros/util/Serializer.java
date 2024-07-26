package br.alkazuz.tesouros.util;

import br.alkazuz.correio.utils.objectStream.BukkitObjectInputStream;
import br.alkazuz.correio.utils.objectStream.BukkitObjectOutputStream;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Serializer {

    public static ItemStack deserializeItemStack(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            dataInput.readInt();
            ItemStack item = (ItemStack) dataInput.readObject();

            dataInput.close();
            return item;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemStack[] deserializeListItemStack(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String serializeItemStack(ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(1);

            dataOutput.writeObject(item);

            dataOutput.close();

            byte[] in = outputStream.toByteArray();

            return Base64Coder.encodeLines(in, 0, in.length, 76, ""); // Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int computeHash(String world, int x, int y, int z) {
        return (world + ";" + x + ";" + y + ";" + z).hashCode();
    }

    public static int computeHash(Location loc) {
        return computeHash(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public static String serializeLocation(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ();
    }

    public static Location deserializeLocation(String data) {
        String[] parts = data.split(";");
        return new Location(org.bukkit.Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
    }

    public static String serializeListItemStack(ItemStack[] items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(items.length);

            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Location getLocation(String string) {
        String[] parts = string.split(";");
        return new Location(org.bukkit.Bukkit.getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
    }

    public static String getStringLocation(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ();
    }
}

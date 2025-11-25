package com.gtocore.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class PlayerHeadUtils {

    private static final String GregoriusT = "{SkullOwner:{Id:[I;1756327232,-1482798551,-1740202678,832489332],Name:\"GregoriusT\",Properties:{textures:[{Signature:\"klFKM8zcawqlv/+sTZ/BJ0rCB5rNh+bjZPqHopyz5qC8eY1LxY69StTDW1IF2p213uF14R7SZ6XW13BXa2rXawOlxLFcPxEEXO55/FJ3ZbyWveaP9Awij/xz0DzFTKovtCA2kNrKF0zame6Ljx1PPh/rhOv7GwpGTrn4daypC4kdW1VK4pVPQr2gGt97KVAEQt65prPoYesoZFNUbXnOJdCzZuKOshwcmuhnmp3mGBd+8g6dki78mp/l9d2//UTDabJRh2F+WAhmNVAnVL39RMD6/xnnZrQ3fztlEGLb7gkEAsN/XX9WuQT9NXGER2vKqw0wRlc2VZGJ3t0uNAO97JF5YqLwSbuzGr0Ej6K5FlOCX5UK3hRtKXd2+mNxX6BYmXHE4VsdgLbY8DtsNGsaaVRtAfDc2rqagRu66zpBr/t1ogtED0U3xjelTppqI0msM6QhVzkPn1I+1Ar0/RT62ISvVBwsQzm5/Z2qZGiz5P1D9RtDM/A46tvaSgIzAvRwcxg5cyJRQD9r6YLIsEoc4BTA3SGHaXEZ7A11pbcMWOGy25NvtAL/V5WuHjoaeeCupf/IhW9T+Nu0Q+iCkWbgvt0scm2hO2aQlN98pMis2vxYwNrRXf0ZDbepxn2zJElIHlCrUUwvjvs6Mrv5fwkP7db40mJxZnhS/MW6jkvmoqE=\",Value:\"ewogICJ0aW1lc3RhbXAiIDogMTc2MzQ0MTU5OTc1NCwKICAicHJvZmlsZUlkIiA6ICI2OGFmNmQ0MGE3OWU0YTI5OTg0NjlkNGEzMTllYzc3NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJHcmVnb3JpdXNUIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzdlYzgwOGMwNzZkYjc4NGVlZTkwOWFjZmRkNzI3MWZiYWUxMGVkZWNkMTM2ZGRlZjVmMDFjMmFhZTIwOWFiZjQiCiAgICB9CiAgfQp9\"}]}}}";

    public static ItemStack get_GregoriusT_Head() {
        return itemStackAddNbtString(new ItemStack(Items.PLAYER_HEAD), GregoriusT);
    }

    private static final String maple197 = "{SkullOwner:{Id:[I;-1727425842,-1532149505,-1149567170,555059709],Name:\"maple197\",Properties:{textures:[{Signature:\"YoNwOf1/5+dkpGw/s9MytLY7bNfIVxeOWeeoYZz1pMrOUU+vvBIHZ05JdvMsx10zHnEVkFWVhBKrvb8bl77e1YW8KqGWVHxNzbA66FjmEP0xjkBLZJois58rkHaJAFXkbk+Q7Il5CZsLPJGlLryqIKEz25lCJcKmVXT/Pu52M2QZwooeYXaA3/AKMVNoQUujgN0PpfofWVpgoBw7WsmbcEUfBLGksZVNG/zNttQvS8OYlPfYy4AR/4ij3tJ6jwJPeJrL2vPCENlHd/lXsJOaxSIGA/MFjWD0PmNV++Ef3ZPucnCwisxLuM9oDkBoJv2bZHHQhRfXm7jJpPzsFukd+ypdnst7JdazFHFYzvttlEnvIzD8cwKvX3Xjt1xfXO5ntxriiK640taB0pr8ixBQMGHC63cZDd5KDMl5tMaUB8zQZqJzdSvE1RpSXHutD5ec8mFLgVx3+JM6AI8Ua7COKl7fFRdX2g7pJKqcveKAx+e9ppUxogH9/uXPlC7vxyFqoEqyLTygkbl3AWaElXIdfuWXpow7++qcWaILzW0tgxHbuVeSgi2LfMdyQTj78mlG4t5j73uLWbc2lRkWAy7GwaBsIoOTbNScJAB5im0SUxhuRLr3hIgF6qy2SPOf8tbBgqQWyDH75CGjt2jRQCQMDYibws7e4JsWV+OaMP26ePc=\",Value:\"ewogICJ0aW1lc3RhbXAiIDogMTc2MzQ0NDc1ODE3MSwKICAicHJvZmlsZUlkIiA6ICI5OTA5OTJjZWE0YWQ0MGZmYmI3YWZmM2UyMTE1ODlmZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJtYXBsZTE5NyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kYjE1ZTUzYzM0OTkwMjQwYTkxZDQ0ZDU2ZDQyY2ZmODUyY2Y0MjMzMzJiZWJjMmU0MGU5NmNjYWQ2YmYzY2UyIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81NjliN2YyYTFkMDBkMjZmMzBlZmUzZjlhYjlhYzgxN2IxZTZkMzVmNGYzY2ZiMDMyNGVmMmQzMjgyMjNkMzUwIgogICAgfQogIH0KfQ==\"}]}}}";

    public static ItemStack get_maple197_Head() {
        return itemStackAddNbtString(new ItemStack(Items.PLAYER_HEAD), maple197);
    }

    /**
     * 通过完整的 NBT 标签字符串创建玩家头
     * 
     * @param nbtString 玩家头的完整 NBT 标签字符串
     * @return 带有 NBT 数据的玩家头 ItemStack
     */
    public static ItemStack itemStackAddNbtString(ItemStack itemStack, String nbtString) {
        ItemStack stack = itemStack.copy();
        try {
            CompoundTag tag = TagParser.parseTag(nbtString);
            stack.setTag(tag);
        } catch (Exception ignored) {
            return itemStack;
        }
        return stack;
    }
}

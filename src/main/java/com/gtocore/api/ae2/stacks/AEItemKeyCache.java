package com.gtocore.api.ae2.stacks;

import com.gtocore.mixin.ae2.stacks.AEItemKeyInvoker;

import com.gtolib.api.ae2.IAEItemKey;

import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;

import net.minecraft.world.item.ItemStack;

import appeng.api.stacks.AEItemKey;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;

import static it.unimi.dsi.fastutil.HashCommon.arraySize;

public class AEItemKeyCache {

    public static final AEItemKeyCache INSTANCE = new AEItemKeyCache();

    private Node[] node;
    private int mask;
    private int n;
    private int maxFill;
    private int size;

    private AEItemKeyCache() {
        this.n = arraySize(Hash.DEFAULT_INITIAL_SIZE, Hash.DEFAULT_LOAD_FACTOR);
        this.mask = n - 1;
        this.maxFill = HashCommon.maxFill(n, Hash.DEFAULT_LOAD_FACTOR);
        this.node = new Node[n + 1];
    }

    public AEItemKey get(final ItemStack stack) {
        final int hash = ItemStackHashStrategy.ITEM_AND_TAG.hashCode(stack);
        synchronized (this) {
            final Node[] node = this.node;
            final int mask = this.mask;
            int pos = HashCommon.mix(hash) & mask;
            Node curr = node[pos];
            if (curr == null) {
                pos = -(pos + 1);
            } else if (!(hash == curr.hash && ItemStackHashStrategy.ITEM_AND_TAG.equals(stack, curr.stack))) {
                while (true) {
                    if ((curr = node[pos = (pos + 1) & mask]) == null) {
                        pos = -(pos + 1);
                        break;
                    }
                    if (hash == curr.hash && ItemStackHashStrategy.ITEM_AND_TAG.equals(stack, curr.stack)) break;
                }
            }
            if (pos >= 0) return node[pos].key;
            final var copy = stack.copyWithCount(1);
            final var newValue = AEItemKeyInvoker.of(copy.getItem(), copy.getTag(), AEItemKeyInvoker.serializeStackCaps(copy));
            final var itemKey = ((IAEItemKey) (Object) newValue);
            itemKey.gtolib$setMaxStackSize(stack.getMaxStackSize());
            itemKey.gtolib$setReadOnlyStack(copy);
            pos = -pos - 1;
            node[pos] = new Node(copy, newValue, hash);
            if (this.size++ >= this.maxFill) rehash(HashCommon.arraySize(this.size + 1, Hash.DEFAULT_LOAD_FACTOR));
            return newValue;
        }
    }

    private void rehash(final int newN) {
        final Node[] node = this.node;
        final int mask = newN - 1;
        final Node[] newNode = new Node[newN + 1];
        int i = this.n, pos;
        for (int j = this.size; j-- != 0;) {
            while (node[--i] == null);
            if (!(newNode[pos = (HashCommon.mix((node[i]).hash)) & mask] == null)) while (!((newNode[pos = (pos + 1) & mask]) == null));
            newNode[pos] = node[i];
        }
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, Hash.DEFAULT_LOAD_FACTOR);
        this.node = newNode;
    }

    private record Node(ItemStack stack, AEItemKey key, int hash) {}
}
